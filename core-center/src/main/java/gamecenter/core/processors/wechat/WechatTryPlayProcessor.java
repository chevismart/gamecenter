package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.db.SubscribeService;

public class WechatTryPlayProcessor extends GeneralProcessor {
    //services
    SubscribeService subscribeService;
    //beans
    UserProfile userProfile;

    @Override
    public String execute() {
        //进行试玩
        //TODO: execute the topup action and record the data
        boolean result = subscribeService.consumeBonus(userProfile.getOpenId(), userProfile.getDeviceId(), 1);
        if (result)
            return Action.SUCCESS;
        else
            return Action.ERROR;
    }

    public void setSubscribeService(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}
