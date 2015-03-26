package gamecenter.core.processors.wechat;

import gamecenter.core.beans.GlobalPaymentBean;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.services.CoinTopUpService;
import gamecenter.core.utils.ParameterUtil;
import weixin.popular.util.XMLConverUtil;

import java.util.Map;

/**
 * Created by Chevis on 15/2/20.
 */
public class PaidNotificationProcessor extends WechatPayNotificationProcessor {


    GlobalPaymentBean globalPaymentBean;

    CoinTopUpService coinTopUpService = new CoinTopUpService();

    @Override
    public String execute() throws Exception {

        PayNotification payNotification = getPayNotification();

        logger.info("PayNotification object is {} obtained.", payNotification == null ? "" : "not");

        // Get the appId from the attach field of payNotification
        Map attachMap = ParameterUtil.extractParam(payNotification.getAttach());
        String appId = ParameterUtil.NativePrePayOrder.extractAppId(attachMap);
        int coins = Integer.valueOf(ParameterUtil.NativePrePayOrder.extractCoins(attachMap));
        wechatProfile = profileManager.getAppProfile(appId).getWechatProfile();
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
            boolean topupResult = false;
            try {
                topupResult = coinTopUpService.topupCoins(coins);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            logger.info("Top up result is: {}", topupResult);
            if (topupResult) {
                globalPaymentBean.getUnSettlementPayments().remove(payNotification.getOut_trade_no());
                globalPaymentBean.getSettledPayments().put(payNotification.getOut_trade_no(), payNotification);
                logger.info(globalPaymentBean.getSettledPayments().toString());
                logger.info(globalPaymentBean.getUnSettlementPayments().toString());
            }
            replyWechatForPayNotification(topupResult);
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
    }


    protected boolean isPaymentProcessed(PayNotification payNotification) {
        String payNotificationId = payNotification.getOut_trade_no();
        boolean isProcessed = !globalPaymentBean.getUnSettlementPayments().keySet().contains(payNotificationId) &&
                globalPaymentBean.getSettledPayments().keySet().contains(payNotificationId);
        logger.info("Payment id() is {}", payNotificationId, isProcessed ? "processed" : "not processed");
        return isProcessed;
    }

    public GlobalPaymentBean getGlobalPaymentBean() {
        return globalPaymentBean;
    }

    public void setGlobalPaymentBean(GlobalPaymentBean globalPaymentBean) {
        this.globalPaymentBean = globalPaymentBean;
    }

}
