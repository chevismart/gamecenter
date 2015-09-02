package gamecenter.core.services.db;

import gamecenter.core.dao.CustomerMapper;
import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.domain.Customer;
import gamecenter.core.domain.CustomerWechat;

public class CustomerService {

    private final CustomerWechatMapper customerWechatMapper;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerWechatMapper customerWechatMapper, CustomerMapper customerMapper) {
        this.customerWechatMapper = customerWechatMapper;
        this.customerMapper = customerMapper;
    }

    public int getCustomerWalletBalanceByOpenId(String openId) {
        CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
        if (customerWechat != null) {
            Customer customer = customerMapper.selectByPrimaryKey(customerWechat.getCustomerid());
            if (customer != null) {
                return customer.getWallet().intValue();
            }
        }
        return 0;
    }
}
