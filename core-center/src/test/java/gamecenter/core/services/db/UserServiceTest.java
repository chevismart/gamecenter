package gamecenter.core.services.db;

import static org.junit.Assert.*;
import gamecenter.core.services.db.UserService;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserServiceTest {
	
	static ApplicationContext ctx;
	static UserService userService ;
	
	@BeforeClass
	public static void initial() throws Exception {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		userService = (UserService)ctx.getBean("userService");
	}
	@Before
	public void setUp() throws Exception {
		String openId = "testOpenId";
		userService.removeWechatCustomer(openId);
	}

	@Test
	public void testAddWechatCustomer() {
		String openId = "testOpenId";
		assertEquals(userService.hasWechatCustomer(openId), false);
		
		assertEquals(userService.addWechatCustomer("testName", openId, "testappId"), true);
		assertEquals(userService.hasWechatCustomer(openId), true);
	}

}
