package com.gcs.wb.batch;

import com.gcs.wb.bapi.helper.TransportagentGetListBapi;
import com.gcs.wb.bapi.helper.structure.TransportagentGetListStructure;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.repositorys.VendorRepository;
import java.util.ArrayList;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by THANGLH
 */
public class SyncVendorJob extends SyncJob {

    private final VendorRepository vendorRepository = new VendorRepository();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        logger.info("Sync Vendor: is starting...");

        logger.info("Sync Vendor: get data from SAP...");
        TransportagentGetListBapi bapi = new TransportagentGetListBapi();
        bapi.setIvEkorg(configuration.getWkPlant());
        List<Vendor> sapVendors = new ArrayList<>();
        try {
            session.execute(bapi);

            List<TransportagentGetListStructure> etVendors = bapi.getEtVendor();
            for (TransportagentGetListStructure vens : etVendors) {
                Vendor vendor = new Vendor();
                vendor.setMandt(configuration.getSapClient());
                vendor.setLifnr(vens.getLifnr());
                vendor.setName1(vens.getName1());
                vendor.setName2(vens.getName2());
                sapVendors.add(vendor);
            }
        } catch (Exception ex) {
            logger.error("Sync Vendor: get data from SAP failed", ex);
            logger.info("Sync Vendor: is finished...");
            return;
        }

        logger.info("Sync Vendor: sync data from SAP to Database...");
        try {
            List<Vendor> dbVendors = vendorRepository.getListVendor();
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            // update remove DB
            for (Vendor vendor : dbVendors) {
                if (sapVendors.indexOf(vendor) == -1) {
                    entityManager.remove(vendor);
                }
            }

            // update SAP - DB
            for (Vendor sapVendor : sapVendors) {
                int index = dbVendors.indexOf(sapVendor);
                if (index == -1) {
                    entityManager.persist(sapVendor);
                } else {
                    sapVendor.setId(dbVendors.get(index).getId());
                    entityManager.merge(sapVendor);
                }
            }

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            logger.error("Sync Vendor: sync data from SAP to Database failed", ex);
        }

        logger.info("Sync Vendor: is finished...");
    }
}
