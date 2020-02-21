package com.gcs.wb.batch;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;

/**
 * Created by PHUCPH on 2/11/2020.
 */
public class CronTriggerService {

    private final String CRON_EXPRESSION = "0 0 0 ? * * *"; // 00:00:00 every day

    public void execute() throws ParseException, SchedulerException {
        // Init Job
        JobDetail syncMasterDataJob = new JobDetail();
        syncMasterDataJob.setName("master-data-sync");
        syncMasterDataJob.setJobClass(SyncMasterDataJob.class);

        CronTrigger syncMasterDataTrigger = new CronTrigger();
        syncMasterDataTrigger.setName("sync-master-data-trigger");
        syncMasterDataTrigger.setCronExpression(CRON_EXPRESSION);

        // Execute
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(syncMasterDataJob, syncMasterDataTrigger);
    }
}
