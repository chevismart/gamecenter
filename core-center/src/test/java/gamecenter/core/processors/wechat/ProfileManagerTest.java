package gamecenter.core.processors.wechat;


import gamecenter.core.beans.AppProfile;
import gamecenter.core.services.wechat.AccessTokenService;
import gamecenter.core.services.wechat.SnsAuthService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import weixin.popular.bean.Token;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfileManagerTest {

    ProfileManager profileManager;
    String appName = "testAppName";
    String appId = "testAppId";
    AccessTokenService accessTokenService;
    SnsAuthService snsAuthService;
    Token accessToken;

    @Before
    public void setUp() throws Exception {
        accessTokenService = mock(AccessTokenService.class);
        snsAuthService = mock(SnsAuthService.class);
        accessToken = mock(Token.class);
        profileManager = new ProfileManager(new HashMap<String, AppProfile>());
    }

    @Test
    public void requestAccessTokenByProfileManagerSuccessfully() throws Exception {
        profileManager.addAppProfile(appId, appName);
        profileManager.addWechatProfile(appId, "wxe89a9d2fa17df80f", "71d8fc7778571e6b54712953b68084e4");
        Whitebox.setInternalState(profileManager, "accessTokenService", accessTokenService);
        Whitebox.setInternalState(profileManager, "snsAuthService", snsAuthService);
        when(accessTokenService.requestWechatAccessToken(any(AppProfile.class))).thenReturn(accessToken);
        Token token = profileManager.requestWechatAccessToken(appId);

        assertNotNull(token);
        assertThat(token, is(accessToken));
    }
}