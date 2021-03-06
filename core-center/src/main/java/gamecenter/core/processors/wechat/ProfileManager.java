package gamecenter.core.processors.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.builders.AppProfileBuilder;
import gamecenter.core.beans.builders.WechatProfileBuilder;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.services.wechat.AccessTokenService;
import gamecenter.core.services.wechat.JsApiTicketService;
import gamecenter.core.services.wechat.SnsAuthService;
import gamecenter.core.utils.ProfileUtil;
import gamecenter.core.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.SnsAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.api.UserAPI;
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
    JsApiTicketService jsApiTicketService;
    private Map<String, AppProfile> profiles;
    private Boolean isHost;

    public ProfileManager(Map<String, AppProfile> profiles) {
        this.profiles = profiles;
        accessTokenService = new AccessTokenService(new TokenAPI());
        snsAuthService = new SnsAuthService(new SnsAPI(), new UserAPI());
        jsApiTicketService = new JsApiTicketService();
    }

    public Map<String, AppProfile> getProfiles() {
        return profiles;
    }

    public AppProfile addAppProfile(String appId, String appName) {
        AppProfile appProfile = AppProfileBuilder.newBuilder().appId(appId).appName(appName).build();
        profiles.put(appProfile.getAppId(), appProfile);
        return appProfile;
    }

    public AppProfile findAppProfileByWechatId(String appId) {
        for (Map.Entry<String, AppProfile> entry : profiles.entrySet()) {
            AppProfile appProfile = entry.getValue();
            if (appProfile.isWechatProfileValid() &&
                    appProfile.getWechatProfile().getWechatAppId().equals(appId))
                return appProfile;
        }
        return null;
    }

    public void addWechatProfile(String appId, String wechatAppId, String wechatAppSecret, String mchid, String payKey) {

        WechatProfile wechatProfile = WechatProfileBuilder.newBuilder()
                .wechatAppId(wechatAppId)
                .wechatAppSecret(wechatAppSecret)
                .mchid(mchid)
                .payKey(payKey)
                .build();
        AppProfile appProfile = profiles.get(appId);
        if (ProfileUtil.verifyAppProfile(appProfile)) {
            appProfile.setWechatProfile(wechatProfile);
            logger.info("{} appId is added successfully", appId);
        } else {
            logger.warn("{} appId is not setup.", appId);
        }
    }

    public Token requestWechatAccessToken(String appId) {
        AppProfile appProfile = profiles.get(appId);
        if (ProfileUtil.verifyAppProfile(appProfile)) {
            appProfile.getWechatProfile().setWechatAccessToken(getToken(appProfile));
        } else {
            logger.warn("AppId {} is invalid to be requested!", appId);
            return null;
        }
        return appProfile.getWechatProfile().getWechatAccessToken();
    }

    private Token getToken(AppProfile appProfile) {
        return isHost ?
                accessTokenService.requestWechatAccessToken(appProfile) :
                accessTokenService.requestWechatAccessTokenFromHost(appProfile);
    }

    public String requestWechatJsapiTicket(String appId) {
        AppProfile appProfile = profiles.get(appId);
        if (ProfileUtil.verifyAppProfile(appProfile)) {
            String jsApiTicket = jsApiTicketService.requestWechatJsApiTicket(appProfile);
            appProfile.getWechatProfile().setWechatJsapiTicket(jsApiTicket);
        } else {
            logger.warn("AppId {} is invalid to be requested!", appId);
            return null;
        }
        return appProfile.getWechatProfile().getWechatJsapiTicket();
    }


    public User getUserInfo(String appId, String code, Locale locale) {
        User user = null;
        AppProfile appProfile = profiles.get(appId);
        if (ProfileUtil.verifyAppProfile(appProfile)) {
            user = snsAuthService.getUserInfo(appProfile, code, locale.getLanguage());
        }
        if (!isUserValid(user)) {
            logger.warn("User info is not found for {}", appId);
        }
        return user;
    }

    public void checkAndUpdateAllAccessToken() {
        for (AppProfile appProfile : profiles.values()) {
            chevkWechatProfile(appProfile);
        }
    }

    private boolean isUserValid(User user) {
        return null != user && StringUtils.isNotEmpty(user.getOpenid());
    }

    private void chevkWechatProfile(AppProfile appProfile) {
        WechatProfile wechatProfile = appProfile.getWechatProfile();
        if (ProfileUtil.verifyAppProfile(appProfile) &&
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
        //检查jsapiticket是否过时
        if (ProfileUtil.verifyAppProfile(appProfile) &&
                appProfile.isWechatProfileValid() && (null == wechatProfile.getWechatAccessToken() ||
                TimeUtil.isExpiry(TimeUtil.getCurrentDateTime(),
                        TimeUtil.getExpiryDateTime(wechatProfile.getWechatJsapiTicketUpdateTime(),
                                CommonConstants.DEFAULT_WECHAT_JSAPI_TICKET_EXPIRY_TIME_IN_SECOND,
                                CommonConstants.EXPIRY_SHIFT_PERIOD_IN_SECOND
                        )))) {
            String appId = appProfile.getAppId();
            logger.info("Updating wechat jsapiTicket for appId {}", appId);
            requestWechatJsapiTicket(appId);
        }
    }

    private int getExpiryInSecond(int defaultPeriod, int actualPeriod) {
        return actualPeriod <= 0 ? defaultPeriod : actualPeriod;
    }

    public Boolean getIsHost() {
        return isHost;
    }

    public void setIsHost(Boolean isHost) {
        this.isHost = isHost;
    }

    public AppProfile getAppProfile(String appId) {
        return profiles.get(appId);
    }
}
