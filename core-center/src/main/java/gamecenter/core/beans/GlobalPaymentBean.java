package gamecenter.core.beans;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Chevis on 2015/1/16.
 */
public class GlobalPaymentBean {
    ConcurrentHashMap unSettlementPayments = new ConcurrentHashMap();
    ConcurrentHashMap settledPayments = new ConcurrentHashMap();

    public ConcurrentHashMap getUnSettlementPayments() {
        return unSettlementPayments;
    }

    public void setUnSettlementPayments(ConcurrentHashMap unSettlementPayments) {
        this.unSettlementPayments = unSettlementPayments;
    }

    public ConcurrentHashMap getSettledPayments() {
        return settledPayments;
    }

    public void setSettledPayments(ConcurrentHashMap settledPayments) {
        this.settledPayments = settledPayments;
    }
}
