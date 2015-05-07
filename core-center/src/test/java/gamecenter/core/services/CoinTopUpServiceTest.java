package gamecenter.core.services;

import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import junit.framework.TestCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class CoinTopUpServiceTest extends TestCase {

    HttpServer httpServer;
    String response;

    @Override
    public void setUp() throws Exception {
        httpServer = HttpServer.create(new InetSocketAddress(1234), 0);
        response = "SUCCESS";
        httpServer.createContext("/test", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream os = httpExchange.getResponseBody();
                int i = 0;
                LoggerFactory.getLogger(this.getClass()).info("Start waiting");

                boolean isContinue = true;
                Date now = new Date();
                while (isContinue) {

                    isContinue = (new Date()).before(DateUtils.addSeconds(now, 2));

                }
                LoggerFactory.getLogger(this.getClass()).info("Finished waiting");
                os.write("SUCCESS".getBytes());
                os.close();
            }
        });
        httpServer.setExecutor(null);
        httpServer.start();
    }

    @Test
    public void testName() throws Exception {

//        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createMinimal();
//        try {
//            httpclient.start();
//            HttpGet request = new HttpGet("http://localhost/test");
//            Future<HttpResponse> future = httpclient.execute(request, null);
//            HttpResponse response = future.get();// 获取结果
//            System.out.println("Response: " + response.getStatusLine());
//            System.out.println("Shutting down");
//        } finally {
//            httpclient.close();
//        }
//        System.out.println("Done");
        System.err.println(RandomStringUtils.randomAlphanumeric(10));
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(3000).setConnectTimeout(3000).build();
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig).build();
        try {
            httpclient.start();
            final HttpGet[] requests = new HttpGet[]{
//                    new HttpGet("http://www.apache.org/"),
//                    new HttpGet("https://www.verisign.com/"),
//                    new HttpGet("http://www.google.com/"),
//                    new HttpGet("http://www.baidu.com/") ,
                    new HttpGet("http://localhost:1234/test"),
                    new HttpGet("http://localhost:1234/test")};
            final CountDownLatch latch = new CountDownLatch(requests.length);
            for (final HttpGet request : requests) {
                httpclient.execute(request, new FutureCallback<HttpResponse>() {
                    //无论完成还是失败都调用countDown()
                    @Override
                    public void completed(final HttpResponse response) {
                        latch.countDown();
                        LoggerFactory.getLogger(this.getClass()).info(request.getRequestLine() + "->"
                                + response.getStatusLine());
                        try {
                            LoggerFactory.getLogger(this.getClass()).info(EntityUtils.toString(response.getEntity()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(final Exception ex) {
                        latch.countDown();
                        LoggerFactory.getLogger(this.getClass()).info(request.getRequestLine() + "->" + ex);
                    }

                    @Override
                    public void cancelled() {
                        latch.countDown();
                        LoggerFactory.getLogger(this.getClass()).info(request.getRequestLine()
                                + " cancelled");
                    }
                });
            }
            LoggerFactory.getLogger(this.getClass()).info("Request completed and waiting for response");
            latch.await();
        } finally {
            httpclient.close();
            LoggerFactory.getLogger(this.getClass()).info("Shutting down");
        }
        LoggerFactory.getLogger(this.getClass()).info("Done");
    }


}