package gamecenter.core.services;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import weixin.popular.client.HttpClientFactory;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

public class HttpService {
    private static Logger logger = LoggerFactory.getLogger(HttpService.class);
    public static HttpClient httpClient = HttpClientFactory.createHttpClient();

    public static HttpResponse get(String uri, BasicNameValuePair... params) throws IOException {
        HttpGet httpGet = new HttpGet(uri.concat("?").concat(getParams(params)));
        return httpClient.execute(httpGet);
    }

    public static HttpResponse post(String uri, List<BasicNameValuePair> params) throws IOException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        return httpClient.execute(httpPost);
    }

    private static String getParams(BasicNameValuePair... params) {
        return URLEncodedUtils.format(asList(params), "UTF-8");
    }

}
