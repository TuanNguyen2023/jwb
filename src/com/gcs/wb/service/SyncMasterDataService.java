/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.constant.Constants.InteractiveObject;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.repositorys.BatchStockRepository;
import com.gcs.wb.jpa.repositorys.CustomerRepository;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.PartnerRepository;
import com.gcs.wb.jpa.repositorys.PurchaseOrderRepository;
import com.gcs.wb.jpa.repositorys.SLocRepository;
import com.gcs.wb.jpa.repositorys.SaleOrderRepository;
import com.gcs.wb.jpa.repositorys.VendorRepository;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl.qx
 */
public class SyncMasterDataService {

    private VendorRepository vendorRepository = new VendorRepository();
    private MaterialRepository materialRepository = new MaterialRepository();
    private SLocRepository sLocRepository = new SLocRepository();
    private BatchStockRepository batchStockRepository = new BatchStockRepository();
    private PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();
    private SaleOrderRepository saleOrderRepository = new SaleOrderRepository();
    private CustomerRepository customerRepository = new CustomerRepository();
    private PartnerRepository partnerRepository = new PartnerRepository();

    private SAPService sapService;
    private Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    public static Logger logger = Logger.getLogger(SyncMasterDataService.class);

    public SyncMasterDataService() {
        this.sapService = new SAPService();
    }

    public SyncMasterDataService(InteractiveObject interactiveObject) {
        this.sapService = new SAPService(interactiveObject);
    }

    public void syncMasterData() throws Exception {
        logger.info("Sync master data is processing...");

        logger.info("Sync SAP setting...");
        SAPSetting sapSetting = syncSapSetting();
        WeighBridgeApp.getApplication().setSapSetting(sapSetting);

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

        logger.info("Sync PO, POSTO...");
        syncPoPostoDatas();

        logger.info("Sync SO...");
        syncSoDatas();

        logger.info("Sync customer...");
        syncCustomer();

        logger.info("Sync Weigh Bridge vendor master data oubound...");
        syncPartner();

        logger.info("Sync master data is finished...");

        logger.info("Confirm refresh app...");
        handleRefreshApplication();
    }

    public void syncMasterDataWhenLogin() throws Exception {
        String mandt = configuration.getSapClient();
        String wplant = configuration.getWkPlant();

        logger.info("Sync master data is processing...");

        logger.info("Sync SAP setting...");
        SAPSetting sapSetting = syncSapSetting();
        WeighBridgeApp.getApplication().setSapSetting(sapSetting);

        logger.info("Sync vendor...");
        if (!vendorRepository.hasData(mandt, wplant)) {
            syncVendor();
        }

        logger.info("Sync material...");
        List<Material> materials = new ArrayList<>();
        if (!materialRepository.hasData(mandt, wplant)) {
            materials = syncMaterial();
        }

        logger.info("Sync sloc...");
        List<SLoc> slocs = new ArrayList<>();
        if (!sLocRepository.hasData(mandt, wplant)) {
            slocs = syncSloc();
        }

        logger.info("Sync batch stock...");
        if (!batchStockRepository.hasData(mandt, wplant)) {
            if (!materials.isEmpty() && !slocs.isEmpty()) {
                for (SLoc sloc : slocs) {
                    for (Material material : materials) {
                        syncBatchStock(sloc.getLgort(), material.getMatnr());
                    }
                }
            }
        }

        logger.info("Sync PO, POSTO...");
        if (!purchaseOrderRepository.hasData()) {
            syncPoPostoDatas();
        }

        logger.info("Sync SO...");
        if (!saleOrderRepository.hasData()) {
            syncSoDatas();
        }

        logger.info("Sync customer...");
        if (!customerRepository.hasData()) {
            syncCustomer();
        }

        logger.info("Sync Weigh Bridge vendor master data oubound...");
        if (!partnerRepository.hasData()) {
            syncPartner();
        }

        logger.info("Sync master data is finished...");
    }

    public SAPSetting syncSapSetting() {
        return sapService.syncSapSetting(configuration.getSapClient(), configuration.getWkPlant());
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

    // sync for offline
    public void syncPoPostoDatas() throws Exception {
        sapService.syncPoPostoDatas();
    }

    public void syncSoDatas() {
        sapService.syncSoDatas();
    }

    public void syncCustomer() {
        sapService.syncCustomer();
    }

    public void syncPartner() {
        sapService.syncPartnerDatas();
    }

    public void handleRefreshApplication() {
        int answer = JOptionPane.showConfirmDialog(WeighBridgeApp.getApplication().getMainFrame(),
                "Đồng bộ dữ liệu thành công. Cập nhật lại ứng dụng?", "Đồng bộ dữ liệu", JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            WeighBridgeApp.getApplication().refreshApplicationView();
        }
    }
}
