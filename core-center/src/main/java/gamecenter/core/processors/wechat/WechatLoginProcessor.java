package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import gamecenter.core.beans.AccessChannel;
import gamecenter.core.beans.AccessInfo;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.utils.ParameterUtil;
import gamecenter.core.utils.ProfileUtil;
import org.apache.commons.lang3.StringUtils;
import weixin.popular.bean.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Chevis on 14/12/20.
 */
public class WechatLoginProcessor extends ActionSupport {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    ProfileManager profileManager;

    @Override
    public String execute() throws Exception {

        String code = getHttpRequest().getParameter(CommonConstants.WECHAT_AUTH_CODE);
        String state = getHttpRequest().getParameter(CommonConstants.WECHAT_AUTH_STATE);
        logger.info("code = {}, state = {}", code, state);

        Map<String, String> stateParam = ParameterUtil.extractParam(state);
        String appId = stateParam.get(CommonConstants.WECHAT_STATE_PARAM_APPID);

        String result = Action.LOGIN;

        try {
            if (StringUtils.isNotEmpty(appId) &&
                    ProfileUtil.verifyAppProfile(profileManager.getAppProfile(appId)) &&
                    profileManager.getAppProfile(appId).isWechatProfileValid()) {

                User user = profileManager.getUserInfo(appId, code, getHttpRequest().getLocale());

                if (StringUtils.isEmpty(user.getOpenid())) {
                    result = CommonConstants.ACCESS_ROUTER_WECHAT_OAUTH;
                } else {

                    UserProfile userProfile = new UserProfile();

                    AccessInfo accessInfo = new AccessInfo();
                    accessInfo.setAccessChannel(AccessChannel.WECHAT);
                    accessInfo.setAppProfile(profileManager.getAppProfile(appId));

                    // TODO: to retrieve the user from DB by openId, or create profile
                    userProfile.setAccessInfo(accessInfo);
                    userProfile.setDisplayName(user.getNickname());
                    userProfile.setInternalId(ProfileUtil.getUserUnifyId(AccessChannel.WECHAT, user.getOpenid()));
                    userProfile.setUserImgUrl(user.getHeadimgurl());
                    userProfile.setIsFollowed(null != user.getSubscribe() && user.getSubscribe() != 0);
                    userProfile.setDeviceId(stateParam.get(CommonConstants.WECHAT_STATE_PARAM_DEVICEID));
                    getSession().put(CommonConstants.SESSION_KEY_IS_LOGIN_VALID, userProfile);
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

    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
    }

    protected Map<String, Object> getSession() {
        return ActionContext.getContext().getSession();
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }
}
