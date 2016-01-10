package gamecenter.core.services.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.services.Service;
import org.apache.commons.lang3.StringUtils;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.user.User;

import static weixin.popular.api.SnsAPI.*;
import static weixin.popular.api.UserAPI.userInfo;

/**
 * Created by Chevis on 2014/12/15.
 */
public class SnsAuthService extends Service {
    private static boolean IS_USER_INFO_CACHED = false;
    private SnsAPI wechatSnsApi;

    public SnsAuthService(SnsAPI wechatSnsApi) {
        this.wechatSnsApi = wechatSnsApi;
    }

    private boolean isUserInfoCached(WechatProfile wechatProfile, String openId) {
        return IS_USER_INFO_CACHED ? wechatProfile.getActiveUserList().keySet().contains(openId) : false;
    }

    public User getUserInfo(AppProfile appProfile, String code, String local) {
        User userInfo = null;
        if (appProfile.isWechatProfileValid()) {
            WechatProfile wechatProfile = appProfile.getWechatProfile();
            SnsToken snsToken = oauth2AccessToken(wechatProfile.getWechatAppId(), wechatProfile.getWechatAppSecret(), code);

            userInfo = getUser(appProfile, local, wechatProfile, snsToken.getOpenid(), snsToken.getAccess_token());

        }
        return userInfo;
    }

    protected User getUser(AppProfile appProfile, String local, WechatProfile wechatProfile, String openId, String accessToken) {
        User userInfo;
        if (isUserInfoCached(appProfile.getWechatProfile(), openId)) {
            userInfo = wechatProfile.getActiveUserList().get(openId);
            logForUserInfoRetrieval("cache", userInfo);
        } else {
            userInfo = userInfo(wechatProfile.getWechatAccessToken().getAccess_token(), openId);
            if (!isUserValid(userInfo)) {
                userInfo = wechatSnsApi.userinfo(accessToken, openId, local);
            } else {
                wechatProfile.getActiveUserList().put(openId, userInfo);
            }
            logForUserInfoRetrieval("wechat server", userInfo);
        }
        return userInfo;
    }

    public User getUserInfo(String openId, AppProfile appProfile, String locale) {
        User user = null;
        if (null != appProfile && StringUtils.isNotEmpty(openId)) {
            user = getUser(appProfile, locale, appProfile.getWechatProfile(), openId, "");
        }
        logger.debug("User is null? [{}]", user == null);
        return user;
    }

    private void logForUserInfoRetrieval(String channel, User userInfo) {
        logger.info("User({}:{}) info is retrieved from {}.", userInfo.getNickname(), userInfo.getOpenid(), channel);
    }

    private boolean isUserValid(User user) {
        return StringUtils.isNotEmpty(user.getNickname());
    }

}
