package gamecenter.core.domain;

import java.util.Date;

public class CustomerWechat extends CustomerWechatKey {
    private String openid;

    private Boolean subscribebonus;

    private Date subscribetime;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public Boolean getSubscribebonus() {
        return subscribebonus;
    }

    public void setSubscribebonus(Boolean subscribebonus) {
        this.subscribebonus = subscribebonus;
    }

    public Date getSubscribetime() {
        return subscribetime;
    }

    public void setSubscribetime(Date subscribetime) {
        this.subscribetime = subscribetime;
    }

    @Override
    public String toString() {
        return "CustomerWechat{" +
                "openid='" + openid + '\'' +
                ", subscribebonus=" + subscribebonus +
                ", subscribetime=" + subscribetime +
                '}';
    }
}