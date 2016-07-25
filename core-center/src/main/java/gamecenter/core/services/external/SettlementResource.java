package gamecenter.core.services.external;

import com.google.common.base.Optional;
import gamecenter.core.domain.Device;
import gamecenter.core.domain.PaymentHistory;
import gamecenter.core.services.db.DBServices;
import org.apache.commons.lang3.StringUtils;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class SettlementResource extends ServerResource {

    public static final int YOUBAO_PAYMENT_TYPE = 2;
    public static String SETTLEMENT_URI = "/external/youbao/settlement";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DBServices dbServices;
    private Form form;

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public StringRepresentation retrieve(Representation entity) throws IOException {

        DBServices instance = DBServices.instance();
        logger.info("DB service is {}", instance);

        form = new Form(entity);
//        String internalAppId = "wxe89a9d2fa17df80f";
        String internalAppId = "liyuanapp";
        logger.info("Receive paid notification with: {}", form.getQueryString());

        String type = getParameter("type");
        String openId = getParameter("openid");
        String orderNum = getParameter("order");
        String deviceId = getParameter("deviceid");
        String code = getParameter("code");
        String tradeId = getParameter("cltno");
        String amount = getParameter("amount");
        String timestamp = getParameter("timestamp");
        String nonstr = getParameter("nonstr");
        String sign = getParameter("sign");

        if (StringUtils.isNotEmpty(code)) {
            SettlementInfo settlementInfo = new SettlementInfo(internalAppId, type, openId, orderNum, deviceId, code, tradeId, amount, timestamp, nonstr, sign);
            logger.info("Settlement Info is: {}", settlementInfo);
            logger.info("Device Service is: ", instance.getDeviceService());
            Optional<Device> deviceOptional = instance.getDeviceService().deviceByMac(deviceId);
            if (deviceOptional.isPresent()) {
                PaymentHistory paymentHistory = new PaymentHistory(2, 2, deviceOptional.get().getCenterid(), Double.valueOf(amount));
                instance.getPaymentHistoryService().addWechatPaymentHistory(paymentHistory);
                logger.info("Add payment history successfully for {}: {}", tradeId, paymentHistory);
            } else {
                logger.warn("Device not found when adding payment history!");
            }

            logger.info("Received settlement notification details is {}", settlementInfo);
        }
        return new StringRepresentation("SUCCESS".toCharArray());
    }

    private void redirectTo(String to, Parameter... parameters) {
        Reference reference = new Reference(to);
        for (Parameter parameter : parameters) {
            reference.addQueryParameter(parameter);
        }
        redirectSeeOther(reference);
    }

    @Override
    public void init(Context context, Request request, Response response) {
        super.init(context, request, response);
        form = request.getResourceRef().getQueryAsForm();
    }

    private String getParameter(String key) {
        return form.getFirstValue(key);
    }

    private class SettlementInfo {
        private final String internalAppId;
        private final String type;
        private final String openId;
        private final String orderNum;
        private final String deviceId;
        private final String code;
        private final String tradeId;
        private final String amount;
        private final String timestamp;
        private final String nonstr;
        private final String sign;

        private SettlementInfo(String internalAppId, String type, String openId, String orderNum, String deviceId, String code, String tradeId, String amount, String timestamp, String nonstr, String sign) {
            this.internalAppId = internalAppId;
            this.type = type;
            this.openId = openId;
            this.orderNum = orderNum;
            this.deviceId = deviceId;
            this.code = code;
            this.tradeId = tradeId;
            this.amount = amount;
            this.timestamp = timestamp;
            this.nonstr = nonstr;
            this.sign = sign;
        }

        @Override
        public String toString() {
            return "SettlementInfo{" +
                    "internalAppId='" + internalAppId + '\'' +
                    ", type='" + type + '\'' +
                    ", openId='" + openId + '\'' +
                    ", orderNum='" + orderNum + '\'' +
                    ", deviceId='" + deviceId + '\'' +
                    ", code='" + code + '\'' +
                    ", tradeId='" + tradeId + '\'' +
                    ", amount='" + amount + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", nonstr='" + nonstr + '\'' +
                    ", sign='" + sign + '\'' +
                    '}';
        }

        public String getType() {
            return type;
        }

        public String getOpenId() {
            return openId;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public String getCode() {
            return code;
        }

        public String getTradeId() {
            return tradeId;
        }

        public String getAmount() {
            return amount;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getNonstr() {
            return nonstr;
        }

        public String getSign() {
            return sign;
        }

        public String getInternalAppId() {
            return internalAppId;
        }
    }
}
