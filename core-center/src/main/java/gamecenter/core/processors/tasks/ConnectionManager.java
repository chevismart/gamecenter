package gamecenter.core.processors.tasks;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import gamecenter.core.domain.Device;
import gamecenter.core.services.BroadcastService;
import gamecenter.core.services.CloudServerService;
import gamecenter.core.services.db.DBServices;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static gamecenter.core.utils.DateUtil.dateToString;
import static gamecenter.core.utils.TimeUtil.millionSecondFromSecond;

public class ConnectionManager implements ScheduleTask {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DBServices dbServices;
    private final CloudServerService cloudServerService;
    private final BroadcastService broadcastService;
    private Multimap<String, Device> onlineDevices = ArrayListMultimap.create();

    public ConnectionManager(DBServices dbServices, CloudServerService cloudServerService, BroadcastService broadcastService) {
        this.dbServices = dbServices;
        this.cloudServerService = cloudServerService;
        this.broadcastService = broadcastService;
    }

    public void run() {
        List<Device> devices = dbServices.getDeviceService().retrieveAllDeviceByCenterId(1);
        String result = "没有设备在线！";
        List<String> onlineDeviceList = Lists.newArrayList();
        List<String> deviceNames = Lists.newArrayList();
        try {
            onlineDeviceList = cloudServerService.getOnlineClientMac("00000000");
        } catch (Exception ignore) {
            logger.debug("Http request error: ", ignore);
        }

        Multimap<String, Device> tempCache = ArrayListMultimap.create();
        for (Device device : devices) {
            String preStatus = device.getConnectionstatus();
            String centerId = String.valueOf(device.getCenterid());
            if (onlineDeviceList.contains(device.getMacaddr())) {
                deviceNames.add(device.getDevicename());
                device.setConnectionstatus("ONLINE");
                device.setPowerstatus("ON");
                tempCache.put(centerId, device);
                onlineDevices = tempCache;
            } else {
                device.setConnectionstatus("OFFLINE");
                device.setPowerstatus("OFF");
                List<Device> deviceList = Lists.newArrayList();
                for (Device onlineDevice : onlineDevices.get(centerId)) {
                    if (onlineDevice.getMacaddr().equalsIgnoreCase(device.getMacaddr()))
                        deviceList.add(device);
                }
                onlineDevices.removeAll(deviceList);
            }
            if (!device.getConnectionstatus().equals(preStatus)) {
                broadcastService.notifyDeviceStatusForOwner(device);
            }
            device.setRemark(dateToString(new Date()));
            dbServices.getDeviceService().updateDeviceStatus(device); // TODO: should be update device by batch here.
        }
        if (deviceNames.size() > 0)
            result = "在线设备：" + StringUtils.join(deviceNames.toArray(new String[deviceNames.size()]), ",");
//                onlineDevices.clear();
        logger.info(result);

    }

    public long interval() {
        return millionSecondFromSecond(60);
    }

    public long initDelay() {
        return 0;
    }

    public Collection<Device> getOnlineDevicesByAppId(String appId) {
        return onlineDevices.get(appId);
    }
}
