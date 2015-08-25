package gamecenter.core.services.db;

import gamecenter.core.dao.CustomerMapper;
import gamecenter.core.dao.CustomerWechatMapper;
import gamecenter.core.dao.UserMapper;
import gamecenter.core.dao.WechatMapper;
import gamecenter.core.domain.Customer;
import gamecenter.core.domain.CustomerWechat;
import gamecenter.core.domain.User;
import gamecenter.core.domain.Wechat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class UserService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    //dao
    private final CustomerWechatMapper customerWechatMapper;
    private final UserMapper userMapper;
    private final CustomerMapper customerMapper;
    private final WechatMapper wechatMapper;

    public UserService(CustomerWechatMapper customerWechatMapper, UserMapper userMapper, CustomerMapper customerMapper, WechatMapper wechatMapper) {
        this.customerWechatMapper = customerWechatMapper;
        this.userMapper = userMapper;
        this.customerMapper = customerMapper;
        this.wechatMapper = wechatMapper;
    }

    public boolean hasWechatCustomer(String openId) {
        CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
        if (customerWechat == null)
            return false;
        else
            return true;
    }

    public boolean addWechatCustomer(String name, String openId, String appId) {
        //添加user
        User user = new User();
        user.setActive(true);
        user.setName(name);
        userMapper.insert(user);
        //添加customer
        Customer customer = new Customer();
        customer.setActive(true);
        customer.setName(name);
        customer.setUserid(user.getUserid());
        customer.setWallet((float) 0);
        customer.setRegisterdatetime(new Date(System.currentTimeMillis()));
        customerMapper.insert(customer);
        //添加customer_wehcat
        CustomerWechat customerWechat = new CustomerWechat();
        customerWechat.setCustomerid(customer.getCustomerid());
        customerWechat.setOpenid(openId);
        Wechat wechat = wechatMapper.selectByWechatAppId(appId);
        customerWechat.setWechatid(wechat.getWechatid());
        customerWechatMapper.insert(customerWechat);
        return true;
    }

    //清除微信客户（测试）
    public boolean removeWechatCustomer(String openId) {
        CustomerWechat customerWechat = customerWechatMapper.selectByOpenId(openId);
        if (customerWechat == null) {
            logger.error("微信用户不存在对应的customerWechat");
            return false;
        }
        Customer customer = customerMapper.selectByPrimaryKey(customerWechat.getCustomerid());
        if (customer == null) {
            logger.error("微信用户不存在对应的customer");
            return false;
        }
        User user = userMapper.selectByPrimaryKey(customer.getUserid());
        if (user == null) {
            logger.error("微信用户不存在对应的user");
            return false;
        }
        customerWechatMapper.deleteByOpenId(customerWechat.getOpenid());
        customerMapper.deleteByPrimaryKey(customer.getCustomerid());
        userMapper.deleteByPrimaryKey(user.getUserid());
        return true;
    }

}
