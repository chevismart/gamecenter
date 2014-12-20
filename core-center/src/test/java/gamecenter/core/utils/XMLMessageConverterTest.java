package gamecenter.core.utils;

import gamecenter.core.beans.AppProfile;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XMLMessageConverterTest {

    String example = "<applications>\n" +
            "    <application>\n" +
            "        <appName>荔园新天地</appName>\n" +
            "        <appId>liyuanapp</appId>\n" +
            "        <wechatProfile>\n" +
            "            <wechatAppId>wxe89a9d2fa17df80f</wechatAppId>\n" +
            "            <wechatAppSecret>71d8fc7778571e6b54712953b68084e4</wechatAppSecret>\n" +
            "        </wechatProfile>\n" +
            "    </application>\n" +
            "</applications>";

    @Test
    public void convertXmlSuccessfully() throws Exception {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("appProfiles.xml");
        assertNotNull(is);
        List<AppProfile> appProfileList = (new XMLMessageConverter("applications")).convertXML2Messages(is);
        assertEquals(1, appProfileList.size());
    }
}