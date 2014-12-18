package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.beans.AppProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.utils.ParameterUtil;
import gamecenter.core.utils.ProfileUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Chevis on 2014/12/16.
 */
public class WechatOAuthProcessor extends ActionSupport {

    ProfileManager profileManager;

    private String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid=%s" +
            "&" +
            "redirect_uri=http://alcock.gicp.net:8888/auth" +
            "&" +
            "response_type=code" +
            "&" +
            "scope=snsapi_userinfo" +
            "&" +
            "state=%s" +
            "#wechat_redirect";

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
        String state = request.getParameter(CommonConstants.WECHAT_AUTH_STATE);

        String url = getRedirectUrl(state);

        if (StringUtils.isNotEmpty(url)) response.sendRedirect(url);

        return Action.ERROR;
    }

    protected String getRedirectUrl(String state) {
        Map<String, String> params = ParameterUtil.extractParam(state);
        String result = StringUtils.EMPTY;
        String appId = params.get(CommonConstants.WECHAT_STATE_PARAM_APPID);

        AppProfile appProfile = profileManager.getAppProfile(appId);
        if (ProfileUtil.verifyAppProfile(appProfile) && appProfile.isWechatProfileValid()) {
            String applicationId = profileManager.getAppProfile(appId).getWechatProfile().getWechatAppId();
            result = String.format(oauthUrl, applicationId, state);
        }
        return result;
    }
}
