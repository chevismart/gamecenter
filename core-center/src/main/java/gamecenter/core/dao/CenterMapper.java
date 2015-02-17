package gamecenter.core.dao;

import gamecenter.core.domain.Center;

public interface CenterMapper {
    int deleteByPrimaryKey(Integer centerid);

    int insert(Center record);

    int insertSelective(Center record);

    Center selectByPrimaryKey(Integer centerid);

    int updateByPrimaryKeySelective(Center record);

    int updateByPrimaryKey(Center record);
}