package gamecenter.core.beans;

/**
 * Created by Chevis on 2014/12/16.
 */
public enum AccessChannel {
    WECHAT("WECHAT"),
    BROWSER("BROWSER");
    private String name;

    AccessChannel(String name) {
        this.name = name;
    }
}
