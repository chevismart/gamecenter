package gamecenter.core.processors.tasks;

import gamecenter.core.processors.wechat.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static gamecenter.core.constants.CommonConstants.DEFAULT_WECHAT_ACCESS_TOKEN_CHECK_INTERVAL_IN_SECOND;
import static gamecenter.core.utils.TimeUtil.millionSecondFromSecond;

public class WechatTokenManager implements ScheduleTask {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ProfileManager profileManager;

    public WechatTokenManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public void run() {
                logger.debug("Start to check the access token.");
                profileManager.checkAndUpdateAllAccessToken();
    }

    @Override
    public long interval() {
        return millionSecondFromSecond(DEFAULT_WECHAT_ACCESS_TOKEN_CHECK_INTERVAL_IN_SECOND);
    }

    @Override
    public long initDelay() {
        return 0;
    }
}
