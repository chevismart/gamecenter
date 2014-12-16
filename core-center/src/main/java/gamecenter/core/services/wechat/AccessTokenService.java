package gamecenter.core.services.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.utils.TimeUtil;
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

    public Token requestWechatAccessToken(AppProfile appProfile) {

        String appId = appProfile.getAppId();
        if (appProfile.isWechatProfileValid()) {
            WechatProfile wechatProfile = appProfile.getWechatProfile();
            Token accessToken = wechatAccessTokenApi.token(appProfile.getWechatProfile().getWechatAppId(), appProfile.getWechatProfile().getWechatAppSecret());
            wechatProfile.setWechatAccessTokenUpdateTime(TimeUtil.getCurrentDateTime());
            wechatProfile.setWechatAccessToken(accessToken);
            logger.info("Access token is updated successfully for appId: {}", appId);
            System.err.println(accessToken.getAccess_token());
        } else {
            logger.warn("AppId({}) is invalid to be requested!", appId);
            return null;
        }
        return appProfile.getWechatProfile().getWechatAccessToken();
    }

}
