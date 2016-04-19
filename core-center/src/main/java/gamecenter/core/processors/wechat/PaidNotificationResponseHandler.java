package gamecenter.core.processors.wechat;

import gamecenter.core.beans.GlobalPaymentBean;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.HttpResponseHandler;
import gamecenter.core.services.CloudServerService;
import gamecenter.core.services.db.DBServices;
import gamecenter.core.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import weixin.popular.util.XMLConverUtil;

import java.util.Map;

import static gamecenter.core.utils.ParameterUtil.NativePrePayOrder.*;

public class PaidNotificationResponseHandler extends WechatPayNotificationProcessor implements HttpResponseHandler {
    private final GlobalPaymentBean globalPaymentBean;
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

        logger.info("PayNotification object is {}obtained.", payNotification != null ? "" : "not ");

        // Get the appId from the attach field of payNotification
        Map attachMap = ParameterUtil.extractParam(payNotification.getAttach());
        String appId = extractAppId(attachMap);
        int coins = Integer.valueOf(extractCoins(attachMap));
        String deviceName = extractDeviceId(attachMap);
        profileManager.getAppProfile(appId).getWechatProfile();
        boolean isNewPayment = false;

        // Verify the response is successful or not
        if (payNotification.getReturn_code().equalsIgnoreCase(CommonConstants.SUCCESS)) {
            if (payNotification.getResult_code().equalsIgnoreCase(CommonConstants.SUCCESS)) {
                // To check if the payment notification is processed before
                isNewPayment = globalPaymentBean.isNewPayment(payNotification.getOut_trade_no());
                logger.debug("Global payment bean is {}",globalPaymentBean);
                logger.info("Payment id({}) is {}", payNotification.getOut_trade_no(), !isNewPayment ? "processed before" : "never processed");
            } else {
                logger.warn("Payment notification with error: ({}={}) \n Error Details: {}",
                        payNotification.getErr_code(),
                        payNotification.getErr_code_des(),
                        payNotification.toString());
            }
        } else {
            logger.warn("Received a failure payment notification with the error: {}", payNotification.getReturn_msg());
        }

        if (isNewPayment) {
            logger.info("Received success payment with amount {} for user({})", payNotification.getTotal_fee(), payNotification.getOpenid());
            paymentLogger.info(payNotification.toString());
            globalPaymentBean.startProcessingPayment(payNotification);

            dbServices.getCustomerService().chargeWallet(payNotification.getOpenid(), coins, Double.valueOf(payNotification.getCash_fee())/100, payNotification.getTransaction_id());

            logger.info("Requesting device to topup coins");

            String mac = dbServices.getDeviceService().macAddressByDeviceName(payNotification.getDevice_info());

            if(StringUtils.isEmpty(mac)){
                logger.error("Could not locate the device mac address by device Id: {}", deviceName);
                logger.warn("Could not continue topup coins to the cloud server!");
            }else {

                boolean topupResult = cloudServerService.topUpCoin(mac, coins, payNotification.getOpenid());

                if (topupResult) {
                    globalPaymentBean.completePayment(payNotification);
                }
                replyWechatForPayNotification(topupResult);
                logger.info("Response sent!");
            }

        } else {
            logger.warn("Invalid message for top up notification or top up has been processed.");
            replyWechatForPayNotification(isNewPayment);
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
