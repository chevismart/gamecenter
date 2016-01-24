package gamecenter.core.processors.wechat.messages;

import gamecenter.core.processors.Filter;
import gamecenter.core.processors.MessageHandler;
import gamecenter.core.processors.wechat.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.Message;

public abstract class WechatMessageHandler implements MessageHandler<EventMessage> {
    private final Filter<String> msgTypeFilter;
    private final Filter<String> eventTypeFilter;
    private final Filter<String> keyFilter;
    private final ProfileManager profileManager;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public WechatMessageHandler(Filter<String> msgTypeFilter, Filter<String> eventTypeFilter, Filter<String> keyFilter, ProfileManager profileManager) {
        this.msgTypeFilter = msgTypeFilter;
        this.eventTypeFilter = eventTypeFilter;
        this.keyFilter = keyFilter;
        this.profileManager = profileManager;
    }

    public void process(EventMessage eventMessage) {
        String msgType = eventMessage.getMsgType();
        if (msgTypeFilter.shouldInclude(msgType) &&
                ((msgType.equalsIgnoreCase("event") &&
                        eventTypeFilter.shouldInclude(eventMessage.getEvent()) &&
                        keyFilter.shouldInclude(eventMessage.getEventKey()))
                        || (msgType.equals("text") && keyFilter.shouldInclude(eventMessage.getContent())))) {
            handleIt(eventMessage);
        }
    }

    abstract void handleIt(EventMessage eventMessage);

    protected String openId(EventMessage eventMessage) {
        return eventMessage.getFromUserName();
    }

    protected String initId(EventMessage eventMessage) {
        return eventMessage.getToUserName();
    }

    protected String getAccessToken(EventMessage eventMessage) {
        return profileManager.findAppProfileByWechatInitId(initId(eventMessage))
                .getWechatProfile().getWechatAccessToken().getAccess_token();
    }

    protected BaseResult replyClient(EventMessage receivedMessage, Message replyMessage) {
        BaseResult result = MessageAPI.messageCustomSend(getAccessToken(receivedMessage), replyMessage);
        //无错误信息返回
        if (result.getErrmsg().equals(""))
            logger.info("subscribe response success");
        return result;
    }
}
