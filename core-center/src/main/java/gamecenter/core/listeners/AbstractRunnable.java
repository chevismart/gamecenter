package gamecenter.core.listeners;

/**
 * Created by Chevis on 2014/12/11.
 */
public abstract class AbstractRunnable implements Runnable {

    protected boolean isContinue = true;

    public void stop() {
        isContinue = false;
    }
}
