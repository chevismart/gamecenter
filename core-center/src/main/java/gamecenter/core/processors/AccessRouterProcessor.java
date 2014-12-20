package gamecenter.core.processors;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.beans.AccessChannel;
import gamecenter.core.constants.ActionResults;
import gamecenter.core.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Chevis on 2014/12/13.
 */
public class AccessRouterProcessor extends ActionSupport {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String execute() throws Exception {

        String result;

        AccessChannel accessChannel = identifyAccessChannel(getHttpRequest());

        switch (accessChannel) {
            case BROWSER:
                result = Action.LOGIN;
                break;
            case WECHAT:
                result = ActionResults.WECHAT_LOGIN;
                break;
            default:
                result = Action.LOGIN;
        }
        return result;

    }

    public AccessChannel identifyAccessChannel(HttpServletRequest httpRequest) {
        return isWechatAccess(httpRequest) ? AccessChannel.WECHAT : AccessChannel.BROWSER;
    }

    private boolean isWechatAccess(HttpServletRequest httpRequest) {
        return httpRequest.getParameterMap().keySet().contains(CommonConstants.WECHAT_AUTH_STATE) && httpRequest.getParameterMap().size() >= 2;
    }

    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
    }

}
