package com.gcs.wb.batch;

import com.gcs.wb.bapi.helper.SLocsGetListBapi;
import com.gcs.wb.bapi.helper.structure.SLocsGetListStructure;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.repositorys.SLocRepository;
import java.util.ArrayList;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by THANGLH
 */
public class SyncSlocJob extends SyncJob {

    private final SLocRepository sLocRepository = new SLocRepository();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        logger.info("Sync Sloc: is starting...");

        logger.info("Sync Sloc: get data from SAP...");
        String mandt = configuration.getSapClient();
        String wplant = configuration.getWkPlant();
        SLocsGetListBapi bSloc = new SLocsGetListBapi(mandt, wplant);
        List<SLoc> sapSLocs = new ArrayList<>();
        try {
            session.execute(bSloc);
            List<SLocsGetListStructure> tdSLocs = bSloc.getTdSLocs();
            for (SLocsGetListStructure s : tdSLocs) {
                SLoc sloc = new SLoc(s.getLgort());
                sloc.setLgobe(s.getLgobe());
                sapSLocs.add(sloc);
            }
        } catch (Exception ex) {
            logger.error("Sync Sloc: get data from SAP failed", ex);
            logger.info("Sync Sloc: is finished...");
            return;
        }

        logger.info("Sync Sloc: sync data from SAP to Database...");
        try {
            List<SLoc> dbSLocs = sLocRepository.getListSLoc();
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            // update case delete
            for (SLoc sloc : dbSLocs) {
                if (sapSLocs.indexOf(sloc) == -1) {
                    entityManager.remove(sloc);
                }
            }

            // update case persit/merge
            for (SLoc sloc : sapSLocs) {
                sloc.setMandt(mandt);
                sloc.setWplant(wplant);

                int index = dbSLocs.indexOf(sloc);
                if (index == -1) {
                    entityManager.persist(sloc);
                } else {
                    sloc.setId(dbSLocs.get(index).getId());
                    entityManager.merge(sloc);
                }
            }

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            logger.error("Sync Sloc: sync data from SAP to Database failed", ex);
        }

        logger.info("Sync Sloc: is finished...");
    }
}
