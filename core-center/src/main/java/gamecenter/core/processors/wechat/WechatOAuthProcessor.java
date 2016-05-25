package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.AppProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.utils.ProfileUtil;
import weixin.popular.api.SnsAPI;

import java.util.Map;

import static gamecenter.core.beans.CoreCenterHost.AUTH_URL;
import static gamecenter.core.beans.CoreCenterHost.getHttpURL;
import static gamecenter.core.constants.CommonConstants.*;
import static gamecenter.core.utils.ParameterUtil.extractParam;
import static org.apache.commons.lang3.StringUtils.*;

public class WechatOAuthProcessor extends GeneralProcessor {

    ProfileManager profileManager;

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public String execute() throws Exception {
        String url = getRedirectUrl(getHttpRequest().getParameter(WECHAT_AUTH_STATE));

        if (isNotEmpty(url)) getHttpResponse().sendRedirect(url);

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
            result = SnsAPI.connectOauth2Authorize(applicationId, getUri(optionalUrl), true, state);
        }
        return result;
    }

    private String getUri(String optionalUrl) {
        return isEmpty(optionalUrl) ? getHttpURL(AUTH_URL) : optionalUrl;
    }
}
