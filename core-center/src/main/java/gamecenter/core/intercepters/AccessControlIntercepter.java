package gamecenter.core.intercepters;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import gamecenter.core.constants.ActionResults;
import gamecenter.core.constants.CommonConstants;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Chevis on 2014/12/16.
 */
public class AccessControlIntercepter extends AbstractInterceptor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);

        String result;
        if (isLoginValid(session)) {
            result = ActionResults.AUTHENTIC;
        } else {
            result = ActionResults.LOGIN_REQUIRE;
        }

        return result;

    }

    private boolean isLoginValid(Map<String, Object> session) {
        return null != session.get(CommonConstants.SESSION_KEY_IS_LOGIN_VALID);
    }



}
