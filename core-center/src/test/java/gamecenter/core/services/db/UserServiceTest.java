package gamecenter.core.services.db;

import org.springframework.context.ApplicationContext;

public class UserServiceTest {

    static ApplicationContext ctx;
    static UserService userService;
    private String testOpenId = "testOpenId";

//    @BeforeClass
//    public static void initial() throws Exception {
//        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        userService = (UserService) ctx.getBean("userService");
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        userService.removeWechatCustomer(testOpenId);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        userService.removeWechatCustomer(testOpenId);
//    }
//
//    @Ignore
//    @Test
//    public void testAddWechatCustomer() {
//        assertEquals(userService.hasWechatCustomer(testOpenId), false);
//
//        assertEquals(userService.addWechatCustomer("testName", testOpenId, "testappId"), true);
//        assertEquals(userService.hasWechatCustomer(testOpenId), true);
//    }
//
//    @Test
//    public void testName() throws Exception {
////        User userList =userService.getOperatorByOpenId("oJpyYuBcMmRKmVCt6AaAKN9EDGac");
//        userService.hasWechatCustomer("oJpyYuBcMmRKmVCt6AaAKN9EDGac");
//    }
}
