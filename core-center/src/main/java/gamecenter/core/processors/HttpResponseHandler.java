package gamecenter.core.processors;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

/**
 * Created by Chevis on 15/3/31.
 */
public interface HttpResponseHandler extends FutureCallback<HttpResponse> {
}
