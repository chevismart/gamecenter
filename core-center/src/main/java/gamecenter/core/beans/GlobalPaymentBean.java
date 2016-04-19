package gamecenter.core.beans;

import gamecenter.core.beans.wechat.PayNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class GlobalPaymentBean {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    ConcurrentHashMap<String, PayNotification> unSettlementPayments = new ConcurrentHashMap();
    ConcurrentHashMap<String, PayNotification> settledPayments = new ConcurrentHashMap();
    ConcurrentHashMap<String, PayNotification> processingPayments = new ConcurrentHashMap();

    public ConcurrentHashMap getUnSettlementPayments() {
        return unSettlementPayments;
    }

    public void setUnSettlementPayments(ConcurrentHashMap<String, PayNotification> unSettlementPayments) {
        this.unSettlementPayments = unSettlementPayments;
    }

    public ConcurrentHashMap<String, PayNotification> getSettledPayments() {
        return settledPayments;
    }

    public void setSettledPayments(ConcurrentHashMap<String, PayNotification> settledPayments) {
        this.settledPayments = settledPayments;
    }

    public ConcurrentHashMap<String, PayNotification> getProcessingPayments() {
        return processingPayments;
    }

    public void newPayment(PayNotification payNotification) {
        unSettlementPayments.put(payNotification.getOut_trade_no(), payNotification);
    }

    public boolean isProcessingPayment(PayNotification payNotification) {
        return processingPayments.containsKey(payNotification.getOut_trade_no());
    }

    public boolean isProcessedPayment(PayNotification payNotification){
        return !isNewPayment(payNotification.getOut_trade_no()) && !isProcessingPayment(payNotification);
    }

    public boolean isNewPayment(String tradeNumber) {
        return unSettlementPayments.containsKey(tradeNumber) &&
                !processingPayments.containsKey(tradeNumber) &&
                !settledPayments.containsKey(tradeNumber);
    }

    public void startProcessingPayment(PayNotification payNotification){
        processingPayments.put(payNotification.getOut_trade_no(), payNotification);
        unSettlementPayments.remove(payNotification.getOut_trade_no());
    }

    public void completePayment(PayNotification payNotification) {
        if (isProcessingPayment(payNotification)) {
            settledPayments.put(payNotification.getOut_trade_no(), payNotification);
            if (processingPayments.containsKey(payNotification.getOut_trade_no()))
                processingPayments.remove(payNotification.getOut_trade_no());

            logger.info("Settled Payment: {}", settledPayments.toString());
            logger.info("Unsettled Payment: {}", unSettlementPayments.toString());
        } else {
            logger.warn("This payment notification is never processed before: {}", payNotification);
        }
    }

    @Override
    public String toString() {
        return "GlobalPaymentBean{" +
                "unSettlementPayments=" + unSettlementPayments +
                ", settledPayments=" + settledPayments +
                '}';
    }
}
