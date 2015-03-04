package gamecenter.core.services.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.services.Service;
import org.apache.commons.lang3.StringUtils;
import weixin.popular.api.SnsAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.SnsToken;
import weixin.popular.bean.User;

/**
 * Created by Chevis on 2014/12/15.
 */
public class SnsAuthService extends Service {
    private static boolean IS_USER_INFO_CACHED = false;
    private SnsAPI wechatSnsApi;
    private UserAPI userAPI;

    public SnsAuthService(SnsAPI wechatSnsApi, UserAPI userAPI) {
        this.wechatSnsApi = wechatSnsApi;
        this.userAPI = userAPI;
    }

    private boolean isUserInfoCached(WechatProfile wechatProfile, String openId) {
        return IS_USER_INFO_CACHED ? wechatProfile.getActiveUserList().keySet().contains(openId) : false;
    }

    public User getUserInfo(AppProfile appProfile, String code, String local) {
        User userInfo = null;
        if (appProfile.isWechatProfileValid()) {
            WechatProfile wechatProfile = appProfile.getWechatProfile();
            SnsToken snsToken = wechatSnsApi.oauth2AccessToken(wechatProfile.getWechatAppId(), wechatProfile.getWechatAppSecret(), code);

            if (isUserInfoCached(appProfile.getWechatProfile(), snsToken.getOpenid())) {
                userInfo = wechatProfile.getActiveUserList().get(snsToken.getOpenid());
                logForUserInfoRetrieval("cache", userInfo);
            } else {
                userInfo = userAPI.userInfo(wechatProfile.getWechatAccessToken().getAccess_token(), snsToken.getOpenid());
                if (!isUserValid(userInfo)) {
                    userInfo = wechatSnsApi.userinfo(snsToken.getAccess_token(), snsToken.getOpenid(), local);
                } else {
                    wechatProfile.getActiveUserList().put(snsToken.getOpenid(), userInfo);
                }
                logForUserInfoRetrieval("wechat server", userInfo);
            }

        }
        return userInfo;
    }

    private void logForUserInfoRetrieval(String channel, User userInfo) {
        logger.info("User({}:{}) info is retrieved from {}.", userInfo.getNickname(), userInfo.getOpenid(), channel);
    }

    private boolean isUserValid(User user) {
        return StringUtils.isNotEmpty(user.getNickname());
    }

}
