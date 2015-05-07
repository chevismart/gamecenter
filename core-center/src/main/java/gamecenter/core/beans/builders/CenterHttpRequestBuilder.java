package gamecenter.core.beans.builders;

import gamecenter.core.services.HttpRequestService;
import org.apache.http.HttpRequest;

/**
 * Created by Chevis on 15/4/1.
 */
public class CenterHttpRequestBuilder implements HttpRequestService<HttpRequest, Builder> {

    @Override
    public HttpRequest submit(Builder value) {

        return null;
    }
}
