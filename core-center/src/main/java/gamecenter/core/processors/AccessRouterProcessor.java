package gamecenter.core.processors;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.wechat.ProfileManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.bean.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by Chevis on 2014/12/13.
 */
public class AccessRouterProcessor extends ActionSupport {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ProfileManager profileManager;

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public String execute() throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
        logger.info("Request User: {}", request.getRemoteUser());
        logger.info("Request Url: {}", request.getRequestURI());
        logger.info("Request query string: {}", request.getQueryString());
        String code = request.getParameter(CommonConstants.WECHAT_AUTH_CODE);
        logger.info("code = {}", code);
        logger.info("local = {}", Locale.CHINA);
        try {
            User user = profileManager.getUserInfo(profileManager.getAppProfile("chevisappid").getAppId(), code, Locale.CHINA);
            logger.info("userName = {}", user.getNickname());
            if (StringUtils.isEmpty(user.getNickname())) return "wechatAuth";
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return super.execute();

    }

    private boolean isWechatEntry() {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
        String code = request.getParameter(CommonConstants.WECHAT_AUTH_CODE);
        String state = request.getParameter(CommonConstants.WECHAT_AUTH_STATE);
        return request.getParameterMap().keySet().contains(CommonConstants.WECHAT_AUTH_STATE) && request.getParameterMap().size() <= 2;
    }
}
