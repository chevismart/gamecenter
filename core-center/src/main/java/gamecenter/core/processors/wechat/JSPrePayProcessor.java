package gamecenter.core.processors.wechat;

import gamecenter.core.beans.UserProfile;
import gamecenter.core.beans.wechat.WechatJsConfig;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.wechat.WechatJsConfigService;

public class JSPrePayProcessor extends GeneralProcessor {

    private final WechatJsConfigService wechatJsConfigService;
    private final UserProfile userProfile;

    public JSPrePayProcessor(WechatJsConfigService wechatJsConfigService, UserProfile userProfile) {
        this.wechatJsConfigService = wechatJsConfigService;
        this.userProfile = userProfile;
    }

    @Override
    public String execute() throws Exception {

        WechatProfile wechatProfile = userProfile.getAccessInfo().getAppProfile().getWechatProfile();
        WechatJsConfig wechatJsConfig = wechatJsConfigService.getConfig(wechatProfile.getWechatJsapiTicket(), wechatProfile.getWechatAppId(), getUrl());
        logger.info("Wechat JS config is: {}", wechatJsConfig);
        getHttpRequest().setAttribute("wechatJsConfig", wechatJsConfig);

        return SUCCESS;
    }

    private String getUrl() {
        return getScanUrl("payment");
    }
}
