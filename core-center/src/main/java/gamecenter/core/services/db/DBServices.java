package gamecenter.core.services.db;

/**
 * Created by chevi on 2016/1/11.
 */
public class DBServices extends DBService {
    private final AppService appService;
    private final CustomerService customerService;
    private final DeviceService deviceService;
    private final SubscribeService subscribeService;
    private final UserService userService;

    public DBServices(AppService appService, CustomerService customerService, DeviceService deviceService, SubscribeService subscribeService, UserService userService) {
        this.appService = appService;
        this.customerService = customerService;
        this.deviceService = deviceService;
        this.subscribeService = subscribeService;
        this.userService = userService;
    }

    public AppService getAppService() {
        return appService;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public DeviceService getDeviceService() {
        return deviceService;
    }

    public SubscribeService getSubscribeService() {
        return subscribeService;
    }

    public UserService getUserService() {
        return userService;
    }
}
