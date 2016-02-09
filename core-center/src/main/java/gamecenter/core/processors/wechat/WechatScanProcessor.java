package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.beans.wechat.WechatJsConfig;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.wechat.WechatJsConfigService;

import static gamecenter.core.constants.CommonConstants.WECHAT_AUTH_CODE;

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
        String code = getHttpRequest().getParameter(WECHAT_AUTH_CODE);
        WechatJsConfig wechatJsConfig = wechatJsConfigService.getConfig(jsapi_ticket, wechatAppId, getUrl(code));
        getHttpRequest().setAttribute("wechatJsConfig", wechatJsConfig);
        return Action.SUCCESS;
    }

    private String getUrl(String code) {
        return "http://wawaonline.net/corecenter/auth?code=" + code + "&state=appid%3Aliyuanapp%2Cdeviceid%3AATM0001";
    }

}
