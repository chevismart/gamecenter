package gamecenter.core.beans;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Chevis on 2015/1/17.
 */
public class CoreCenterHost {
    public static final String CORECENTER_HOST = "wawaonline.net";
    public static final int CORECENTER_PORT = 80;
    public static final String CONTEXT_NAME = "corecenter";
    public static final String SEPEARTOR = "/";
    public static final String COLON = ":";
    public static final String CORECENTER_HOST_AND_PORT = StringUtils.join(StringJoiner(CORECENTER_HOST, String.valueOf(CORECENTER_PORT)), COLON);
    private static final String WECHAT_PAYMENT_CALLBACK = "wechatNotice";
    // Sample: alcock.gicp.net:8888/corecenter/wechatNotice
    public static final String WECHAT_PAYMENT_NOTIFICATION_CALLBACK_URL = StringUtils.join(StringJoiner(CORECENTER_HOST_AND_PORT, CONTEXT_NAME, WECHAT_PAYMENT_CALLBACK), SEPEARTOR);
    private static final String WECHAT_ORDER = "wechatOrder";
    // Sample: alcock.gicp.net:8888/corecenter/wechatOrder
    public static final String WECHAT_ORDER_URL = StringUtils.join(StringJoiner(CORECENTER_HOST_AND_PORT, CONTEXT_NAME, WECHAT_ORDER), SEPEARTOR);
    private static final String AUTH = "auth";
    // Sample: alcock.gicp.net:8888/corecenter/auth
    public static final String AUTH_URL = StringUtils.join(StringJoiner(CORECENTER_HOST_AND_PORT, CONTEXT_NAME, AUTH), SEPEARTOR);

    public static String getHttpURL(String val) {
        return "http://" + val;
    }

    private static String[] StringJoiner(String... strings) {
        return strings;
    }

    public static void main(String[] args) {
        System.err.println(WECHAT_ORDER_URL);
    }
}
