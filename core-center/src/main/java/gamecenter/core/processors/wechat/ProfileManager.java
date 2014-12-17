package gamecenter.core.processors.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.builders.AppProfileBuilder;
import gamecenter.core.beans.builders.WechatProfileBuilder;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.services.wechat.AccessTokenService;
import gamecenter.core.services.wechat.SnsAuthService;
import gamecenter.core.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.SnsAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.Token;
import weixin.popular.bean.User;

import java.util.Locale;
import java.util.Map;

/**
 * Created by Chevis on 2014/12/11.
 */
public class ProfileManager {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    AccessTokenService accessTokenService;
    SnsAuthService snsAuthService;
    private Map<String, AppProfile> profiles;

    public ProfileManager(Map<String, AppProfile> profiles) {
        this.profiles = profiles;
        accessTokenService = new AccessTokenService(new TokenAPI());
        snsAuthService = new SnsAuthService(new SnsAPI());
    }

    public Map<String, AppProfile> getProfiles() {
        return profiles;
    }

    public AppProfile addAppProfile(String appId, String appName) {
        AppProfile appProfile = AppProfileBuilder.newBuilder().appId(appId).appName(appName).build();
        profiles.put(appProfile.getAppId(), appProfile);
        return appProfile;
    }

    public void addWechatProfile(String appId, String wechatAppId, String wechatAppSecret) {

        WechatProfile wechatProfile = WechatProfileBuilder.newBuilder()
                .wechatAppIdBuilder(wechatAppId)
                .WechatAppSecretBuilder(wechatAppSecret)
                .build();
        AppProfile appProfile = profiles.get(appId);
        if (verifyAppProfile(appProfile)) {
            appProfile.setWechatProfile(wechatProfile);
            logger.info("{} appId is added successfully", appId);
        } else {
            logger.warn("{} appId is not setup.", appId);
        }
    }

    public Token requestWechatAccessToken(String appId) {
        AppProfile appProfile = profiles.get(appId);
        if (verifyAppProfile(appProfile)) {
            accessTokenService.requestWechatAccessToken(appProfile);
        } else {
            logger.warn("AppId {} is invalid to be requested!", appId);
            return null;
        }
        return appProfile.getWechatProfile().getWechatAccessToken();
    }

    public User getUserInfo(String appId, String code, Locale locale) {
        User user = null;
        AppProfile appProfile = profiles.get(appId);
        if (verifyAppProfile(appProfile)) {
            user = snsAuthService.getUserInfo(appProfile, code, locale.getLanguage());
        }
        if (null == user)
            logger.warn("User info is not found for {}", appId); // TODO: enhance the method to check the user is empty or not
        return user;
    }

    public void checkAndUpdateAllAccessToken() {
        for (AppProfile appProfile : profiles.values()) {
            chevkWechatProfile(appProfile);
        }
    }

    private void chevkWechatProfile(AppProfile appProfile) {
        WechatProfile wechatProfile = appProfile.getWechatProfile();
        if (verifyAppProfile(appProfile) &&
                appProfile.isWechatProfileValid() && (null == wechatProfile.getWechatAccessToken() ||
                TimeUtil.isExpiry(TimeUtil.getCurrentDateTime(),
                        TimeUtil.getExpiryDateTime(wechatProfile.getWechatAccessTokenUpdateTime(),
                                getExpiryInSecond(CommonConstants.DEFAULT_WECHAT_ACCESS_TOKEN_EXPIRY_TIME_IN_SECOND,
                                        wechatProfile.getWechatAccessToken().getExpires_in()),
                                CommonConstants.EXPIRY_SHIFT_PERIOD_IN_SECOND
                        )))) {
            String appId = appProfile.getAppId();
            logger.info("Updating wechat access token for appId {}", appId);
            requestWechatAccessToken(appId);
        }
    }

    private int getExpiryInSecond(int defaultPeriod, int actualPeriod) {
        return actualPeriod <= 0 ? defaultPeriod : actualPeriod;
    }

    private boolean verifyAppProfile(AppProfile appProfile) {
        return null != appProfile &&
                StringUtils.isNotEmpty(appProfile.getAppId()) &&
                StringUtils.isNotEmpty(appProfile.getAppName());
    }

    public AppProfile getAppProfile(String appId) {
        return profiles.get(appId);
    }
}
