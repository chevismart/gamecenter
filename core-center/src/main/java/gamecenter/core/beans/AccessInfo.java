package gamecenter.core.beans;

/**
 * Created by Chevis on 15/1/11.
 */
public class AccessInfo {

    private AccessChannel accessChannel;
    private AppProfile appProfile;

    @Override
    public String toString() {
        return "AccessInfo{" +
                "accessChannel=" + accessChannel +
                ", appProfile=" + appProfile +
                '}';
    }

    public AccessChannel getAccessChannel() {
        return accessChannel;
    }

    public void setAccessChannel(AccessChannel accessChannel) {
        this.accessChannel = accessChannel;
    }

    public AppProfile getAppProfile() {
        return appProfile;
    }

    public void setAppProfile(AppProfile appProfile) {
        this.appProfile = appProfile;
    }

    public String getChargeURL() {
        switch (accessChannel) {
            case WECHAT:
                return CoreCenterHost.getHttpURL(CoreCenterHost.WECHAT_ORDER_URL);
            default:
                return null;
        }
    }
}
