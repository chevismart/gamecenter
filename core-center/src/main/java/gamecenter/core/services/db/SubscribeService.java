package gamecenter.core.services.db;
import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.dao.DeviceMapper;
import gamecenter.core.dao.PlayrecordMapper;
import gamecenter.core.domain.CustomerWechat;
import gamecenter.core.domain.Device;
import gamecenter.core.domain.Playrecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Frank on 15/02/14.
 */
public class SubscribeService extends DBService {
    //dao
    private final CustomerWechatMapper customerWechatMapper;
    private final PlayrecordMapper playrecordMapper;
    private final DeviceMapper deviceMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SubscribeService(CustomerWechatMapper customerWechatMapper, PlayrecordMapper playrecordMapper, DeviceMapper deviceMapper) {
        this.customerWechatMapper = customerWechatMapper;
        this.playrecordMapper = playrecordMapper;
        this.deviceMapper = deviceMapper;
    }

    private SubscribeDetail getSubscribeDetails(String openId) {
        boolean hasSubscribeBonus;
        boolean hasSubscribed;
        CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
        if (customerWechat == null || customerWechat.getSubscribetime() == null) {
            hasSubscribed = false;
            hasSubscribeBonus = false;
        } else {
            hasSubscribed = true;
            if (customerWechat.getSubscribebonus())
                hasSubscribeBonus = true;
            else
                hasSubscribeBonus = false;
        }
        return new SubscribeDetail(hasSubscribeBonus, hasSubscribed);
    }

    private boolean isSubscribed(CustomerWechat customerWechat) {
        if (customerWechat.getSubscribetime() != null) {
            logger.debug("User({}) subscribed previously.");
            return true;// Return if the user is subscribed before.
        } else {
            logger.debug("User({}) is not subscribed.");
        }
        return false;
    }

    //订阅（关注）公众号
    public boolean subscribe(String openId) {
        CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
        if (!isSubscribed(customerWechat)) {
            //首次订阅，纪录时间
            customerWechat.setSubscribetime(new Date(System.currentTimeMillis()));
            return customerWechatMapper.updateByPrimaryKey(customerWechat) > 0;
        }
        return true;
    }

    //兑现订阅福利（免费试玩）
    public boolean consumeBonus(String openId, String deviceMacAddr, int bonus) {
        //随机生成币数
//        int amount = (int) (maxBonus * Math.random() + 1);
        //加币试玩
        //获取Device对象
        Device device = deviceMapper.selectByMacAddr(deviceMacAddr);
        //获取CustomerWechat对象
        logger.debug("Device is {} with device mac address [{}]", device, deviceMacAddr);
        CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
        //生成Playrecord纪录
        Playrecord playrecord = new Playrecord();
        playrecord.setCustomerid(customerWechat.getCustomerid());
        playrecord.setDeviceid(device.getDeviceid());
        playrecord.setTime(new Date(System.currentTimeMillis()));
        playrecordMapper.insert(playrecord);
        logger.debug("Update Playrecord Table successfully with play record: [{}]", playrecord);
        //完成试玩
        customerWechat.setSubscribebonus(false);
        customerWechatMapper.updateByPrimaryKey(customerWechat);
        logger.debug("Update CustomerWechat table successfully with data [{}]", customerWechat);

        return true;
    }

    //getter attr
    public boolean getHasSubscibed(String openId) {
        return getSubscribeDetails(openId).isSubscribed;
    }

    public boolean getHasSubscribeBonus(String openId) {
        return getSubscribeDetails(openId).hasBonus;
    }

    private static class SubscribeDetail {
        private final boolean isSubscribed;
        private final boolean hasBonus;

        public SubscribeDetail(boolean hasBonus, boolean isSubscribed) {
            this.hasBonus = hasBonus;
            this.isSubscribed = isSubscribed;
        }
    }
}
