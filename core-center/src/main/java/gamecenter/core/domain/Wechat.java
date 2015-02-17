package gamecenter.core.domain;

public class Wechat {
    private Integer wechatid;

    private String channelname;

    private String wechatappname;

    private String wechatappid;

    private String wechatappsecret;

    private String wechatmchid;

    private String wechatpaykey;

    public Integer getWechatid() {
        return wechatid;
    }

    public void setWechatid(Integer wechatid) {
        this.wechatid = wechatid;
    }

    public String getChannelname() {
        return channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname == null ? null : channelname.trim();
    }

    public String getWechatappname() {
        return wechatappname;
    }

    public void setWechatappname(String wechatappname) {
        this.wechatappname = wechatappname == null ? null : wechatappname.trim();
    }

    public String getWechatappid() {
        return wechatappid;
    }

    public void setWechatappid(String wechatappid) {
        this.wechatappid = wechatappid == null ? null : wechatappid.trim();
    }

    public String getWechatappsecret() {
        return wechatappsecret;
    }

    public void setWechatappsecret(String wechatappsecret) {
        this.wechatappsecret = wechatappsecret == null ? null : wechatappsecret.trim();
    }

    public String getWechatmchid() {
        return wechatmchid;
    }

    public void setWechatmchid(String wechatmchid) {
        this.wechatmchid = wechatmchid == null ? null : wechatmchid.trim();
    }

    public String getWechatpaykey() {
        return wechatpaykey;
    }

    public void setWechatpaykey(String wechatpaykey) {
        this.wechatpaykey = wechatpaykey == null ? null : wechatpaykey.trim();
    }
}