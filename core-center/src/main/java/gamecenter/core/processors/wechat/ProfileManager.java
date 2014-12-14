package gamecenter.core.processors.wechat;

import gamecenter.core.Constants.CommonConstants;
import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.SnsAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.Token;

import java.util.Map;

/**
 * Created by Chevis on 2014/12/11.
 */
public class ProfileManager {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, AppProfile> profiles;
    private SnsAPI wechatSnsApi;
    private TokenAPI wechatAccessTokenApi;

    public ProfileManager(Map<String, AppProfile> profiles) {
        this.profiles = profiles;
        wechatSnsApi = new SnsAPI();
        wechatAccessTokenApi = new TokenAPI();
    }

    public Map<String, AppProfile> getProfiles() {
        return profiles;
    }

    public AppProfile addProfile(String appId, String appName) {
        AppProfile appProfile = new AppProfile(appId, appName);
        profiles.put(appProfile.getAppId(), appProfile);
        return appProfile;
    }

    public void addWechatProfile(String appId, String wechatAppId, String wechatAppSecret) {
        WechatProfile wechatProfile = new WechatProfile();
        wechatProfile.setWechatAppId(wechatAppId);
        wechatProfile.setWechatAppSecret(wechatAppSecret);
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
        if (verifyAppProfile(appProfile) && appProfile.isWechatProfileValid()) {
            WechatProfile wechatProfile = appProfile.getWechatProfile();
            Token accessToken = wechatAccessTokenApi.token(appProfile.getWechatProfile().getWechatAppId(), appProfile.getWechatProfile().getWechatAppSecret());
            wechatProfile.setWechatAccessTokenUpdateTime(TimeUtil.getCurrentDateTime());
            accessToken.setExpires_in(CommonConstants.DEFAULT_WECHAT_ACCESS_TOKEN_EXPIRY_TIME_IN_SECOND);
            wechatProfile.setWechatAccessToken(accessToken);
            logger.info("Access token is updated successfully for appId {}", appId);
            System.err.println(accessToken.getAccess_token());
        } else {
            logger.warn("AppId {} is invalid to be requested!", appId);
            return null;
        }
        return appProfile.getWechatProfile().getWechatAccessToken();
    }

    public void checkAndUpdateAllAccessToken() {
        for (AppProfile appProfile : profiles.values()) {
            chevkWechatProfile(appProfile);
        }
    }

    private void chevkWechatProfile(AppProfile appProfile) {
        WechatProfile wechatProfile = appProfile.getWechatProfile();
        if (verifyAppProfile(appProfile) &&
                appProfile.isWechatProfileValid() &&
                TimeUtil.isExpiry(TimeUtil.getCurrentDateTime(),
                        TimeUtil.getExpiryDateTime(wechatProfile.getWechatAccessTokenUpdateTime(),
                                CommonConstants.DEFAULT_WECHAT_ACCESS_TOKEN_EXPIRY_TIME_IN_SECOND
                        ))) {
            String appId = appProfile.getAppId();
            logger.info("Updating wechat access token for appId {}", appId);
            requestWechatAccessToken(appId);
        }
    }

    public void updateAllProfiles() {
        for (AppProfile appProfile : profiles.values()) {
            if (verifyAppProfile(appProfile)) {
                logger.info("{} is ready for update!", appProfile);
                refreshWechatAccessToken(appProfile);
            }
        }
    }

    private void refreshWechatAccessToken(AppProfile appProfile) {
        if (appProfile.isWechatProfileValid()) {
//            appProfile.setWechatProfile(wechatSnsApi.oauth2RefreshToken(appProfile.getWechatProfile().getWechatAppId(),appProfile.getWechatProfile().getWechatSnsToken().getRefresh_token()));
//            WechatProfile wechatProfile = wechatSnsApi.oauth2AccessToken(wechatProfile.getWechatAppId(),wechatProfile.getWechatAppSecret(),);
            // TODO: to be implemented
        } else {
            logger.warn("Wechat is not valid to be updated.");
        }
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
