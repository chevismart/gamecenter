package gamecenter.core.processors;

/**
 * Created by Chevis on 2015/1/17.
 */
public abstract class AbstractTopupProcessor extends GeneralProcessor {

    protected int getChargeAmount() {
        return Integer.valueOf(getHttpRequest().getParameter("chargeAmount"));
    }

}
