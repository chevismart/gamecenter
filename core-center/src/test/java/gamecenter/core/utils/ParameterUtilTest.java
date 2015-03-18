package gamecenter.core.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ParameterUtilTest {

    Random random = new Random();
    int total;

    @Before
    public void setUp() throws Exception {
        total = random.nextInt(10);
    }

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

    @Test
    public void hasParametersEmptyWhileVerifying() throws Exception {

        List<String> stringList = new ArrayList<String>();

        for (int index = 0; index < total; index++) {
            stringList.add(RandomStringUtils.random(10));
        }

        String emptyStr = StringUtils.EMPTY;
        stringList.add(emptyStr);
        assertTrue(ParameterUtil.hasEmptyParam(stringList.toArray(new String[stringList.size()])));
    }


    @Test
    public void allParametersHaveValueWhileVerifying() throws Exception {

        List<String> stringList = new ArrayList<String>();

        for (int index = 0; index < total; index++) {
            stringList.add(RandomStringUtils.random(10));
        }
        assertFalse(ParameterUtil.hasEmptyParam(stringList.toArray(new String[stringList.size()])));
    }

    @Test
    public void zipParametersMapSuccessfully() throws Exception {
        Map<String, String> map = new HashMap();

        String key1 = RandomStringUtils.randomAlphabetic(10);
        String key2 = RandomStringUtils.randomAlphabetic(10);
        String val1 = RandomStringUtils.randomAlphabetic(10);
        String val2 = RandomStringUtils.randomAlphabetic(10);

        map.put(key1, val1);
        map.put(key2, val2);

        String val = ParameterUtil.zipParam(map);
        Map<String, String> actualMap = ParameterUtil.extractParam(val);
        assertThat(actualMap.size(), is(2));
        assertThat(actualMap.get(key1), is(val1));
        assertThat(actualMap.get(key2), is(val2));
    }
}