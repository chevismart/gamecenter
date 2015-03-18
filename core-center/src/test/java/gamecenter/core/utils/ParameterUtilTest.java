package gamecenter.core.utils;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ParameterUtilTest {
    @Test
    public void extractParamSuccessfully() throws Exception {

        String key1 = "key0";
        String key2 = "key1";
        String val1 = "val0";
        String val2 = "val1";

        String params = new StringBuilder()
                .append(key1).append(ParameterUtil.COLON).append(val1)
                .append(ParameterUtil.SEPERATOR)
                .append(key2).append(ParameterUtil.COLON).append(val2)
                .toString();

        Map<String, String> paramMap = ParameterUtil.extractParam(params);

        assertEquals(2, paramMap.size());
        for (int i = 0; i < paramMap.size(); i++) {
            assertEquals("val" + i, paramMap.get("key" + i));
        }


    }
}