package gamecenter.core.services.wechat;

import gamecenter.core.beans.wechat.WechatJsConfig;
import gamecenter.core.utils.EncryptUtil;
import gamecenter.core.utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;



/**
 * Created by Frank on 15/02/14.
 */
public class WechatJsConfigService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public WechatJsConfig getConfig(String jsapi_ticket,String wechatAppId){
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
		String nonceStr = "wdffd";
		String url = "http://wawaonline.net:8080/gamecenter/scan";//当前页面的url
		String signature = getSignature(jsapi_ticket,nonceStr,timestamp,url);
		
		WechatJsConfig wechatJsConfig  = new WechatJsConfig();
		wechatJsConfig.setAppId(wechatAppId);
		wechatJsConfig.setNonceStr(nonceStr);
		wechatJsConfig.setTimestamp(timestamp);
		wechatJsConfig.setSignature(signature);
		
		return wechatJsConfig;
	}
	
	private String getSignature(String jsapi_ticket,String nonceStr,String timestamp,String url){
		String raw ="jsapi_ticket=%s"+"&"+
					"noncestr=%s"+"&"+
					"timestamp=%s"+"&"+
					"url=%s";
		return EncryptUtil.SHA1(String.format(raw, jsapi_ticket,nonceStr,timestamp,url));
		
	}
}
