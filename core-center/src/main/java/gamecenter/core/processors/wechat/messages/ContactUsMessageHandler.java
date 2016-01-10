package gamecenter.core.processors.wechat.messages;

import gamecenter.core.processors.Filter;
import gamecenter.core.processors.wechat.ProfileManager;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;

public class ContactUsMessageHandler extends WechatMessageHandler {

    public ContactUsMessageHandler(Filter<String> msgTypeFilter, Filter<String> eventTypeFilter, Filter<String> keyFilter, ProfileManager profileManager) {
        super(msgTypeFilter, eventTypeFilter, keyFilter, profileManager);
    }

    void handleIt(EventMessage eventMessage) {
        Message message = new TextMessage(openId(eventMessage), "请拨打 [首领] 电话 >>> 13602211275, 密码：荔园");
        replyClient(eventMessage, message);
    }

}
