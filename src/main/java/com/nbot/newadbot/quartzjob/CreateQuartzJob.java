package com.nbot.newadbot.quartzjob;

import com.nbot.newadbot.NewAdBotService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CreateQuartzJob implements Job {
    private Logger log = Logger.getLogger(CreateQuartzJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        long chatId = jobDataMap.getLong("paramKey");
        NewAdBotService newAdBotService = (NewAdBotService) jobDataMap.get( "newAdBotService");
        newAdBotService.checkForNewData(chatId);
        log.debug("CreateQuartezJob run " + chatId);
    }
}
