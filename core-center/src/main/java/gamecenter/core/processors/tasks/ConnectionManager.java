package gamecenter.core.processors.tasks;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import gamecenter.core.domain.Device;
import gamecenter.core.listeners.AbstractRunnable;
import gamecenter.core.services.HttpService;
import gamecenter.core.services.db.DBServices;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

import static gamecenter.core.utils.DateUtil.dateToString;
import static gamecenter.core.utils.TimeUtil.millionSecondFromSecond;
import static java.lang.Thread.sleep;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ConnectionManager extends AbstractRunnable implements ScheduleTask {

    private final DBServices dbServices;
    private Map<String, List<Device>> onlineDevices = new HashMap<String, List<Device>>();
    private Map<String, List<Device>> offlineDevices = new HashMap<String, List<Device>>();

    public ConnectionManager(DBServices dbServices) {
        this.dbServices = dbServices;
    }

    public void run() {

        List<Device> devices = dbServices.getDeviceService().retrieveAllDeviceByCenterId(1);
        while (isContinue) {
            try {
                sleep(millionSecondFromSecond(interval()));
                List<Device> onlineDeviceList = Lists.newArrayList();
                List<Device> offLineDeviceList = Lists.newArrayList();
                for (Device device : devices) {
                    HttpResponse response;
                    String coinQty = EMPTY;
                    String queryTime = EMPTY;
                    try {
                        response = HttpService.get("http://localhost:8003",
                                new BasicNameValuePair("CENTER_ID", "00000000"),
                                new BasicNameValuePair("TOKEN", "tokenStr"),
                                new BasicNameValuePair("DATA_TYPE", "JSON"),
                                new BasicNameValuePair("REQ_TYPE", "COUNTER_QTY"),
                                new BasicNameValuePair("MAC", device.getMacaddr()),
                                new BasicNameValuePair("COIN_QTY", "true"),
                                new BasicNameValuePair("PRIZE_QTY", "true"));
                        Map<String, String> result = JSONObject.parseObject(IOUtils.toString(response.getEntity().getContent()), Map.class);
                        coinQty = result.get("COIN_QTY");
                        queryTime = result.get("COUNTER_QTY_TIMESTAMP");
                    } catch (Exception ignore) {
                    }

                    if (isNotEmpty(coinQty)) {
                        logger.info("Total coin quantity is {} at {}", coinQty, queryTime);
                        device.setConnectionstatus("ONLINE");
                        device.setRemark(dateToString(new Date(Long.valueOf(queryTime))));
                        device.setPowerstatus("ON");
                        onlineDeviceList.add(device);
                    } else {
                        device.setConnectionstatus("OFFLINE");
                        device.setPowerstatus("OFF");
                        device.setRemark(dateToString(new Date()));
                        offLineDeviceList.add(device);
                    }
                    dbServices.getDeviceService().updateDeviceStatus(device); // TODO: should be update device by batch here.
                }
                this.onlineDevices.put("1", onlineDeviceList);
                this.offlineDevices.put("1", offLineDeviceList);

                logger.debug("Total online devices: {}, {}", onlineDeviceList.size(),Arrays.toString(onlineDeviceList.toArray(new Device[onlineDeviceList.size()])));
                logger.debug("Total offline devices: {}, {}", offLineDeviceList.size(), Arrays.toString(onlineDeviceList.toArray(new Device[offLineDeviceList.size()])));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public int interval() {
        return 60;
    }
}
