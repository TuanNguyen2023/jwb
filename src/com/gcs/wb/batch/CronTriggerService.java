package com.gcs.wb.batch;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.model.AppConfig;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import org.apache.log4j.Logger;

/**
 * Created by PHUCPH on 2/11/2020.
 */
public class CronTriggerService {

    static Scheduler scheduler;
    private static Logger logger = Logger.getLogger(CronTriggerService.class);
    public void execute() throws ParseException, SchedulerException {
        AppConfig appConf = WeighBridgeApp.getApplication().getConfig();
        // Check auto sync is allowed or not in application.properties
        if (!appConf.getAutoSync()) {
            logger.info("Auto sync is disable. Please update application.properties to enable.");
            return;
        }
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
