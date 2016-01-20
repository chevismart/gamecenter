package gamecenter.core.processors.wechat.messages;

import com.google.common.collect.Lists;
import gamecenter.core.domain.Device;
import gamecenter.core.processors.Filter;
import gamecenter.core.processors.tasks.ConnectionManager;
import gamecenter.core.processors.wechat.ProfileManager;
import gamecenter.core.services.db.DBServices;
import org.apache.commons.lang3.StringUtils;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;

import java.util.Collection;
import java.util.List;

public class ConnectionStatusMessageHandler extends WechatMessageHandler {

    private final ConnectionManager connectionManager;
    private final DBServices dbServices;

    public ConnectionStatusMessageHandler(Filter<String> msgTypeFilter, Filter<String> eventTypeFilter, Filter<String> keyFilter, ProfileManager profileManager, ConnectionManager connectionManager, DBServices dbServices) {
        super(msgTypeFilter, eventTypeFilter, keyFilter, profileManager);
        this.connectionManager = connectionManager;
        this.dbServices = dbServices;
    }

    void handleIt(EventMessage eventMessage) {
        try {
            String openId = openId(eventMessage);
//            User user = dbServices.getUserService().getOperatorByOpenId(openId);
//            if (user != null) {
            Collection<Device> onlineDevices = connectionManager.getOnlineDevicesByAppId("1");
            String content = "没有机器在线！";
            List<String> deviceNames = Lists.newArrayList();
            if (onlineDevices != null && onlineDevices.size() > 0) {
                for (Device device : onlineDevices) {
                    deviceNames.add(device.getDevicename());
                }
                content = "在线设备：" + StringUtils.join(deviceNames.toArray(new String[deviceNames.size()]), ",");
                logger.info(content);
            }
            Message message = new TextMessage(openId, content);
            replyClient(eventMessage, message);
        } catch (Exception e) {
            logger.error("Query device connection status error:", e);
        }
    }
}
