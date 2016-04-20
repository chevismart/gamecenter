package gamecenter.core.services;

import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.dao.DeviceMapper;
import gamecenter.core.dao.PlayrecordMapper;
import gamecenter.core.domain.CustomerWechat;
import gamecenter.core.domain.Device;
import gamecenter.core.services.db.CustomerService;
import gamecenter.core.services.db.DBServices;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CloudServerServiceTest {

    private final DeviceMapper deviceMapper = mock(DeviceMapper.class);
    private final CustomerWechatMapper customerWechatMapper = mock(CustomerWechatMapper.class);
    private final PlayrecordMapper playMapper = mock(PlayrecordMapper.class);
    private final DBServices dbServices = mock(DBServices.class);
    private final String mac = RandomStringUtils.randomAlphanumeric(8);
    private final int coins = 10;
    private final String openId = RandomStringUtils.randomAlphanumeric(10);
    private final CustomerService customerService = mock(CustomerService.class);
    private final Device device = new Device();
    private final CustomerWechat customerWechat = new CustomerWechat();
    private final CoinTopUpService coinTopUpService = mock(CoinTopUpService.class);
    private final Map<String, String> params = new HashMap<String, String>();
    private final HttpResponse httpResponse = mock(HttpResponse.class);
    private CloudServerService cloudServerService;
    private java.util.concurrent.Future<org.apache.http.HttpResponse> futureResponse = mock(Future.class);

    @Before
    public void setUp() throws Exception {
        cloudServerService = new CloudServerService(deviceMapper, customerWechatMapper, playMapper, dbServices, coinTopUpService);
        when(dbServices.getCustomerService()).thenReturn(customerService);
        when(coinTopUpService.submit(any(Map.class))).thenReturn(futureResponse);
        when(futureResponse.get()).thenReturn(httpResponse);
    }

//    @Test
//    public void topupCoinSuccessfully() throws Exception {
//        when(customerService.getCustomerWalletBalanceByOpenId(openId)).thenReturn(coins + 1);
//        when(deviceMapper.selectByMacAddr(mac)).thenReturn(device);
//        when(customerWechatMapper.selectByOpenId(openId)).thenReturn(customerWechat);
//        when(playMapper.insert(any(Playrecord.class))).thenReturn(0);
//        when(customerService.payBill(openId, coins)).thenReturn(true);
//        when(httpResponse.getEntity()).thenReturn(params.toString().cha)
//        assertThat(cloudServerService.topUpCoin(mac, coins, openId), is(true));
//    }

//    @Test
//    public void insufficientCoinToTopUpWillBeError() throws Exception {
//        CustomerService customerService = mock(CustomerService.class);
//        when(dbServices.getCustomerService()).thenReturn(customerService);
//        when(customerService.getCustomerWalletBalanceByOpenId(oId)).thenReturn(0);
//        String result = wechatTopupProcessor.execute();
//        verify(logger, times(1)).info("Preparing top up");
//        verify(logger, times(1)).debug("Start to topup!\n User profile is: {}", randomString);
//        verify(logger, times(1)).warn("User {} wallet balance {} is insufficient for {} coin top up", oId, 0 - 1, coins);
//        verify(dbServices, only()).getCustomerService();
//        verify(cloudServerService, never()).isHanding(oId);
//        verify(customerService, times(0)).payBill(anyString(), anyInt());
//        assertThat(result, is("error"));
//    }
//
//    @Test
//    public void topupCoinIsProcessingWillBeError() throws Exception {
//        CustomerService customerService = mock(CustomerService.class);
//        when(dbServices.getCustomerService()).thenReturn(customerService);
//        when(customerService.getCustomerWalletBalanceByOpenId(oId)).thenReturn(10);
//        when(cloudServerService.isHanding(oId)).thenReturn(true);
//        String result = wechatTopupProcessor.execute();
//        verify(logger, times(1)).info("Preparing top up");
//        verify(logger, times(1)).debug("Start to topup!\n User profile is: {}", randomString);
//        verify(logger, times(1)).warn("User {} wallet balance {} is insufficient for {} coin top up", oId, 9, coins);
//        verify(logger, times(0)).info("Top up {} coins for {} {}", coins, oId, false ? "success" : "fail");
//        verify(dbServices, only()).getCustomerService();
//        verify(cloudServerService, times(1)).isHanding(oId);
//        verify(customerService, times(0)).payBill(anyString(), anyInt());
//        assertThat(result, is("error"));
//    }
//
//    @Test
//    public void topupFailWillBeError() throws Exception {
//        CustomerService customerService = mock(CustomerService.class);
//        when(dbServices.getCustomerService()).thenReturn(customerService);
//        when(customerService.getCustomerWalletBalanceByOpenId(oId)).thenReturn(10);
//        when(cloudServerService.isHanding(oId)).thenReturn(false);
//        when(cloudServerService.topUpCoin(mac, 1, oId)).thenReturn(false);
//        when(customerService.payBill(oId, 1)).thenReturn(true);
//
//        String result = wechatTopupProcessor.execute();
//        verify(logger, times(1)).info("Preparing top up");
//        verify(logger, times(1)).debug("Start to topup!\n User profile is: {}", randomString);
//        verify(logger, times(1)).info("Top up {} coins for {} {}", coins, oId, false ? "success" : "fail");
//        verify(dbServices, only()).getCustomerService();
//        verify(cloudServerService, times(1)).isHanding(oId);
//        verify(customerService, times(1)).getCustomerWalletBalanceByOpenId(oId);
//        verify(customerService, times(0)).payBill(anyString(), anyInt());
//        assertThat(result, is("error"));
//    }

}