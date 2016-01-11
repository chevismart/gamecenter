package gamecenter.core.processors.tasks;

import gamecenter.core.constants.CommonConstants;
import gamecenter.core.listeners.AbstractRunnable;
import gamecenter.core.processors.wechat.ProfileManager;
import gamecenter.core.utils.TimeUtil;

/**
 * Created by Chevis on 2014/12/11.
 */
public class WechatTokenManager extends AbstractRunnable {

    private final ProfileManager profileManager;

    public WechatTokenManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public void run() {

        while (isContinue) {
            try {
                //休眠时间
                Thread.sleep(TimeUtil.millionSecondFromSecond(CommonConstants.DEFAULT_WECHAT_ACCESS_TOKEN_CHECK_INTERVAL_IN_SECOND));
                logger.debug("Start to check the access token.");
                profileManager.checkAndUpdateAllAccessToken();

            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

        }

    }
}
