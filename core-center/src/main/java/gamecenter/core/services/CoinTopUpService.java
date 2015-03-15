package gamecenter.core.services;

import gamecenter.core.beans.CoreCenterHost;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import weixin.popular.client.LocalHttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chevis on 15/3/12.
 */
public class CoinTopUpService extends Service {

    public boolean topupCoins(int coins) {

        logger.info("Ready to top up coin.");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("CENTER_ID", "00000000"));
        params.add(new BasicNameValuePair("TOKEN", "tokenStr"));
        params.add(new BasicNameValuePair("DATA_TYPE", "JSON"));
        params.add(new BasicNameValuePair("REQ_TYPE", "TOP_UP"));
        params.add(new BasicNameValuePair("MAC", "accf233b95f6"));
        // TODO: the reference should be unique from db id
        params.add(new BasicNameValuePair("TOP_UP_REFERENCE_ID", RandomStringUtils.randomAlphanumeric(10)));
        params.add(new BasicNameValuePair("TOP_UP_COIN_QTY", String.valueOf(coins)));
        String param = URLEncodedUtils.format(params, "UTF-8");


        HttpResponse response = null;
        String json = StringUtils.EMPTY;

        try {
            HttpUriRequest httpUriRequest = RequestBuilder.get()
                    .setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()))
                    .setUri(URIUtils.createURI("http", CoreCenterHost.CORECENTER_HOST, 8003,
                            "/", param, null))
                    .build();
            response = LocalHttpClient.execute(httpUriRequest);
            json = EntityUtils.toString(response.getEntity());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.info("Topup result is: {}", json);
        return true;
    }


}
