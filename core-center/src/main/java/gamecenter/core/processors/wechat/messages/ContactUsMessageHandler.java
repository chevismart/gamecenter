package gamecenter.core.processors.wechat.messages;

import gamecenter.core.processors.Filter;
import gamecenter.core.processors.wechat.ProfileManager;
import weixin.popular.bean.EventMessage;
import weixin.popular.bean.message.Message;
import weixin.popular.bean.message.TextMessage;

public class ContactUsMessageHandler extends WechatMessageHandler {

    private final ProfileManager profileManager;

    public ContactUsMessageHandler(Filter<String> msgTypeFilter, Filter<String> eventTypeFilter, Filter<String> keyFilter, ProfileManager profileManager) {
        super(msgTypeFilter, eventTypeFilter, keyFilter);
        this.profileManager = profileManager;
    }

    void handleIt(EventMessage eventMessage) {
        Message message = new TextMessage(openId(eventMessage), "请拨打 [首领] 电话 >>> 13631383391, 密码：荔园");
        replyClient(getAccessToken(), message);
    }

    private String getAccessToken() {
        return profileManager.getAppProfile("liyuanapp").getWechatProfile().getWechatAccessToken().getAccess_token();
    }
}
