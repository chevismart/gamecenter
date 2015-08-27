package gamecenter.core.processors.wechat;

import gamecenter.core.beans.UserProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.db.SubscribeService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Chevis on 15/6/9.
 */
public class WechatTopupProcessor extends GeneralProcessor {

    SubscribeService subscribeService;
    UserProfile userProfile;

    private static String getParams(int coins) {
        NameValuePair[] param = {
                new BasicNameValuePair("CENTER_ID", "00000000"),
                new BasicNameValuePair("TOKEN", "tokenStr"),
                new BasicNameValuePair("DATA_TYPE", "JSON"),
                new BasicNameValuePair("REQ_TYPE", "TOP_UP"),
                new BasicNameValuePair("MAC", "accf233b95f6"),
                new BasicNameValuePair("TOP_UP_REFERENCE_ID", "ABCDEF0000"),
                new BasicNameValuePair("TOP_UP_COIN_QTY", String.valueOf(coins))
        };
        return StringUtils.join(param, "&");
    }

    private static String getUrl(String url) {
        return url.concat("?").concat(getParams(1));
    }

    public static void main(String[] args) {
        System.err.println(getUrl("http://wawaonline.net:8003"));
    }

    public String execute() throws Exception {

        logger.debug("Start to topup!");
        logger.debug("User profile is: {}", userProfile.toString());

        String appId = getHttpRequest().getParameter(CommonConstants.WECHAT_STATE_PARAM_APPID);
        String deviceId = getHttpRequest().getParameter(CommonConstants.WECHAT_STATE_PARAM_DEVICEID);
        String openId = userProfile.getOpenId();

        logger.debug("appId={}, deviceId={}, openId={}", appId, deviceId, openId);

        //TODO: Retrieve the bonus and send to topup
        logger.info("Preparing topup");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String uri = "http://wawaonline.net:8003";
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectionRequestTimeout(5000).build();
        HttpGet httpGet = new HttpGet(uri.concat("?").concat(getParams(userProfile.getBonus())));
        httpGet.setConfig(requestConfig);
        HttpResponse response = httpClient.execute(httpGet);

        String value = IOUtils.toString(response.getEntity().getContent());

        logger.debug("Response is: {}", value);


        boolean result = subscribeService.consumeBonus(userProfile.getOpenId(), deviceId, userProfile.getBonus());

        logger.info("Topup result: {}", result);

        userProfile.setBonus(0);

        return SUCCESS;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void setSubscribeService(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

}
