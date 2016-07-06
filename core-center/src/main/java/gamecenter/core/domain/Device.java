package gamecenter.core.domain;

public class Device {
    private Integer deviceid;

    private Integer centerid;

    private String devicename;

    private String macaddr;

    private String powerstatus;

    private String connectionstatus;

    private String remark;

    private String devicedesc;

    private int paymenttype;

    public Integer getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(Integer deviceid) {
        this.deviceid = deviceid;
    }

    public Integer getCenterid() {
        return centerid;
    }

    public void setCenterid(Integer centerid) {
        this.centerid = centerid;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename == null ? null : devicename.trim();
    }

    public String getMacaddr() {
        return macaddr;
    }

    public void setMacaddr(String macaddr) {
        this.macaddr = macaddr == null ? null : macaddr.trim();
    }

    public String getPowerstatus() {
        return powerstatus;
    }

    public void setPowerstatus(String powerstatus) {
        this.powerstatus = powerstatus == null ? null : powerstatus.trim();
    }

    public String getConnectionstatus() {
        return connectionstatus;
    }

    public void setConnectionstatus(String connectionstatus) {
        this.connectionstatus = connectionstatus == null ? null : connectionstatus.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDevicedesc() {
        return devicedesc;
    }

    public void setDevicedesc(String devicedesc) {
        this.devicedesc = devicedesc == null ? null : devicedesc.trim();
    }

    public int getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(int paymenttype) {
        this.paymenttype = paymenttype;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceid=" + deviceid +
                ", centerid=" + centerid +
                ", devicename='" + devicename + '\'' +
                ", macaddr='" + macaddr + '\'' +
                ", powerstatus='" + powerstatus + '\'' +
                ", connectionstatus='" + connectionstatus + '\'' +
                ", remark='" + remark + '\'' +
                ", devicedesc='" + devicedesc + '\'' +
                ", paymenttype=" + paymenttype +
                '}';
    }
}