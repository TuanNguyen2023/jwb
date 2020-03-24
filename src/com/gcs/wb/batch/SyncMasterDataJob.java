package com.gcs.wb.batch;

import com.gcs.wb.base.constant.Constants.InteractiveObject;
import com.gcs.wb.service.SyncMasterDataService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by THANGLH
 */
public class SyncMasterDataJob implements Job {

    public static Logger logger = Logger.getLogger(SyncMasterDataJob.class);
    InteractiveObject interactiveObject = InteractiveObject.SYSTEM;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            SyncMasterDataService syncMasterDataService = new SyncMasterDataService(interactiveObject);
            syncMasterDataService.syncMasterData();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
}
