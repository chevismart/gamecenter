package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.ActionContext;
import gamecenter.core.beans.CoreCenterHost;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.utils.CollectionUtils;
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
    WechatProfile wechatProfile;

    @Override
    public String execute() throws Exception {
        String nextStep = StringUtils.EMPTY;
        PayNotification payNotification = getPayNotification();
        if (verifySign(payNotification)) {
            if (StringUtils.isNotEmpty(payNotification.getAttach())) {
                // Handle the request for the common paid notification.
                nextStep = CommonConstants.ACCESS_ROUTER_PAID_NOTIFY;
            } else if (StringUtils.isNotEmpty(payNotification.getProduct_id())) {
                // Handle the request for the native pre-pay order
                nextStep = CommonConstants.ACCESS_ROUTER_PREPAY_ORDER;
            }
        } else {
            logger.warn("The payment notification is invalid to be processed.");
        }
        return nextStep;
    }

    protected PayNotification getPayNotification() throws IOException {
        String xml = IOUtils.toString(getHttpRequest().getInputStream());
        logger.debug("Received pay notification xml: {}", xml);
        PayNotification payNotification = XMLConverUtil.convertToObject(PayNotification.class, new String(xml.getBytes("iso-8859-1"), "utf-8"));
        logger.info("Converted payment notification is: {}", payNotification.toString());
        return payNotification;
    }


    protected void returnWechatResponse(String content) {
        try {
            ServletOutputStream os = getHttpResponse().getOutputStream();
            getHttpResponse().setStatus(HttpServletResponse.SC_OK);
            logger.info("Return notification response: {}", content);
            os.write(content.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error("Return wechat response with error: {}", e.getMessage());
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
        return StringUtils.isNotEmpty(sign) && sign.equals(validationSign);
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

