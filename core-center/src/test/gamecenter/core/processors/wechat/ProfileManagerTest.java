package gamecenter.core.processors.wechat;


import gamecenter.core.beans.AppProfile;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.Token;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfileManagerTest {

    ProfileManager profileManager;
    String appName = "testAppName";
    String appId = "testAppId";
    TokenAPI wechatAccessTokenApi;
    Token mockToken;

    @Before
    public void setUp() throws Exception {
        profileManager = new ProfileManager(new HashMap<String, AppProfile>());
        wechatAccessTokenApi = mock(TokenAPI.class);
        mockToken = mock(Token.class);
    }

    @Test
    public void requestAccessTokenByProfileManagerSuccessfully() throws Exception {
        profileManager.addProfile(appName, appId);
        profileManager.addWechatProfile(appId, "wxe89a9d2fa17df80f", "71d8fc7778571e6b54712953b68084e4");
        Whitebox.setInternalState(profileManager, "wechatAccessTokenApi", wechatAccessTokenApi);
        when(mockToken.getAccess_token()).thenReturn("tokenDetails");
        when(wechatAccessTokenApi.token(anyString(), anyString())).thenReturn(mockToken);

        Token token = profileManager.requestWechatAccessToken(appId);

        assertNotNull(token);
        assertThat(token.getAccess_token(), is("tokenDetails"));
    }
}