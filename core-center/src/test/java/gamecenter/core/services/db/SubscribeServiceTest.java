package gamecenter.core.services.db;

import gamecenter.core.dao.CustomerMapper;
import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.dao.DeviceMapper;
import gamecenter.core.dao.PlayrecordMapper;
import gamecenter.core.domain.CustomerWechat;
import gamecenter.core.domain.Device;
import gamecenter.core.domain.Playrecord;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class SubscribeServiceTest {
    static SubscribeService subscribeService;
    CustomerWechatMapper customerWechatMapper = mock(CustomerWechatMapper.class);
    PlayrecordMapper playrecordMapper = mock(PlayrecordMapper.class);
    DeviceMapper deviceMapper = mock(DeviceMapper.class);
    CustomerWechat customerWechat = mock(CustomerWechat.class);
    CustomerMapper customerMapper = mock(CustomerMapper.class);
    private String testOpenId = "testOpenId";
    private String deviceMacAddr = "testMacAddr";

    @Before
    public void setUp() throws Exception {
        subscribeService = new SubscribeService(customerWechatMapper, customerMapper);
    }

    @Test
    public void userSubscribedBeforeAndReturnTrue() {
        when(customerWechatMapper.selectByOpenId(testOpenId)).thenReturn(customerWechat);
        when(customerWechat.getSubscribetime()).thenReturn(new Date());
        verify(customerWechatMapper, never()).updateByPrimaryKey(customerWechat);
        assertTrue(subscribeService.subscribe(testOpenId));
    }

    @Test
    public void userNeverSubscribeBeforeAndCreateNewRecordForTheFirstSubscribe() throws Exception {
        when(customerWechatMapper.selectByOpenId(testOpenId)).thenReturn(customerWechat);
        when(customerWechat.getSubscribetime()).thenReturn(null);
        when(customerWechatMapper.updateByPrimaryKey(customerWechat)).thenReturn(1);
        boolean result = subscribeService.subscribe(testOpenId);
        verify(customerWechatMapper, times(1)).updateByPrimaryKey(customerWechat);
        assertThat(result, is(true));
    }

    @Test
    public void addBonusConsumeRecordSuccessfully() {
        Device device = mock(Device.class);
        when(deviceMapper.selectByMacAddr(anyString())).thenReturn(device);
        when(customerWechatMapper.selectByOpenId(testOpenId)).thenReturn(customerWechat);
        when(playrecordMapper.insert(any(Playrecord.class))).thenReturn(1);

        boolean result = subscribeService.consumeBonus(testOpenId, deviceMacAddr, 1);

        verify(customerWechatMapper, times(1)).selectByOpenId(anyString());
        verify(customerWechatMapper, times(1)).updateByPrimaryKey(customerWechat);
        assertThat(result, is(true));
    }
}
