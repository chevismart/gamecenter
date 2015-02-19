package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.ActionContext;
import gamecenter.core.beans.CoreCenterHost;
import gamecenter.core.beans.Figure;
import gamecenter.core.beans.GlobalPaymentBean;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.utils.CollectionUtils;
import gamecenter.core.utils.ParameterUtil;
import gamecenter.core.utils.TimeUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.util.MapUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.XMLConverUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chevis on 2014/12/25.
 */
public class WechatPayNotificationProcessor extends GeneralProcessor {

    Logger paymentLogger = LoggerFactory.getLogger("wechatPaymentLogger");
    ProfileManager profileManager;
    GlobalPaymentBean globalPaymentBean;
    WechatProfile wechatProfile;
    private UserProfile userProfile;

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = getHttpRequest();
        InputStream inputStream = request.getInputStream();
        String xml = IOUtils.toString(inputStream);
        logger.debug("Received pay notification xml: {}", xml);
        PayNotification payNotification = XMLConverUtil.convertToObject(PayNotification.class, new String(xml.getBytes("iso-8859-1"), "utf-8"));
        logger.info("Converted payment notification is: {}", payNotification.toString());
        if (verifySign(payNotification)) {
            if (StringUtils.isNotEmpty(payNotification.getAttach())) {
                handlePaidNotification(payNotification);
            } else if (StringUtils.isNotEmpty(payNotification.getProduct_id())) {
                handleNativePrePay(payNotification);
            }
        } else {
            logger.warn("The payment notification is invalid to be processed.");
        }
        return null;
    }

    private void handleNativePrePay(PayNotification payNotification) {
        String coins = getTotalPaidInCents(payNotification);
        PayMchAPI payMchAPI = new PayMchAPI();
        Unifiedorder unifiedorder = new Unifiedorder();
        unifiedorder.setAppid(payNotification.getAppid());
        unifiedorder.setMch_id(payNotification.getMch_id());
        unifiedorder.setNonce_str(RandomStringUtils.random(32, true, true));
        unifiedorder.setBody("荔园娃娃机代币【" + coins + "】个");
        unifiedorder.setAttach("liyuanapp");
        unifiedorder.setOut_trade_no("trade_" + TimeUtil.getCurrentDateTime().getTime());
        unifiedorder.setTotal_fee(Figure.COIN_TO_MONEY.calculate(Integer.valueOf(coins)).toString());
        unifiedorder.setNotify_url(CoreCenterHost.getHttpURL(CoreCenterHost.WECHAT_PAYMENT_NOTIFICATION_CALLBACK_URL));
        unifiedorder.setOpenid(payNotification.getOpenid());
        unifiedorder.setTrade_type("NATIVE");
        unifiedorder.setDevice_info("ATM001");
        unifiedorder.setSign(SignatureUtil.generateSign(MapUtil.order(MapUtil.objectToMap(unifiedorder)), "wawaonline20150101wechatpaybilly"));

        UnifiedorderResult result = payMchAPI.payUnifiedorder(unifiedorder);

        PayNotification returnInfo = new PayNotification();
        returnInfo.setReturn_code("SUCCESS");
        returnInfo.setAppid(result.getAppid());
        returnInfo.setMch_id(result.getMch_id());
        returnInfo.setNonce_str(result.getNonce_str());
        returnInfo.setPrepay_id(result.getPrepay_id());
        returnInfo.setResult_code("SUCCESS");

        Map map = MapUtil.order(MapUtil.objectToMap(returnInfo));
        returnInfo.setSign(SignatureUtil.generateSign(map, "wawaonline20150101wechatpaybilly"));

        String responseXML = XMLConverUtil.convertToXML(returnInfo);
        returnWechatResponse(responseXML);
    }

    private String getTotalPaidInCents(PayNotification payNotification) {
        String amount = ParameterUtil.extractParam(payNotification.getProduct_id()).get(CommonConstants.Payment.AMOUNT_IN_CENT);
        return StringUtils.isNotEmpty(amount) ? amount : payNotification.getProduct_id().substring(payNotification.getProduct_id().length() - 1);
    }

    private void handlePaidNotification(PayNotification payNotification) throws URISyntaxException {
        String appId = payNotification.getAttach();
        wechatProfile = profileManager.getAppProfile(appId).getWechatProfile();
        boolean isValidMsg = false;

        if (payNotification.getReturn_code().toUpperCase().equals(CommonConstants.SUCCESS.toUpperCase())) {
            if (payNotification.getResult_code().toUpperCase().equals(CommonConstants.SUCCESS.toUpperCase())) {
                isValidMsg = !isPaymentProcessed(payNotification);
            } else {
                logger.warn("Payment notification with error: ({}={}) \n Error Details: {}",
                        payNotification.getErr_code(),
                        payNotification.getErr_code_des(),
                        payNotification.toString());
            }

        } else {
            logger.warn("Received a failure payment notification with the error: {}", payNotification.getReturn_msg());
        }

        if (isValidMsg) {
            logger.info("Received success payment with amount {} for user({})", payNotification.getTotal_fee(), payNotification.getOpenid());
            paymentLogger.info(payNotification.toString());
            boolean isSuccess = topupCoins(payNotification.getOut_trade_no(), getChargeCoinNum(payNotification, Figure.MONEY_TO_COIN, Figure.BUY_1_GET_1_FREE));
            logger.info("Top up result is: {}", isSuccess);
            if (isSuccess) {
                globalPaymentBean.getUnSettlementPayments().remove(payNotification.getOut_trade_no());
                globalPaymentBean.getSettledPayments().put(payNotification.getOut_trade_no(), payNotification);
                logger.info(globalPaymentBean.getSettledPayments().toString());
                logger.info(globalPaymentBean.getUnSettlementPayments().toString());
            }
            replayWechatForPayNotification(isSuccess);
        } else {
            replayWechatForPayNotification(isValidMsg);
        }

        logger.info(payNotification.getProduct_id());
    }

    private int getChargeCoinNum(PayNotification payNotification, Figure moneyToCoin, Figure... figures) {
        int actualCoinQty = moneyToCoin.calculate(Integer.valueOf(payNotification.getTotal_fee()));
        for (Figure figure : figures) {
            actualCoinQty = figure.calculate(actualCoinQty);
        }
        return actualCoinQty;
    }

    protected boolean isPaymentProcessed(PayNotification payNotification) {
        String payNotificationId = payNotification.getOut_trade_no();
        return !globalPaymentBean.getUnSettlementPayments().keySet().contains(payNotificationId) &&
                globalPaymentBean.getSettledPayments().keySet().contains(payNotificationId);
    }

    private void replayWechatForPayNotification(boolean isSuccess) {
        PayNotification payNotification = new PayNotification();
        payNotification.setReturn_code(isSuccess ? CommonConstants.SUCCESS : CommonConstants.FAIL);
        String responseXML = XMLConverUtil.convertToXML(payNotification);
        returnWechatResponse(responseXML);
    }

    private void returnWechatResponse(String content) {
        try {
            ServletOutputStream os = getHttpResponse().getOutputStream();
            getHttpResponse().setStatus(HttpServletResponse.SC_OK);
            logger.info("Return notification response: {}", content);
            os.write(content.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error("Return wechat response error: {}", e.getMessage());
        }
    }

    protected boolean topupCoins(String tradeNum, int coins) throws URISyntaxException {

        // TODO: Enhance the dynamic way to construct the parameter...

        logger.info("Ready to top up coin.");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("CENTER_ID", "00000000"));
        params.add(new BasicNameValuePair("TOKEN", "tokenStr"));
        params.add(new BasicNameValuePair("DATA_TYPE", "JSON"));
        params.add(new BasicNameValuePair("REQ_TYPE", "TOP_UP"));
        params.add(new BasicNameValuePair("MAC", "accf233b95f6"));
        params.add(new BasicNameValuePair("TOP_UP_REFERENCE_ID", "ABCDEF3456"));
        params.add(new BasicNameValuePair("TOP_UP_COIN_QTY", String.valueOf(coins)));
        String param = URLEncodedUtils.format(params, "UTF-8");


        HttpUriRequest httpUriRequest = RequestBuilder.get()
                .setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()))
                .setUri(URIUtils.createURI("http", CoreCenterHost.CORECENTER_HOST, 8003,
                        "/", param, null))
                .build();

        HttpResponse response = LocalHttpClient.execute(httpUriRequest);
        String json = null;
        try {
            json = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        logger.info("Topup result is: {}", json);
        return true;
    }

    private boolean verifySign(PayNotification payNotification) {
        String sign = payNotification.getSign();
        Map<String, String> valueableMap = MapUtil.order(MapUtil.objectToMap(payNotification));
        String validationSign = SignatureUtil.generateSign(MapUtil.order(CollectionUtils.removeEmptyValueEntry(valueableMap)),
                wechatProfile.getPayKey());
        return sign.equals(validationSign);
    }

    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);

    }

    protected HttpServletResponse getHttpResponse() {
        return (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public GlobalPaymentBean getGlobalPaymentBean() {
        return globalPaymentBean;
    }

    public void setGlobalPaymentBean(GlobalPaymentBean globalPaymentBean) {
        this.globalPaymentBean = globalPaymentBean;
    }
}

