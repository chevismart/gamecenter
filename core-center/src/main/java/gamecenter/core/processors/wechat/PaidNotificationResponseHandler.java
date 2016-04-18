package gamecenter.core.processors.wechat;

import gamecenter.core.beans.CoreCenterHost;
import gamecenter.core.beans.GlobalPaymentBean;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.HttpResponseHandler;
import gamecenter.core.services.CloudServerService;
import gamecenter.core.services.CoinTopUpService;
import gamecenter.core.services.db.DBServices;
import gamecenter.core.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import weixin.popular.util.XMLConverUtil;

import java.util.Map;

import static gamecenter.core.utils.ParameterUtil.NativePrePayOrder.extractAppId;
import static gamecenter.core.utils.ParameterUtil.NativePrePayOrder.extractCoins;
import static gamecenter.core.utils.ParameterUtil.NativePrePayOrder.extractDeviceId;

public class PaidNotificationResponseHandler extends WechatPayNotificationProcessor implements HttpResponseHandler {
    private final GlobalPaymentBean globalPaymentBean;
    private final CoinTopUpService coinTopUpService = new CoinTopUpService(CoreCenterHost.CORECENTER_HOST, this);
    private final DBServices dbServices;
    private final CloudServerService cloudServerService;

    public PaidNotificationResponseHandler(ProfileManager profileManager, GlobalPaymentBean globalPaymentBean, DBServices dbServices, CloudServerService cloudServerService) {
        super(profileManager);
        this.globalPaymentBean = globalPaymentBean;
        this.dbServices = dbServices;
        this.cloudServerService = cloudServerService;
    }

    @Override
    public String execute() throws Exception {

        PayNotification payNotification = getPayNotification();

        logger.info("PayNotification object is {} obtained.", payNotification == null ? "" : "not");

        // Get the appId from the attach field of payNotification
        Map attachMap = ParameterUtil.extractParam(payNotification.getAttach());
        String appId = extractAppId(attachMap);
        int coins = Integer.valueOf(extractCoins(attachMap));
        String deviceName = extractDeviceId(attachMap);
        profileManager.getAppProfile(appId).getWechatProfile();
        boolean isValidMsg = false;

        // Verify the response is successful or not
        if (payNotification.getReturn_code().toUpperCase().equals(CommonConstants.SUCCESS.toUpperCase())) {
            if (payNotification.getResult_code().toUpperCase().equals(CommonConstants.SUCCESS.toUpperCase())) {
                // To topup coins here
                isValidMsg = !isPaymentProcessed(payNotification);
            } else {
                logger.warn("Payment notification with error: ({}={}) \n Error Details: {}",
                        payNotification.getErr_code(),
                        payNotification.getErr_code_des(),
                        payNotification.toString());
            }

        } else {
            logger.warn("Received a failure payment notification with the error: {}", payNotification.getReturn_msg());
        }

        if (isValidMsg) {
            logger.info("Received success payment with amount {} for user({})", payNotification.getTotal_fee(), payNotification.getOpenid());
            paymentLogger.info(payNotification.toString());


            dbServices.getCustomerService().chargeWallet(payNotification.getOpenid(), coins, Double.valueOf(payNotification.getCash_fee()), payNotification.getTransaction_id());

            logger.info("Requesting device to topup coins");

            String mac = dbServices.getDeviceService().macAddressByDeviceName(deviceName);

            if(StringUtils.isEmpty(mac)){
                logger.error("Could not locate the device mac address by device Id: {}", deviceName);
                logger.warn("Could not continue topup coins to the cloud server!");
            }else {

                boolean topupResult = cloudServerService.topUpCoin(mac, coins, payNotification.getOpenid());

                if (topupResult) {
                    globalPaymentBean.getUnSettlementPayments().remove(payNotification.getOut_trade_no());
                    globalPaymentBean.getSettledPayments().put(payNotification.getOut_trade_no(), payNotification);
                    logger.info("Settled Payment: {}", globalPaymentBean.getSettledPayments().toString());
                    logger.info("Unsettled Payment: {}", globalPaymentBean.getUnSettlementPayments().toString());
                }
                replyWechatForPayNotification(topupResult);
                logger.info("Response sent!");
            }

        } else {
            logger.warn("Invalid message for top up notification or top up has been processed.");
            replyWechatForPayNotification(isValidMsg);
        }


        return super.execute();
    }

    private void replyWechatForPayNotification(boolean isSuccess) {
        PayNotification payNotification = new PayNotification();
        payNotification.setReturn_code(isSuccess ? CommonConstants.SUCCESS : CommonConstants.FAIL);
        String responseXML = XMLConverUtil.convertToXML(payNotification);
        returnWechatResponse(responseXML);
        logger.debug("Response xml is returned: {}", responseXML);
    }


    protected boolean isPaymentProcessed(PayNotification payNotification) {
        String payNotificationId = payNotification.getOut_trade_no();
        boolean isProcessed = !globalPaymentBean.getUnSettlementPayments().keySet().contains(payNotificationId) &&
                globalPaymentBean.getSettledPayments().keySet().contains(payNotificationId);
        logger.info("Payment id({}) is {}", payNotificationId, isProcessed ? "processed before" : "never processed");
        return isProcessed;
    }

    public GlobalPaymentBean getGlobalPaymentBean() {
        return globalPaymentBean;
    }

    @Override
    public void completed(HttpResponse httpResponse) {
        logger.info("Receive response in complete");
    }

    @Override
    public void failed(Exception e) {
        logger.error("Topup failed due to: {}", e.getMessage());
//        replyWechatForPayNotification(false);
    }

    @Override
    public void cancelled() {
        logger.warn("Request is cancelled!");
    }
}
