package gamecenter.core.processors.tasks;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.listeners.AbstractRunnable;
import gamecenter.core.processors.wechat.ProfileManager;
import gamecenter.core.utils.XMLMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Chevis on 2014/12/20.
 */
public class AppLoader extends AbstractRunnable {

    private final static String ROOT_NODE = "applications";
    Logger logger = LoggerFactory.getLogger(this.getClass());
    ProfileManager profileManager;

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public void run() {

        XMLMessageConverter converter = new XMLMessageConverter(ROOT_NODE);
        List<AppProfile> appProfileList = converter.convertXML2Messages(this.getClass().getClassLoader().getResourceAsStream("apps/appProfiles.xml"));

        for (AppProfile appProfile : appProfileList) {
            profileManager.addAppProfile(appProfile.getAppId(), appProfile.getAppName());
            if (null != appProfile.getWechatProfile()) {
                profileManager.addWechatProfile(appProfile.getAppId(), appProfile.getWechatProfile().getWechatAppId(), appProfile.getWechatProfile().getWechatAppSecret());
                profileManager.requestWechatAccessToken(appProfile.getAppId());
                logger.info("Wechat access token is successfully obtained!");
            }
            logger.info("{}({}) is successfully loaded!", appProfile.getAppName(), appProfile.getAppId());
        }


    }
}
