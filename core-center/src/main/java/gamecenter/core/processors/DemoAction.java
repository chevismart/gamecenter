package gamecenter.core.processors;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import gamecenter.core.processors.wechat.ProfileManager;

/**
 * Created by Chevis on 2014/12/7.
 */
public class DemoAction extends ActionSupport {

    private ProfileManager profileManager;

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public String hello(){

        String appId = "chevisappid";
        profileManager.addProfile("chevisApp", appId);
        profileManager.addWechatProfile(appId, "wxe89a9d2fa17df80f", "71d8fc7778571e6b54712953b68084e4");
//        profileManager.requestWechatAccessToken("chevisappid");

        LoggerFactory.getLogger(this.getClass()).error(profileManager.getAppProfile(appId).toString());

        return ActionSupport.SUCCESS;
    }

    @Override
    public String execute() throws Exception {
        return hello();
    }
}
