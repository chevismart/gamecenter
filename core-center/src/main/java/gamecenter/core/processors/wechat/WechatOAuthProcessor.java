package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import gamecenter.core.beans.AppProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.utils.ProfileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static gamecenter.core.beans.CoreCenterHost.AUTH_URL;
import static gamecenter.core.beans.CoreCenterHost.getHttpURL;
import static gamecenter.core.constants.CommonConstants.*;
import static gamecenter.core.utils.ParameterUtil.extractParam;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.struts2.StrutsStatics.HTTP_REQUEST;
import static org.apache.struts2.StrutsStatics.HTTP_RESPONSE;

public class WechatOAuthProcessor extends GeneralProcessor {

    ProfileManager profileManager;

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(HTTP_RESPONSE);
        String url = getRedirectUrl(request.getParameter(WECHAT_AUTH_STATE));

        if (isNotEmpty(url)) response.sendRedirect(url);

        return Action.ERROR;
    }

    protected String getRedirectUrl(String state) {
        Map<String, String> params = extractParam(state);
        String result = EMPTY;
        String appId = params.get(WECHAT_STATE_PARAM_APPID);
        String optionalUrl = params.get(OPTIONAL_URL);

        AppProfile appProfile = profileManager.getAppProfile(appId);
        if (ProfileUtil.verifyAppProfile(appProfile) && appProfile.isWechatProfileValid()) {
            String applicationId = profileManager.getAppProfile(appId).getWechatProfile().getWechatAppId();
            String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                    "appid=%s" +
                    "&" +
                    "redirect_uri=%s" +
                    "&" +
                    "response_type=code" +
                    "&" +
                    "scope=snsapi_userinfo" +
                    "&" +
                    "state=%s" +
                    "#wechat_redirect";
            result = String.format(oauthUrl, applicationId, getUri(optionalUrl), state);
        }
        return result;
    }

    private String getUri(String optionalUrl) {
        return isEmpty(optionalUrl) ? getHttpURL(AUTH_URL) : optionalUrl;
    }
}
