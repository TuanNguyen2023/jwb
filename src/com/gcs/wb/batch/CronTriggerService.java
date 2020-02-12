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

    public void execute() throws ParseException, SchedulerException {
        JobDetail companyJob = new JobDetail();
        companyJob.setName("company-sync");
        companyJob.setJobClass(SyncCompany.class);

        JobDetail vendorJob = new JobDetail();
        companyJob.setName("vendor-sync");
        companyJob.setJobClass(SyncVendor.class);

        CronTrigger trigger = new CronTrigger();
        trigger.setName("master-data-sync-trigger");
        trigger.setCronExpression("0/30 * * * * ?");

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(companyJob, trigger);
        scheduler.scheduleJob(vendorJob, trigger);
    }
}
