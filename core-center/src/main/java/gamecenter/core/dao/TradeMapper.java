package gamecenter.core.dao;

import gamecenter.core.domain.Trade;

public interface TradeMapper {
    int deleteByPrimaryKey(Integer tradeid);

    int insert(Trade record);

    int insertSelective(Trade record);

    Trade selectByPrimaryKey(Integer tradeid);

    int updateByPrimaryKeySelective(Trade record);

    int updateByPrimaryKey(Trade record);

    Trade selectByOpenId(String openId);
}