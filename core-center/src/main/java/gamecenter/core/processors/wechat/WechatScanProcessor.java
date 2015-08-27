package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.beans.wechat.WechatJsConfig;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.wechat.WechatJsConfigService;

public class WechatScanProcessor extends GeneralProcessor {
    //services
    private final WechatJsConfigService wechatJsConfigService;
    //beans
    private final UserProfile userProfile;
    private final ProfileManager profileManager;

    public WechatScanProcessor(WechatJsConfigService wechatJsConfigService, UserProfile userProfile, ProfileManager profileManager) {
        this.wechatJsConfigService = wechatJsConfigService;
        this.userProfile = userProfile;
        this.profileManager = profileManager;
    }

    @Override
    public String execute() {
        String wechatAppId = userProfile.getAccessInfo().getAppProfile().getWechatProfile().getWechatAppId();
        String appId = userProfile.getAccessInfo().getAppProfile().getAppId();
        String jsapi_ticket = profileManager.getAppProfile(appId).getWechatProfile().getWechatJsapiTicket();
        logger.info("用户获取到jsapiticket：" + jsapi_ticket);
        String code = getHttpRequest().getParameter(CommonConstants.WECHAT_AUTH_CODE);
        WechatJsConfig wechatJsConfig = wechatJsConfigService.getConfig(jsapi_ticket, wechatAppId, code);
        getHttpRequest().setAttribute("wechatJsConfig", wechatJsConfig);
        return Action.SUCCESS;
    }

}
