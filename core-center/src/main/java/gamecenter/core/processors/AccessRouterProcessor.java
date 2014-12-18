package gamecenter.core.processors;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.beans.AccessChannel;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.wechat.ProfileManager;
import gamecenter.core.utils.ParameterUtil;
import gamecenter.core.utils.ProfileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.bean.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Chevis on 2014/12/13.
 */
public class AccessRouterProcessor extends ActionSupport {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ProfileManager profileManager;

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
    }

    protected Map<String, Object> getSession() {
        return ActionContext.getContext().getSession();
    }

    @Override
    public String execute() throws Exception {

        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
        Map<String, Object> session = ActionContext.getContext().getSession();

        String result = Action.ERROR;
        UserProfile user = (UserProfile) session.get(CommonConstants.SESSION_KEY_IS_LOGIN_VALID);
        AccessChannel accessChannel = AccessChannel.BROWSER;
        if (user != null) {
            accessChannel = user.getAccessChannel();

            switch (accessChannel) {
                case BROWSER:
                    result = Action.LOGIN;
                    break;
                case WECHAT:
                    result = handleWechatAccess(user);
                    break;
                default:
                    result = Action.LOGIN;
            }
        }

        return result;

    }

    private String handleWechatAccess(UserProfile userProfile) {

        String result = Action.LOGIN;

        String code = getHttpRequest().getParameter(CommonConstants.WECHAT_AUTH_CODE);
        String state = getHttpRequest().getParameter(CommonConstants.WECHAT_AUTH_STATE);
        logger.info("code = {}, state = {}", code, state);

        Map<String, String> stateParam = ParameterUtil.extractParam(state);
        String appId = stateParam.get(CommonConstants.WECHAT_STATE_PARAM_APPID);

        try {
            if (StringUtils.isNotEmpty(appId) &&
                    ProfileUtil.verifyAppProfile(profileManager.getAppProfile(appId)) &&
                    profileManager.getAppProfile(appId).isWechatProfileValid()) {

                User user = profileManager.getUserInfo(appId, code, getHttpRequest().getLocale());

                if (StringUtils.isEmpty(user.getOpenid())) {
                    result = CommonConstants.ACCESS_ROUTER_WECHAT_OAUTH;
                } else {

                    // TODO: to retrieve the user from DB by openId, or create profile
                    userProfile.setDisplayName(user.getNickname());
                    userProfile.setInternalId(ProfileUtil.getUserUnifyId(userProfile.getAccessChannel(), user.getOpenid()));
                    userProfile.setUserImgUrl(user.getHeadimgurl());
                    profileManager.getAppProfile(appId).getWechatProfile().getActiveUserList().put(user.getOpenid(), user);
                    result = Action.SUCCESS;
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            return Action.ERROR;
        }

        return result;
    }
}
