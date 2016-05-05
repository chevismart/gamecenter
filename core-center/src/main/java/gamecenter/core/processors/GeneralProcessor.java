package gamecenter.core.processors;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static gamecenter.core.constants.CommonConstants.WECHAT_AUTH_CODE;

public abstract class GeneralProcessor extends ActionSupport {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
    }

    protected HttpServletResponse getHttpResponse() {
        return (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
    }

    protected String getScanUrl(String action) {
        String code = getHttpRequest().getParameter(WECHAT_AUTH_CODE);
        String url;
        if (StringUtils.isNotEmpty(code)) {
            url = "http://wawaonline.net/corecenter/auth?code=" + code + "&state=appid%3Aliyuanapp%2CoptionalUrl%3Awawaonline.net%2Fcorecenter%2F"+action;
        } else {
            url = "http://wawaonline.net/corecenter/pocket";
        }
        logger.debug("URL is {}", url);
        return url;
    }
}
