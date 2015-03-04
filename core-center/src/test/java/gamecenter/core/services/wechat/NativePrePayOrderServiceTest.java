package gamecenter.core.services.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.Figure;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.beans.wechat.WechatProfile;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import weixin.popular.api.PayMchAPI;

import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class NativePrePayOrderServiceTest {

    Logger logger = Mockito.mock(Logger.class);
    AppProfile appProfile = Mockito.mock(AppProfile.class);
    PayMchAPI payMchAPI = Mockito.mock(PayMchAPI.class);

    NativePrePayOrderService nativePrePayOrderService;
    WechatProfile wechatProfile;

    @Before
    public void setUp() throws Exception {
        nativePrePayOrderService = new NativePrePayOrderService();
        wechatProfile = Mockito.mock(WechatProfile.class);
        when(appProfile.getWechatProfile()).thenReturn(wechatProfile);
        when(appProfile.getAppId()).thenReturn(RandomStringUtils.random(10));

    }

    @Test
    public void requestPrePayOrderWithoutEnoughParameterFromPayNotification() throws Exception {

        PayNotification payNotification = new PayNotification();
        payNotification.setAppid(RandomStringUtils.random(10));
        Whitebox.setInternalState(nativePrePayOrderService, "logger", logger);
        String result = nativePrePayOrderService.requestPrePayOrder(payNotification, appProfile, 10);
        assertThat(result, is(StringUtils.EMPTY));
        verify(logger, times(1)).warn(("The parameters of pay notification is insufficient for the request."));
    }

    //    @Test
    public void requestPrePayOrderWithEnoughParameterFromPayNotification() throws Exception {

        PayNotification payNotification = Mockito.mock(PayNotification.class);
        Whitebox.setInternalState(nativePrePayOrderService, "logger", logger);
        Whitebox.setInternalState(nativePrePayOrderService, "payMchAPI", payMchAPI);

        when(payNotification.getAppid()).thenReturn(RandomStringUtils.random(10));
        when(payNotification.getOpenid()).thenReturn(RandomStringUtils.random(10));
        when(payNotification.getMch_id()).thenReturn(RandomStringUtils.random(10));
        when(payNotification.getNonce_str()).thenReturn(RandomStringUtils.random(32));
        when(payNotification.getTotal_fee()).thenReturn(String.valueOf(Figure.COIN_TO_MONEY.calculate((new Random()).nextInt())));


        String result = nativePrePayOrderService.requestPrePayOrder(payNotification, appProfile, 10);
        System.err.println(result);
        assertNotNull(result, is(StringUtils.EMPTY));
        verify(logger, never()).warn(("The parameters of pay notification is insufficient for the request."));
    }
}