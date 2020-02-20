package com.gcs.wb.batch;

import com.gcs.wb.bapi.helper.BatchStocksGetListBapi;
import com.gcs.wb.bapi.helper.structure.BatchStocksStructure;
import com.gcs.wb.jpa.entity.BatchStock;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.repositorys.BatchStockRepository;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.SLocRepository;
import java.util.ArrayList;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by THANGLH
 */
public class SyncBatchStockJob extends SyncJob {

    private final MaterialRepository materialRepository = new MaterialRepository();
    private final SLocRepository sLocRepository = new SLocRepository();
    private final BatchStockRepository batchStockRepository = new BatchStockRepository();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        logger.info("Sync Batch stock: is starting...");

        logger.info("Sync Batch stock: get data from SAP...");
        String mandt = configuration.getSapClient();
        String wplant = configuration.getWkPlant();

        logger.info("Sync Batch stock: get data of Material and Sloc...");
        List<Material> materials = materialRepository.getListMaterial();
        List<SLoc> sLocs = sLocRepository.getListSLoc();

        if (materials.isEmpty() || sLocs.isEmpty()) {
            logger.info("Sync Batch stock: Data of Material and Sloc not exist");
        } else {
            for (SLoc sLoc : sLocs) {
                for (Material material : materials) {
                    BatchStocksGetListBapi bBatch = new BatchStocksGetListBapi();
                    List<BatchStock> sapBatchStocks = new ArrayList<>();
                    bBatch.setIdMandt(mandt);
                    bBatch.setIdWerks(wplant);
                    bBatch.setIdLgort(sLoc.getLgort());
                    bBatch.setIdMatnr(material.getMatnr());

                    try {
                        session.execute(bBatch);
                        List<BatchStocksStructure> bBatchStocks = bBatch.getBatchStocks();
                        System.out.println("size: " + bBatchStocks.size() + wplant);
                        for (BatchStocksStructure b : bBatchStocks) {
                            BatchStock bs = new BatchStock(mandt, wplant, b.getLgort(), b.getMatnr(), b.getCharg());
                            bs.setLvorm(b.getLvorm() == null || b.getLvorm().trim().isEmpty() ? ' ' : b.getLvorm().charAt(0));

                            sapBatchStocks.add(bs);
                        }
                    } catch (Exception ex) {
                        logger.error("Sync Batch stock: get data from SAP failed", ex);
                        continue;
                    }

                    logger.info("Sync Batch stock: sync data from SAP to Database...");
                    try {
                        List<BatchStock> dbBatchStocks = batchStockRepository.getListBatchStock(configuration.getWkPlant(), sLoc.getLgort(), material.getMatnr());
                        if (!entityTransaction.isActive()) {
                            entityTransaction.begin();
                        }

                        //case delete
                        for (BatchStock batchStock : dbBatchStocks) {
                            if (sapBatchStocks.indexOf(batchStock) == -1) {
                                entityManager.remove(batchStock);
                            }
                        }
                        //case persit/merge
                        for (BatchStock batchStock : sapBatchStocks) {
                            int index = dbBatchStocks.indexOf(batchStock);
                            if (index == -1) {
                                entityManager.persist(batchStock);
                            } else {
                                batchStock.setId(dbBatchStocks.get(index).getId());
                                entityManager.merge(batchStock);
                            }
                        }

                        entityTransaction.commit();
                        entityManager.clear();
                    } catch (Exception ex) {
                        if (entityTransaction.isActive()) {
                            entityTransaction.rollback();
                        }
                        logger.error("Sync Batch stock: sync data from SAP to Database failed", ex);
                    }
                }
            }
        }

        logger.info("Sync Batch stock: is finished...");
    }
}
