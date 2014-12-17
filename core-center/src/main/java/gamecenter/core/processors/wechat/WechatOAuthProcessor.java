package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.constants.CommonConstants;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Chevis on 2014/12/16.
 */
public class WechatOAuthProcessor extends ActionSupport {
    @Override
    public String execute() throws Exception {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
        String state = request.getParameter(CommonConstants.WECHAT_AUTH_STATE);
        System.err.println("state = " + state);
        // TODO: redirect to wechat oauth
        response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe89a9d2fa17df80f&redirect_uri=http%3A%2F%2Falcock.gicp.net:8888/auth&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect\n");
        return super.execute();
    }
}
