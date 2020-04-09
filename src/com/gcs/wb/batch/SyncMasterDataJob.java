package com.gcs.wb.batch;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants.InteractiveObject;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.SchedulerSync;
import com.gcs.wb.jpa.repositorys.SchedulerSyncRepository;
import com.gcs.wb.service.SyncMasterDataService;
import static com.gcs.wb.service.SyncMasterDataService.logger;
import java.util.Date;
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
    private Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        logger.info("Check sync scheduler...");
        String mandt = configuration.getSapClient();
        String wplant = configuration.getWkPlant();
        SchedulerSyncRepository schedulerSyncRepository = new SchedulerSyncRepository();
        SchedulerSync schedulerSync = schedulerSyncRepository.findByParamMandtWplant(mandt, wplant);

        if (schedulerSync != null && !schedulerSync.isAutoSyncAllowed()) {
            logger.info("The master data was already synced today.");
            return;
        } else if (schedulerSync == null) {
            schedulerSync = new SchedulerSync(mandt, wplant);
        }
        schedulerSync.setAutoSyncStatus(SchedulerSync.SYNC_IN_PROGRESS);
        schedulerSync.setLastAutoSync(new Date());
        
        try {
            SyncMasterDataService syncMasterDataService = new SyncMasterDataService(interactiveObject);
            syncMasterDataService.syncMasterData();

            schedulerSync.setAutoSyncStatus(SchedulerSync.SYNC_COMPLETED);
        } catch (Exception ex) {
            logger.error(ex);
            schedulerSync.setAutoSyncStatus(SchedulerSync.SYNC_ERROR);
        } finally {
            schedulerSyncRepository.updateLastSync(schedulerSync);
        }
    }
}
