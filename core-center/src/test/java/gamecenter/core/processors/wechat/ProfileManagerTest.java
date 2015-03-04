package gamecenter.core.processors.wechat;


import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.services.wechat.AccessTokenService;
import gamecenter.core.services.wechat.SnsAuthService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;
import weixin.popular.bean.Token;
import weixin.popular.bean.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ProfileManagerTest {

    ProfileManager profileManager;
    String appName = "testAppName";
    String appId = "testAppId";
    String wechatId = "wechatId";
    String wechatSecret = " wechatSecret";
    AccessTokenService accessTokenService;
    SnsAuthService snsAuthService;
    Token accessToken;
    AppProfile appProfile;
    WechatProfile wechatProfile1;
    WechatProfile wechatProfile2;
    Map<String, AppProfile> appProfiles;
    User user;
    Logger logger;

    @Before
    public void setUp() throws Exception {
        accessTokenService = mock(AccessTokenService.class);
        snsAuthService = mock(SnsAuthService.class);
        accessToken = mock(Token.class);
        appProfiles = mock(Map.class);
        appProfile = mock(AppProfile.class);
        wechatProfile1 = mock(WechatProfile.class);
        wechatProfile2 = mock(WechatProfile.class);
        logger = mock(Logger.class);
        user = mock(User.class);
        profileManager = new ProfileManager(new HashMap<String, AppProfile>());
    }

    @Test
    public void requestAccessTokenByProfileManagerSuccessfully() throws Exception {
        profileManager.addAppProfile(appId, appName);
        profileManager.addWechatProfile(appId, "wxe89a9d2fa17df80f", "71d8fc7778571e6b54712953b68084e4", null, null);
        Whitebox.setInternalState(profileManager, "accessTokenService", accessTokenService);
        Whitebox.setInternalState(profileManager, "snsAuthService", snsAuthService);
        when(accessTokenService.requestWechatAccessToken(any(AppProfile.class))).thenReturn(accessToken);
        profileManager.setIsHost(true);
        Token token = profileManager.requestWechatAccessToken(appId);

        assertNotNull(token);
        assertThat(token, is(accessToken));
    }

    @Test
    public void requestAccessTokenFailed() throws Exception {

        Whitebox.setInternalState(profileManager, "profiles", appProfiles);
        when(appProfiles.get(appId)).thenReturn(appProfile);
        when(appProfile.getAppId()).thenReturn(StringUtils.EMPTY);
        Whitebox.setInternalState(profileManager, "accessTokenService", accessTokenService);
        Whitebox.setInternalState(profileManager, "logger", logger);

        Token token = profileManager.requestWechatAccessToken(appId);

        assertNull(token);
        verify(accessTokenService, never()).requestWechatAccessToken(any(AppProfile.class));
        verify(logger, only()).warn(contains("invalid"), eq(appId));
    }

    @Test
    public void addWechatProfileSuccessfully() throws Exception {
        Whitebox.setInternalState(profileManager, "profiles", appProfiles);
        Whitebox.setInternalState(profileManager, "logger", logger);
        when(appProfiles.get(eq(appId))).thenReturn(appProfile);
        when(appProfile.getAppId()).thenReturn(appId);
        when(appProfile.getAppName()).thenReturn(appName);

        profileManager.addWechatProfile(appId, wechatId, wechatSecret, RandomStringUtils.random(10), RandomStringUtils.random(32));

        verify(appProfiles, atLeastOnce()).get(eq(appId));
        verify(appProfile, times(1)).setWechatProfile(any(WechatProfile.class));
        verify(logger, times(1)).info(contains("success"), eq(appId));
    }

    @Test
    public void couldNotAddWechatProfile() throws Exception {
        Whitebox.setInternalState(profileManager, "profiles", appProfiles);
        Whitebox.setInternalState(profileManager, "logger", logger);
        when(appProfiles.get(eq(appId))).thenReturn(appProfile);
        when(appProfile.getAppId()).thenReturn(StringUtils.EMPTY);

        profileManager.addWechatProfile(appId, wechatId, wechatSecret, RandomStringUtils.random(10), RandomStringUtils.random(32));

        verify(appProfiles, only()).get(eq(appId));
        verify(appProfile, never()).setWechatProfile(any(WechatProfile.class));
        verify(logger, times(1)).warn(contains("not setup"), eq(appId));
    }

    @Test
    public void addAppProfileSuccessfully() throws Exception {
        Whitebox.setInternalState(profileManager, "profiles", appProfiles);
        AppProfile appProfile = profileManager.addAppProfile(appId, appName);
        assertNotNull(appProfile);
        assertEquals(appId, appProfile.getAppId());
        verify(appProfiles, atLeastOnce()).put(eq(appId), any(AppProfile.class));
        assertEquals(appName, appProfile.getAppName());
    }


    @Test
    public void checkAndUpdateWechatProfileSuccessfully() throws Exception {
        Map<String, AppProfile> realProfiles = new HashMap<String, AppProfile>();
        Whitebox.setInternalState(profileManager, "logger", logger);
        Whitebox.setInternalState(profileManager, "profiles", realProfiles);
        realProfiles.put("app1", appProfile);
        when(appProfile.getWechatProfile()).thenReturn(wechatProfile1);
        when(appProfile.getAppId()).thenReturn(appId);
        when(appProfile.getAppName()).thenReturn(appName);
        when(appProfile.isWechatProfileValid()).thenReturn(true);
        when(wechatProfile1.getWechatAccessToken()).thenReturn(null);
        Date now = new Date();
        when(wechatProfile1.getWechatAccessTokenUpdateTime()).thenReturn(now);
        when(wechatProfile1.getWechatAccessToken()).thenReturn(accessToken);
        when(accessToken.getExpires_in()).thenReturn(CommonConstants.EXPIRY_SHIFT_PERIOD_IN_SECOND - 1);

        profileManager.checkAndUpdateAllAccessToken();

        verify(logger, times(1)).info(contains("Updating wechat access token for appId "), anyString());
    }

    @Test
    public void checkWechatProfileButWithNoUpdateSinceNotYetTimeUp() throws Exception {
        Map<String, AppProfile> realProfiles = new HashMap<String, AppProfile>();
        Whitebox.setInternalState(profileManager, "logger", logger);
        Whitebox.setInternalState(profileManager, "profiles", realProfiles);
        realProfiles.put("app1", appProfile);
        when(appProfile.getWechatProfile()).thenReturn(wechatProfile1);
        when(appProfile.getAppId()).thenReturn(appId);
        when(appProfile.getAppName()).thenReturn(appName);
        when(appProfile.isWechatProfileValid()).thenReturn(true);
        when(wechatProfile1.getWechatAccessToken()).thenReturn(null);
        Date now = new Date();
        when(wechatProfile1.getWechatAccessTokenUpdateTime()).thenReturn(now);
        when(wechatProfile1.getWechatAccessToken()).thenReturn(accessToken);
        when(accessToken.getExpires_in()).thenReturn(CommonConstants.EXPIRY_SHIFT_PERIOD_IN_SECOND + 1);

        profileManager.checkAndUpdateAllAccessToken();

        verify(logger, never()).info(contains("Updating wechat access token for appId"), anyString());
    }

    @Test
    public void getUserInfoSuccessfully() throws Exception {
        String code = "code";
        Whitebox.setInternalState(profileManager, "logger", logger);
        Whitebox.setInternalState(profileManager, "profiles", appProfiles);
        Whitebox.setInternalState(profileManager, "snsAuthService", snsAuthService);

        when(appProfiles.get(appId)).thenReturn(appProfile);
        when(appProfile.getAppId()).thenReturn(appId);
        when(appProfile.getAppName()).thenReturn(appName);
        when(snsAuthService.getUserInfo(appProfile, code, Locale.CHINA.getLanguage())).thenReturn(user);
        when(user.getOpenid()).thenReturn("openid");

        User user = profileManager.getUserInfo(appId, code, Locale.CHINA);
        assertEquals(this.user, user);
        verify(logger, never()).info(anyString());
    }

    @Test
    public void userInfoNotFound() throws Exception {
        String code = "code";
        Whitebox.setInternalState(profileManager, "logger", logger);
        Whitebox.setInternalState(profileManager, "profiles", appProfiles);
        Whitebox.setInternalState(profileManager, "snsAuthService", snsAuthService);

        when(appProfiles.get(appId)).thenReturn(appProfile);
        when(appProfile.getAppId()).thenReturn(appId);
        when(appProfile.getAppName()).thenReturn(appName);
        when(snsAuthService.getUserInfo(appProfile, code, Locale.CHINA.getLanguage())).thenReturn(user);
        when(user.getOpenid()).thenReturn(null);

        User user = profileManager.getUserInfo(appId, code, Locale.CHINA);

        verify(logger, only()).warn(contains("not found"), eq(appId));
    }

    @Test
    @Ignore
    public void findAppProfileByWechatAppId() throws Exception {
        //TODO implement here

    }
}