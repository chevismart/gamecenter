package gamecenter.core.processors.tasks;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import gamecenter.core.domain.Device;
import gamecenter.core.listeners.AbstractRunnable;
import gamecenter.core.services.CloudServerService;
import gamecenter.core.services.db.DBServices;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static gamecenter.core.utils.DateUtil.dateToString;
import static gamecenter.core.utils.TimeUtil.millionSecondFromSecond;
import static java.lang.Thread.sleep;

public class ConnectionManager extends AbstractRunnable implements ScheduleTask {

    private final DBServices dbServices;
    private Multimap<String, Device> onlineDevices = ArrayListMultimap.create();
    private CloudServerService cloudServerService;

    public ConnectionManager(DBServices dbServices, CloudServerService cloudServerService) {
        this.dbServices = dbServices;
        this.cloudServerService = cloudServerService;
    }

    public void run() {
        List<Device> devices = dbServices.getDeviceService().retrieveAllDeviceByCenterId(1);
        String result = "没有设备在线！";
        while (isContinue) {
            List<String> tempList = Lists.newArrayList();
            try {
                sleep(millionSecondFromSecond(interval()));
                tempList = cloudServerService.getOnlineClientMac("00000000");
            } catch (Exception ignore) {
                logger.debug("Http request error: ", ignore);
            }

            List<String> deviceNames = Lists.newArrayList();
            if (tempList != null && tempList.size() > 0) {
                Multimap<String, Device> tempCache = ArrayListMultimap.create();
                for (Device device : devices) {
                    String centerId = String.valueOf(device.getCenterid());
                    if (tempList.contains(device.getMacaddr())) {
                        deviceNames.add(device.getDevicename());
                        device.setConnectionstatus("ONLINE");
                        device.setPowerstatus("ON");
                        tempCache.put(centerId, device);
                        onlineDevices = tempCache;
                    } else {
                        device.setConnectionstatus("OFFLINE");
                        device.setPowerstatus("OFF");
                        List<Device> deviceList = Lists.newArrayList();
                        for(Device onlineDevice :onlineDevices.get(centerId)){
                            if(onlineDevice.getMacaddr().equalsIgnoreCase(device.getMacaddr()))
                                deviceList.add(device);
                        }
                        onlineDevices.removeAll(deviceList);
                    }
                    device.setRemark(dateToString(new Date()));
                    dbServices.getDeviceService().updateDeviceStatus(device); // TODO: should be update device by batch here.
                }
                result = "在线设备：" + StringUtils.join(deviceNames.toArray(new String[deviceNames.size()]), ",");
            }else{
                onlineDevices.clear();
            }

        }
        logger.info(result);
    }

    public int interval() {
        return 60;
    }

    public Collection<Device> getOnlineDevicesByAppId(String appId) {
        return onlineDevices.get(appId);
    }
}
