package gamecenter.core.processors.wechat;

import gamecenter.core.beans.UserProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.HttpService;
import gamecenter.core.services.db.SubscribeService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class WechatTopupProcessor extends GeneralProcessor {

    private final SubscribeService subscribeService;
    private final UserProfile userProfile;

    public WechatTopupProcessor(SubscribeService subscribeService, UserProfile userProfile) {
        this.subscribeService = subscribeService;
        this.userProfile = userProfile;
    }

    private static BasicNameValuePair[] getTopupParams(int coins) {
        return new BasicNameValuePair[]{
                new BasicNameValuePair("CENTER_ID", "00000000"),
                new BasicNameValuePair("TOKEN", "tokenStr"),
                new BasicNameValuePair("DATA_TYPE", "JSON"),
                new BasicNameValuePair("REQ_TYPE", "TOP_UP"),
                new BasicNameValuePair("MAC", "accf233b95f6"),
                new BasicNameValuePair("TOP_UP_REFERENCE_ID", "ABCDEF0000"),
                new BasicNameValuePair("TOP_UP_COIN_QTY", String.valueOf(coins))
        };
    }

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
        boolean result = false;

        try {
            String uri = "http://wawaonline.net:8003";
            HttpResponse response = HttpService.get(uri, getTopupParams(userProfile.getBonus()));
            String value = IOUtils.toString(response.getEntity().getContent());
            logger.debug("Response is: {}", value);
            result = subscribeService.consumeBonus(userProfile.getOpenId(), deviceId, userProfile.getBonus());
            userProfile.setBonus(0);
        } catch (Exception e) {
            logger.error("Topup failed since: {}", e);
        }

        logger.info("Topup result: {}", result);
        return result ? SUCCESS : ERROR;
    }
}
