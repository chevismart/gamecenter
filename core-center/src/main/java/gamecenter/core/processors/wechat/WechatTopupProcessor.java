package gamecenter.core.processors.wechat;

import gamecenter.core.beans.UserProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.BroadcastService;
import gamecenter.core.services.CloudServerService;
import gamecenter.core.services.db.DBServices;
import org.apache.commons.lang3.StringUtils;

import static gamecenter.core.constants.CommonConstants.*;

public class WechatTopupProcessor extends GeneralProcessor {

    private final DBServices dbServices;
    private final UserProfile userProfile;
    private final CloudServerService cloudServerService;
    private final BroadcastService broadcastService;

    public WechatTopupProcessor(DBServices dbServices, UserProfile userProfile, CloudServerService cloudServerService, BroadcastService broadcastService) {
        this.dbServices = dbServices;
        this.userProfile = userProfile;
        this.cloudServerService = cloudServerService;
        this.broadcastService = broadcastService;
    }

    public String execute() throws Exception {
        boolean result = false;
        try {
            logger.debug("Start to topup!\n User profile is: {}", userProfile.toString());

            String appId = getHttpRequest().getParameter(WECHAT_STATE_PARAM_APPID);
            String deviceId = getHttpRequest().getParameter(WECHAT_STATE_PARAM_DEVICEID);
            String coins = getHttpRequest().getParameter(WECHAT_TOP_UP_COINS);
            String openId = userProfile.getOpenId();

            logger.debug("appId={}, mac={}, openId={}", appId, deviceId, openId);

            if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(deviceId) || StringUtils.isEmpty(coins) || StringUtils.isEmpty(openId)) {
                logger.warn("Missing parameters for top up! appId={}, deviceId={}, openId={}, coins={}", appId, deviceId, openId, coins);
            } else {
                logger.info("Preparing top up");

                String mac = dbServices.getDeviceService().macAddressByDeviceName(deviceId);

                logger.info("Device({}) mac address is {}", deviceId, mac);

                result = cloudServerService.topUpCoin(mac, Integer.valueOf(coins), openId);
                logger.info("Top up {} coins for {} {}", coins, openId, result ? "success" : "fail");
            }
        } catch (Exception e) {
            logger.error("Top up failed since: {}", e);
        }
        return result ? SUCCESS : ERROR;
    }
}
