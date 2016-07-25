package gamecenter.core.domain;

public class PaymentHistory {
//    <result column="payment_system_id" property="paymentSystemId" jdbcType="INTEGER"/>
//    <result column="charge_history_id" property="chargeHistoryId" jdbcType="INTEGER"/>
//    <result column="center_id" property="centerId" jdbcType="INTEGER"/>
//    <result column="amount" property="amount" jdbcType="DOUBLE"/>

    private final int paymentSystemId;
    private final int chargeHistoryId;
    private final int centerId;
    private final double amount;

    public PaymentHistory(int paymentSystemId, int chargeHistoryId, int centerId, double amount) {
        this.paymentSystemId = paymentSystemId;
        this.chargeHistoryId = chargeHistoryId;
        this.centerId = centerId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentHistory{" +
                "paymentSystemId=" + paymentSystemId +
                ", chargeHistoryId=" + chargeHistoryId +
                ", centerId=" + centerId +
                ", amount=" + amount +
                '}';
    }

    public int getPaymentSystemId() {
        return paymentSystemId;
    }

    public int getChargeHistoryId() {
        return chargeHistoryId;
    }

    public int getCenterId() {
        return centerId;
    }

    public double getAmount() {
        return amount;
    }
}
