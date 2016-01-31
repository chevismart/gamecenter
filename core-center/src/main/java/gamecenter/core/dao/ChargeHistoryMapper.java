package gamecenter.core.dao;

import gamecenter.core.domain.ChargeHistory;

public interface ChargeHistoryMapper {

    ChargeHistory selectByPrimaryKey(Integer chargeHistoryId);

    int deleteByPrimaryKey(Integer chargeHistoryId);

    int insert(ChargeHistory chargeHistory);

    int insertSelective(ChargeHistory chargeHistory);

    int updateByPrimaryKeySelective(ChargeHistory chargeHistory);

    int updateByPrimaryKey(ChargeHistory chargeHistory);
}
