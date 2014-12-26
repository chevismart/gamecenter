package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Chevis on 2014/12/25.
 */
public class WechatPayNotificationProcessor extends ActionSupport {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String execute() throws Exception {

        logger.info("Received payment notification: {}", getHttpRequest().getParameterMap());
        return Action.SUCCESS;
    }

    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
    }
}

