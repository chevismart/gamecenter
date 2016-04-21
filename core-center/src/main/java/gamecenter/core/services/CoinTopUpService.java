package gamecenter.core.services;

import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.HttpResponseHandler;
import gamecenter.core.utils.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.Future;

import static gamecenter.core.beans.builders.TopUpRequestParamsBuilder.newBuilder;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class CoinTopUpService implements HttpRequestService<Future<HttpResponse>, Map<String, String>>, HttpResponseHandler {

    public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
    public static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String serverUrl;

    public CoinTopUpService(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public static boolean verifyTopupResult(String json) {
        return StringUtils.isNotEmpty(json);
    }

    public Future<HttpResponse> submit(Map<String, String> params) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig).build();

        httpclient.start();

        String postParams = newBuilder()
                .centerId("00000000")
                .macAddress(params.get(CommonConstants.KEY_MAC))
                .referenceId(params.get(CommonConstants.KEY_REF_ID))
                .coins(Integer.valueOf(params.get(CommonConstants.KEY_COIN)))
                .build();

        Future<HttpResponse> response = null;
        try {
            HttpUriRequest httpUriRequest = RequestBuilder.get()
                    .setHeader(new BasicHeader(CONTENT_TYPE, APPLICATION_JSON.toString()))
                    .setUri(URIUtils.createURI("http", serverUrl, 8003,
                            "/topup", postParams, null))
                    .build();
            response = httpclient.execute(httpUriRequest, this);
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }

        return response;
    }

    @Override
    public void completed(HttpResponse httpResponse) {
        logger.info("HttpRequest completed.");

    }

    @Override
    public void failed(Exception e) {
        logger.warn("HttpRequest failed: {}", e);
    }

    @Override
    public void cancelled() {
        logger.warn("HttpRequest cancelled");
    }
}
