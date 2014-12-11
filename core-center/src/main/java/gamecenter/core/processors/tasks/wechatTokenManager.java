package gamecenter.core.processors.tasks;

import gamecenter.core.listeners.AbstractRunnable;
import gamecenter.core.processors.wechat.ProfileManager;
import org.springframework.scheduling.TaskScheduler;

/**
 * Created by Chevis on 2014/12/11.
 */
public class wechatTokenManager extends AbstractRunnable {

    private ProfileManager profileManager;

    public wechatTokenManager(ProfileManager profileManager) {

        this.profileManager = profileManager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public void run() {

    }
}
