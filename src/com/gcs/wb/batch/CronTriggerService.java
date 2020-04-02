package com.gcs.wb.batch;

import com.gcs.wb.base.constant.Constants;
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

    static Scheduler scheduler;

    public void execute() throws ParseException, SchedulerException {
        // Init Job
        JobDetail syncMasterDataJob = new JobDetail();
        syncMasterDataJob.setName("master-data-sync");
        syncMasterDataJob.setJobClass(SyncMasterDataJob.class);

        CronTrigger syncMasterDataTrigger = new CronTrigger();
        syncMasterDataTrigger.setName("sync-master-data-trigger");
        syncMasterDataTrigger.setCronExpression(Constants.SyncMasterData.CRON_EXPRESSION);

        // Execute
        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(syncMasterDataJob, syncMasterDataTrigger);
    }

    public static void close() throws SchedulerException {
        if (scheduler != null && scheduler.isStarted()) {
            scheduler.shutdown();
        }
    }
}
