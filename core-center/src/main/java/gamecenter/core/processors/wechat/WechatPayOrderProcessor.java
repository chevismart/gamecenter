package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.CoreCenterHost;
import gamecenter.core.beans.GlobalPaymentBean;
import gamecenter.core.beans.UserProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.processors.AbstractTopupProcessor;
import gamecenter.core.utils.ProfileUtil;
import gamecenter.core.utils.TimeUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.util.MapUtil;
import weixin.popular.util.PayUtil;
import weixin.popular.util.SignatureUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Chevis on 2014/12/25.
 */
public class WechatPayOrderProcessor extends AbstractTopupProcessor {

    ProfileManager profileManager;
    GlobalPaymentBean globalPaymentBean;
    private UserProfile userProfile;
    private String tempJsonStr;

    @Override
    public String execute() throws Exception {

        logger.info("Received payment order request");
        String wechatOpenId = ProfileUtil.getUserOriginalId(userProfile.getInternalId());


        PayMchAPI payMchAPI = new PayMchAPI();
        Unifiedorder unifiedorder = new Unifiedorder();
        AppProfile appProfile = userProfile.getAccessInfo().getAppProfile();
        if (null != appProfile && appProfile.isWechatProfileValid()) {
            WechatProfile wechatProfile = appProfile.getWechatProfile();
            unifiedorder.setAppid(wechatProfile.getWechatAppId());
            unifiedorder.setMch_id(wechatProfile.getMchid());
            unifiedorder.setNonce_str(RandomStringUtils.random(32, true, true));
            unifiedorder.setBody("Testing for" + wechatOpenId);
            unifiedorder.setOut_trade_no("trade_" + TimeUtil.getCurrentDateTime().getTime());
            unifiedorder.setTotal_fee(String.valueOf(getChargeAmount()));
            unifiedorder.setSpbill_create_ip(getRemoteAddress(getHttpRequest()));
            unifiedorder.setNotify_url(CoreCenterHost.WECHAT_PAYMENT_NOTIFICATION_CALLBACK_URL);
            unifiedorder.setTrade_type("NATIVE");
            unifiedorder.setOpenid(wechatOpenId);
            unifiedorder.setAttach(appProfile.getAppId());
            unifiedorder.setDevice_info("ATM001");
            unifiedorder.setSign(SignatureUtil.generateSign(MapUtil.order(MapUtil.objectToMap(unifiedorder, StringUtils.EMPTY)), wechatProfile.getPayKey()));

            UnifiedorderResult result = payMchAPI.payUnifiedorder(unifiedorder);
            logger.info(result.getCode_url());
            globalPaymentBean.getUnSettlementPayments().put(unifiedorder.getOut_trade_no(), unifiedorder.getOut_trade_no());
            getHttpResponse().sendRedirect(result.getCode_url());

            tempJsonStr = PayUtil.generateMchPayJsRequestJson(result.getPrepay_id(), result.getAppid(), appProfile.getWechatProfile().getPayKey());
        } else {
            tempJsonStr = "Null String";
        }

        logger.info("Order info: {}", tempJsonStr);

        return ActionSupport.SUCCESS;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
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

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public GlobalPaymentBean getGlobalPaymentBean() {
        return globalPaymentBean;
    }

    public void setGlobalPaymentBean(GlobalPaymentBean globalPaymentBean) {
        this.globalPaymentBean = globalPaymentBean;
    }
}

