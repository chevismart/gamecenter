package gamecenter.core.services.db;

import gamecenter.core.dao.*;
import gamecenter.core.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class UserService {
    //dao
    private final CustomerWechatMapper customerWechatMapper;
    private final UserMapper userMapper;
    private final CustomerMapper customerMapper;
    private final WechatMapper wechatMapper;
    private final CenterMapper centerMapper;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserService(CustomerWechatMapper customerWechatMapper, UserMapper userMapper, CustomerMapper customerMapper, WechatMapper wechatMapper, CenterMapper centerMapper) {
        this.customerWechatMapper = customerWechatMapper;
        this.userMapper = userMapper;
        this.customerMapper = customerMapper;
        this.wechatMapper = wechatMapper;
        this.centerMapper = centerMapper;
    }

    public List<CustomerWechat> getCenterOperators(String cneterId) {

        Center center = centerMapper.selectByCenterId(cneterId);
        if (center != null) {
            // TODO
        }
        return newArrayList();
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
        customerWechat.setSubscribebonus(true);
        customerWechat.setSubscribetime(new Date());
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

    public User getOperatorByOpenId(String openid) {
        return userMapper.getOperatorByOpenId(openid);
    }

}
