package gamecenter.core.processors.wechat.messages;

import gamecenter.core.processors.Filter;
import gamecenter.core.processors.wechat.ProfileManager;
import gamecenter.core.services.CloudServerService;
import gamecenter.core.services.db.DBServices;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;

public class TopupMessageHandler extends WechatMessageHandler {
    private final DBServices dbServices;
    private final CloudServerService cloudServerService;

    public TopupMessageHandler(Filter<String> msgTypeFilter, Filter<String> eventTypeFilter, Filter<String> keyFilter, ProfileManager profileManager, DBServices dbServices, CloudServerService cloudServerService) {
        super(msgTypeFilter, eventTypeFilter, keyFilter, profileManager);
        this.dbServices = dbServices;
        this.cloudServerService = cloudServerService;
    }

    @Override
    void handleIt(EventMessage eventMessage) {
        try {
            String openId = openId(eventMessage);
//            User user = dbServices.getUserService().getOperatorByOpenId(openId);
            String content = cloudServerService.topUpCoin("accf233b95f6", 1, openId) ? "加币成功" : "加币失败";
            Message message = new TextMessage(openId, content);
            replyClient(eventMessage, message);
        } catch (Exception e) {
            logger.error("Top up error:", e);
        }
    }
}
