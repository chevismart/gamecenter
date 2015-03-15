package gamecenter.core.dao;

import gamecenter.core.domain.Playrecord;

public interface PlayrecordMapper {
    int deleteByPrimaryKey(Integer playrecordid);

    int insert(Playrecord record);

    int insertSelective(Playrecord record);

    Playrecord selectByPrimaryKey(Integer playrecordid);

    int updateByPrimaryKeySelective(Playrecord record);

    int updateByPrimaryKey(Playrecord record);
}