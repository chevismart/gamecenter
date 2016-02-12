package gamecenter.core.services;

import gamecenter.core.beans.DailyReport;
import gamecenter.core.domain.Device;
import gamecenter.core.processors.wechat.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;

import java.util.List;

import static gamecenter.core.utils.DateUtil.fullDate_format;
import static java.util.Arrays.asList;

public class BroadcastService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProfileManager profileManager;
    private final List<String> openIdList = asList("oJpyYuBcMmRKmVCt6AaAKN9EDGac", "oJpyYuEVmu6_O4ntXbhyG3GJoUuo", "oJpyYuG9MkaHqr3yFwZJnS3X8X7k");

    public BroadcastService(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public void notifyBalance(String openId, String accessToken, int coin) {
        notifyUserByAppId(accessToken, createTextMessage(openId, "您的钱包当前账户余额是: " + coin + " 币"));
    }


    public void notifyDeviceStatusForOwner(Device device) {
        for (String openId : openIdList) {
            notifyUserByAppId(profileManager.getAppProfile("liyuanapp").getWechatProfile().getWechatAccessToken().getAccess_token(),
                    createTextMessage(openId,
                            device.getDevicedesc() + ((device.getConnectionstatus().equalsIgnoreCase("ONLINE")) ? "上线" : "下线")));
        }
    }

    public void sendDailyReportForOwner(DailyReport report) {
        String FORMAT = "%s 报告：共送出%s个币，实际出币%s个；收入%s元；新增关注人数：%s人，共关注人数：%s人。";
        for (String openId : openIdList) {
            notifyUserByAppId(profileManager.getAppProfile("liyuanapp").getWechatProfile().getWechatAccessToken().getAccess_token(),
                    createTextMessage(openId, String.format(FORMAT,
                            fullDate_format.format(report.getDate()),
                            report.getOutput(),
                            report.getActualOutput(),
                            report.getIncome(),
                            report.getNewSubscribersQty(),
                            report.getAllSubscribersQty())));
        }
    }

    private void dispatchMessage(Message message) {
        String token = profileManager.getAppProfile("liyuanapp").getWechatProfile().getWechatAccessToken().getAccess_token();
        for (String openId : openIdList) {
            notifyUserByAppId(token, message);
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
