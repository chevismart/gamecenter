package gamecenter.core.processors;

import gamecenter.core.beans.UserProfile;

public class PocketProcessor extends GeneralProcessor {
    private final UserProfile userProfile;

    public PocketProcessor(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
