package gamecenter.core.dao;

import gamecenter.core.domain.CustomerWechat;
import gamecenter.core.domain.CustomerWechatKey;

public interface CustomerWechatMapper {
    int deleteByPrimaryKey(CustomerWechatKey key);

    int insert(CustomerWechat record);

    int insertSelective(CustomerWechat record);

    CustomerWechat selectByPrimaryKey(CustomerWechatKey key);
    //根据openId选择
    CustomerWechat selectByOpenId(String openId);

    int updateByPrimaryKeySelective(CustomerWechat record);

    int updateByPrimaryKey(CustomerWechat record);
}