package gamecenter.core.beans;

/**
 * Created by Chevis on 2014/12/16.
 */
public class UserProfile {
    private String openId;
    private String displayName;
    private String internalId; //channel+id
    private AccessInfo accessInfo;
    private String userImgUrl;
    private Boolean isFollowed;
    private String deviceId;
    private int bonus = 0;

    @Override
    public String toString() {
        return "UserProfile{" +
                "openId='" + openId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", internalId='" + internalId + '\'' +
                ", accessInfo=" + accessInfo +
                ", userImgUrl='" + userImgUrl + '\'' +
                ", isFollowed=" + isFollowed +
                ", deviceId='" + deviceId + '\'' +
                ", bonus=" + bonus +
                '}';
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(Boolean isFollowed) {
        this.isFollowed = isFollowed;
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


    public AccessInfo getAccessInfo() {
        return accessInfo;
    }

    public void setAccessInfo(AccessInfo accessInfo) {
        this.accessInfo = accessInfo;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}
