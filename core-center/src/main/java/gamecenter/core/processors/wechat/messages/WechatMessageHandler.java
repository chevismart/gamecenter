package gamecenter.core.processors.wechat.messages;

import gamecenter.core.processors.Filter;
import gamecenter.core.processors.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.EventMessage;
import weixin.popular.bean.message.Message;


public abstract class WechatMessageHandler implements MessageHandler<EventMessage> {
    private final Filter<String> msgTypeFilter;
    private final Filter<String> eventTypeFilter;
    private final Filter<String> keyFilter;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public WechatMessageHandler(Filter<String> msgTypeFilter, Filter<String> eventTypeFilter, Filter<String> keyFilter) {
        this.msgTypeFilter = msgTypeFilter;
        this.eventTypeFilter = eventTypeFilter;
        this.keyFilter = keyFilter;
    }

    public void process(EventMessage eventMessage) {
        if (msgTypeFilter.shouldInclude(eventMessage.getMsgType()) &&
                eventTypeFilter.shouldInclude(eventMessage.getEvent()) &&
                keyFilter.shouldInclude(eventMessage.getEventKey())) {
            handleIt(eventMessage);
        }
    }

    abstract void handleIt(EventMessage eventMessage);

    protected String openId(EventMessage eventMessage) {
        return eventMessage.getFromUserName();
    }

    protected void replyClient(String token, Message message) {
        MessageAPI messageAPI = new MessageAPI();
        BaseResult result = messageAPI.messageCustomSend(token, message);
        //无错误信息返回
        if (result.getErrmsg().equals(""))
            logger.info("subscribe response success");
    }
}
