package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.ActionContext;
import gamecenter.core.beans.*;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.wechat.NativePrePayOrderService;
import gamecenter.core.utils.CollectionUtils;
import gamecenter.core.utils.ParameterUtil;
import org.apache.commons.io.IOUtils;
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
import weixin.popular.client.LocalHttpClient;
import weixin.popular.util.MapUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.XMLConverUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    NativePrePayOrderService nativePrePayOrderService = new NativePrePayOrderService();
    private UserProfile userProfile;

    @Override
    public String execute() throws Exception {
        String xml = IOUtils.toString(getHttpRequest().getInputStream());
        logger.debug("Received pay notification xml: {}", xml);
        PayNotification payNotification = XMLConverUtil.convertToObject(PayNotification.class, new String(xml.getBytes("iso-8859-1"), "utf-8"));
        logger.info("Converted payment notification is: {}", payNotification.toString());
        if (verifySign(payNotification)) {
            if (StringUtils.isNotEmpty(payNotification.getAttach())) {
                // Handle the request for the common paid notification.
                handlePaidNotification(payNotification);
            } else if (StringUtils.isNotEmpty(payNotification.getProduct_id())) {
                // Handle the request for the native pre-pay order
                handleNativePrePayOrder(payNotification);
            }
        } else {
            logger.warn("The payment notification is invalid to be processed.");
        }
        return null;
    }

    private void handleNativePrePayOrder(PayNotification payNotification) {
        if (NativePrePayOrderService.isParamEnough(payNotification)) {

            // Convert the parameters in qr code to the fields, the following fields could not be empty.
            Map<String, String> product = ParameterUtil.extractParam(payNotification.getProduct_id());
            String appId = ParameterUtil.NativePrePayOrder.extractAppId(product);
            String coins = ParameterUtil.NativePrePayOrder.extractCoins(product);
            String deviceId = ParameterUtil.NativePrePayOrder.extractDeviceId(product);
            String money = ParameterUtil.NativePrePayOrder.extractMoney(product);

            if (!ParameterUtil.hasEmptyParam(appId, coins, deviceId, money)) {
                // Supply the field into payNotification object for further request
                payNotification.setDevice_info(deviceId);
                payNotification.setAttach(appId);
                payNotification.setTotal_fee(String.valueOf(Figure.COIN_TO_MONEY.calculate(Integer.valueOf(money))));
                AppProfile appProfile = profileManager.getAppProfile(appId);
                // Request the native pre-pay order response content(xml)
                String content = nativePrePayOrderService.requestPrePayOrder(payNotification, appProfile, Integer.valueOf(coins));
                // return the pre-pay order result to the requester
                returnWechatResponse(content);

            } else {
                logger.warn("Insufficiency info for the native prepay order. appId = {}, coins = {}, deviceId = {}, money={}",
                        appId, coins, deviceId, money);
            }
        }
    }

    private void handlePaidNotification(PayNotification payNotification) throws URISyntaxException {
        // Get the appId from the attach field of payNotification
        Map attachMap = ParameterUtil.extractParam(payNotification.getAttach());
        String appId = ParameterUtil.NativePrePayOrder.extractAppId(attachMap);
        int coins = Integer.valueOf(ParameterUtil.NativePrePayOrder.extractCoins(attachMap));
        wechatProfile = profileManager.getAppProfile(appId).getWechatProfile();
        boolean isValidMsg = false;

        // Verify the response is successful or not
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
            boolean isSuccess = topupCoins(payNotification.getOut_trade_no(), coins);
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

