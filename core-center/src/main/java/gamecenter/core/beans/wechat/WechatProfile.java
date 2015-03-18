package gamecenter.core.beans.wechat;


import weixin.popular.bean.SnsToken;
import weixin.popular.bean.Token;
import weixin.popular.bean.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chevis on 2014/12/13.
 */
public class WechatProfile {

    private String wechatAppId;
    private String wechatAppSecret;
    private SnsToken wechatSnsToken;
    private String mchid;
    private String payKey;
    private User wechatUserProfile;
    private Date wechatAccessTokenUpdateTime;
    private Token wechatAccessToken;
    private Date wechatJsapiTicketUpdateTime;
    private String wechatJsapiTicket;
    private Map<String, User> activeUserList;

    public WechatProfile(String wechatAppId, String wechatAppSecret, String mchid, String payKey) {
        this.wechatAppId = wechatAppId;
        this.wechatAppSecret = wechatAppSecret;
        this.mchid = mchid;
        this.payKey = payKey;
        activeUserList = new HashMap<String, User>();
    }

    @Override
    public String toString() {
        return "WechatProfile{" +
                "wechatAppId='" + wechatAppId + '\'' +
                ", wechatAppSecret='" + wechatAppSecret + '\'' +
                ", wechatSnsToken=" + wechatSnsToken +
                ", mchid='" + mchid + '\'' +
                ", payKey='" + payKey + '\'' +
                ", wechatUserProfile=" + wechatUserProfile +
                ", wechatAccessTokenUpdateTime=" + wechatAccessTokenUpdateTime +
                ", wechatAccessToken=" + wechatAccessToken +
                ", activeUserList=" + activeUserList +
                '}';
    }


    public Date getWechatJsapiTicketUpdateTime() {
        return wechatJsapiTicketUpdateTime;
    }

    public void setWechatJsapiTicketUpdateTime(Date wechatJsapiTicketUpdateTime) {
        this.wechatJsapiTicketUpdateTime = wechatJsapiTicketUpdateTime;
    }

    public String getWechatJsapiTicket() {
        return wechatJsapiTicket;
    }

    public void setWechatJsapiTicket(String wechatJsapiTicket) {
        this.wechatJsapiTicket = wechatJsapiTicket;
    }

    public Map<String, User> getActiveUserList() {
        return activeUserList;
    }

    public void setActiveUserList(Map<String, User> activeUserList) {
        this.activeUserList = activeUserList;
    }

    public Date getWechatAccessTokenUpdateTime() {
        return wechatAccessTokenUpdateTime;
    }

    public void setWechatAccessTokenUpdateTime(Date wechatAccessTokenUpdateTime) {
        this.wechatAccessTokenUpdateTime = wechatAccessTokenUpdateTime;
    }

    public Token getWechatAccessToken() {
        return wechatAccessToken;
    }

    public void setWechatAccessToken(Token wechatAccessToken) {
        this.wechatAccessToken = wechatAccessToken;
    }

    public String getWechatAppSecret() {
        return wechatAppSecret;
    }

    public void setWechatAppSecret(String wechatAppSecret) {
        this.wechatAppSecret = wechatAppSecret;
    }

    public String getWechatAppId() {

        return wechatAppId;
    }

    public void setWechatAppId(String wechatAppId) {
        this.wechatAppId = wechatAppId;
    }

    public User getWechatUserProfile() {
        return wechatUserProfile;
    }

    public void setWechatUserProfile(User wechatUserProfile) {
        this.wechatUserProfile = wechatUserProfile;
    }

    public SnsToken getWechatSnsToken() {
        return wechatSnsToken;
    }

    public void setWechatSnsToken(SnsToken wechatSnsToken) {
        this.wechatSnsToken = wechatSnsToken;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getPayKey() {
        return payKey;
    }

    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }
}
