package gamecenter.core.services.db;

import static org.junit.Assert.*;
import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.dao.DeviceMapper;
import gamecenter.core.dao.PlayrecordMapper;
import gamecenter.core.dao.TradeMapper;
import gamecenter.core.services.db.SubscribeService;
import gamecenter.core.services.db.UserService;

import java.util.Arrays;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.interceptor.annotations.After;


public class SubscribeServiceTest {
	
	static ApplicationContext ctx;
	static SubscribeService subscribeService;
	@BeforeClass
	public static void initial() throws Exception {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		subscribeService = (SubscribeService)ctx.getBean("subscribeService");
	}
	@Before
	public void setUp() throws Exception {
		String openId = "testopenId";
		subscribeService.removeSubsribeInfo(openId);
	}
	
	@Test
	public void testSubscribeService() {
		String openId = "testopenId";
		String deviceMacAddr = "testMacAddr";
		//初始
		assertEquals(subscribeService.getHasSubscibed(openId),false);
		assertEquals(subscribeService.getHasSubscribeBonus(openId),false);
		//订阅
		assertEquals(subscribeService.subscribe(openId), true);
		assertEquals(subscribeService.getHasSubscibed(openId),true);
		assertEquals(subscribeService.getHasSubscribeBonus(openId),true);
		//使用订阅福利
		assertEquals(subscribeService.useSubscribeBonus(openId, deviceMacAddr), true);
		assertEquals(subscribeService.getHasSubscribeBonus(openId), false);
		assertEquals(subscribeService.getHasSubscibed(openId),true);
		
	}

	@After
	public void after(){
		String openId = "testopenId";
		subscribeService.removeSubsribeInfo(openId);
	}
}
