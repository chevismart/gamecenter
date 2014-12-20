package gamecenter.core.processors;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.constants.CommonConstants;

import java.util.Map;

/**
 * Created by Chevis on 14/12/20.
 */
public class WelcomeProcessor extends ActionSupport {

    UserProfile userProfile;

    @Override
    public String execute() throws Exception {

        userProfile = (UserProfile) getSession().get(CommonConstants.SESSION_KEY_IS_LOGIN_VALID);

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
