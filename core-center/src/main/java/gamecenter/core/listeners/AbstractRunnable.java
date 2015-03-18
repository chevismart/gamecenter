package gamecenter.core.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Chevis on 2014/12/11.
 */
public abstract class AbstractRunnable implements Runnable {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected boolean isContinue = true;

    public void stop() {
        isContinue = false;
    }
}
