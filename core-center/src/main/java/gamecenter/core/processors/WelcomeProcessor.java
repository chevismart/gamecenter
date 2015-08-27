package gamecenter.core.processors;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import gamecenter.core.beans.UserProfile;

import java.util.Map;

public class WelcomeProcessor extends GeneralProcessor {

    private final UserProfile userProfile;

    public WelcomeProcessor(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public String execute() throws Exception {

        return Action.SUCCESS;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    protected Map<String, Object> getSession() {
        return ActionContext.getContext().getSession();
    }

}
