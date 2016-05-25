package gamecenter.core.services.external;

import org.apache.commons.lang3.StringUtils;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.api.SnsAPI;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static gamecenter.core.beans.CoreCenterHost.PREPAY_URL;
import static gamecenter.core.beans.CoreCenterHost.getHttpURL;

public class PrepayResource extends ServerResource {

    public static String PRE_PAY_URI = "/external/youbao/prepay";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String state;

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public String retrieve() throws IOException {

        String appId = "wxe89a9d2fa17df80f";

        if (StringUtils.isNotEmpty(state)) {
            String url = SnsAPI.connectOauth2Authorize(appId, getHttpURL(PREPAY_URL), false, state);
            logger.info("Redirect to oauth for openId: {}", url);
            redirectTo(url);
        } else
            logger.warn("Missing value of parameter [state]. Ignore the request.");
        return "SUCCESS";
    }

    private void redirectTo(String to, Parameter... parameters) {
        Reference reference = new Reference(to);
        for (Parameter parameter : parameters) {
            reference.addQueryParameter(parameter);
        }
        redirectSeeOther(reference);
    }

    @Override
    public void init(Context context, Request request, Response response) {
        super.init(context, request, response);
        Form form = request.getResourceRef().getQueryAsForm();
        state = form.getFirstValue("state");
    }
}
