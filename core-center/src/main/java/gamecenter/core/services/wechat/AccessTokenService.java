package gamecenter.core.services.wechat;

import gamecenter.core.beans.AccessChannel;
import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.services.Service;
import gamecenter.core.utils.TimeUtil;
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
import weixin.popular.api.TokenAPI;
import weixin.popular.bean.Token;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.util.JsonUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chevis on 14/12/15.
 */
public class AccessTokenService extends Service {

    TokenAPI wechatAccessTokenApi = new TokenAPI();

    public AccessTokenService(TokenAPI wechatAccessTokenApi) {
        this.wechatAccessTokenApi = wechatAccessTokenApi;
    }

    public Token requestWechatAccessToken(final AppProfile appProfile) {
        Token accessToken = null;
        String appId = appProfile.getAppId();
        if (appProfile.isWechatProfileValid()) {
            WechatProfile wechatProfile = appProfile.getWechatProfile();
            accessToken = verifyToken(wechatAccessTokenApi.token(wechatProfile.getWechatAppId(), wechatProfile.getWechatAppSecret()),
                    wechatProfile.getWechatAppId(),
                    wechatProfile);
        } else {
            logger.warn("AppId({}) is invalid to be requested!", appId);
        }
        return accessToken;
    }

    private Token verifyToken(Token accessToken, String appId, WechatProfile wechatProfile) {
        Token token = null;
        if (!isAccessTokenValid(accessToken)) {
            logger.error("Request wechat access token failed: error code = {}, error message ={}", accessToken.getErrcode(), accessToken.getErrmsg());
        } else {
            token = accessToken;
            wechatProfile.setWechatAccessTokenUpdateTime(TimeUtil.getCurrentDateTime());
            logger.info("Access token is returned successfully for appId: {}", appId);
        }
        return token;
    }

    private boolean isAccessTokenValid(Token token) {
        return StringUtils.isEmpty(token.getErrcode()) && StringUtils.isEmpty(token.getErrmsg());
    }

    public Token requestWechatAccessTokenFromHost(final AppProfile appProfile) {
        return requestWechatAccessTokenFromHost(appProfile, new LocalHttpClient());
    }

    public Token requestWechatAccessTokenFromHost(final AppProfile appProfile, LocalHttpClient localHttpClient) {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("clientId", "client01"));
        params.add(new BasicNameValuePair("appId", appProfile.getAppId()));
        params.add(new BasicNameValuePair("credentialType", AccessChannel.WECHAT.name()));
        String param = URLEncodedUtils.format(params, "UTF-8");

        HttpUriRequest httpUriRequest = null;
        try {
            // TODO: enhance here for the hard code host and port
            httpUriRequest = RequestBuilder.get()
                    .setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString()))
                    .setUri(URIUtils.createURI("http", "localhost", 8080,
                            "/credential", param, null))
                    .build();
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }
        String json = StringUtils.EMPTY;
        HttpResponse response = localHttpClient.execute(httpUriRequest);
        try {
            json = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Token token = JsonUtil.parseObject(json, Token.class);
        return token;
    }

}
