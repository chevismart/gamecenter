package gamecenter.core.dao;

import gamecenter.core.domain.Playrecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PlayrecordMapper {
    int deleteByPrimaryKey(Integer playrecordid);

    int insert(Playrecord record);

    int insertSelective(Playrecord record);

    Playrecord selectByPrimaryKey(Integer playrecordid);

    int updateByPrimaryKeySelective(Playrecord record);

    int updateByPrimaryKey(Playrecord record);

    List<Playrecord> selectPlayRecordByDate(@Param("startDate") Date startDate, @Param("endDate")Date endDate);
}