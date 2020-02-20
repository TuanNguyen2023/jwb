package com.gcs.wb.batch;

import com.gcs.wb.bapi.helper.MaterialGetListBapi;
import com.gcs.wb.bapi.helper.structure.MaterialGetListStructure;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import java.util.ArrayList;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by THANGLH
 */
public class SyncMaterialJob extends SyncJob {

    private final MaterialRepository materialRepository = new MaterialRepository();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        logger.info("Sync Material: is starting...");

        logger.info("Sync Material: get data from SAP...");
        List<Material> sapMaterials = new ArrayList<>();
        MaterialGetListBapi bapi = new MaterialGetListBapi();
        try {
            session.execute(bapi);
            List<MaterialGetListStructure> mats = bapi.getEtMaterial();
            for (MaterialGetListStructure mat : mats) {
                if (configuration.getWkPlant().equalsIgnoreCase(mat.getWerks())) {
                    Material m;
                    m = new Material(configuration.getSapClient(), mat.getWerks(), mat.getMatnr());
                    m.setMaktx(mat.getMaktx());
                    m.setMaktg(mat.getMaktxLong());
                    sapMaterials.add(m);
                }
            }
        } catch (Exception ex) {
            logger.error("Sync Material: get data from SAP failed", ex);
            logger.info("Sync Material: is finished...");
            return;
        }

        logger.info("Sync Material: sync data from SAP to Database...");
        try {
            List<Material> dbMaterials = materialRepository.getListMaterial();
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            //update for remove DB
            for (Material material : dbMaterials) {
                if (sapMaterials.indexOf(material) == -1) {
                    entityManager.remove(material);
                }
            }
            // update SAP -> DB    
            for (Material sapMaterial : sapMaterials) {
                int index = dbMaterials.indexOf(sapMaterial);
                if (index == -1) {
                    entityManager.persist(sapMaterial);
                } else {
                    sapMaterial.setId(dbMaterials.get(index).getId());
                    entityManager.merge(sapMaterial);
                }
            }

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            logger.error("Sync Material: sync data from SAP to Database failed", ex);
        }

        logger.info("Sync Material: is finished...");
    }
}
