package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.processors.MessageHandler;
import gamecenter.core.utils.EncryptUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.util.XMLConverUtil;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class WechatMessageProcessor extends GeneralProcessor {
    //services
    private final List<MessageHandler<EventMessage>> handlers;

    public WechatMessageProcessor(List<MessageHandler<EventMessage>> handlers) {
        this.handlers = handlers;
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
                    for (MessageHandler<EventMessage> handler : handlers)
                        handler.process(eventMessage);
                }
            }
        } else {
            logger.warn("Invalid message");
        }
        return Action.NONE;
    }

}
