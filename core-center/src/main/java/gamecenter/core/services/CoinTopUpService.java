package gamecenter.core.services;

import gamecenter.core.processors.HttpResponseHandler;
import org.apache.commons.lang3.RandomStringUtils;
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

import java.net.URISyntaxException;
import java.util.concurrent.Future;

import static gamecenter.core.beans.builders.TopUpRequestParamsBuilder.newBuilder;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class CoinTopUpService implements HttpRequestService<Future<HttpResponse>, Integer> {

    public static final int DEFAULT_SOCKET_TIMEOUT = 3000;
    public static final int DEFAULT_CONNECT_TIMEOUT = 3000;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String serverUrl;
    private final HttpResponseHandler handler;


    public CoinTopUpService(String serverUrl, HttpResponseHandler handler) {
        this.serverUrl = serverUrl;
        this.handler = handler;
    }

    public static boolean verifyTopupResult(String json) {
        return StringUtils.isNotEmpty(json);
    }

    public Future<HttpResponse> submit(Integer coins) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT).setConnectTimeout(DEFAULT_CONNECT_TIMEOUT).build();
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig).build();

        httpclient.start();

        String postParams = newBuilder()
                .centerId("00000000")
                .macAddress("accf233b95f6")
                .referenceId(RandomStringUtils.randomAlphanumeric(10))
                .coins(coins)
                .build();

        Future<HttpResponse> response = null;
        try {
            HttpUriRequest httpUriRequest = RequestBuilder.get()
                    .setHeader(new BasicHeader(CONTENT_TYPE, APPLICATION_JSON.toString()))
                    .setUri(URIUtils.createURI("http", serverUrl, 8003,
                            "/topup", postParams, null))
                    .build();
            response = httpclient.execute(httpUriRequest, handler);
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }

        return response;
    }
}
