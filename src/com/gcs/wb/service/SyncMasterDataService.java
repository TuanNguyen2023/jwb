/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.SLoc;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl.qx
 */
public class SyncMasterDataService {

    private SAPService sapService = new SAPService();
    private Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    public static Logger logger = Logger.getLogger(SyncMasterDataService.class);

    public void syncMasterData() {
        logger.info("Sync master data is processing...");

        logger.info("Sync SAP setting...");
        syncSapSetting();
        
        logger.info("Sync vendor...");
        syncVendor();

        logger.info("Sync material...");
        List<Material> materials = syncMaterial();
        
        logger.info("Sync sloc...");
        List<SLoc> slocs = syncSloc();

        logger.info("Sync batch stock...");
        if (!materials.isEmpty() && !slocs.isEmpty()) {
            for (SLoc sloc : slocs) {
                for (Material material : materials) {
                    syncBatchStock(sloc.getLgort(), material.getMatnr());
                }
            }
        }
        
        logger.info("Sync master data is finished...");
    }

    public void syncSapSetting() {
        sapService.syncSapSetting(configuration.getSapClient(), configuration.getWkPlant());
    }

    public void syncVendor() {
        sapService.syncVendor();
    }

    public List<Material> syncMaterial() {
        return sapService.syncMaterial();
    }

    public List<SLoc> syncSloc() {
        return sapService.syncSloc();
    }

    public void syncBatchStock(String lgort, String matnr) {
        sapService.syncBatchStocks(lgort, matnr);
    }
}
