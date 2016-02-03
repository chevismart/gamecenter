package gamecenter.core.beans;

import gamecenter.core.processors.tasks.ScheduleTask;

import java.util.List;

/**
 * Created by Chevis on 2014/12/11.
 */
public class Tasks {
    List<ScheduleTask> scheduledTasks;
    List<Runnable> normalTask;

    public List<ScheduleTask> getScheduledTasks() {
        return scheduledTasks;
    }

    public void setScheduledTasks(List<ScheduleTask> scheduledTasks) {
        this.scheduledTasks = scheduledTasks;
    }

    public List<Runnable> getNormalTask() {
        return normalTask;
    }

    public void setNormalTask(List<Runnable> normalTask) {
        this.normalTask = normalTask;
    }

    @Override
    public String toString() {
        return "Tasks{" +
                "scheduledTasks=" + scheduledTasks +
                ", normalTask=" + normalTask +
                '}';
    }
}
