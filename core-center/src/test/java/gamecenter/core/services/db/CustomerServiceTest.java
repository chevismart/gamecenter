package gamecenter.core.services.db;

import gamecenter.core.dao.ChargeHistoryMapper;
import gamecenter.core.dao.CustomerMapper;
import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.domain.Customer;
import gamecenter.core.domain.CustomerWechat;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    CustomerMapper customerMapper = mock(CustomerMapper.class);
    CustomerWechatMapper customerWechatMapper = mock(CustomerWechatMapper.class);
    ChargeHistoryMapper chargeHistoryMapper = mock(ChargeHistoryMapper.class);
    CustomerService customerService;

    @Before
    public void setUp() throws Exception {
        customerService = new CustomerService(customerWechatMapper, customerMapper, chargeHistoryMapper);
    }

    @Test
    public void customerWechatRecordNotFoundAndReturnZeroForUserWallet() throws Exception {
        when(customerWechatMapper.selectByOpenId(anyString())).thenReturn(null);
        int wallet = customerService.getCustomerWalletBalanceByOpenId("openId");
        verify(customerWechatMapper, times(1)).selectByOpenId(anyString());
        verify(customerMapper, never()).selectByPrimaryKey(anyInt());
        assertThat(wallet, is(0));
    }

    @Test
    public void customerNotFoundWithSpecificCustomerId() throws Exception {
        CustomerWechat customerWechat = mock(CustomerWechat.class);
        when(customerWechatMapper.selectByOpenId(anyString())).thenReturn(customerWechat);
        when(customerWechat.getCustomerid()).thenReturn(1234);
        when(customerMapper.selectByPrimaryKey(anyInt())).thenReturn(null);
        int wallet = customerService.getCustomerWalletBalanceByOpenId("openId");
        verify(customerWechatMapper, times(1)).selectByOpenId(anyString());
        verify(customerMapper, times(1)).selectByPrimaryKey(anyInt());
        assertThat(wallet, is(0));
    }

    @Test
    public void customerInfoFoundAndReturnTheWalletBalence() throws Exception {
        CustomerWechat customerWechat = mock(CustomerWechat.class);
        Customer customer =  mock(Customer.class);
        when(customerWechatMapper.selectByOpenId(anyString())).thenReturn(customerWechat);
        when(customerWechat.getCustomerid()).thenReturn(1234);
        when(customerMapper.selectByPrimaryKey(anyInt())).thenReturn(customer);
        when(customer.getWallet()).thenReturn(9999f);
        int wallet = customerService.getCustomerWalletBalanceByOpenId("openId");
        verify(customerWechatMapper, times(1)).selectByOpenId(anyString());
        verify(customerMapper, times(1)).selectByPrimaryKey(anyInt());
        assertThat(wallet, is(9999));
    }
}