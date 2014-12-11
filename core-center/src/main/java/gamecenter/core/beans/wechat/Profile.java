package gamecenter.core.beans.wechat;

/**
 * Created by Chevis on 2014/12/11.
 */
public class Profile {
    private String appId;
    private String appSecret;
    private String name;
    private Token accessToken;

    @Override
    public String toString() {
        return "Profile{" +
                "appId='" + appId + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", name='" + name + '\'' +
                ", accessToken=" + accessToken +
                '}';
    }

    public Token getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
