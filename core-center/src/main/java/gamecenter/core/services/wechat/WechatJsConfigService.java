package gamecenter.core.services.wechat;

import gamecenter.core.beans.wechat.WechatJsConfig;
import gamecenter.core.utils.EncryptUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Frank on 15/02/14.
 */
public class WechatJsConfigService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public WechatJsConfig getConfig(String jsapi_ticket, String wechatAppId, String code) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomStringUtils.randomAlphabetic(6);
//        String url = "http://wawaonline.net/corecenter/scan.action";//当前页面的url

        String url = "http://wawaonline.net/corecenter/auth?code=" + code + "&state=appid%3Aliyuanapp%2Cdeviceid%3AATM0001";//当前页面的url
        String signature = getSignature(jsapi_ticket, nonceStr, timestamp, url);

        WechatJsConfig wechatJsConfig = new WechatJsConfig();
        wechatJsConfig.setAppId(wechatAppId);
        wechatJsConfig.setNonceStr(nonceStr);
        wechatJsConfig.setTimestamp(timestamp);
        wechatJsConfig.setSignature(signature);

        return wechatJsConfig;
    }

    private String getSignature(String jsapi_ticket, String nonceStr, String timestamp, String url) {
        String raw = "jsapi_ticket=%s" + "&" +
                "noncestr=%s" + "&" +
                "timestamp=%s" + "&" +
                "url=%s";

        return EncryptUtil.SHA1(String.format(raw, jsapi_ticket, nonceStr, timestamp, url));

    }
}
