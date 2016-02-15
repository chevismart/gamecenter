package gamecenter.core.services;

import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.dao.DeviceMapper;
import gamecenter.core.dao.PlayrecordMapper;
import gamecenter.core.domain.CustomerWechat;
import gamecenter.core.domain.Device;
import gamecenter.core.domain.Playrecord;
import gamecenter.core.utils.HttpUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import static weixin.popular.util.JsonUtil.parseObject;

public class CloudServerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DeviceMapper deviceMapper;
    private final CustomerWechatMapper customerWechatMapper;
    private final PlayrecordMapper playrecordMapper;
    private final ConcurrentLinkedQueue<String> handlingUsers = new ConcurrentLinkedQueue<String>();

    public CloudServerService(DeviceMapper deviceMapper, CustomerWechatMapper customerWechatMapper, PlayrecordMapper playrecordMapper) {
        this.deviceMapper = deviceMapper;
        this.customerWechatMapper = customerWechatMapper;
        this.playrecordMapper = playrecordMapper;
    }

    public boolean topUpCoin(String mac, int coinsQty, String openId) throws IOException {
        handlingUsers.add(openId);
        String refId = RandomStringUtils.randomAlphabetic(10);
        HttpResponse response = HttpService.get("http://localhost:8003/topup",
                new BasicNameValuePair("TOP_UP_COIN_QTY", String.valueOf(coinsQty)),
                new BasicNameValuePair("MAC", mac),
                new BasicNameValuePair("TOP_UP_REFERENCE_ID", refId)
        );

        String reply = HttpUtil.getContent(response).trim();
        Map<String, String> result = parseObject(reply.trim(), Map.class);
        logger.debug("User {} top up response is {}", openId, reply);
        if (Boolean.TRUE.toString().equalsIgnoreCase(result.get("TOP_UP_RESULT"))) {
            logger.info("加币成功！");
            Device device = deviceMapper.selectByMacAddr(mac);
            //获取CustomerWechat对象
            logger.debug("Device is {} with device mac address [{}]", device, mac);
            CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
            //生成Playrecord纪录
            Playrecord playrecord = new Playrecord();
            playrecord.setCustomerid(customerWechat.getCustomerid());
            playrecord.setDeviceid(device.getDeviceid());
            playrecord.setTime(new Date(System.currentTimeMillis()));
            playrecord.setRefid(refId);
            playrecord.setQuantity(coinsQty);
            playrecordMapper.insert(playrecord);
            logger.debug("Update Playrecord Table successfully with play record: [{}]", playrecord);
            return true;
        } else {
            logger.warn("Top up failed!");
        }
        handlingUsers.remove(openId);
        return false;
    }

    public List<String> getOnlineClientMac(String centerId) throws IOException {
        logger.info("Requesting online client for centerId: {}", centerId);
        HttpResponse response = HttpService.get("http://localhost:8003/listDevice",
                new BasicNameValuePair("CENTER_ID", centerId)
        );
        String content = HttpUtil.getContent(response);
        logger.debug("Received online clinet response is {}", content);
        return parseObject(content, List.class);
    }

    public boolean isHanding(String openId) {
        logger.info("{} top up request are handling together, current openId is {}.", handlingUsers.size(), openId);
        return handlingUsers.contains(openId);
    }
}
