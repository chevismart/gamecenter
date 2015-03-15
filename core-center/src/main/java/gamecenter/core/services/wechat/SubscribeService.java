package gamecenter.core.services.wechat;

import java.util.Date;

import weixin.popular.bean.User;
import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.dao.DeviceMapper;
import gamecenter.core.dao.PlayrecordMapper;
import gamecenter.core.dao.TradeMapper;
import gamecenter.core.domain.CustomerWechat;
import gamecenter.core.domain.Device;
import gamecenter.core.domain.Playrecord;
import gamecenter.core.domain.Trade;
/**
 * Created by Frank on 15/02/14.
 */
public class SubscribeService {
	//dao
	CustomerWechatMapper customerWechatMapper;
	TradeMapper tradeMapper;
	PlayrecordMapper playrecordMapper;
	DeviceMapper deviceMapper;
	
	private boolean isInitial;
	private boolean hasSubscribeBonus;
	private boolean hasSubscribed;
	private boolean isSubscribing;
	
	private void inital(String openId){
		CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
		if(customerWechat.getSubscribetime()!=null)
			hasSubscribed = true;
		else
			hasSubscribed = false;
		if(customerWechat.getSubscribebonus())
			hasSubscribeBonus = true;
		else
			hasSubscribeBonus = false;
		isInitial = true;
	}
	//订阅（关注）公众号
	public boolean subscribe(String openId){
		CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
		//订阅过公众号，不得修改
		if(customerWechat.getSubscribetime()!=null)
			return false;
		//首次订阅，纪录时间
		customerWechat.setSubscribetime(new Date(System.currentTimeMillis()));
		if(customerWechatMapper.updateByPrimaryKey(customerWechat)>0){
			hasSubscribed = true;
			hasSubscribeBonus = true;
			return true;
		}
		else
			return false;
	}
	//兑现订阅福利（免费试玩）
	public boolean useSubscribeBonus(String openId, String deviceMacAddr){
		int max_num = 4;
		//随机生成币数
		int amount = (int)(max_num*Math.random()+1);
		//获取Device对象
		Device device = deviceMapper.selectByMacAddr(deviceMacAddr);
		//获取CustomerWechat对象
		CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
		//生成Trade和Playrecord纪录
		Date curDate = new Date(System.currentTimeMillis());
			
		Trade trade = new Trade();
		trade.setCenterid(device.getCenterid());
		trade.setCustomerid(customerWechat.getCustomerid());
		trade.setAmount((float)amount);
		trade.setIspaid(0); //代表试玩
		trade.setTradetime(curDate);
		
		Playrecord playrecord = new Playrecord();
		playrecord.setCustomerid(customerWechat.getCustomerid());
		playrecord.setDeviceid(device.getDeviceid());
		playrecord.setTime(curDate);
		//完成试玩
		customerWechat.setSubscribebonus(false);
		customerWechatMapper.updateByPrimaryKey(customerWechat);
		hasSubscribeBonus = false;
		
		return true;
	}
	//getter
	public boolean getHasSubscibed(String openId){
		if(!isInitial)
			inital(openId);
		return hasSubscribed;
	}
	public boolean getHasSubscribeBonus(String openId){
		if(!isInitial)
			inital(openId);
		return hasSubscribeBonus;
	}
	public boolean getIsSubscibing(User userInfo){
		if(userInfo.getSubscribe()>0)
			isSubscribing = true;
		else 
			isSubscribing = false;
		return isSubscribing;
	}
}
