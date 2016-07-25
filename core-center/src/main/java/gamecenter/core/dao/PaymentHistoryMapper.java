package gamecenter.core.dao;

import gamecenter.core.domain.PaymentHistory;

public interface PaymentHistoryMapper {
    void addWechatPaymentHistory(PaymentHistory paymentHistory);
}
