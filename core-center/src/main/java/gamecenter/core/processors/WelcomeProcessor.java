package gamecenter.core.processors;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.beans.UserProfile;

import java.util.Map;

/**
 * Created by Chevis on 14/12/20.
 */
public class WelcomeProcessor extends ActionSupport {

    UserProfile userProfile;

    @Override
    public String execute() throws Exception {

        return Action.SUCCESS;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    protected Map<String, Object> getSession() {
        return ActionContext.getContext().getSession();
    }

}
