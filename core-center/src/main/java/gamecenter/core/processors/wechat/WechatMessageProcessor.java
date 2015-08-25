package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.wechat.EventMessage;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.db.SubscribeService;
import gamecenter.core.utils.EncryptUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.Message;
import weixin.popular.bean.message.NewsMessage;
import weixin.popular.bean.message.TextMessage;
import weixin.popular.util.XMLConverUtil;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import static java.util.Arrays.asList;

public class WechatMessageProcessor extends GeneralProcessor {
    //services
    private final SubscribeService subscribeService;
    private final ProfileManager profileManager;

    public WechatMessageProcessor(SubscribeService subscribeService, ProfileManager profileManager) {
        this.subscribeService = subscribeService;
        this.profileManager = profileManager;
    }

    @Override
    public String execute() throws Exception {
        logger.debug("Received wechat message.");

        String signature = getHttpRequest().getParameter("signature");
        String timestamp = getHttpRequest().getParameter("timestamp");
        String echostr = getHttpRequest().getParameter("echostr");
        String nonce = getHttpRequest().getParameter("nonce");
        logger.debug("signature: {}, timestamp: {}, nonce: {}", signature, timestamp, nonce);
        String token = "wawa";
        String[] array = new String[]{token, timestamp, nonce};
        Arrays.sort(array);
        logger.debug("Received parameters values after sorted: {}", ArrayUtils.toString(array));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
        }
        //验证消息真实性(是否来自微信)
        if (EncryptUtil.SHA1(sb.toString()).equals(signature)) {
            //判断是否为第一次
            if (echostr != null) {
                PrintWriter pw = getHttpResponse().getWriter();
                pw.print(echostr);
                pw.close();
            } else {
                //获取并解析xml
                InputStream inputStream = getHttpRequest().getInputStream();
                String xml = IOUtils.toString(inputStream).trim();
                logger.debug("Received message xml: {}", xml);
                EventMessage eventMessage = XMLConverUtil.convertToObject(EventMessage.class, new String(xml.getBytes("iso-8859-1"), "utf-8"));
                if (eventMessage != null) {
                    //订阅事件
                    if (eventMessage.getMsgType().equals("event") && eventMessage.getEvent().equals("subscribe")) {
                        String openId = eventMessage.getFromUserName();
                        logger.info("User{} is subscribing.", openId);
                        //判断是否第一次关注
                        String content = "";
                        boolean hasSubscibed = subscribeService.getHasSubscibed(openId);
                        if (!hasSubscibed)
                            content = "谢谢关注公众号!您可获得一次免费试玩机会";
                        else
                            content = "谢谢关注公众号";
                        //发送消息
                        MessageAPI messageAPI = new MessageAPI();
                        Message message = new TextMessage(eventMessage.getFromUserName(), content);
                        NewsMessage.Article article = new NewsMessage.Article(
                                "一起疯狂吧",
                                "谢谢关注公众号! 点击连接扫描娃娃机二维码，可以免费获得一次抽奖机会！",
                                "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe89a9d2fa17df80f&redirect_uri=http://wawaonline.net/corecenter/auth&response_type=code&scope=snsapi_base&state=appid:liyuanapp,deviceid:ATM0001#wechat_redirect",
                                "https://mmbiz.qlogo.cn/mmbiz/OeNzyqPZgIJMt1YGFM7hMA7ggibQyNe70sk29PoeK8ce4PAZXXMfIhQXC8lOQSB9PkyAm9RXRsr3Ida8Yg9cA8Q/0");

                        Message articleMsg = new NewsMessage(eventMessage.getFromUserName(), asList(article));

                        BaseResult result = messageAPI.messageCustomSend(profileManager.getAppProfile("liyuanapp").getWechatProfile().getWechatAccessToken().getAccess_token(), articleMsg);
                        //无错误信息返回
                        if (result.getErrmsg().equals(""))
                            logger.info("subscribe response success");
                    }
                    //其他信息

                }
            }
        } else {
            logger.warn("Invalid message");
        }
        return Action.NONE;
    }

}
