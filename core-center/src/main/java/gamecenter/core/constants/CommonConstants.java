package gamecenter.core.constants;

/**
 * Created by Chevis on 2014/12/11.
 */
public class CommonConstants {
    public static final String TASKS_BEAN_NAME = "tasks";
    public static final String TASK_EXECUTOR_NAME = "taskExecutor";
    public static final String WECHAT_AUTH_STATE = "state";
    public static final String WECHAT_AUTH_CODE = "code";
    public static final String SESSION_KEY_IS_LOGIN_VALID = "loginStatus";
    public static final String SESSION_KEY_IS_LOGINOUT = "logout";


    public static final String WECHAT_STATE_PARAM_APPID = "appid";
    public static final String WECHAT_STATE_PARAM_DEVICEID = "deviceid";


    public static final int DEFAULT_WECHAT_ACCESS_TOKEN_EXPIRY_TIME_IN_SECOND = 5400;
    public static final int EXPIRY_SHIFT_PERIOD_IN_SECOND = 120;
    public static final int DEFAULT_WECHAT_ACCESS_TOKEN_CHECK_INTERVAL_IN_SECOND = EXPIRY_SHIFT_PERIOD_IN_SECOND / 2;

    public static final String ACCESS_ROUTER_REGISTER = "register";
    public static final String ACCESS_ROUTER_WECHAT_OAUTH = "wechatAuth";

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    public static class Payment {
        public static final String AMOUNT_IN_CENT = "amount";
    }

}
