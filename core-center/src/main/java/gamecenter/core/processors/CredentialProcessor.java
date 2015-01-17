package gamecenter.core.processors;


import com.opensymphony.xwork2.ActionContext;
import gamecenter.core.beans.AccessChannel;
import gamecenter.core.beans.AppProfile;
import gamecenter.core.processors.wechat.ProfileManager;
import org.apache.struts2.StrutsStatics;
import weixin.popular.bean.Token;
import weixin.popular.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by Chevis on 2015/1/7.
 */
public class CredentialProcessor extends GeneralProcessor {
    ProfileManager profileManager;

    @Override
    public String execute() throws Exception {
        logger.info("Receive credential request.");
        String clientId = getHttpRequest().getParameter("clientId").trim();
        String appId = getHttpRequest().getParameter("appId").trim();
        AccessChannel channel = AccessChannel.valueOf(getHttpRequest().getParameter("credentialType").trim());
        Token token = null;
        if (clientId.startsWith("client")) {
            AppProfile appProfile = profileManager.getAppProfile(appId);

            if (null != appProfile && channel.equals(AccessChannel.WECHAT) && appProfile.isWechatProfileValid()) {
                token = appProfile.getWechatProfile().getWechatAccessToken();
            }

            String json = JsonUtil.toJSONString(token);

            HttpServletResponse response = getHttpResponse();
            response.setContentType("text/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.println(json);

            out.flush();
            out.close();
        }

        return null;
    }

    protected HttpServletResponse getHttpResponse() {
        return (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
    }

    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }
}
