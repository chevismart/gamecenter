package gamecenter.core.processors.wechat;

import gamecenter.core.beans.UserProfile;
import gamecenter.core.services.BroadcastService;
import gamecenter.core.services.db.CustomerService;
import gamecenter.core.services.db.DBServices;
import gamecenter.core.services.db.SubscribeService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class WechatTryPlayProcessorTest {
    private final Logger logger = mock(Logger.class);
    private final UserProfile userProfile = mock(UserProfile.class);
    private final DBServices dbServices = mock(DBServices.class);
    private final BroadcastService broadcastService = mock(BroadcastService.class);
    private final CustomerService customerService = mock(CustomerService.class);
    private final SubscribeService subscribeService = mock(SubscribeService.class);
    private final String opId = RandomStringUtils.randomAlphanumeric(10);
    private final int bonus = 5;
    private final String mac = RandomStringUtils.randomAlphanumeric(10);
    WechatTryPlayProcessor wechatTryPlayProcessor;

    @Before
    public void setUp() throws Exception {
        when(dbServices.getCustomerService()).thenReturn(customerService);
        when(dbServices.getSubscribeService()).thenReturn(subscribeService);
        when(userProfile.getOpenId()).thenReturn(opId);
        when(userProfile.getDeviceId()).thenReturn(mac);
        wechatTryPlayProcessor = new WechatTryPlayProcessor(userProfile, dbServices, broadcastService);
        Whitebox.setInternalState(wechatTryPlayProcessor, "logger", logger);
    }

    @Test
    public void chargeWalletIfUserHasBonusAsExcepted() throws Exception {
//        when(userProfile.getBonus()).thenReturn(bonus);

    }

    @Test
    public void notChargeWalletIfUserHasNoBonusAndReturnError() throws Exception {
        when(userProfile.getBonus()).thenReturn(0);
        String result = wechatTryPlayProcessor.execute();
        verify(customerService, never()).chargeWallet(anyString(), anyInt());
        verify(logger, never()).info("Successfully charge wallet!");
        assertThat(result, is("error"));
    }

    @Test
    public void chargeWalletFailAndReturnError() throws Exception {
        when(userProfile.getBonus()).thenReturn(bonus);
        when(customerService.chargeWallet(opId, bonus)).thenReturn(false);
        String result = wechatTryPlayProcessor.execute();
        verify(customerService, only()).chargeWallet(opId, bonus);
        verify(logger, never()).info("Successfully charge wallet!");
        assertThat(result, is("error"));
    }

    @Test
    public void consumeBonusFailAndReturnError() throws Exception {
        when(userProfile.getBonus()).thenReturn(bonus);
        when(customerService.chargeWallet(opId, bonus)).thenReturn(true);
        when(subscribeService.consumeBonus(opId, mac, bonus)).thenReturn(false);
        String result = wechatTryPlayProcessor.execute();
        verify(customerService, only()).chargeWallet(opId, bonus);
        verify(logger, never()).info("Successfully charge wallet!");
        assertThat(result, is("error"));
    }
}