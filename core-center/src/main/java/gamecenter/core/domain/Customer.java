package gamecenter.core.domain;

import java.util.Date;

public class Customer {
    private Integer customerid;

    private Integer userid;

    private Date registerdatetime;

    private Float wallet;

    private String name;

    private Boolean active;

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getRegisterdatetime() {
        return registerdatetime;
    }

    public void setRegisterdatetime(Date registerdatetime) {
        this.registerdatetime = registerdatetime;
    }

    public Float getWallet() {
        return wallet;
    }

    public void setWallet(Float wallet) {
        this.wallet = wallet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}