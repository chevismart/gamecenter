package gamecenter.core.services.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.Token;

/**
 * Created by Chevis on 14/12/15.
 */
public class AccessTokenService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    TokenAPI wechatAccessTokenApi = new TokenAPI();

    public AccessTokenService(TokenAPI wechatAccessTokenApi) {
        this.wechatAccessTokenApi = wechatAccessTokenApi;
    }

    public Token requestWechatAccessToken(final AppProfile appProfile) {
        Token accessToken;
        String appId = appProfile.getAppId();
        if (appProfile.isWechatProfileValid()) {
            WechatProfile wechatProfile = appProfile.getWechatProfile();
            accessToken = wechatAccessTokenApi.token(wechatProfile.getWechatAppId(), wechatProfile.getWechatAppSecret());
            logger.info("Access token is returned successfully for appId: {}", appId);
        } else {
            logger.warn("AppId({}) is invalid to be requested!", appId);
            accessToken = null;
        }
        return accessToken;
    }

}
