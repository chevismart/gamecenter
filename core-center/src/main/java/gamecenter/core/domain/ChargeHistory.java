package gamecenter.core.domain;

import java.util.Date;

public class ChargeHistory {
    private Integer chargeHistoryId;
    private Integer customerId;
    private Integer wechatId;
    private Integer centerId;
    private Date timestamp;
    private Double paid;
    private Integer coin;
    private String transactionId;
    private Integer paymentSystemId;

    @Override
    public String toString() {
        return "ChargeHistory{" +
                "chargeHistoryId=" + chargeHistoryId +
                ", customerId=" + customerId +
                ", wechatId=" + wechatId +
                ", centerId=" + centerId +
                ", timestamp=" + timestamp +
                ", paid=" + paid +
                ", coin=" + coin +
                ", transactionId='" + transactionId + '\'' +
                ", paymentSystemId=" + paymentSystemId +
                '}';
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Double getPaid() {
        return paid;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }

    public Integer getChargeHistoryId() {
        return chargeHistoryId;
    }

    public void setChargeHistoryId(Integer chargeHistoryId) {
        this.chargeHistoryId = chargeHistoryId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getWechatId() {
        return wechatId;
    }

    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }

    public Integer getCenterId() {
        return centerId;
    }

    public void setCenterId(Integer centerId) {
        this.centerId = centerId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getPaymentSystemId() {
        return paymentSystemId;
    }

    public void setPaymentSystemId(Integer paymentSystemId) {
        this.paymentSystemId = paymentSystemId;
    }
}
