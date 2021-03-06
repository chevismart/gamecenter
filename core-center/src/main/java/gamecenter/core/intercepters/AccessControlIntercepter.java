package gamecenter.core.intercepters;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.constants.ActionResults;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Chevis on 2014/12/16.
 */
public class AccessControlIntercepter extends AbstractInterceptor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserProfile userProfile;

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {

        String result;
        if (isLoginValid()) {
            result = ActionResults.AUTHENTIC;
        } else {
            result = ActionResults.LOGIN_REQUIRE;
        }

        return result;

    }

    private boolean isLoginValid() {
        return StringUtils.isNotEmpty(userProfile.getInternalId());

    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
