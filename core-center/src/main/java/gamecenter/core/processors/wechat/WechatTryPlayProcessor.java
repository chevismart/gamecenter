package gamecenter.core.processors.wechat;

import gamecenter.core.beans.UserProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.BroadcastService;
import gamecenter.core.services.db.DBServices;

public class WechatTryPlayProcessor extends GeneralProcessor {
    private final UserProfile userProfile;
    private final DBServices dbServices;
    private final BroadcastService broadcastService;

    public WechatTryPlayProcessor(UserProfile userProfile, DBServices dbServices, BroadcastService broadcastService) {
        this.userProfile = userProfile;
        this.dbServices = dbServices;
        this.broadcastService = broadcastService;
    }

    @Override
    public String execute() {
        logger.debug("Received try play request!");
        String openId = userProfile.getOpenId();
        boolean isSuccess = false;
        //将试玩数量加入钱包
        int bonus = userProfile.getBonus();
        try {
            if (bonus > 0) {
                if (dbServices.getCustomerService().chargeWallet(openId, bonus, userProfile.getAccessInfo().getAppProfile().getWechatProfile().getWechatAccessToken())) {
                    logger.info("Charge {} coins for open id {} success!", bonus, openId);
                    userProfile.setBonus(0);
                    isSuccess = dbServices.getSubscribeService().consumeBonus(userProfile.getOpenId(), userProfile.getDeviceId(), bonus);
                    logger.debug("{} bonus left!", userProfile.getBonus());
                    broadcastService.notifyBalance(openId, userProfile.getAccessInfo().getAppProfile().getWechatProfile().getWechatAccessToken().getAccess_token(), dbServices.getCustomerService().getCustomerWalletBalanceByOpenId(openId));
                }
            }
        } catch (Exception e) {
            logger.error("Exception is caught when charge wallet. ", e);
        }
        if (isSuccess) {
            logger.info("Successfully charge wallet!");
            return SUCCESS;
        } else
            return ERROR;
    }
}
