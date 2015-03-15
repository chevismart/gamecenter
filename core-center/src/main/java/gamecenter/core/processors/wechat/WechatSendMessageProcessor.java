package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.services.wechat.SubscribeService;

public class WechatSendMessageProcessor extends GeneralProcessor {
    //services
    SubscribeService subscribeService;
    //beans
    UserProfile userProfile;

    @Override
    public String execute() {
        //获取参数

        //判断是否第一次关注
        String message = "";
        boolean hasSubscibed = subscribeService.getHasSubscibed(userProfile.getOpenId());
        if (!hasSubscibed)
            message = "谢谢关注公众号!您可获得一次免费试玩机会";
        else
            message = "谢谢关注公众号";
        return Action.SUCCESS;
    }

    public void setSubscribeService(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

}
