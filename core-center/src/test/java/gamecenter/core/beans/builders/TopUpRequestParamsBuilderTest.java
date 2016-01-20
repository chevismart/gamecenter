package gamecenter.core.beans.builders;

import gamecenter.core.beans.Enums;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TopUpRequestParamsBuilderTest {

    TopUpRequestParamsBuilder builder;
    private String centerId = "00000000";
    private String token = "tokenStr";
    private Enums.REQUEST_DATA_TYPE dataType = Enums.REQUEST_DATA_TYPE.JSON;
    private Enums.CORE_CENTER_REQ_TYPE reqType = Enums.CORE_CENTER_REQ_TYPE.TOP_UP;
    private String macAdd = "accf233b95f6";
    private String refId = "ABCDE12345";
    private int coins = 10;

    @Test
    public void buildTopUpRequestParamsSuccessfully() throws Exception {

        String values = TopUpRequestParamsBuilder.newBuilder()
                .centerId(centerId)
                .macAddress(macAdd)
                .referenceId(refId)
                .coins(coins)
                .build();
        assertEquals(getExceptedResult(), values);
    }

    private String getExceptedResult() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("CENTER_ID", centerId));
        params.add(new BasicNameValuePair("DATA_TYPE", dataType.name()));
        params.add(new BasicNameValuePair("REQ_TYPE", reqType.name()));
        params.add(new BasicNameValuePair("MAC", macAdd));
        params.add(new BasicNameValuePair("TOP_UP_REFERENCE_ID", refId));
        params.add(new BasicNameValuePair("TOP_UP_COIN_QTY", String.valueOf(coins)));
        return URLEncodedUtils.format(params, "UTF-8");
    }
}