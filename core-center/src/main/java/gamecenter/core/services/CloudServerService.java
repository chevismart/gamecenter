package gamecenter.core.services;

import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.dao.DeviceMapper;
import gamecenter.core.dao.PlayrecordMapper;
import gamecenter.core.domain.CustomerWechat;
import gamecenter.core.domain.Device;
import gamecenter.core.domain.Playrecord;
import gamecenter.core.services.db.DBServices;
import gamecenter.core.utils.HttpUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

import static gamecenter.core.constants.CommonConstants.*;
import static java.lang.Boolean.TRUE;
import static weixin.popular.util.JsonUtil.parseObject;

public class CloudServerService {

    public static final String TOP_UP_RESULT = "TOP_UP_RESULT";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DeviceMapper deviceMapper;
    private final CustomerWechatMapper customerWechatMapper;
    private final PlayrecordMapper playrecordMapper;
    private final ConcurrentLinkedQueue<String> handlingUsers = new ConcurrentLinkedQueue<String>();
    private final DBServices dbServices;
    private final CoinTopUpService coinTopUpService;

    public CloudServerService(DeviceMapper deviceMapper, CustomerWechatMapper customerWechatMapper, PlayrecordMapper playrecordMapper, DBServices dbServices, CoinTopUpService coinTopUpService) {
        this.deviceMapper = deviceMapper;
        this.customerWechatMapper = customerWechatMapper;
        this.playrecordMapper = playrecordMapper;
        this.dbServices = dbServices;
        this.coinTopUpService = coinTopUpService;
    }

    public boolean topUpCoin(String mac, int coinsQty, String openId) throws IOException {
        boolean isSuccess = false;
        try {
            if (isTopupAvailable(coinsQty, openId)) {
                String refId = RandomStringUtils.randomAlphabetic(10);
                if (TRUE.toString().equalsIgnoreCase(triggerTopup(mac, coinsQty, openId, refId).get(TOP_UP_RESULT))) {
                    updatePlayRecord(mac, coinsQty, openId, refId);
                    isSuccess = true;
                }
            }
        } catch (Exception e) {
            logger.error("加币时错误发生：{}", e);
        } finally {
            handlingUsers.remove(openId);
        }
        logger.info(isSuccess ? "加币成功！" : "加币失败!");
        return isSuccess;
    }

    private boolean isTopupAvailable(int coinsQty, String openId) {
        if (isHanding(openId)) {
            logger.warn("Topup is handling for user({})", openId);
            return false;
        }
        if (!availableForTopup(openId, coinsQty)) {
            int wallet = dbServices.getCustomerService().getCustomerWalletBalanceByOpenId(openId);
            int balance = wallet - coinsQty;
            logger.warn("User {} wallet balance {} is insufficient for {} coin top up", openId, balance, coinsQty);
            return false;
        }
        return true;
    }

    private Map<String, String> triggerTopup(String mac, int coinsQty, String openId, String refId) throws IOException {

        Map<String, String> params = new HashMap<String, String>();
        params.put(KEY_MAC, mac);
        params.put(KEY_COIN, String.valueOf(coinsQty));
        params.put(KEY_REF_ID, refId);

        HttpResponse response = null;
        try {
            response = coinTopUpService.submit(params).get();
        } catch (InterruptedException e) {
            logger.error("InterruptedException error while topup: ",e);
        } catch (ExecutionException e) {
            logger.error("topup error: ",e);
        }
        String reply = HttpUtil.getContent(response).trim();
        logger.debug("User {} top up response is {}", openId, reply);
        return parseObject(reply.trim(), Map.class);
    }

    private void updatePlayRecord(String mac, int coinsQty, String openId, String refId) {
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
        dbServices.getCustomerService().payBill(openId, coinsQty);
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

    private boolean availableForTopup(String openId, int coinsQty) {
        int wallet = dbServices.getCustomerService().getCustomerWalletBalanceByOpenId(openId);
        return wallet - coinsQty >= 0;
    }
}
