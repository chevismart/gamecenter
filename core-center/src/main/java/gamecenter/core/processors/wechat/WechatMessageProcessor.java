package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.beans.wechat.EventMessage;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.db.SubscribeService;
import gamecenter.core.utils.EncryptUtil;
import org.apache.commons.io.IOUtils;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.Message;
import weixin.popular.bean.message.TextMessage;
import weixin.popular.util.XMLConverUtil;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.util.Arrays;

public class WechatMessageProcessor extends GeneralProcessor {
    //services
    SubscribeService subscribeService;
    //beans
    UserProfile userProfile;

    @Override
    public String execute() throws Exception {
        String access_token = userProfile.getAccessInfo().getAppProfile().getWechatProfile().getWechatAccessToken().getAccess_token();
        //
        String signature = getHttpRequest().getParameter("signature");
        String timestamp = getHttpRequest().getParameter("timestamp");
        String nonce = getHttpRequest().getParameter("nonce");
        String Token = "";
        String[] array = new String[]{Token, timestamp, nonce};
        Arrays.sort(array);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
        }
        String str = sb.toString();
        //验证消息真实性(是否来自微信)
        if (EncryptUtil.SHA1(str).equals(signature)) {
            String echostr = getHttpRequest().getParameter("echostr");
            //判断是否为第一次
            if (echostr != null) {
                ServletOutputStream os = getHttpResponse().getOutputStream();
                os.print(echostr);
                os.flush();
                os.close();
            }
        }
        //获取并解析xml
        InputStream inputStream = getHttpRequest().getInputStream();
        String xml = IOUtils.toString(inputStream);
        logger.debug("Received message xml: {}", xml);
        EventMessage eventMessage = XMLConverUtil.convertToObject(EventMessage.class, new String(xml.getBytes("iso-8859-1"), "utf-8"));
        if (eventMessage != null) {
            if (eventMessage.getMsgType().equals("event") && eventMessage.getEvent().equals("subscribe")) {
                logger.debug("user subscribe");
                //判断是否第一次关注
                String content = "";
                boolean hasSubscibed = subscribeService.getHasSubscibed(userProfile.getOpenId());
                if (!hasSubscibed)
                    content = "谢谢关注公众号!您可获得一次免费试玩机会";
                else
                    content = "谢谢关注公众号";
                //发送消息
                MessageAPI messageAPI = new MessageAPI();
                Message message = new TextMessage(eventMessage.getFromUserName(), content);
                BaseResult result = messageAPI.messageCustomSend(access_token, message);
                //无错误信息返回
                if (result.getErrmsg().equals(""))
                    return Action.SUCCESS;
            }

        }
        return Action.ERROR;
    }

    public void setSubscribeService(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}
