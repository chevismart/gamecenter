package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.db.DBServices;

public class WechatTryPlayProcessor extends GeneralProcessor {
    private final UserProfile userProfile;
    private final DBServices dbServices;

    public WechatTryPlayProcessor(UserProfile userProfile, DBServices dbServices) {
        this.userProfile = userProfile;
        this.dbServices = dbServices;
    }

    @Override
    public String execute() {
        String openId = userProfile.getOpenId();
        boolean isSuccess = false;
        //将试玩数量加入钱包
        int bonus = userProfile.getBonus();
        try {
            if (bonus > 0) {
                if (dbServices.getCustomerService().chargeWallet(openId, bonus)) {
                    logger.info("Charge {} coins for open id {} success!", bonus, openId);
                    userProfile.setBonus(0);
                    isSuccess = dbServices.getSubscribeService().consumeBonus(userProfile.getOpenId(), userProfile.getDeviceId(), bonus);
                }
            }
        } catch (Exception e) {
            logger.error("Exception is caught when charge wallet. ", e);
        }
        if (isSuccess) {
            logger.info("Successfully charge wallet!");
            return Action.SUCCESS;
        } else
            return Action.ERROR;
    }
}
