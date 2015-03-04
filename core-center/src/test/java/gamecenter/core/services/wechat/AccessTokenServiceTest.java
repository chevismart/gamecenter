package gamecenter.core.services.wechat;

import gamecenter.core.beans.AppProfile;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.Token;
import weixin.popular.client.LocalHttpClient;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AccessTokenService.class})
public class AccessTokenServiceTest {
    AccessTokenService service;
    AppProfile appProfile;
    TokenAPI tokenAPI;
    HttpResponse httpResponse;
    HttpEntity httpEntity;

    @Before
    public void setUp() throws Exception {
        appProfile = mock(AppProfile.class);
        tokenAPI = mock(TokenAPI.class);
        httpResponse = mock(HttpResponse.class);
        httpEntity = mock(HttpEntity.class);
    }


    //    @Test
    public void testRequestWechatAccessTokenFromHost() throws Exception {
        String json = "{\"access_token\":\"7UDbc3t1yBSTPDfveOGxFRycOpD5U-1jvR3le5tjdtowbLeu17FTZTPTWUBu_WyNRtzlYjMlFx4fLMZbfL6QlYESygVI_Rv0DHzCR35722U\",\"expires_in\":7200,\"genTime\":0}";
        when(appProfile.getAppId()).thenReturn("liyuanapp");
        when(LocalHttpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(EntityUtils.toString(httpEntity)).thenReturn(json);

        service = new AccessTokenService(tokenAPI);
        Token token = service.requestWechatAccessTokenFromHost(appProfile);
        assertNotNull(token);
    }
}