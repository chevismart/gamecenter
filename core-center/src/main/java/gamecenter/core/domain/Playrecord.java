package gamecenter.core.domain;

import java.util.Date;

public class Playrecord {
    private Integer playrecordid;

    private Integer customerid;

    private Integer deviceid;

    private Date time;

    private String refid;

    private Integer quantity;

    public Integer getPlayrecordid() {
        return playrecordid;
    }

    public void setPlayrecordid(Integer playrecordid) {
        this.playrecordid = playrecordid;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}