package gamecenter.core.beans.builders;

import gamecenter.core.beans.Enums;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chevis on 15/3/31.
 */
public class TopUpRequestParamsBuilder implements TopUpRequestParamsBuilderInterface.Builder, TopUpRequestParamsBuilderInterface.CenterIdBuilder, TopUpRequestParamsBuilderInterface.CoinBuilder, TopUpRequestParamsBuilderInterface.MacAddressBuilder, TopUpRequestParamsBuilderInterface.ReferenceIdBuilder, TopUpRequestParamsBuilderInterface.TokenBuilder {


    private final Enums.REQUEST_DATA_TYPE dataType = Enums.REQUEST_DATA_TYPE.JSON;
    private final Enums.CORE_CENTER_REQ_TYPE reqType = Enums.CORE_CENTER_REQ_TYPE.TOP_UP;
    private String centerId;
    private String token;
    private String macAddress;
    private String referenceId;
    private int coins;


    public static TopUpRequestParamsBuilderInterface.CenterIdBuilder newBuilder() {
        return new TopUpRequestParamsBuilder();
    }

    @Override
    public TopUpRequestParamsBuilderInterface.TokenBuilder centerId(String centerId) {
        this.centerId = centerId;
        return this;
    }

    @Override
    public TopUpRequestParamsBuilderInterface.ReferenceIdBuilder macAddress(String mac) {
        this.macAddress = mac;
        return this;
    }

    @Override
    public TopUpRequestParamsBuilderInterface.CoinBuilder referenceId(String referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    @Override
    public TopUpRequestParamsBuilderInterface.Builder coins(int coins) {
        this.coins = coins;
        return this;
    }

    @Override
    public TopUpRequestParamsBuilderInterface.MacAddressBuilder token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public String build() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("CENTER_ID", centerId)); // 00000000
        params.add(new BasicNameValuePair("TOKEN", token)); // tokenStr
        params.add(new BasicNameValuePair("DATA_TYPE", dataType.name())); //JSON
        params.add(new BasicNameValuePair("REQ_TYPE", reqType.name())); // TOP_UP
        params.add(new BasicNameValuePair("MAC", macAddress)); // accf233b95f6
        params.add(new BasicNameValuePair("TOP_UP_REFERENCE_ID", referenceId)); // ABCDE12345
        params.add(new BasicNameValuePair("TOP_UP_COIN_QTY", String.valueOf(coins))); // 10 params.add(

        return URLEncodedUtils.format(params, "UTF-8");
    }
}
