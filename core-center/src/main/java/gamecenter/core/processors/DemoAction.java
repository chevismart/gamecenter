package gamecenter.core.processors;

import com.opensymphony.xwork2.Action;
import gamecenter.core.processors.wechat.ProfileManager;

/**
 * Created by Chevis on 2014/12/7.
 */
public class DemoAction extends GeneralProcessor {

    private ProfileManager profileManager;

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public String execute() throws Exception {
        return Action.SUCCESS;
    }
}
