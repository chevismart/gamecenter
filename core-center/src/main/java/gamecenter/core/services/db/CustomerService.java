package gamecenter.core.services.db;

import gamecenter.core.dao.ChargeHistoryMapper;
import gamecenter.core.dao.CustomerMapper;
import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.domain.ChargeHistory;
import gamecenter.core.domain.Customer;
import gamecenter.core.domain.CustomerWechat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class CustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomerWechatMapper customerWechatMapper;
    private final CustomerMapper customerMapper;
    private final ChargeHistoryMapper chargeHistoryMapper;

    public CustomerService(CustomerWechatMapper customerWechatMapper, CustomerMapper customerMapper, ChargeHistoryMapper chargeHistoryMapper) {
        this.customerWechatMapper = customerWechatMapper;
        this.customerMapper = customerMapper;
        this.chargeHistoryMapper = chargeHistoryMapper;
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
        CustomerWechat customerWechat = getCustomerWechat(openId);
        if (customer != null) {
            customer.setWallet(customer.getWallet() + income);
            updateCustomer(customer, customerWechat);
            ChargeHistory chargeHistory = new ChargeHistory();
            chargeHistory.setWechatId(customerWechat.getWechatid());
            chargeHistory.setCustomerId(customer.getCustomerid());
            chargeHistory.setTimestamp(new Date());
            chargeHistory.setCoin(income);
            chargeHistory.setPaid(0d);
            chargeHistory.setCenterId(1);// TODO: fill the value base on the user's actual
            chargeHistoryMapper.insert(chargeHistory);
            return true;
        } else {
            logger.warn("Customer info not found for open id = {}", openId);
        }
        return false;
    }

    private CustomerWechat getCustomerWechat(String openId) {
        return customerWechatMapper.selectByOpenId(openId);
    }

    public boolean payBill(String openId, int cost) {
        Customer customer = getCustomer(openId);
        CustomerWechat customerWechat = getCustomerWechat(openId);
        if (customer != null) {
            if (customer.getWallet() >= cost) {
                customer.setWallet(customer.getWallet() - cost);
                updateCustomer(customer, customerWechat);
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
        CustomerWechat customerWechat = getCustomerWechat(openId);
        if (customerWechat != null) {
            Customer customer = customerMapper.selectByPrimaryKey(customerWechat.getCustomerid());
            if (customer != null) {
                return customer;
            }
        }
        return null;
    }

    public synchronized void updateCustomer(Customer customer, CustomerWechat customerWechat) {
        customerMapper.updateByPrimaryKey(customer);
    }

    public List<Customer> getCustomerByRegistrationDateRange(Date startDate, Date endDate) {
        return customerMapper.selectCustomerByRegistrationDate(startDate, endDate);
    }

    public int countCustomerByWechatId(int wechatId){
        return customerWechatMapper.selectByWechatId(wechatId).size();
    }

}
