package gamecenter.core.processors.wechat.messages;

import gamecenter.core.processors.Filter;
import gamecenter.core.processors.wechat.ProfileManager;
import gamecenter.core.services.db.SubscribeService;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.EventMessage;
import weixin.popular.bean.message.Message;
import weixin.popular.bean.message.NewsMessage;
import weixin.popular.bean.message.TextMessage;

import static java.util.Arrays.asList;

public class SubscribeMessageHandler extends WechatMessageHandler {

    private final SubscribeService subscribeService;
    private final ProfileManager profileManager;

    public SubscribeMessageHandler(Filter<String> msgTypeFilter, Filter<String> eventTypeFilter, Filter<String> keyFilter, SubscribeService subscribeService, ProfileManager profileManager) {
        super(msgTypeFilter, eventTypeFilter, keyFilter);
        this.subscribeService = subscribeService;
        this.profileManager = profileManager;
    }

    void handleIt(EventMessage eventMessage) {
        logger.info("User{} is subscribing.", openId(eventMessage));
        //判断是否第一次关注
        String content = "";
        boolean hasSubscibed = subscribeService.getHasSubscibed(openId(eventMessage));
        if (!hasSubscibed)
            content = "谢谢关注公众号!您可获得一次免费试玩机会";
        else
            content = "谢谢关注公众号";
        //发送消息
        MessageAPI messageAPI = new MessageAPI();
        Message message = new TextMessage(openId(eventMessage), content);
        NewsMessage.Article article = new NewsMessage.Article(
                "一起疯狂吧",
                "谢谢关注公众号! 点击连接扫描娃娃机二维码，可以免费获得一次抽奖机会！",
                "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe89a9d2fa17df80f&redirect_uri=http://wawaonline.net/corecenter/auth&response_type=code&scope=snsapi_base&state=appid:liyuanapp,deviceid:ATM0001#wechat_redirect",
                "https://mmbiz.qlogo.cn/mmbiz/OeNzyqPZgIJMt1YGFM7hMA7ggibQyNe70sk29PoeK8ce4PAZXXMfIhQXC8lOQSB9PkyAm9RXRsr3Ida8Yg9cA8Q/0");

        Message articleMsg = new NewsMessage(eventMessage.getFromUserName(), asList(article));

        BaseResult result = messageAPI.messageCustomSend(getAccessToken(), articleMsg);
        //无错误信息返回
        if (result.getErrmsg().equals(""))
            logger.info("subscribe response success");
    }

    private String getAccessToken() {
        return profileManager.getAppProfile("liyuanapp").getWechatProfile().getWechatAccessToken().getAccess_token();
    }
}
