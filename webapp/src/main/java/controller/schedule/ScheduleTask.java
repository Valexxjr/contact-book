package controller.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class ScheduleTask {

    private static Logger logger = LogManager.getLogger(ScheduleTask.class);

    private static Scheduler scheduler;

    public static void startScheduler() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("cannot start scheduler");
        }
    }

    public static void shutdownScheduler() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            logger.error("cannot shutodown scheduler");
        }
    }

    public static void addEmailJob() {

        JobDetail job = JobBuilder.
                newJob(SendMailJob.class)
                .withIdentity("mailJob", "mailGroup").build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("mailTrigger", "mailGroup")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInHours(24).repeatForever())
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            logger.error("cannot start mailing job");
        }
    }
}