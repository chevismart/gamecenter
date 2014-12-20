package gamecenter.core.services.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.SnsAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.SnsToken;
import weixin.popular.bean.User;

/**
 * Created by Chevis on 2014/12/15.
 */
public class SnsAuthService {
    private static boolean IS_USER_INFO_CACHED = false;
    Logger logger = LoggerFactory.getLogger(this.getClass());
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
            } else {
                userInfo = userAPI.userInfo(wechatProfile.getWechatAccessToken().getAccess_token(), snsToken.getOpenid());
                if (!isUserValid(userInfo)) {
                    userInfo = wechatSnsApi.userinfo(snsToken.getAccess_token(), snsToken.getOpenid(), local);
                } else {
                    wechatProfile.getActiveUserList().put(snsToken.getOpenid(), userInfo);
                }
            }
            logger.debug("User info is retrieved: {}", userInfo);
        }
        return userInfo;
    }

    private boolean isUserValid(User user) {
        return StringUtils.isNotEmpty(user.getNickname());
    }

}
