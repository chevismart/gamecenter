package gamecenter.core.services.db;

import com.opensymphony.xwork2.interceptor.annotations.After;
import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.domain.CustomerWechat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class SubscribeServiceTest {

    static ApplicationContext ctx;
    static SubscribeService subscribeService;
    private String testOpenId = "testOpenId";
    private String deviceMacAddr = "testMacAddr";
    CustomerWechatMapper mapper = mock(CustomerWechatMapper.class);
    CustomerWechat customerWechat = mock(CustomerWechat.class);

    @BeforeClass
    public static void initial() throws Exception {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        subscribeService = (SubscribeService) ctx.getBean("subscribeService");
    }

    @Before
    public void setUp() throws Exception {
        subscribeService = new SubscribeService();
        Whitebox.setInternalState(subscribeService, "customerWechatMapper", mapper);
    }

    @Test
    public void userSubscribedBeforeAndReturnFalse() {
        when(mapper.selectByOpenId(testOpenId)).thenReturn(customerWechat);
        when(customerWechat.getSubscribetime()).thenReturn(new Date());
        assertFalse(subscribeService.subscribe(testOpenId));
    }

    @Test
    public void userDoesNotSubscribeBeforeAndCreateNewRecordForFirstSubscribe() throws Exception {
        when(mapper.selectByOpenId(testOpenId)).thenReturn(customerWechat);
        when(customerWechat.getSubscribetime()).thenReturn(null);
        when(mapper.updateByPrimaryKey(customerWechat)).thenReturn(1);
        boolean result = subscribeService.subscribe(testOpenId);
        assertTrue(result);
    }

    @Ignore
    @Test
    public void testSubscribeService() {
        // The following is integration test but not unit test
        //初始
        assertFalse(subscribeService.getHasSubscibed(testOpenId));
        assertFalse(subscribeService.getHasSubscribeBonus(testOpenId));
        //订阅
        assertTrue(subscribeService.subscribe(testOpenId));
        assertTrue(subscribeService.getHasSubscibed(testOpenId));
        assertTrue(subscribeService.getHasSubscribeBonus(testOpenId));
        //使用订阅福利
        assertTrue(subscribeService.useSubscribeBonus(testOpenId, deviceMacAddr));
        assertFalse(subscribeService.getHasSubscribeBonus(testOpenId));
        assertTrue(subscribeService.getHasSubscibed(testOpenId));
    }

    @After
    public void after() {
        subscribeService.removeSubsribeInfo(testOpenId);
    }
}
