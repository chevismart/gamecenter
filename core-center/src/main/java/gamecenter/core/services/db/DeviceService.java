package gamecenter.core.services.db;

import com.google.common.base.Optional;
import gamecenter.core.dao.DeviceMapper;
import gamecenter.core.domain.Device;

import java.util.List;

public class DeviceService extends DBService {
    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    public void updateDeviceStatus(Device device) {
        deviceMapper.updateByPrimaryKey(device);
    }

    public List<Device> retrieveAllDeviceByCenterId(int centerId) {
        return deviceMapper.selectByCenterId(centerId);
    }

    public String macAddressByDeviceName(String deviceName) {
        return deviceMapper.selectByDeviceName(deviceName).getMacaddr();
    }

    public Optional<Integer> wechatIdByMacAndPaymentType(int paymentType, String mac) {
        return Optional.fromNullable(deviceMapper.selectWechatIdByMacAndPaymentType(paymentType, mac));
    }

    public Optional<Device> deviceByMac(String mac) {
        return Optional.fromNullable(deviceMapper.selectByMacAddr(mac));
    }

}
