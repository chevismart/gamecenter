package gamecenter.core.services.db;

import gamecenter.core.dao.DeviceMapper;
import gamecenter.core.domain.Device;

import java.util.List;

public class DeviceService extends DBService{
    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    public void updateDeviceStatus(Device device){
        deviceMapper.updateByPrimaryKey(device);
    }

    public List<Device> retrieveAllDeviceByCenterId(int centerId){return deviceMapper.selectByCenterId(centerId);}

    public String macAddressByDeviceName(String deviceName){
        return deviceMapper.selectByDeviceName(deviceName).getMacaddr();
    }

}
