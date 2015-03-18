package gamecenter.core.processors.wechat;

import gamecenter.core.beans.CoreCenterHost;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WechatOAuthProcessorTest {
    @Test
    public void oauthUrlGeneration() throws Exception {
        String val1 = "chevisappid";
        String val2 = "appid:chevisappid,device:abcd1234";
        String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=%s" +
                "&" +
                "redirect_uri=" + CoreCenterHost.getHttpURL(CoreCenterHost.AUTH_URL) +
                "&" +
                "response_type=code" +
                "&" +
                "scope=snsapi_userinfo" +
                "&" +
                "state=%s" +
                "#wechat_redirect";
        String expected = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=chevisappid&redirect_uri=" + CoreCenterHost.getHttpURL(CoreCenterHost.AUTH_URL) + "&response_type=code&scope=snsapi_userinfo&state=appid:chevisappid,device:abcd1234#wechat_redirect";
        assertEquals(expected, String.format(oauthUrl, val1, val2));

    }
}