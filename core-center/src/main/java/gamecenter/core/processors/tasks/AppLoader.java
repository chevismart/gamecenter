package gamecenter.core.processors.tasks;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.listeners.AbstractRunnable;
import gamecenter.core.processors.wechat.ProfileManager;
import gamecenter.core.utils.XMLMessageConverter;

import java.util.List;

/**
 * Created by Chevis on 2014/12/20.
 */
public class AppLoader extends AbstractRunnable {

    private final static String ROOT_NODE = "applications";

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
                profileManager.addWechatProfile(appProfile.getAppId(),
                        wechatProfile.getWechatAppId(),
                        wechatProfile.getWechatAppSecret(),
                        wechatProfile.getMchid(),
                        wechatProfile.getPayKey());
                profileManager.requestWechatAccessToken(appProfile.getAppId());
                logger.info("Wechat access token is successfully obtained!");
                profileManager.requestWechatJsapiTicket(appProfile.getAppId());
                logger.info("Wechat jsapi ticket is successfully obtained!");
            }
            logger.info("{}({}) is successfully loaded!", appProfile.getAppName(), appProfile.getAppId());
        }


    }
}
