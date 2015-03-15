package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;

import gamecenter.core.beans.UserProfile;
import gamecenter.core.beans.wechat.WechatJsConfig;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.wechat.SubscribeService;
import gamecenter.core.services.wechat.WechatJsConfigService;

public class WechatScanProcessor extends GeneralProcessor{
	//services
	WechatJsConfigService wechatJsConfigService;
	//beans
	UserProfile userProfile;
	ProfileManager profileManager;
	@Override
	public String execute(){
		String wechatAppId = userProfile.getAccessInfo().getAppProfile().getWechatProfile().getWechatAppId();
		String appId = userProfile.getAccessInfo().getAppProfile().getAppId();
		String jsapi_ticket = profileManager.getAppProfile(appId).getWechatProfile().getWechatJsapiTicket();
		logger.info("用户获取到jsapiticket："+ jsapi_ticket);
		WechatJsConfig wechatJsConfig = wechatJsConfigService.getConfig(jsapi_ticket, wechatAppId);
		getHttpRequest().setAttribute("wechatJsConfig", wechatJsConfig);
		return Action.SUCCESS;
	}
}
