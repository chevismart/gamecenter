package gamecenter.core.dao;

import gamecenter.core.domain.ChargeHistory;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ChargeHistoryMapper {

    ChargeHistory selectByPrimaryKey(Integer chargeHistoryId);

    int deleteByPrimaryKey(Integer chargeHistoryId);

    int insert(ChargeHistory chargeHistory);

    int insertSelective(ChargeHistory chargeHistory);

    int updateByPrimaryKeySelective(ChargeHistory chargeHistory);

    int updateByPrimaryKey(ChargeHistory chargeHistory);

    List<ChargeHistory> selectHistoryRecordByDate(@Param("startDate") Date startDate, @Param("endDate")Date endDate);
}
