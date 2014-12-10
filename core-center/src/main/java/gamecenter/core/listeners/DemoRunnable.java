package gamecenter.core.listeners;

/**
 * Created by Chevis on 2014/12/11.
 */
public class DemoRunnable extends AbstractRunnable {

    @Override
    public void run() {
        while (isContinue) {
            try {
                //休眠时间
                Thread.sleep(1000);
                System.err.print(this.getClass().getSimpleName());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
