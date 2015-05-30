package gamecenter.core.processors;

import com.opensymphony.xwork2.Action;
import gamecenter.core.beans.AccessChannel;
import gamecenter.core.constants.ActionResults;
import gamecenter.core.constants.CommonConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Chevis on 2014/12/13.
 */
public class AccessRouterProcessor extends GeneralProcessor {

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
        return httpRequest.getParameterMap().keySet().contains(CommonConstants.WECHAT_AUTH_STATE) && httpRequest.getParameterMap().size() >= 2
                || httpRequest.getParameterMap().containsKey("openId");
    }

}
