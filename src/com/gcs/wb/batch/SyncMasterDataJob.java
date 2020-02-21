package com.gcs.wb.batch;

import com.gcs.wb.service.SyncMasterDataService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by THANGLH
 */
public class SyncMasterDataJob implements Job {

    SyncMasterDataService syncMasterDataService = new SyncMasterDataService();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        syncMasterDataService.syncMasterData();
    }
}
