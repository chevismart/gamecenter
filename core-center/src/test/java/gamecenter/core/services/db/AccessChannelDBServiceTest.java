package gamecenter.core.services.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccessChannelDBServiceTest {

    private AccessChannelDBService accessChannelDBService;

    //    @Before
    public void setUp() throws Exception {
        ApplicationContext aCtx = new ClassPathXmlApplicationContext(
                "classpath:applicationContext.xml");
        accessChannelDBService = (AccessChannelDBService) aCtx.getBean("accessChannelDBService");
    }

    //    @Test
    public void accessChannelConnection() throws Exception {
        List<String> accessChannels = accessChannelDBService.getAllAccessChannel();
        assertNotNull(accessChannels);

        for (String accessChannel : accessChannels) {
            System.err.println(accessChannel);
        }
    }

    //    @Test
    public void retrieveAccessChannelIdSuccessfully() throws Exception {
        String channelId = "1";
        String channel = accessChannelDBService.getAccessChannel(channelId);
        System.err.println(channel);
        assertNotNull(channel);
        assertEquals(channelId, channel);
    }
}