package gamecenter.core.beans;

import gamecenter.core.beans.wechat.WechatProfile;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Chevis on 2014/12/11.
 */
public class AppProfile {
    private String appName;
    private String appId;
    private WechatProfile wechatProfile;

    public AppProfile(String appName, String appId) {
        this.appName = appName;
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "AppProfile{" +
                "appName='" + appName + '\'' +
                ", appId='" + appId + '\'' +
                ", wechatProfile=" + wechatProfile +
                '}';
    }

    public boolean isWechatProfileValid() {
        return null != wechatProfile &&
                StringUtils.isNotEmpty(wechatProfile.getWechatAppId()) &&
                StringUtils.isNotEmpty(wechatProfile.getWechatAppSecret());
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public WechatProfile getWechatProfile() {
        return wechatProfile;
    }

    public void setWechatProfile(WechatProfile wechatProfile) {
        this.wechatProfile = wechatProfile;
    }
}
