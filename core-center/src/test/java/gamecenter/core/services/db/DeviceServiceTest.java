package gamecenter.core.services.db;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DeviceServiceTest {
    static ApplicationContext ctx;
    static DeviceService deviceService;
    private String testOpenId = "testOpenId";

    @BeforeClass
    public static void initial() throws Exception {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        deviceService = (DeviceService) ctx.getBean("deviceService");
    }

//    @Before
//    public void setUp() throws Exception {
//        deviceService.removeWechatCustomer(testOpenId);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        deviceService.removeWechatCustomer(testOpenId);
//    }
//
//    @Ignore
//    @Test
//    public void testAddWechatCustomer() {
//        assertEquals(deviceService.hasWechatCustomer(testOpenId), false);
//
//        assertEquals(deviceService.addWechatCustomer("testName", testOpenId, "testappId"), true);
//        assertEquals(deviceService.hasWechatCustomer(testOpenId), true);
//    }
//
    @Test
    public void testName() throws Exception {
        System.err.println(deviceService.wechatIdByMacAndPaymentType(2,"8f12ff353039593043157733"));
    }

}