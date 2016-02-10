package gamecenter.core.services.db;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class ChargeHistoryServiceTest {

    static ApplicationContext ctx;
    static ChargeHistoryService chargeHistoryService;

    @BeforeClass
    public static void initial() throws Exception {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        chargeHistoryService = (ChargeHistoryService) ctx.getBean("chargeHistoryService");
    }

    @Test
    public void newAChargeHistoryRecord() throws Exception {
//        chargeHistoryService.addChargeHistory();
        Date now = new Date();
        Date yesterday = DateUtils.addDays(now, -1);
        System.err.println(chargeHistoryService.selectPlayRecordByDate(yesterday, now).size());
    }
}