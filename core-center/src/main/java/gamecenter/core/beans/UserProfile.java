package gamecenter.core.beans;

/**
 * Created by Chevis on 2014/12/16.
 */
public class UserProfile {
    private String displayName;
    private String internalId;
    private String userImgUrl;
    private AccessChannel accessChannel;

    @Override
    public String toString() {
        return "UserProfile{" +
                "displayName='" + displayName + '\'' +
                ", internalId='" + internalId + '\'' +
                ", userImgUrl='" + userImgUrl + '\'' +
                ", accessChannel=" + accessChannel +
                '}';
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public AccessChannel getAccessChannel() {
        return accessChannel;
    }

    public void setAccessChannel(AccessChannel accessChannel) {
        this.accessChannel = accessChannel;
    }
}
