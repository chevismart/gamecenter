package gamecenter.core.services;

import gamecenter.core.domain.Device;
import gamecenter.core.processors.wechat.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;

import java.util.List;

import static java.util.Arrays.asList;

public class BroadcastService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProfileManager profileManager;

    public BroadcastService(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public void notifyBalance(String openId, String accessToken, int coin) {
        notifyUserByAppId(accessToken, createTextMessage(openId, "您的钱包当前账户余额是: " + coin + " 币"));
    }


    public void notifyDeviceStatusForOwner(Device device) {
        List<String> openIdList = asList("oJpyYuBcMmRKmVCt6AaAKN9EDGac","oJpyYuEVmu6_O4ntXbhyG3GJoUuo","oJpyYuG9MkaHqr3yFwZJnS3X8X7k");

        for (String openId: openIdList){
            notifyUserByAppId(profileManager.getAppProfile("liyuanapp").getWechatProfile().getWechatAccessToken().getAccess_token(),
                    createTextMessage(openId,
                            device.getDevicedesc() + ((device.getConnectionstatus().equalsIgnoreCase("ONLINE")) ? "上线" : "下线")));
        }
    }

    protected BaseResult notifyUserByAppId(String accessToken, Message message) {
        BaseResult result = MessageAPI.messageCustomSend(accessToken, message);
        if (result.getErrmsg().equals(""))
            logger.info("Message is sent");
        return result;
    }

    private Message createTextMessage(String openId, String message) {
        return new TextMessage(openId, message);
    }

}
