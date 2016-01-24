package gamecenter.core.services;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.Collections;

import static gamecenter.core.services.HttpService.HTTP_TIMEOUT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HttpServiceTest {

    public static final String URI = "http://localhost:8888/test";
    private HttpServer httpServer;
    private String content;
    private boolean isWaiting;

    @Before
    public void setUp() throws Exception {
        httpServer = HttpServer.create(new InetSocketAddress(8888), 0);
        httpServer.createContext("/test", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                if (isWaiting) try {
                    Thread.sleep(HTTP_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                content = RandomStringUtils.randomAlphabetic(10);
                httpExchange.sendResponseHeaders(SC_OK, content.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(content.getBytes());
                os.close();
            }
        });
        httpServer.setExecutor(null);
        httpServer.start();
    }

    @After
    public void tearDown() throws Exception {
        httpServer.stop(1);
        isWaiting = false;
    }

    @Test
    public void canSendPostMethodMessageAndGetResponse() throws Exception {
        assertThat(getContent(HttpService.get(URI)), is(this.content));
    }

    @Test
    public void canSendGetMethodMessageAndGetResponse() throws Exception {
        assertThat(getContent(HttpService.post(URI, Collections.<BasicNameValuePair>emptyList())), is(this.content));
    }

    @Test(expected = SocketTimeoutException.class)
    public void exceptionWillBeThrownIfTimeoutForGetMethod() throws Exception {
        isWaiting = true;
        HttpService.get(URI);
    }

    @Test(expected = SocketTimeoutException.class)
    public void exceptionWillBeThrownIfTimeoutForPostMethod() throws Exception {
        isWaiting = true;
        HttpService.post(URI, Collections.<BasicNameValuePair>emptyList());
    }

    @Test(expected = HttpHostConnectException.class)
    public void exceptionWillBeThrownIfNotAvailableForConnectForGet() throws Exception {
        httpServer.stop(1);
        HttpService.get(URI);
    }

    @Test(expected = HttpHostConnectException.class)
    public void exceptionWillBeThrownIfNotAvailableForConnectForPost() throws Exception {
        httpServer.stop(1);
        HttpService.post(URI, Collections.<BasicNameValuePair>emptyList());
    }

    private String getContent(HttpResponse httpResponse) throws IOException {
        return IOUtils.toString(httpResponse.getEntity().getContent());
    }
}