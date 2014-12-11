package gamecenter.core.processors.wechat;

import gamecenter.core.beans.wechat.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by Chevis on 2014/12/11.
 */
public class ProfileManager {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, Profile> profiles;

    public ProfileManager(Map<String, Profile> profiles) {
        this.profiles = profiles;
    }

    public Map<String, Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<String, Profile> profiles) {
        this.profiles = profiles;
    }

    public void addProfile(Profile profile) {
        profiles.put(profile.getName(), profile);
    }

    public void updateAllProfiles() {
        for (Profile profile : profiles.values()) {
            if (verifyProfile(profile)) {
                logger.info("{} is ready for update!", profile);
                updateProfile(profile);
            }
        }
    }

    private void updateProfile(Profile profile) {
        // TODO: to be implemented
    }

    private boolean verifyProfile(Profile profile) {
        logger.debug("Checking expiry for {}", profile);
        return true; // TODO: to be implemented
    }
}
