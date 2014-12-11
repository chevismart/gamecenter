package gamecenter.core.beans.wechat;

import java.util.Date;

/**
 * Created by Chevis on 2014/12/11.
 */
public class Token {
    private String token;

    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
