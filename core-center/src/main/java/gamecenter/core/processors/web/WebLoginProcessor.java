package gamecenter.core.processors.web;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.AccessChannel;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.utils.ProfileUtil;

/**
 * Created by Chevis on 15/3/12.
 */
public class WebLoginProcessor extends GeneralProcessor {
    UserProfile userProfile;
    String userName;
    String userPwd;

    @Override
    public String execute() throws Exception {
        if ("admin".equals(userName) && "admin".equals(userPwd)) {
            userProfile.setInternalId(ProfileUtil.getUserUnifyId(AccessChannel.BROWSER, "admin"));
            return Action.SUCCESS;
        }
        return "login";
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
