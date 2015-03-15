package gamecenter.core.dao;

import gamecenter.core.domain.Device;

public interface DeviceMapper {
    int deleteByPrimaryKey(Integer deviceid);

    int insert(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Integer deviceid);

    Device selectByMacAddr(String macAddr);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKeyWithBLOBs(Device record);

    int updateByPrimaryKey(Device record);
}