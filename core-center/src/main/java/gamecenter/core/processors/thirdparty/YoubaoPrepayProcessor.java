package gamecenter.core.processors.thirdparty;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.processors.wechat.ProfileManager;
import org.apache.commons.lang3.StringUtils;
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
        if(StringUtils.isEmpty(getHttpRequest().getQueryString())){
            logger.warn("Query string is empty! Terminating request!");
            return null;
        }
        String state = getParameter("state");
        String code = getParameter("code");
        AppProfile appProfile = profileManager.getAppProfile("liyuanapp");
        logger.info("State = {}, Code = {}, AppId= {}", state, code, appProfile.getWechatProfile().getWechatAppId());
        String openId = profileManager.getOpenId(appProfile.getAppId(), code);
        logger.info("Retrieved openId = {}", openId);
        String youbaoUri = uri(state, openId);
        logger.info("Redirecting to external system: " + youbaoUri);
        getHttpResponse().sendRedirect(youbaoUri);
        return null;
    }

    private String uri(String state, String openId) {
        return "http://www.youbon.net/p/thirdpay?openid=" + openId + "&state=" + state;
    }
}
