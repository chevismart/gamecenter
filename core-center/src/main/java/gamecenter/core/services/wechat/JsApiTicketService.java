package gamecenter.core.services.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.utils.HttpUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JsApiTicketService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public String requestWechatJsApiTicket(final AppProfile appProfile) {
        String jsApiTicket = "";
        String appId = appProfile.getAppId();
        if (appProfile.isWechatProfileValid()) {
            WechatProfile wechatProfile = appProfile.getWechatProfile();
            String access_token = wechatProfile.getWechatAccessToken().getAccess_token();
            jsApiTicket = getTicket(access_token);
            //更新获取ticket时间
            wechatProfile.setWechatJsapiTicketUpdateTime(new Date(System.currentTimeMillis()));
        } else {
            logger.warn("AppId({}) is invalid to be requested!", appId);
        }
        return jsApiTicket;
    }
	private String getTicket(String access_token){
		String ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
		JSONObject jsonObject = HttpUtil.getJson(String.format(ticketUrl, access_token));
		String ticket="";
			try {
				if(jsonObject.getString("errcode").equals("0") && jsonObject.getString("errmsg").equals("ok") )
					ticket = jsonObject.getString("ticket");
					logger.info("更新ticket："+ticket);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				logger.error("更新ticket失败：");
				e.printStackTrace();
			}
		return ticket;	
	}

}
