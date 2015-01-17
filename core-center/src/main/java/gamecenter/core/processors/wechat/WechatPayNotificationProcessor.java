package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.ActionContext;
import gamecenter.core.beans.GlobalPaymentBean;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.utils.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
// TODO: handle the duplicate notification case
        if (StringUtils.isNotEmpty(payNotification.getAttach())) {
            String appId = payNotification.getAttach();
            wechatProfile = profileManager.getAppProfile(appId).getWechatProfile();
            boolean isValidMsg = false;

            if (payNotification.getReturn_code().toUpperCase().equals(CommonConstants.SUCCESS.toUpperCase())) {
                if (payNotification.getResult_code().toUpperCase().equals(CommonConstants.SUCCESS.toUpperCase())) {
                    isValidMsg = verifySign(payNotification) && !isPaymentProcessed(payNotification);
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
                boolean isSuccess = topupCoins(payNotification.getOut_trade_no(), 1); // TODO: calculate the topup amount
                globalPaymentBean.getUnSettlementPayments().remove(payNotification.getOut_trade_no());
                globalPaymentBean.getSettledPayments().put(payNotification.getOut_trade_no(), payNotification);
                replayWechatForPayNotification(isSuccess);
            } else {
                replayWechatForPayNotification(isValidMsg);
            }
        }
        return null;
    }

    protected boolean isPaymentProcessed(PayNotification payNotification) {
        return globalPaymentBean.getSettledPayments().keySet().contains(payNotification.getOut_trade_no());
    }

    private void replayWechatForPayNotification(boolean isSuccess) {
        PayNotification payNotification = new PayNotification();
        payNotification.setReturn_code(isSuccess ? CommonConstants.SUCCESS : CommonConstants.FAIL);
        try {
            ServletOutputStream os = getHttpResponse().getOutputStream();
            String responseXML = XMLConverUtil.convertToXML(payNotification);
            logger.info("Payment notification response: {}", responseXML);
            os.write(responseXML.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean topupCoins(String tradeNum, int coins) throws URISyntaxException {

        // TODO: Enhance the dynamic way to construct the parameter...

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("CENTER_ID", "00000000"));
        params.add(new BasicNameValuePair("TOKEN", "tokenStr"));
        params.add(new BasicNameValuePair("DATA_TYPE", "JSON"));
        params.add(new BasicNameValuePair("REQ_TYPE", "TOP_UP"));
        params.add(new BasicNameValuePair("MAC", "accf233b95f6"));
        params.add(new BasicNameValuePair("TOP_UP_REFERENCE_ID", tradeNum));
        params.add(new BasicNameValuePair("TOP_UP_COIN_QTY", String.valueOf(coins)));
        String param = URLEncodedUtils.format(params, "UTF-8");


        HttpUriRequest httpUriRequest = RequestBuilder.get()
                .setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()))
                .setUri(URIUtils.createURI("http", "www.ruijunhao.com", 80,
                        "/test/fronter.php", param, null))
                .build();

//        LocalHttpClient.execute(httpUriRequest);

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

