package gamecenter.core.processors.tasks;

import gamecenter.core.domain.Device;
import gamecenter.core.listeners.AbstractRunnable;
import gamecenter.core.services.db.DBServices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gamecenter.core.utils.TimeUtil.millionSecondFromSecond;

public class ConnectionManager extends AbstractRunnable {

    private final DBServices dbServices;
    private Map<String, List<Device>> devices = new HashMap<String, List<Device>>();

    public ConnectionManager(DBServices dbServices) {
        this.dbServices = dbServices;
    }

    public void run() {



        while (isContinue) {
            try {
                //休眠时间
                Thread.sleep(millionSecondFromSecond(60));
                List<Device> devices = dbServices.getDeviceService().retrieveAllDeviceByCenterId(1);
                logger.debug("There are totally devices: {}, {}", devices.size(), devices.get(0).getDevicename());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
