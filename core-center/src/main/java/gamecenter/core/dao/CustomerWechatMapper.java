package gamecenter.core.dao;

import gamecenter.core.domain.CustomerWechat;
import gamecenter.core.domain.CustomerWechatKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerWechatMapper {
    int deleteByPrimaryKey(CustomerWechatKey key);

    int deleteByOpenId(String openId);

    int insert(CustomerWechat record);

    int insertSelective(CustomerWechat record);

    CustomerWechat selectByPrimaryKey(CustomerWechatKey key);

    //根据openId选择
    CustomerWechat selectByOpenId(String openId);


    int updateByPrimaryKeySelective(CustomerWechat record);

    int updateByPrimaryKey(CustomerWechat record);

    List<CustomerWechat> selectByWechatId(@Param("wechatId") int wechatId);
}