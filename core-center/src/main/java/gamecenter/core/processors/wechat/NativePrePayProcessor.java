package gamecenter.core.processors.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.Figure;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.services.wechat.NativePrePayOrderService;
import gamecenter.core.utils.ParameterUtil;

import java.util.Map;

public class NativePrePayProcessor extends WechatPayNotificationProcessor {

    NativePrePayOrderService nativePrePayOrderService = new NativePrePayOrderService();

    public NativePrePayProcessor(ProfileManager profileManager) {
        super(profileManager);
    }

    @Override
    public String execute() throws Exception {

        logger.info("{} start!", this.getClass().getSimpleName());

        PayNotification payNotification = getPayNotification();

        if (NativePrePayOrderService.isParamEnough(payNotification)) {

            // Convert the parameters in qr code to the fields, the following fields could not be empty.
            Map<String, String> product = ParameterUtil.extractParam(payNotification.getProduct_id());
            String appId = ParameterUtil.NativePrePayOrder.extractAppId(product);
            String coins = ParameterUtil.NativePrePayOrder.extractCoins(product);
            String deviceId = ParameterUtil.NativePrePayOrder.extractDeviceId(product);
            String money = ParameterUtil.NativePrePayOrder.extractMoney(product);

            logger.info("Extracted params are: appId = {}, coins = {}, deviceId = {}, money = {}",
                    appId, coins, deviceId, money);

            if (!ParameterUtil.hasEmptyParam(appId, coins, deviceId, money)) {
                // Supply the field into payNotification object for further request
                payNotification.setDevice_info(deviceId);
                payNotification.setAttach(appId);
                payNotification.setTotal_fee(String.valueOf(Figure.COIN_TO_MONEY.calculate(Integer.valueOf(money))));
                AppProfile appProfile = profileManager.findAppProfileByWechatId(payNotification.getAppid());
                // Request the native pre-pay order response content(xml)
                logger.info("Prepared pay notification object is: {}", payNotification);
                String content = nativePrePayOrderService.requestPrePayOrder(payNotification, appProfile, Integer.valueOf(coins));
                // return the pre-pay order result to the requester
                returnWechatResponse(content);

            } else {
                logger.warn("Insufficiency info for the native prepay order. appId = {}, coins = {}, deviceId = {}, money={}",
                        appId, coins, deviceId, money);
            }
        }

        logger.info("{} end!", this.getClass().getSimpleName());

        return null;
    }
}
