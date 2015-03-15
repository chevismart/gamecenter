package gamecenter.core.processors.wechat;

import com.opensymphony.xwork2.ActionContext;
import gamecenter.core.beans.wechat.PayNotification;
import gamecenter.core.beans.wechat.WechatProfile;
import gamecenter.core.constants.CommonConstants;
import gamecenter.core.processors.GeneralProcessor;
import gamecenter.core.utils.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weixin.popular.util.MapUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.XMLConverUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Chevis on 2014/12/25.
 */
public class WechatPayNotificationProcessor extends GeneralProcessor {

    Logger paymentLogger = LoggerFactory.getLogger("wechatPaymentLogger");
    ProfileManager profileManager;
    WechatProfile wechatProfile;

    @Override
    public String execute() throws Exception {
        String nextStep = StringUtils.EMPTY;
        PayNotification payNotification = getPayNotification();
        if (verifySign(payNotification)) {
            if (StringUtils.isNotEmpty(payNotification.getAttach())) {
                // Handle the request for the common paid notification.
                nextStep = CommonConstants.ACCESS_ROUTER_PAID_NOTIFY;
            } else if (StringUtils.isNotEmpty(payNotification.getProduct_id())) {
                // Handle the request for the native pre-pay order
                nextStep = CommonConstants.ACCESS_ROUTER_PREPAY_ORDER;
            }
        } else {
            logger.warn("The payment notification is invalid to be processed.");
        }
        logger.info("The next step is {}", nextStep);
        return nextStep;
    }

    protected PayNotification getPayNotification() throws IOException {
        String xml = IOUtils.toString(getHttpRequest().getInputStream());
        PayNotification payNotification;
        if (StringUtils.isNotEmpty(xml)) {
            logger.debug("Received pay notification xml: {}", xml);
            payNotification = XMLConverUtil.convertToObject(PayNotification.class, new String(xml.getBytes("iso-8859-1"), "utf-8"));
            logger.info("Converted payment notification is: {}", payNotification.toString());
            getHttpRequest().setAttribute("Paynotification", payNotification);
        } else {
            payNotification = (PayNotification) getHttpRequest().getAttribute("Paynotification");
            logger.info("Retrieve the pay notification from http request.");
        }
        return payNotification;
    }


    protected void returnWechatResponse(String content) {
        try {
            ServletOutputStream os = getHttpResponse().getOutputStream();
            getHttpResponse().setStatus(HttpServletResponse.SC_OK);
            logger.info("Return notification response: {}", content);
            os.write(content.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error("Return wechat response with error: {}", e.getMessage());
        }
    }

    private boolean verifySign(PayNotification payNotification) {
        String sign = payNotification.getSign();
        logger.info("Sign is {}", sign);
        Map<String, String> valueableMap = MapUtil.order(MapUtil.objectToMap(payNotification));
        logger.info("valuable map is {}", valueableMap);
        String validationSign = SignatureUtil.generateSign(MapUtil.order(CollectionUtils.removeEmptyValueEntry(valueableMap)),
                profileManager.findAppProfileByWechatId(payNotification.getAppid()).getWechatProfile().getPayKey());
        logger.info("Generated sign is {}", validationSign);
        return StringUtils.isNotEmpty(sign) && sign.equals(validationSign);
    }

    protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
    }

    protected HttpServletResponse getHttpResponse() {
        return (HttpServletResponse) ActionContext.getContext().get(StrutsStatics.HTTP_RESPONSE);
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }
}

