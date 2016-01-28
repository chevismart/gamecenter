package gamecenter.core.services.wechat;

import gamecenter.core.beans.wechat.WechatJsConfig;
import gamecenter.core.utils.EncryptUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WechatJsConfigService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args){
        WechatJsConfigService service = new WechatJsConfigService();
        System.err.println(service.getSignature("sM4AOVdWfPE4DxkXGEs8VNPZRV880fq5H7X4ZQqazbirHhcgT9ap_nTMkm8ld5liiNTjDaqJ_PhUeHQ0xdUsiA","lqCCfO","1453903587","http://wawaonline.net/corecenter/auth?code=021c0127dc6c12620a8fffbdbca017eL&state=appid%3Aliyuanapp%2Cdeviceid%3AATM0001%2CoptionalUrl%3Awawaonline.net%2Fcorecenter%2Fpocket"));
    }

    public WechatJsConfig getConfig(String jsapi_ticket, String wechatAppId, String url) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomStringUtils.randomAlphabetic(6);
//        String url = "http://wawaonline.net/corecenter/scan.action";//当前页面的url

//        String url = urlStr+"?code=" + code + "&state=appid%3Aliyuanapp%2Cdeviceid%3AATM0001";//当前页面的url
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
