package gamecenter.core.services.external;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class SettlementResource extends ServerResource {

    public static String SETTLEMENT_URI = "/external/youbao/settlement";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String queryString;

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public String retrieve() throws IOException {

        String appId = "wxe89a9d2fa17df80f";

        logger.info("Receive paid notification with: {}" , queryString);

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
        queryString = form.getQueryString();
    }
}
