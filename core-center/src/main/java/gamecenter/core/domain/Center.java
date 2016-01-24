package gamecenter.core.domain;

public class Center {
    private Integer centerid;

    private String centername;

    private Boolean enabled;

    private String centerRefId;

    private String extraparam;

    public Integer getCenterid() {
        return centerid;
    }

    public void setCenterid(Integer centerid) {
        this.centerid = centerid;
    }

    public String getCentername() {
        return centername;
    }

    public void setCentername(String centername) {
        this.centername = centername == null ? null : centername.trim();
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getExtraparam() {
        return extraparam;
    }

    public void setExtraparam(String extraparam) {
        this.extraparam = extraparam == null ? null : extraparam.trim();
    }

    public String getCenterRefId() {
        return centerRefId;
    }

    public void setCenterRefId(String centerRefId) {
        this.centerRefId = centerRefId;
    }
}