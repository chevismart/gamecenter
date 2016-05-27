package gamecenter.core.services.external;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class SettlementResource extends ServerResource {

    public static String SETTLEMENT_URI = "/external/youbao/settlement";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Form form;

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public String retrieve() throws IOException {

        String appId = "wxe89a9d2fa17df80f";
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

        SettlementInfo settlementInfo = new SettlementInfo(type, openId, orderNum, deviceId, code, tradeId, amount, timestamp, nonstr, sign);

        logger.info("Received settlement notification details is {}", settlementInfo);

        return "SUCCESS";
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

        private SettlementInfo(String type, String openId, String orderNum, String deviceId, String code, String tradeId, String amount, String timestamp, String nonstr, String sign) {
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
                    "type='" + type + '\'' +
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
    }
}
