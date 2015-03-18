package gamecenter.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chevis on 2014/12/18.
 */
public class ParameterUtil {

    public static final String SEPERATOR = ",";
    public static final String COLON = ":";

    public static Map<String, String> extractParam(String paramStr) {
        String params[] = paramStr.split(SEPERATOR);
        Map<String, String> paramsMap = new HashMap<String, String>();
        for (String param : params) {
            String[] val = param.split(COLON);
            paramsMap.put(val[0].trim(), val[1].trim());
        }
        return paramsMap;
    }
}
