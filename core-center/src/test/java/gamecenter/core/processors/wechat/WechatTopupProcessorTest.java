package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.ActionContext;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.services.BroadcastService;
import gamecenter.core.services.CloudServerService;
import gamecenter.core.services.db.CustomerService;
import gamecenter.core.services.db.DBServices;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.struts2.StrutsStatics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.opensymphony.xwork2.Action.ERROR;
import static gamecenter.core.constants.CommonConstants.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class WechatTopupProcessorTest {
    public static final String randomString = RandomStringUtils.randomAlphanumeric(10);
    private final DBServices dbServices = mock(DBServices.class);
    private final UserProfile userProfile = mock(UserProfile.class);
    private final CloudServerService cloudServerService = mock(CloudServerService.class);
    private final BroadcastService broadcastService = mock(BroadcastService.class);
    private final Logger logger = mock(Logger.class);
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    String appId = RandomStringUtils.randomAlphanumeric(10);
    String deviceId = RandomStringUtils.randomAlphanumeric(10);
    String oId = RandomStringUtils.randomAlphanumeric(10);
    String mac = RandomStringUtils.randomAlphanumeric(10);
    String coins = "1";
    private WechatTopupProcessor wechatTopupProcessor;

    @Before
    public void setUp() throws Exception {
        // set the context
        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put(StrutsStatics.HTTP_REQUEST, request);
        ActionContext.setContext(new ActionContext(contextMap));

        when(userProfile.toString()).thenReturn(randomString);
        when(request.getParameter(WECHAT_STATE_PARAM_APPID)).thenReturn(appId);
        when(request.getParameter(WECHAT_STATE_PARAM_DEVICEID)).thenReturn(mac);
        when(request.getParameter(WECHAT_TOP_UP_COINS)).thenReturn(coins);
        when(userProfile.getOpenId()).thenReturn(oId);

        wechatTopupProcessor = new WechatTopupProcessor(dbServices, userProfile, cloudServerService, broadcastService);
        Whitebox.setInternalState(wechatTopupProcessor, "logger", logger);
    }

    @Test
    public void missingAppIdWillBeWarningAndReturnError() throws Exception {
        when(request.getParameter(WECHAT_STATE_PARAM_APPID)).thenReturn(null);
        String result = wechatTopupProcessor.execute();
        verify(logger, times(1)).warn("Missing parameters for top up! appId={}, mac={}, openId={}, coins={}", null, mac, oId, coins);
        assertThat(result, is(ERROR));
    }

    @Test
    public void missingMacWillBeWarningAndReturnError() throws Exception {
        when(request.getParameter(WECHAT_STATE_PARAM_DEVICEID)).thenReturn(null);
        String result = wechatTopupProcessor.execute();
        verify(logger, times(1)).warn("Missing parameters for top up! appId={}, mac={}, openId={}, coins={}", appId, null, oId, coins);
        assertThat(result, is(ERROR));
    }

    @Test
    public void missingOpenIdWillBeWarningAndReturnError() throws Exception {
        when(userProfile.getOpenId()).thenReturn(null);
        String result = wechatTopupProcessor.execute();
        verify(logger, times(1)).warn("Missing parameters for top up! appId={}, mac={}, openId={}, coins={}", appId, mac, null, coins);
        assertThat(result, is(ERROR));
    }

    @Test
    public void missingCoinWillBeWarningAndReturnError() throws Exception {
        when(request.getParameter(WECHAT_TOP_UP_COINS)).thenReturn(null);
        String result = wechatTopupProcessor.execute();
        verify(logger, times(1)).warn("Missing parameters for top up! appId={}, mac={}, openId={}, coins={}", appId, mac, oId, null);
        assertThat(result, is(ERROR));
    }

    @Test
    public void executeAsExcepted() throws Exception {
        CustomerService customerService = mock(CustomerService.class);
        when(dbServices.getCustomerService()).thenReturn(customerService);
        when(customerService.getCustomerWalletBalanceByOpenId(oId)).thenReturn(10);
        when(cloudServerService.isHanding(oId)).thenReturn(false);
        when(cloudServerService.topUpCoin(mac, 1, oId)).thenReturn(true);
        when(customerService.payBill(oId, 1)).thenReturn(true);

        String result = wechatTopupProcessor.execute();
        verify(logger, times(1)).info("Preparing top up");
        verify(logger, times(1)).debug("Start to topup!\n User profile is: {}", randomString);
        verify(logger, times(1)).info("Top up {} coins for {} {}", coins, oId, true ? "success" : "fail");
        verify(dbServices, only()).getCustomerService();
        verify(cloudServerService, times(1)).isHanding(oId);
        verify(customerService, times(1)).getCustomerWalletBalanceByOpenId(oId);
        assertThat(result, is("success"));
    }

    @Test
    public void insufficientCoinToTopUpWillBeError() throws Exception {
        CustomerService customerService = mock(CustomerService.class);
        when(dbServices.getCustomerService()).thenReturn(customerService);
        when(customerService.getCustomerWalletBalanceByOpenId(oId)).thenReturn(0);
        String result = wechatTopupProcessor.execute();
        verify(logger, times(1)).info("Preparing top up");
        verify(logger, times(1)).debug("Start to topup!\n User profile is: {}", randomString);
        verify(logger, times(1)).warn("User {} wallet balance {} is insufficient for {} coin top up", oId, 0-1, coins);
        verify(dbServices, only()).getCustomerService();
        verify(cloudServerService, never()).isHanding(oId);
        verify(customerService, times(0)).payBill(anyString(),anyInt());
        assertThat(result, is("error"));
    }

    @Test
    public void topupCoinIsProcessingWillBeError() throws Exception {
        CustomerService customerService = mock(CustomerService.class);
        when(dbServices.getCustomerService()).thenReturn(customerService);
        when(customerService.getCustomerWalletBalanceByOpenId(oId)).thenReturn(10);
        when(cloudServerService.isHanding(oId)).thenReturn(true);
        String result = wechatTopupProcessor.execute();
        verify(logger, times(1)).info("Preparing top up");
        verify(logger, times(1)).debug("Start to topup!\n User profile is: {}", randomString);
        verify(logger, times(1)).warn("User {} wallet balance {} is insufficient for {} coin top up", oId, 9, coins);
        verify(logger, times(0)).info("Top up {} coins for {} {}", coins, oId, false ? "success" : "fail");
        verify(dbServices, only()).getCustomerService();
        verify(cloudServerService, times(1)).isHanding(oId);
        verify(customerService, times(0)).payBill(anyString(),anyInt());
        assertThat(result, is("error"));
    }

    @Test
    public void topupFailWillBeError() throws Exception {
        CustomerService customerService = mock(CustomerService.class);
        when(dbServices.getCustomerService()).thenReturn(customerService);
        when(customerService.getCustomerWalletBalanceByOpenId(oId)).thenReturn(10);
        when(cloudServerService.isHanding(oId)).thenReturn(false);
        when(cloudServerService.topUpCoin(mac, 1, oId)).thenReturn(false);
        when(customerService.payBill(oId, 1)).thenReturn(true);

        String result = wechatTopupProcessor.execute();
        verify(logger, times(1)).info("Preparing top up");
        verify(logger, times(1)).debug("Start to topup!\n User profile is: {}", randomString);
        verify(logger, times(1)).info("Top up {} coins for {} {}", coins, oId, false ? "success" : "fail");
        verify(dbServices, only()).getCustomerService();
        verify(cloudServerService, times(1)).isHanding(oId);
        verify(customerService, times(1)).getCustomerWalletBalanceByOpenId(oId);
        verify(customerService, times(0)).payBill(anyString(),anyInt());
        assertThat(result, is("error"));
    }

    @After
    public void tearDown() throws Exception {
        ActionContext.setContext(null);
    }
}