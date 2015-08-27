package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.AccessChannel;
import gamecenter.core.beans.AccessInfo;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.GeneralLoginInterface;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.db.SubscribeService;
import gamecenter.core.services.db.UserService;
import gamecenter.core.utils.ParameterUtil;
import gamecenter.core.utils.ProfileUtil;
import org.apache.commons.lang3.StringUtils;
import weixin.popular.bean.User;

import java.util.Locale;
import java.util.Map;

/**
 * Created by Chevis on 14/12/20.
 */
public class WechatLoginProcessor extends GeneralProcessor implements GeneralLoginInterface {
    //services
    private final ProfileManager profileManager;
    private final SubscribeService subscribeService;
    private final UserService userService;
    //beans
    private final UserProfile userProfile;
    User wechatUser;
    //请求参数
    String appId;
    String code;
    String state;
    String openId;

    public WechatLoginProcessor(ProfileManager profileManager, SubscribeService subscribeService, UserService userService, UserProfile userProfile) {
        this.profileManager = profileManager;
        this.subscribeService = subscribeService;
        this.userService = userService;
        this.userProfile = userProfile;
    }

    @Override
    public String execute() throws Exception {

        openId = getHttpRequest().getParameter("openId");
        appId = getHttpRequest().getParameter("appId");

        if (StringUtils.isEmpty(openId) && StringUtils.isEmpty(appId)) {
            code = getHttpRequest().getParameter(CommonConstants.WECHAT_AUTH_CODE);
            state = getHttpRequest().getParameter(CommonConstants.WECHAT_AUTH_STATE);
            Map<String, String> stateParam = ParameterUtil.extractParam(state);
            appId = stateParam.get(CommonConstants.WECHAT_STATE_PARAM_APPID);
            logger.info("Login with code = {}, state = {}", code, state);
        } else {
            logger.debug("Login with openId ={}, appId ={}", openId, appId);
        }

        String result;

        try {
            if (isValidLogin()) {

                logger.debug("Login valid.");
                AccessInfo accessInfo = new AccessInfo();
                accessInfo.setAccessChannel(AccessChannel.WECHAT);
                accessInfo.setAppProfile(profileManager.getAppProfile(appId));
                logger.debug("Stored access info.");
                // TODO: to retrieve the user from DB by openId, or create profile
                userProfile.setAccessInfo(accessInfo);
                userProfile.setOpenId(wechatUser.getOpenid());
                userProfile.setDisplayName(wechatUser.getNickname());
                userProfile.setInternalId(ProfileUtil.getUserUnifyId(AccessChannel.WECHAT, wechatUser.getOpenid()));
                userProfile.setUserImgUrl(wechatUser.getHeadimgurl());
                userProfile.setIsFollowed(null != wechatUser.getSubscribe() && wechatUser.getSubscribe() != 0);
//                userProfile.setDeviceId(stateParam.get(CommonConstants.WECHAT_STATE_PARAM_DEVICEID));
                profileManager.getAppProfile(appId).getWechatProfile().getActiveUserList().put(wechatUser.getOpenid(), wechatUser);
                logger.debug("Construct user profile completely.");
                //如果首次登陆，记录录微信用户信息
                if (!userService.hasWechatCustomer(userProfile.getOpenId())) {
                    userService.addWechatCustomer(userProfile.getDisplayName(), userProfile.getOpenId(),
                            profileManager.getAppProfile(appId).getWechatProfile().getWechatAppId());
                    logger.debug("Recorded user info successfully.");
                }
                //获取订阅相关信息
                boolean hasSubscribed = subscribeService.getHasSubscibed(userProfile.getOpenId());
                logger.error("wechat user is = {}", wechatUser.getNickname());
                logger.error("wechat subscribe is = {}", wechatUser.getSubscribe());

                boolean isSubscribing = false; // subscribeService.getIsSubscibing(wechatUser);
                boolean hasSubscribeBonus = subscribeService.getHasSubscribeBonus(userProfile.getOpenId());
                logger.debug("Request the subscribe details from wechat: subscribed = {}, isSubscribing = {}, hasBonus = {}",
                        hasSubscribed, isSubscribing, hasSubscribeBonus);
                getHttpRequest().setAttribute("hasSubscribed", hasSubscribed);
                getHttpRequest().setAttribute("isSubscribing", isSubscribing);
                getHttpRequest().setAttribute("hasSubscribeBonus", hasSubscribeBonus);
                result = Action.SUCCESS;
            } else {
                result = CommonConstants.ACCESS_ROUTER_WECHAT_OAUTH;
            }

        } catch (Exception e) {
            logger.error("Login failed!", e);
            return Action.ERROR;
        }
        return result;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public boolean isValidLogin() {
        logger.debug("Profile size: {}", profileManager.getProfiles().size());
        if (StringUtils.isEmpty(openId)) {
            if (StringUtils.isNotEmpty(appId) &&
                    ProfileUtil.verifyAppProfile(profileManager.getAppProfile(appId)) &&
                    profileManager.getAppProfile(appId).isWechatProfileValid()) {
                logger.debug("Profile is = {}, wechatProfileValid = ",
                        profileManager.getAppProfile(appId),
                        profileManager.getAppProfile(appId).isWechatProfileValid());
                wechatUser = profileManager.getUserInfo(appId, code, getLocale());

                if (wechatUser == null) return false;

            } else {
                return false;
            }
        } else {
            wechatUser = profileManager.getUserInfo(appId, getLocale(), openId);
        }
        return StringUtils.isNotEmpty(wechatUser.getOpenid());
    }

    public Locale getLocale() {
        return getHttpRequest().getLocale();
    }
}
