package gamecenter.core.processors.wechat;

import gamecenter.core.beans.GlobalPaymentBean;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.services.CloudServerService;
import gamecenter.core.services.db.DBServices;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;

import static org.mockito.Mockito.mock;

public class PaidNotificationResponseHandlerTest {

    private final ProfileManager profileManager = mock(ProfileManager.class);
    private final DBServices dbServices = mock(DBServices.class);
    private final CloudServerService cloudService = mock(CloudServerService.class);
    private final PayNotification payNotification = new PayNotification();
    public GlobalPaymentBean bean;
    private PaidNotificationResponseHandler handler;

    @Before
    public void setUp() throws Exception {
        bean = new GlobalPaymentBean();
        handler = new PaidNotificationResponseHandler(profileManager, bean, dbServices, cloudService);
        payNotification.setOut_trade_no(RandomStringUtils.randomAlphanumeric(10));
    }

}