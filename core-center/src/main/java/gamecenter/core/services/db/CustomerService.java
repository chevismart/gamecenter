package gamecenter.core.services.db;

import gamecenter.core.dao.CustomerMapper;
import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.domain.Customer;
import gamecenter.core.domain.CustomerWechat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomerWechatMapper customerWechatMapper;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerWechatMapper customerWechatMapper, CustomerMapper customerMapper) {
        this.customerWechatMapper = customerWechatMapper;
        this.customerMapper = customerMapper;
    }

    public int getCustomerWalletBalanceByOpenId(String openId) {
        Customer customer = getCustomer(openId);
        if (customer != null) {
            return customer.getWallet().intValue();
        }
        return 0;
    }

    private void updateWallet(String openId, float coins) {
        Customer customer = getCustomer(openId);
        if (customer != null) {
            customer.setWallet(coins);
            customerMapper.updateByPrimaryKey(customer);
        }
    }

    public boolean chargeWallet(String openId, int income) {
        Customer customer = getCustomer(openId);
        if (customer != null) {
            customer.setWallet(customer.getWallet() + income);
            updateCustomer(customer);
            return true;
        } else {
            logger.warn("Customer info not found for open id = {}", openId);
        }
        return false;
    }

    public boolean payBill(String openId, int cost) {
        Customer customer = getCustomer(openId);
        if (customer != null) {
            if (customer.getWallet() >= cost) {
                customer.setWallet(customer.getWallet() - cost);
                updateCustomer(customer);
                return true;
            } else {
                logger.warn("User can not pay for his bill: {} coins, balance is {}", cost, customer.getWallet());
            }
        } else {
            logger.warn("Customer info not found for open id = {}", openId);
        }
        return false;
    }

    private Customer getCustomer(String openId) {
        CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
        if (customerWechat != null) {
            Customer customer = customerMapper.selectByPrimaryKey(customerWechat.getCustomerid());
            if (customer != null) {
                return customer;
            }
        }
        return null;
    }

    public synchronized void updateCustomer(Customer customer) {
        customerMapper.updateByPrimaryKey(customer);
    }
}
