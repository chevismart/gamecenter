package gamecenter.core.processors;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.wechat.ProfileManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Chevis on 2014/12/13.
 */
public class AccessRouterProcessor extends ActionSupport {

    private ProfileManager profileManager;

    @Override
    public String execute() throws Exception {

        return super.execute();
    }

    private boolean isWechatEntry() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
        String code = request.getParameter(CommonConstants.WECHAT_AUTH_CODE);
        String state = request.getParameter(CommonConstants.WECHAT_AUTH_STATE);
        return request.getParameterMap().keySet().contains(CommonConstants.WECHAT_AUTH_STATE) && request.getParameterMap().size() <= 2;
    }
}
