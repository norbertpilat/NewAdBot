package com.nbot.newadbot.quartzjob;

import com.nbot.newadbot.NewAdBotService;
import com.nbot.newadbot.user.User;
import org.apache.log4j.BasicConfigurator;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


public class ScheduleTaskNewData {
    private static Scheduler scheduler;
    public static void starScheduler( NewAdBotService newAdBotService, User user) throws SchedulerException {
         BasicConfigurator.configure();
         scheduler = new StdSchedulerFactory().getScheduler();

        int timeRefresh = user.getTimeRefresh();
        long chatId = user.getChatId();

        Trigger trigger = createTrigger(timeRefresh);
        scheduleJob(trigger,chatId,newAdBotService);
        scheduler.start();
    }

    private static Trigger createTrigger(int timeRefresh) {
        return TriggerBuilder.newTrigger()
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(timeRefresh)
                                .repeatForever()
                )
                .build();
    }

    private static void scheduleJob(Trigger trigger, long chatId, NewAdBotService newAdBotService) throws SchedulerException {
        JobDetail jobInstance = JobBuilder.newJob( CreateQuartzJob.class)
                .usingJobData("paramKey", chatId)
                .build();
        jobInstance.getJobDataMap().put("newAdBotService",newAdBotService);
        scheduler.scheduleJob(jobInstance,trigger);
    }
}
