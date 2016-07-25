package gamecenter.core.services.db;

public class DBServices extends DBService {
    private static DBServices dbServices;
    private final AppService appService;
    private final CustomerService customerService;
    private final DeviceService deviceService;
    private final SubscribeService subscribeService;
    private final UserService userService;
    private final ChargeHistoryService chargeHistoryService;
    private final PaymentHistoryService paymentHistoryService;

    public DBServices(AppService appService, CustomerService customerService, DeviceService deviceService, SubscribeService subscribeService, UserService userService, ChargeHistoryService chargeHistoryService, PaymentHistoryService paymentHistoryService) {
        this.appService = appService;
        this.customerService = customerService;
        this.deviceService = deviceService;
        this.subscribeService = subscribeService;
        this.userService = userService;
        this.chargeHistoryService = chargeHistoryService;
        this.paymentHistoryService = paymentHistoryService;
        this.dbServices = this;
    }

    public static DBServices instance() {
        while (true) {
            if (dbServices != null) {
                return dbServices;
            }
        }
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

    public ChargeHistoryService getChargeHistoryService() {
        return chargeHistoryService;
    }

    public PaymentHistoryService getPaymentHistoryService() {
        return paymentHistoryService;
    }
}
