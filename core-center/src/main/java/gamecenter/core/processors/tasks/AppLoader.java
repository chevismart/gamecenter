package gamecenter.core.processors.tasks;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.processors.wechat.ProfileManager;
import gamecenter.core.utils.XMLMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.bean.token.Token;

import java.util.List;

public class AppLoader implements Runnable {
    private final static String ROOT_NODE = "applications";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProfileManager profileManager;

    public AppLoader(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public void run() {

        XMLMessageConverter converter = new XMLMessageConverter(ROOT_NODE);
        List<AppProfile> appProfileList = converter.convertXML2Messages(this.getClass().getClassLoader().getResourceAsStream("apps/appProfiles.xml"));

        for (AppProfile appProfile : appProfileList) {
            profileManager.addAppProfile(appProfile.getAppId(), appProfile.getAppName());
            WechatProfile wechatProfile = appProfile.getWechatProfile();
            if (null != wechatProfile) {
                logger.info("Ready for requesting the wechat access token with wechat profile: {}", wechatProfile);
                profileManager.addWechatProfile(appProfile.getAppId(),
                        wechatProfile.getWechatAppId(),
                        wechatProfile.getWechatAppSecret(),
                        wechatProfile.getMchid(),
                        wechatProfile.getPayKey(),
                        wechatProfile.getInitId());
                Token token = profileManager.requestWechatAccessToken(appProfile.getAppId());
                if (null != token) {
                    logger.info("Wechat access token is successfully obtained!");
                }
                String jsapiTicket = profileManager.requestWechatJsapiTicket(appProfile.getAppId());
                if (null != jsapiTicket) {
                    logger.info("Wechat jsapi ticket is successfully obtained!");
                }
            }
            logger.info("{}({}) is successfully loaded!", appProfile.getAppName(), appProfile.getAppId());
        }
    }
}
