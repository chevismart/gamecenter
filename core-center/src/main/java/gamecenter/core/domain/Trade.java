package gamecenter.core.domain;

import java.util.Date;

public class Trade {
    private Integer tradeid;

    private Integer customerid;

    private Integer centerid;

    private Date tradetime;

    private Float amount;

    private String paymentsystem;

    private Integer ispaid;

    public Integer getTradeid() {
        return tradeid;
    }

    public void setTradeid(Integer tradeid) {
        this.tradeid = tradeid;
    }

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getCenterid() {
        return centerid;
    }

    public void setCenterid(Integer centerid) {
        this.centerid = centerid;
    }

    public Date getTradetime() {
        return tradetime;
    }

    public void setTradetime(Date tradetime) {
        this.tradetime = tradetime;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getPaymentsystem() {
        return paymentsystem;
    }

    public void setPaymentsystem(String paymentsystem) {
        this.paymentsystem = paymentsystem == null ? null : paymentsystem.trim();
    }

    public Integer getIspaid() {
        return ispaid;
    }

    public void setIspaid(Integer ispaid) {
        this.ispaid = ispaid;
    }
}