package gamecenter.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chevis on 2014/12/18.
 */
public class ParameterUtil {

    public static final String SEPERATOR = ",";
    public static final String COLON = ":";
    private static Logger logger = LoggerFactory.getLogger(ParameterUtil.class);

    public static Map<String, String> extractParam(String paramStr) {
        String params[] = paramStr.split(SEPERATOR);
        Map<String, String> paramsMap = new HashMap<String, String>();
        for (String param : params) {
            String[] val = param.split(COLON);
            paramsMap.put(val[0].trim(), val[1].trim());
        }
        return paramsMap;
    }

    public static String zipParam(Map<String, String> map) {
        String result = StringUtils.EMPTY;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String keyValuePair = StringUtils.join(new String[]{entry.getKey(), String.valueOf(entry.getValue())}, COLON);

            result = StringUtils.isEmpty(result) ? keyValuePair :
                    StringUtils.join(new String[]{result, keyValuePair}, SEPERATOR);

        }
        return result;
    }


    public static boolean hasEmptyParam(String... params) {
        for (String param : params) {
            if (StringUtils.isEmpty(param)) {
                logger.warn("There is empty param for checking: {}", params);
                return true;
            }
        }
        return false;
    }

    public static class NativePrePayOrder {

        public static String APPID = "A"; // APPID
        public static String COINS = "C"; // COINS
        public static String DEVICE = "D"; // DEVICE
        public static String MONEY = "M"; // MONEY

        public static String extractAppId(Map<String, String> map) {
            return map.get(APPID);
        }

        public static String extractCoins(Map<String, String> map) {
            return map.get(COINS);
        }

        public static String extractDeviceId(Map<String, String> map) {
            return map.get(DEVICE);
        }

        public static String extractMoney(Map<String, String> map) {
            return map.get(MONEY);
        }
    }
}
