package gamecenter.core.constant;

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

    public static final int DEFAULT_WECHAT_ACCESS_TOKEN_EXPIRY_TIME_IN_SECOND = 5400;
    public static final int DEFAULT_WECHAT_ACCESS_TOKEN_CHECK_INTERVAL_IN_SECOND = 60;
    public static final int EXPIRY_SHIFT_PERIOD_IN_SECOND = 120;

}
