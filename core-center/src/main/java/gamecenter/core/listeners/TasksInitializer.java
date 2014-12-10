package gamecenter.core.listeners;

import gamecenter.core.Constants.CommonConstants;
import gamecenter.core.beans.Tasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by Chevis on 2014/12/11.
 */
public class TasksInitializer implements ServletContextListener {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private TaskExecutor taskExecutor;
    private Tasks tasks;

    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        tasks = (Tasks) context.getBean(CommonConstants.TASKS_BEAN_NAME);
        taskExecutor = (TaskExecutor) context.getBean(CommonConstants.TASK_EXECUTOR_NAME);
        logger.debug("The following tasks to be initialized: {}", tasks);
        for (Runnable normalTask : tasks.getNormalTask()) {
            taskExecutor.execute(normalTask);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        for (Runnable noramlTask : tasks.getNormalTask()) {
            ((AbstractRunnable) noramlTask).stop();//TODO: Refactor here
        }
    }
}
