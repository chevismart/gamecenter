package gamecenter.core.services.db;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;

public class UserServiceTest {

    static ApplicationContext ctx;
    static UserService userService;
    private String testOpenId = "testOpenId";

    @BeforeClass
    public static void initial() throws Exception {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        userService = (UserService) ctx.getBean("userService");
    }

    @Before
    public void setUp() throws Exception {
        userService.removeWechatCustomer(testOpenId);
    }

    @After
    public void tearDown() throws Exception {
        userService.removeWechatCustomer(testOpenId);
    }

    @Test
    public void testAddWechatCustomer() {
        assertEquals(userService.hasWechatCustomer(testOpenId), false);

        assertEquals(userService.addWechatCustomer("testName", testOpenId, "testappId"), true);
        assertEquals(userService.hasWechatCustomer(testOpenId), true);
    }

}
