package gamecenter.core.processors.tasks;

/**
 * Created by chevi on 2016/1/12.
 */
public interface ScheduleTask extends Runnable{
    long interval();
    long initDelay();
}
