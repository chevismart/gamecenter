package gamecenter.core.services;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.http.client.utils.URLEncodedUtils.format;

public class HttpService {
    public static final int HTTP_TIMEOUT = 15000;
    private static HttpRequestRetryHandler retryhandler = new DefaultHttpRequestRetryHandler(1, false);
    public static HttpClient httpClient = HttpClients.custom().setRetryHandler(retryhandler).build();
    private static Logger logger = LoggerFactory.getLogger(HttpService.class);

    public static HttpResponse get(String uri, BasicNameValuePair... params) throws IOException {
        HttpGet httpGet = new HttpGet(uri.concat("?").concat(getParams(params)));
        httpGet.setConfig(getHttpConfig());
        return httpClient.execute(httpGet);
    }

    public static HttpResponse post(String uri, List<BasicNameValuePair> params) throws IOException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setConfig(getHttpConfig());
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        return httpClient.execute(httpPost);
    }

    private static RequestConfig getHttpConfig() {
        return RequestConfig.custom()
                .setSocketTimeout(HTTP_TIMEOUT)
                .setConnectTimeout(HTTP_TIMEOUT)
                .build();
    }

    private static String getParams(BasicNameValuePair... params) {
        return format(asList(params), "UTF-8");
    }

}
