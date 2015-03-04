package gamecenter.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chevis on 15/1/11.
 */
public class CollectionUtils {

    public static Map<String, String> EMPTY_MAP = new HashMap<String, String>();

    public static Map<String, String> removeEmptyValueEntry(Map<String, String> oriMap) {
        Map<String, String> newMap = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : oriMap.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getValue())) newMap.put(entry.getKey(), entry.getValue());
        }
        return newMap;
    }
}
