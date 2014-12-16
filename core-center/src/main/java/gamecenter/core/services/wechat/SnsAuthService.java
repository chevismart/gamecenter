package gamecenter.core.services.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.SnsToken;
import weixin.popular.bean.User;

/**
 * Created by Chevis on 2014/12/15.
 */
public class SnsAuthService {
    private static boolean IS_USER_INFO_CACHED = false;
    Logger logger = LoggerFactory.getLogger(this.getClass());
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
            SnsToken snsToken = wechatSnsApi.oauth2AccessToken(wechatProfile.getWechatAppId(), wechatProfile.getWechatAppSecret(), code);

            if (isUserInfoCached(appProfile.getWechatProfile(), snsToken.getOpenid())) {
                userInfo = wechatProfile.getActiveUserList().get(snsToken.getOpenid());
            } else {
                userInfo = wechatSnsApi.userinfo(snsToken.getAccess_token(), snsToken.getOpenid(), local);
                wechatProfile.getActiveUserList().put(snsToken.getOpenid(), userInfo);
            }
            logger.debug("User info is retrieved: {}", userInfo);
        }
        return userInfo;
    }

}
