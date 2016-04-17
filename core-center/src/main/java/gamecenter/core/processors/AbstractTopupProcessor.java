package gamecenter.core.processors;

public abstract class AbstractTopupProcessor extends GeneralProcessor {

    protected int getChargeAmount() {
        String value = getHttpRequest().getParameter("chargeAmount");
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            logger.error("Convert to charge amount failed for {}", value);
        }
        return 0;
    }

    protected String getDeviceId() {
        return getHttpRequest().getParameter("deviceId");
    }

}
