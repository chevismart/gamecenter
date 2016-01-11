package gamecenter.core.dao;

import gamecenter.core.domain.Device;

import java.util.List;

public interface DeviceMapper {
    int deleteByPrimaryKey(Integer deviceid);

    int insert(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Integer deviceid);

    Device selectByMacAddr(String macAddr);

    List<Device> selectByCenterId(int centerid);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKeyWithBLOBs(Device record);

    int updateByPrimaryKey(Device record);
}