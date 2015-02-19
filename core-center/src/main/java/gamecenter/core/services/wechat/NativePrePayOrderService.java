package gamecenter.core.services.wechat;

import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.CoreCenterHost;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.services.Service;
import gamecenter.core.utils.ParameterUtil;
import gamecenter.core.utils.TimeUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.util.MapUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.XMLConverUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chevis on 15/2/19.
 */
public class NativePrePayOrderService extends Service {

    public static boolean isParamEnough(PayNotification payNotification) {

        return !ParameterUtil.hasEmptyParam(
                payNotification.getAppid(),
                payNotification.getOpenid(),
                payNotification.getMch_id()
        );

    }

    public static String generateProductId(String appId, String coins, String money, String deviceId) {
        Map productMap = new HashMap();
        productMap.put(ParameterUtil.NativePrePayOrder.APPID, appId);
        productMap.put(ParameterUtil.NativePrePayOrder.COINS, coins);
        productMap.put(ParameterUtil.NativePrePayOrder.MONEY, money);
        productMap.put(ParameterUtil.NativePrePayOrder.DEVICE, deviceId);

        return ParameterUtil.zipParam(productMap);
    }

    public String requestPrePayOrder(PayNotification payNotification, AppProfile appProfile, int coins) {

        String result = StringUtils.EMPTY;

        if (isParamEnough(payNotification)) {

            String payKey = appProfile.getWechatProfile().getPayKey();

            Unifiedorder unifiedorder = new Unifiedorder();
            unifiedorder.setAppid(payNotification.getAppid());
            unifiedorder.setMch_id(payNotification.getMch_id());
            unifiedorder.setNonce_str(RandomStringUtils.random(32, true, true));
            unifiedorder.setBody(appProfile.getAppName() + "娃娃机代币【" + coins + "】个");

            Map attachMap = new HashMap();
            attachMap.put(ParameterUtil.NativePrePayOrder.COINS, coins);
            attachMap.put(ParameterUtil.NativePrePayOrder.APPID, appProfile.getAppId());
            String attach = ParameterUtil.zipParam(attachMap);


            unifiedorder.setAttach(attach);
            unifiedorder.setOut_trade_no("trade_" + TimeUtil.getCurrentDateTime().getTime());
            unifiedorder.setTotal_fee(payNotification.getTotal_fee());
            unifiedorder.setNotify_url(CoreCenterHost.getHttpURL(CoreCenterHost.WECHAT_PAYMENT_NOTIFICATION_CALLBACK_URL));
            unifiedorder.setOpenid(payNotification.getOpenid());
            unifiedorder.setTrade_type("NATIVE");
            unifiedorder.setDevice_info(payNotification.getDevice_info());
            unifiedorder.setSign(SignatureUtil.generateSign(MapUtil.order(MapUtil.objectToMap(unifiedorder)), payKey));

            UnifiedorderResult unifiedorderResult = PayMchAPI.payUnifiedorder(unifiedorder);

            PayNotification returnInfo = new PayNotification();
            returnInfo.setReturn_code(CommonConstants.SUCCESS);
            returnInfo.setAppid(unifiedorderResult.getAppid());
            returnInfo.setMch_id(unifiedorderResult.getMch_id());
            returnInfo.setNonce_str(unifiedorderResult.getNonce_str());
            returnInfo.setPrepay_id(unifiedorderResult.getPrepay_id());
            returnInfo.setResult_code(CommonConstants.SUCCESS);
            Map map = MapUtil.order(MapUtil.objectToMap(returnInfo));

            returnInfo.setSign(SignatureUtil.generateSign(map, payKey));

            result = XMLConverUtil.convertToXML(returnInfo);

        } else {
            logger.warn("The parameters of pay notification is insufficient for the request.");
        }

        return result;
    }

}
