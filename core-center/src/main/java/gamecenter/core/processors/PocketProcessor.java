package gamecenter.core.processors;

import gamecenter.core.beans.UserProfile;
import gamecenter.core.beans.wechat.WechatJsConfig;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.services.db.DBServices;
import gamecenter.core.services.wechat.WechatJsConfigService;

public class PocketProcessor extends GeneralProcessor {
    private final WechatJsConfigService wechatJsConfigService;
    private final UserProfile userProfile;
    private final DBServices dbServices;
    private int wallet = 0;

    public PocketProcessor(WechatJsConfigService wechatJsConfigService, UserProfile userProfile, DBServices dbServices) {
        this.wechatJsConfigService = wechatJsConfigService;
        this.userProfile = userProfile;
        this.dbServices = dbServices;
    }

    @Override
    public String execute() throws Exception {
        wallet = dbServices.getCustomerService().getCustomerWalletBalanceByOpenId(userProfile.getOpenId());

        WechatProfile wechatProfile = userProfile.getAccessInfo().getAppProfile().getWechatProfile();
        String wechatAppId = wechatProfile.getWechatAppId();
        String jsapi_ticket = wechatProfile.getWechatJsapiTicket();

        WechatJsConfig wechatJsConfig = wechatJsConfigService.getConfig(jsapi_ticket, wechatAppId, getUrl());
        getHttpRequest().setAttribute("wechatJsConfig", wechatJsConfig);

        logger.debug("User profile is {} and the wallet is {}", userProfile, wallet);
        return super.execute();
    }

    private String getUrl() {
        return getScanUrl("pocket");
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public int getWallet() {
        return wallet;
    }
}
