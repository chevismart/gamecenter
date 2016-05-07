package gamecenter.core.processors.tasks;

public interface ScheduleTask extends Runnable{
    long interval();
    long initDelay();
}
