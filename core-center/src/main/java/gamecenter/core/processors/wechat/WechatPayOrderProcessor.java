package gamecenter.core.processors.wechat;

import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.CoreCenterHost;
import gamecenter.core.beans.GlobalPaymentBean;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.processors.AbstractTopupProcessor;
import gamecenter.core.utils.ParameterUtil;
import gamecenter.core.utils.TimeUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.util.MapUtil;
import weixin.popular.util.SignatureUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.Map;

import static gamecenter.core.beans.CoreCenterHost.WECHAT_PAYMENT_NOTIFICATION_CALLBACK_URL;
import static gamecenter.core.beans.Figure.FIVE_PERCENTAGE_OFF;
import static gamecenter.core.beans.Figure.MONEY_TO_COIN;
import static gamecenter.core.utils.ParameterUtil.NativePrePayOrder.APPID;
import static gamecenter.core.utils.ParameterUtil.NativePrePayOrder.COINS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static weixin.popular.api.PayMchAPI.payUnifiedorder;
import static weixin.popular.util.PayUtil.generateMchPayJsRequestJson;

public class WechatPayOrderProcessor extends AbstractTopupProcessor {

    private final ProfileManager profileManager;
    private final GlobalPaymentBean globalPaymentBean;
    private final UserProfile userProfile;
    private String tempJsonStr;

    public WechatPayOrderProcessor(ProfileManager profileManager, GlobalPaymentBean globalPaymentBean, UserProfile userProfile) {
        this.profileManager = profileManager;
        this.globalPaymentBean = globalPaymentBean;
        this.userProfile = userProfile;
    }

    @Override
    public String execute() throws Exception {

        logger.info("Received payment order request");


        if (userProfile == null) logger.debug("User Profile is null");
        else if (userProfile.getAccessInfo() == null) logger.debug("Access info is null");
        else if (userProfile.getAccessInfo().getAppProfile() == null) logger.debug("App profile is null");
        else if(getChargeAmount()<=0) logger.error("Charge amount is not greater than 1");
        else if(StringUtils.isEmpty(getDeviceId())) logger.error("Could not locate the device!");
        else {
            String openId = userProfile.getOpenId();
            AppProfile appProfile = userProfile.getAccessInfo().getAppProfile();
            logger.debug("Open id is {}", openId);

            if (null != appProfile && appProfile.isWechatProfileValid()) {
                Unifiedorder unifiedorder = new Unifiedorder();
                WechatProfile wechatProfile = appProfile.getWechatProfile();
                unifiedorder.setAppid(wechatProfile.getWechatAppId());
                unifiedorder.setMch_id(wechatProfile.getMchid());
                unifiedorder.setNonce_str(RandomStringUtils.random(32, true, true));
                unifiedorder.setBody("娃娃机代币" + getChargeAmount()+"个");
                unifiedorder.setOut_trade_no("trade_" + TimeUtil.getCurrentDateTime().getTime());
                unifiedorder.setTotal_fee(String.valueOf(FIVE_PERCENTAGE_OFF.calculate(getChargeAmount())));
                logger.info("Charge {} coins", getChargeAmount());
                unifiedorder.setSpbill_create_ip(getRemoteAddress(getHttpRequest()));
                unifiedorder.setNotify_url(CoreCenterHost.getHttpURL(WECHAT_PAYMENT_NOTIFICATION_CALLBACK_URL));
                unifiedorder.setTrade_type("JSAPI");
//                unifiedorder.setTrade_type("NATIVE");
                unifiedorder.setOpenid(openId);

                logger.debug("Unifiedorder is built: {}", unifiedorder);

                Map<String, String> attachMap = Maps.newHashMap();
                attachMap.put(COINS, String.valueOf(MONEY_TO_COIN.calculate(getChargeAmount())));
                attachMap.put(APPID, appProfile.getAppId());
                String attach = ParameterUtil.zipParam(attachMap);

                unifiedorder.setAttach(attach);
                unifiedorder.setDevice_info(getDeviceId());
                unifiedorder.setSign(SignatureUtil.generateSign(MapUtil.order(MapUtil.objectToMap(unifiedorder, EMPTY)), wechatProfile.getPayKey()));

                UnifiedorderResult result = payUnifiedorder(unifiedorder, wechatProfile.getPayKey());
//                logger.info("Code url is {}", result.getCode_url());
                globalPaymentBean.getUnSettlementPayments().put(unifiedorder.getOut_trade_no(), unifiedorder.getOut_trade_no());

                tempJsonStr = generateMchPayJsRequestJson(result.getPrepay_id(), result.getAppid(), appProfile.getWechatProfile().getPayKey());

                PrintWriter out = getHttpResponse().getWriter();
                out.println(tempJsonStr);
                out.flush();
                out.close();
            } else {
                tempJsonStr = "Null String";
            }
        }

        logger.info("Order info: {}", tempJsonStr);

        return ActionSupport.SUCCESS;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getTempJsonStr() {
        return tempJsonStr;
    }

    public void setTempJsonStr(String tempJsonStr) {
        this.tempJsonStr = tempJsonStr;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }


    public GlobalPaymentBean getGlobalPaymentBean() {
        return globalPaymentBean;
    }

}

