package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import gamecenter.core.beans.AppProfile;
import gamecenter.core.beans.wechat.WechatProfile;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class WechatPayOrderProcessor extends ActionSupport {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    ProfileManager profileManager;

    private String tempJsonStr;

    @Override
    public String execute() throws Exception {

        logger.info("Received payment order request");

        PayMchAPI payMchAPI = new PayMchAPI();
        Unifiedorder unifiedorder = new Unifiedorder();
        AppProfile appProfile = profileManager.getAppProfile("liyuanapp");
        if (null != appProfile && appProfile.isWechatProfileValid()) {
            WechatProfile wechatProfile = appProfile.getWechatProfile();
            unifiedorder.setAppid(wechatProfile.getWechatAppId());
            unifiedorder.setMch_id(wechatProfile.getMchid());
            unifiedorder.setNonce_str(RandomStringUtils.random(32, true, true));
            unifiedorder.setBody("Testing body");
            unifiedorder.setOut_trade_no("trade_number_0001");
            unifiedorder.setTotal_fee("1");
            unifiedorder.setSpbill_create_ip(getRemoteAddress(getHttpRequest()));
            unifiedorder.setNotify_url("alcock.gicp.net:8888/wechatNotice");
            unifiedorder.setTrade_type("JSAPI");
            unifiedorder.setOpenid(wechatProfile.getActiveUserList().values().iterator().next().getOpenid());
            unifiedorder.setSign(SignatureUtil.generateSign(MapUtil.order(MapUtil.objectToMap(unifiedorder, StringUtils.EMPTY)), wechatProfile.getPayKey()));
        }
        UnifiedorderResult result = payMchAPI.payUnifiedorder(unifiedorder);

        tempJsonStr = PayUtil.generateMchPayJsRequestJson(result.getPrepay_id(), result.getAppid(), appProfile.getWechatProfile().getPayKey());

        logger.info("Order info: {}", tempJsonStr);

        return ActionSupport.SUCCESS;
    }

    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
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
}

