package gamecenter.core.services.wechat;

import gamecenter.core.services.db.SubscribeService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.fail;
@Ignore
public class SubscribeServiceIntegrationTest {
    ApplicationContext ctx;
    SubscribeService subscribeService;

    @Before
    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        subscribeService = (SubscribeService) ctx.getBean("subscribeService");
    }

    @Test
    public void testSubscribe() {
        fail("Not yet implemented");
    }

    @Test
    public void testUseSubscribeBonus() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetHasSubscibed() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetHasSubscribeBonus() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetIsSubscibing() {
        fail("Not yet implemented");
    }

}
