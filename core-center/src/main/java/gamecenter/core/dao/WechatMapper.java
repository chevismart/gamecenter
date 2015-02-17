package gamecenter.core.dao;

import gamecenter.core.domain.Wechat;

public interface WechatMapper {
    int deleteByPrimaryKey(Integer wechatid);

    int insert(Wechat record);

    int insertSelective(Wechat record);

    Wechat selectByPrimaryKey(Integer wechatid);

    int updateByPrimaryKeySelective(Wechat record);

    int updateByPrimaryKey(Wechat record);
}