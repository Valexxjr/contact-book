package controller;

import controller.schedule.ScheduleTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ScheduleTask.startScheduler();
        ScheduleTask.addEmailJob();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ScheduleTask.shutdownScheduler();
    }

}
