package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.constants.CommonConstants;
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
import weixin.popular.client.LocalHttpClient;
import weixin.popular.util.MapUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.XMLConverUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chevis on 2014/12/25.
 */
public class WechatPayNotificationProcessor extends ActionSupport {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    ProfileManager profileManager;

    @Override
    public String execute() throws Exception {

        WechatProfile wechatProfile = profileManager.getAppProfile("liyuanapp").getWechatProfile();

        HttpServletRequest request = getHttpRequest();
        HttpServletResponse response = getHttpResponse();

        boolean isValidMsg = false;

        InputStream inputStream = request.getInputStream();

        String xml = IOUtils.toString(inputStream);
        logger.debug("Received pay notification xml: {}", xml);

        //获取数据
//        PayNativeReply payNativeReply = XMLConverUtil.convertToObject(PayNativeReply.class, inputStream);
        PayNotification payNotification = XMLConverUtil.convertToObject(PayNotification.class, xml);

        logger.debug("Payment notification is: {} ", payNotification.toString());

        if (payNotification.getReturn_code().toUpperCase().equals(CommonConstants.SUCCESS.toUpperCase())) {
            if (payNotification.getResult_code().toUpperCase().equals(CommonConstants.SUCCESS.toUpperCase())) {
                String sign = payNotification.getSign();
                String validationSign = SignatureUtil.generateSign(MapUtil.objectToMap(payNotification, StringUtils.EMPTY), wechatProfile.getPayKey());
                isValidMsg = true;// TODO: sign.equals(validationSign);

            } else {
                logger.warn("Payment notification with error: ({}={}) \n Error Details: {}", payNotification.getErr_code(), payNotification.getErr_code_des(), payNotification);
            }

        } else {
            logger.warn("Received a failure payment notification with the error: {}", payNotification.getReturn_msg());
        }

        if (isValidMsg) {
            logger.info("Received success payment with amount {} for openid({})", payNotification.getTotal_fee(), payNotification.getOpenid());
            topupCoins(payNotification.getOut_trade_no(), 1); // TODO: calculate the topup amount

            // TODO: Reply to wechat for the payment notification.
        }
        return Action.SUCCESS;
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

        LocalHttpClient.execute(httpUriRequest);

        return true;
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
}

