package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
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
import weixin.popular.bean.user.User;

import java.util.Locale;
import java.util.Map;

import static gamecenter.core.beans.AccessChannel.WECHAT;
import static gamecenter.core.constants.CommonConstants.ACCESS_ROUTER_WECHAT_OAUTH;

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

        String result = ACCESS_ROUTER_WECHAT_OAUTH;

        try {
            if (isValidLogin()) {

                logger.debug("Login valid.");
                if (userProfile.getAccessInfo() == null) {
                    logger.info("Creating profile for the user first login");
                    AccessInfo accessInfo = new AccessInfo();
                    accessInfo.setAccessChannel(WECHAT);
                    accessInfo.setAppProfile(profileManager.getAppProfile(appId));
                    logger.debug("Stored access info.");
                    // TODO: to retrieve the user from DB by openId, or create profile
                    userProfile.setAccessInfo(accessInfo);
                    userProfile.setOpenId(wechatUser.getOpenid());
                    userProfile.setDisplayName(wechatUser.getNickname());
                    userProfile.setInternalId(ProfileUtil.getUserUnifyId(WECHAT, wechatUser.getOpenid()));
                    userProfile.setUserImgUrl(wechatUser.getHeadimgurl());
                    userProfile.setIsFollowed(null != wechatUser.getSubscribe() && wechatUser.getSubscribe() != 0);
                    profileManager.getAppProfile(appId).getWechatProfile().getActiveUserList().put(wechatUser.getOpenid(), wechatUser);
                    logger.debug("Construct user profile completely.");
                    //如果首次登陆，记录录微信用户信息
                    if (!userService.hasWechatCustomer(userProfile.getOpenId())) {
                        userService.addWechatCustomer(userProfile.getDisplayName(), userProfile.getOpenId(),
                                profileManager.getAppProfile(appId).getWechatProfile().getWechatAppId());
                        logger.debug("Recorded user info successfully.");
                    }
                    //获取订阅相关信息
                    boolean hasSubscribed = subscribeService.getHasSubscribed(userProfile.getOpenId());
                    logger.error("wechat user is = {}", wechatUser.getNickname());
                    logger.error("wechat subscribe is = {}", wechatUser.getSubscribe());

                    boolean isSubscribing = false; // subscribeService.getIsSubscibing(wechatUser);
                    boolean hasSubscribeBonus = subscribeService.getHasSubscribeBonus(userProfile.getOpenId());
                    logger.debug("Request the subscribe details from wechat: subscribed = {}, isSubscribing = {}, hasBonus = {}",
                            hasSubscribed, isSubscribing, hasSubscribeBonus);
                    if (hasSubscribeBonus) {
                        userProfile.setBonus(getBonus());
                    }
                }
                result = SUCCESS;
            }

        } catch (Exception e) {
            logger.error("Login failed!", e);
            return Action.ERROR;
        }
        return result;
    }

    private int getBonus() {

        int max_num = 3;
        //随机生成币数
        int coins = (int) (max_num * Math.random() + 1);
        return coins;
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
