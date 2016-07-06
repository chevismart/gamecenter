package gamecenter.core.dao;

import gamecenter.core.domain.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceMapper {
    int deleteByPrimaryKey(Integer deviceid);

    int insert(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Integer deviceid);

    Device selectByMacAddr(String macAddr);

    Device selectByDeviceName(String deviceName);

    List<Device> selectByCenterId(int centerid);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKeyWithBLOBs(Device record);

    int updateByPrimaryKey(Device record);

    int selectWechatIdByMacAndPaymentType(@Param("paymentType") int paymentType, @Param("macAddr") String macAddr);
}