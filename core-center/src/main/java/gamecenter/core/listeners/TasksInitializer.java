package gamecenter.core.listeners;

import gamecenter.core.beans.Tasks;
import gamecenter.core.processors.tasks.ScheduleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static gamecenter.core.constants.CommonConstants.TASKS_BEAN_NAME;
import static gamecenter.core.constants.CommonConstants.TASK_EXECUTOR_NAME;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class TasksInitializer implements ServletContextListener {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private TaskExecutor taskExecutor;
    private Tasks tasks;

    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        tasks = (Tasks) context.getBean(TASKS_BEAN_NAME);
        taskExecutor = (TaskExecutor) context.getBean(TASK_EXECUTOR_NAME);
        logger.debug("The following tasks to be initialized: {}", tasks);
        for (Runnable normalTask : tasks.getNormalTask()) {
            taskExecutor.execute(normalTask);
        }
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(5);
        for (ScheduleTask scheduleTask : tasks.getScheduledTasks()) {
            exec.scheduleAtFixedRate(scheduleTask, scheduleTask.initDelay(), scheduleTask.interval(), MILLISECONDS);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        for (Runnable normalTask : tasks.getNormalTask()) {
            //TODO: Refactor here
        }
    }
}
