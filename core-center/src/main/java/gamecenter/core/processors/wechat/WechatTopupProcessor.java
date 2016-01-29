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

        logger.debug("Start to topup!\n User profile is: {}", userProfile.toString());

        String appId = getHttpRequest().getParameter(WECHAT_STATE_PARAM_APPID);
        String mac = getHttpRequest().getParameter(WECHAT_STATE_PARAM_DEVICEID);
        String coins = getHttpRequest().getParameter(WECHAT_TOP_UP_COINS);
        String openId = userProfile.getOpenId();
        boolean result = false;

        logger.debug("appId={}, mac={}, openId={}", appId, mac, openId);

        if (StringUtils.isEmpty(appId) && StringUtils.isEmpty(mac) || StringUtils.isEmpty(coins) || StringUtils.isEmpty(openId)) {
            logger.warn("Missing parameters for top up! appId={}, mac={}, openId={}, coins={}", appId, mac, openId, coins);
        } else {
            logger.info("Preparing top up");
            int coinsQty = Integer.valueOf(coins);
            try {
                int wallet = dbServices.getCustomerService().getCustomerWalletBalanceByOpenId(openId);
                int balance = wallet - coinsQty;
                if (balance >= 0) {
                    boolean isSuccess = dbServices.getCustomerService().payBill(openId, coinsQty);
                    if (isSuccess) {
                        result = cloudServerService.topUpCoin(mac, coinsQty, openId);
                        if (!result) {
                            logger.warn("Top up failed and revert the topup record");
                            dbServices.getCustomerService().chargeWallet(openId, coinsQty);
                        }
                    }
                    logger.info("Top up {} coins for {} {}", coins, openId, result ? "success" : "fail");
                } else {
                    logger.warn("User {} wallet balance {} is insufficient for {} coin top up", openId, balance, coins);
                }
            } catch (Exception e) {
                logger.error("Top up failed since: {}", e);
                boolean chargeResult = dbServices.getCustomerService().chargeWallet(openId, coinsQty);
                logger.info("Recharge the coins {} for user {}", coinsQty, openId);
            }
        }

        return result ? SUCCESS : ERROR;
    }
}
