package gamecenter.core.beans;

/**
 * Created by Chevis on 15/1/17.
 */
public class Trade {

    private AccessChannel accessChannel;
    private String internalId;
    private long timestamp;
    private int amount;

    @Override
    public String toString() {
        return "Trade{" +
                "accessChannel=" + accessChannel +
                ", internalId='" + internalId + '\'' +
                ", timestamp=" + timestamp +
                ", amount=" + amount +
                '}';
    }

    public AccessChannel getAccessChannel() {
        return accessChannel;
    }

    public void setAccessChannel(AccessChannel accessChannel) {
        this.accessChannel = accessChannel;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
