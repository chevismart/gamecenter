package gamecenter.core.processors;

import gamecenter.core.beans.wechat.Profile;
import gamecenter.core.processors.wechat.ProfileManager;

/**
 * Created by Chevis on 2014/12/7.
 */
public class DemoAction {

    private ProfileManager profileManager;

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public String hello(){

        Profile profile = new Profile();
        profile.setName("ChevisTest");
        profile.setAppId("appId");
        profile.setAppSecret("appsec");

        profileManager.addProfile(profile);
        profileManager.updateAllProfiles();

        return "success";
    }
}
