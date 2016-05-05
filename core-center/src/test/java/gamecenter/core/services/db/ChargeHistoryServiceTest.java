package gamecenter.core.services.db;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.Date;

public class ChargeHistoryServiceTest {

    static ApplicationContext ctx;
    static ChargeHistoryService chargeHistoryService;
    static CustomerService customerService;

    @BeforeClass
    public static void initial() throws Exception {
//        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        chargeHistoryService = (ChargeHistoryService) ctx.getBean("chargeHistoryService");
//        customerService = (CustomerService) ctx.getBean("customerService");
    }

    @Test
    public void newAChargeHistoryRecord() throws Exception {
//        chargeHistoryService.addChargeHistory();
        Date now = new Date();
        Date yesterday = DateUtils.addDays(now, -1);
//        System.err.println(chargeHistoryService.selectPlayRecordByDate(yesterday, now).size());
//        System.err.println(customerService.getCustomerByRegistrationDateRange(yesterday,now).size());
//        System.err.println(customerService.countCustomerByWechatId(1));
    }
}