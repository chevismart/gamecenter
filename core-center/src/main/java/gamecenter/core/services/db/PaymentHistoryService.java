package gamecenter.core.services.db;

import gamecenter.core.dao.PaymentHistoryMapper;
import gamecenter.core.domain.PaymentHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentHistoryService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PaymentHistoryMapper paymentHistoryMapper;

    public PaymentHistoryService(PaymentHistoryMapper paymentHistoryMapper) {
        this.paymentHistoryMapper = paymentHistoryMapper;
    }

    public void addWechatPaymentHistory(PaymentHistory paymentHistory){
        paymentHistoryMapper.addWechatPaymentHistory(paymentHistory);
    }
}
