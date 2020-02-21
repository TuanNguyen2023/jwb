package com.gcs.wb.batch;

import com.gcs.wb.service.SyncMasterDataService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by THANGLH
 */
public class SyncMasterDataJob implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        SyncMasterDataService syncMasterDataService = new SyncMasterDataService();
        syncMasterDataService.syncMasterData();
    }
}
