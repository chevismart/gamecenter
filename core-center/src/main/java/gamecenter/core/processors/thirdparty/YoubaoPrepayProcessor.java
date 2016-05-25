package gamecenter.core.processors.thirdparty;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.processors.wechat.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YoubaoPrepayProcessor extends GeneralProcessor {
    private final ProfileManager profileManager;
    Logger logger = LoggerFactory.getLogger(YoubaoPrepayProcessor.class);

    public YoubaoPrepayProcessor(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public String execute() throws Exception {
        logger.info("Start to prepare for the prepay: {}", getHttpRequest().getQueryString());
        String state = getParameter("state");
        String code = getParameter("code");
        AppProfile appProfile = profileManager.getAppProfile("liyuanapp");
        logger.info("State = {}, Code = {}, AppId= {}", state, code, appProfile.getWechatProfile().getWechatAppId());
        String openId = profileManager.getOpenId(appProfile.getAppId(), code);
        logger.info("Retrieve openId = {}", openId);
        getHttpResponse().sendRedirect("http://www.youbon.net/p/thirdpay?openid=" + openId + "&state=" + state);
        return null;
    }
}
