package gamecenter.core.beans;

import org.springframework.scheduling.TaskScheduler;

import java.util.List;

/**
 * Created by Chevis on 2014/12/11.
 */
public class Tasks {
    List<TaskScheduler> scheduledTasks;
    List<Runnable> normalTask;

    public List<TaskScheduler> getScheduledTasks() {
        return scheduledTasks;
    }

    public void setScheduledTasks(List<TaskScheduler> scheduledTasks) {
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
