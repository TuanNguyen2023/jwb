/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.service;

import com.gcs.wb.bapi.helper.DoGetDetailBapi;
import com.gcs.wb.bapi.helper.TransportagentGetListBapi;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.bapi.helper.structure.BatchStocksStructure;
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.goodsmvt.SOGetDetailBapi;
import com.gcs.wb.bapi.goodsmvt.structure.DOCheckStructure;
import com.gcs.wb.bapi.goodsmvt.structure.SOCheckStructure;
import com.gcs.wb.bapi.helper.BatchStocksGetListBapi;
import com.gcs.wb.bapi.helper.CustomerGetDetailBapi;
import com.gcs.wb.bapi.helper.MatGetDetailBapi;
import com.gcs.wb.bapi.helper.MaterialGetListBapi;
import com.gcs.wb.bapi.helper.PlantGetDetailBapi;
import com.gcs.wb.bapi.helper.PoGetDetailBapi;
import com.gcs.wb.bapi.helper.PoPostGetListBapi;
import com.gcs.wb.bapi.helper.SLocsGetListBapi;
import com.gcs.wb.bapi.helper.SyncContractSOGetListBapi;
import com.gcs.wb.bapi.helper.VendorGetDetailBapi;
import com.gcs.wb.bapi.helper.VendorValiationCheckBapi;
import com.gcs.wb.bapi.helper.constants.PlantGeDetailConstants;
import com.gcs.wb.bapi.helper.structure.CustomerGetDetailStructure;
import com.gcs.wb.bapi.helper.structure.MatGetDetailStructure;
import com.gcs.wb.bapi.helper.structure.MaterialGetListStructure;
import com.gcs.wb.bapi.helper.structure.PODataOuboundStructure;
import com.gcs.wb.bapi.helper.structure.SLocsGetListStructure;
import com.gcs.wb.bapi.helper.structure.SalesOrderStructure;
import com.gcs.wb.bapi.helper.structure.TransportagentGetListStructure;
import com.gcs.wb.bapi.helper.structure.VendorGetDetailStructure;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.converter.CustomerConverter;
import com.gcs.wb.base.converter.MaterialConverter;
import com.gcs.wb.base.converter.MaterialsV2Converter;
import com.gcs.wb.base.converter.OutboundDeliveryConverter;
import com.gcs.wb.base.converter.PurchaseOrderConverter;
import com.gcs.wb.base.converter.TransportAgentsConverter;
import com.gcs.wb.base.converter.VendorConverter;
import com.gcs.wb.base.util.StringUtil;
import com.gcs.wb.jpa.entity.BatchStock;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.MaterialInternal;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.PurchaseOrder;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.TransportAgentVehicle;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.repositorys.BatchStockRepository;
import com.gcs.wb.jpa.repositorys.MaterialInternalRepository;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.PurchaseOrderRepository;
import com.gcs.wb.jpa.repositorys.SAPSettingRepository;
import com.gcs.wb.jpa.repositorys.SLocRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentVehicleRepository;
import com.gcs.wb.jpa.repositorys.VendorRepository;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.LookupMaterialService;
import com.gcs.wb.views.TransportAgentView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.hibersap.session.Session;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author HANGTT
 */
public class SAPService {

    EntityManager entityManager = JPAConnector.getInstance();
    EntityTransaction entityTransaction = entityManager.getTransaction();

    LookupMaterialService lookupMaterialService = new LookupMaterialService();
    BatchStockRepository batchStockRepository = new BatchStockRepository();
    BatchStockRepository batchStocksRepository = new BatchStockRepository();
    VendorRepository vendorRepository = new VendorRepository();
    SLocRepository sLocRepository = new SLocRepository();
    TransportAgentRepository transportAgentRepository = new TransportAgentRepository();
    TransportAgentVehicleRepository transportAgentVehicleRepository = new TransportAgentVehicleRepository();
    MaterialInternalRepository materialInternalRepository = new MaterialInternalRepository();
    MaterialRepository materialRepository = new MaterialRepository();
    SAPSettingRepository sapSettingRepository = new SAPSettingRepository();
    PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();

    AppConfig config = WeighBridgeApp.getApplication().getConfig();
    Configuration configuration = config.getConfiguration();

    private JFrame mainFrame = WeighBridgeApp.getApplication().getMainFrame();
    public ResourceMap resourceMapMsg = Application.getInstance(WeighBridgeApp.class).getContext().getResourceMap(TransportAgentView.class);

    Session session = WeighBridgeApp.getApplication().getSAPSession();

    /**
     * get Material model
     */
    public DefaultComboBoxModel getMaterialModel() {
        //get data from DB
        List<MaterialInternal> materialsDB = materialInternalRepository.getMaterialInternals();

        if (!WeighBridgeApp.getApplication().isOfflineMode()) {
            // get data SAP
            List<MaterialInternal> matsSap = new ArrayList<>();
            MaterialGetListBapi bapi = new MaterialGetListBapi();
            try {
                session.execute(bapi);
                List<MaterialGetListStructure> mats = bapi.getEtMaterial();
                MaterialsV2Converter materialsV2Converter = new MaterialsV2Converter();
                matsSap = materialsV2Converter.convertMaster(mats);
            } catch (Exception ex) {
            }

            //sync DB SAP
            entityTransaction = entityManager.getTransaction();
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }
            //update for remove DB
            for (MaterialInternal mat : materialsDB) {
                if (matsSap.indexOf(mat) == -1) {
                    entityManager.remove(mat);
                }
            }
            // update SAP -> DB    
            for (MaterialInternal mSap : matsSap) {
                int index = materialsDB.indexOf(mSap);
                if (index == -1) {
                    entityManager.persist(mSap);
                } else {
                    mSap.setId(materialsDB.get(index).getId());
                    entityManager.merge(mSap);
                }
            }

            entityTransaction.commit();
            entityManager.clear();

            // return data
            materialsDB = materialInternalRepository.getMaterialInternals();
        }

        return new DefaultComboBoxModel(materialsDB.toArray());
    }

    public List<Material> syncMaterial() {
        //get data from DB
        List<Material> materialsDB = materialRepository.getListMaterial();

        if (!WeighBridgeApp.getApplication().isOfflineMode()) {
            // get data SAP
            List<Material> matsSap = new ArrayList<>();
            MaterialGetListBapi bapi = new MaterialGetListBapi();
            try {
                session.execute(bapi);
                List<MaterialGetListStructure> mats = bapi.getEtMaterial();
                MaterialsV2Converter materialsV2Converter = new MaterialsV2Converter();
                matsSap = materialsV2Converter.convert(mats);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //sync DB SAP
            entityTransaction = entityManager.getTransaction();
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }
            //update for remove DB
            for (Material mat : materialsDB) {
                if (matsSap.indexOf(mat) == -1) {
                    entityManager.remove(mat);
                }
            }
            // update SAP -> DB
            for (Material mSap : matsSap) {
                int index = materialsDB.indexOf(mSap);
                if (index == -1) {
                    entityManager.persist(mSap);
                } else {
                    mSap.setId(materialsDB.get(index).getId());
                    entityManager.merge(mSap);
                }
            }

            entityTransaction.commit();
            entityManager.clear();

            // return data
            return materialRepository.getListMaterial();
        }

        return materialsDB;
    }

    /**
     * get List Vendor
     */
    public DefaultComboBoxModel getVendorList() {
        return new DefaultComboBoxModel(syncVendor().toArray());
    }

    /**
     * sync Vendor
     *
     * @return
     */
    public List<Vendor> syncVendor() {
        List<Vendor> vendorDBs = vendorRepository.getListVendor();

        if (!WeighBridgeApp.getApplication().isOfflineMode()) {
            TransportagentGetListBapi bapi = new TransportagentGetListBapi();
            bapi.setIvEkorg(configuration.getWkPlant());
            List<Vendor> venSaps = new ArrayList<>();
            try {
                session.execute(bapi);

                List<TransportagentGetListStructure> etVendors = bapi.getEtVendor();

                for (TransportagentGetListStructure vens : etVendors) {
                    Vendor ven = new Vendor();
                    ven.setMandt(configuration.getSapClient());
                    ven.setLifnr(vens.getLifnr());
                    ven.setName1(vens.getName1());
                    ven.setName2(vens.getName2());
                    venSaps.add(ven);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            entityTransaction = entityManager.getTransaction();
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            // update remove DB
            for (Vendor ven : vendorDBs) {
                if (venSaps.indexOf(ven) == -1) {
                    entityManager.remove(ven);
                }
            }

            // update SAP - DB
            for (Vendor venSap : venSaps) {
                int index = vendorDBs.indexOf(venSap);
                if (index == -1) {
                    entityManager.persist(venSap);
                } else {
                    venSap.setId(vendorDBs.get(index).getId());
                    entityManager.merge(venSap);
                }
            }

            entityTransaction.commit();
            entityManager.clear();

            // return data
            return vendorRepository.getListVendor();
        }

        return vendorDBs;
    }

    /**
     * get Outbound Delivery follow DO number
     *
     * @param number
     * @param refresh
     * @return
     */
    public OutboundDelivery getOutboundDelivery(String number) throws Exception {
        DoGetDetailBapi bapiDO = new DoGetDetailBapi();
        bapiDO.setId_do(StringUtil.paddingZero(number, 10));
        session.execute(bapiDO);
        OutboundDeliveryConverter outboundDeliveryConverter = new OutboundDeliveryConverter();
        return outboundDeliveryConverter.convertHasParameter(bapiDO, number);
    }

    /**
     * get purchase order follow PO
     *
     * @param poNum
     * @return PurchaseOrder
     * @throws Exception
     */
    public PurchaseOrder getPurchaseOrder(String poNum) throws Exception {
        PoGetDetailBapi bPO = new PoGetDetailBapi();
        bPO.setPURCHASEORDER(poNum);
        session.execute(bPO);
        PurchaseOrderConverter purchaseOrderConverter = new PurchaseOrderConverter();
        return purchaseOrderConverter.convertHasParameter(bPO, poNum);

    }

    /**
     * sync purchase order from SAP to DB
     *
     * @param sapPurchaseOrder
     * @param purchaseOrder
     * @return PurchaseOrder
     */
    public PurchaseOrder syncPurchaseOrder(PurchaseOrder sapPurchaseOrder, PurchaseOrder purchaseOrder) {

        PurchaseOrder result = new PurchaseOrder();
        if (!entityTransaction.isActive()) {
            entityTransaction.begin();
        }

        try {
            if (sapPurchaseOrder != null && purchaseOrder == null) {
                entityManager.persist(sapPurchaseOrder);
                result = sapPurchaseOrder;
            } else if (sapPurchaseOrder != null && purchaseOrder != null) {
                sapPurchaseOrder.setId(purchaseOrder.getId());
                entityManager.merge(sapPurchaseOrder);
                result = sapPurchaseOrder;
            } else {
                if (purchaseOrder != null) {
                    entityManager.remove(purchaseOrder);
                }
                result = null;
            }

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            Logger.getLogger(SAPService.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        }

        return result;
    }

    /**
     * sync Batch Stocks
     *
     * @param lgortSloc
     * @param matnr
     */
    public void syncBatchStocks(String lgortSloc, String matnr) {
        entityTransaction = entityManager.getTransaction();
        // get data DB
        List<BatchStock> batchs = batchStockRepository.getListBatchStock(configuration.getWkPlant(), lgortSloc, matnr);
        // get data SAP
        BatchStocksGetListBapi bBatch = new BatchStocksGetListBapi();
        List<BatchStock> batchStockSaps = new ArrayList<>();
        bBatch.setIdMandt(configuration.getSapClient());
        bBatch.setIdWerks(configuration.getWkPlant());
        bBatch.setIdLgort(lgortSloc);
        bBatch.setIdMatnr(matnr);
        try {
            session.execute(bBatch);
            List<BatchStocksStructure> bBatchStocks = bBatch.getBatchStocks();
            for (BatchStocksStructure b : bBatchStocks) {
                //BatchStock bs = batchStockRepository.findByWerksLgortMatnrCharg(configuration.getWkPlant(), b.getLgort(), b.getMatnr(), b.getCharg());
                BatchStock bs = new BatchStock(configuration.getSapClient(), configuration.getWkPlant(), b.getLgort(), b.getMatnr(), b.getCharg());
                bs.setLvorm(b.getLvorm() == null || b.getLvorm().trim().isEmpty() ? ' ' : b.getLvorm().charAt(0));

                batchStockSaps.add(bs);
            }

            //sync data
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }
            //case delete
            for (BatchStock b : batchs) {
                if (batchStockSaps.indexOf(b) == -1) {
                    entityManager.remove(b);
                }
            }
            //case persit/merge
            for (BatchStock bs : batchStockSaps) {
                int index = batchs.indexOf(bs);
                if (index == -1) {
                    entityManager.persist(bs);
                } else {
                    bs.setId(batchs.get(index).getId());
                    entityManager.merge(bs);
                }
            }

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
        }
    }

    /**
     * get data Customer detail
     *
     * @param kunnr
     * @return
     */
    public Customer getCustomer(String kunnr) {
        CustomerGetDetailBapi bapiCust = new CustomerGetDetailBapi();
        bapiCust.setIdKunnr(kunnr);
        session.execute(bapiCust);
        CustomerGetDetailStructure strucCust = bapiCust.getEsKna1();
        CustomerConverter customerConverter = new CustomerConverter();
        return customerConverter.convert(strucCust);
    }

    /**
     * get data Vendor detail
     *
     * @param lifnr
     * @return
     */
    public Vendor getVendor(String lifnr) {
        VendorGetDetailBapi bapiCust = new VendorGetDetailBapi();
        bapiCust.setIdLifnr(lifnr);
        session.execute(bapiCust);
        VendorGetDetailStructure strucVendor = bapiCust.getEsLfa1();
        VendorConverter vendorConverter = new VendorConverter();
        return vendorConverter.convert(strucVendor);

    }

    public DefaultComboBoxModel getSlocModel() {
        return new DefaultComboBoxModel(syncSloc().toArray());
    }

    public List<SLoc> syncSloc() {
        List<SLoc> slocDBs = sLocRepository.getListSLoc();
        String mandt = configuration.getSapClient();
        String wplant = configuration.getWkPlant();
        if (!WeighBridgeApp.getApplication().isOfflineMode()) {
            // get data SAP
            SLocsGetListBapi bSloc = new SLocsGetListBapi(mandt, wplant);
            List<SLoc> slocSaps = new ArrayList<>();
            try {
                session.execute(bSloc);
                List<SLocsGetListStructure> tdSLocs = bSloc.getTdSLocs();
                for (SLocsGetListStructure s : tdSLocs) {
                    SLoc sloc = new SLoc(s.getLgort());
                    sloc.setLgobe(s.getLgobe());
                    slocSaps.add(sloc);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // sync data
            entityTransaction = entityManager.getTransaction();
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }
            // update case delete
            for (SLoc slocD : slocDBs) {
                if (slocSaps.indexOf(slocD) == -1) {
                    entityManager.remove(slocD);
                }
            }
            // update case persit/merge
            for (SLoc sloc : slocSaps) {
                sloc.setMandt(mandt);
                sloc.setWplant(wplant);
                int index = slocDBs.indexOf(sloc);
                if (index == -1) {
                    entityManager.persist(sloc);
                } else {
                    sloc.setId(slocDBs.get(index).getId());
                    entityManager.merge(sloc);
                }
            }

            entityTransaction.commit();
            entityManager.clear();
            return sLocRepository.getListSLoc();
        }

        return slocDBs;
    }

    public List<TransportAgent> getTransportAgentList() {
        List<TransportAgent> result = new ArrayList<>();
        // get data DB
        List<TransportAgent> transportDBs = transportAgentRepository.getListTransportAgent();
        if (!WeighBridgeApp.getApplication().isOfflineMode()) {
            // get data SAP
            TransportagentGetListBapi bapi = new TransportagentGetListBapi();
            bapi.setIvEkorg(configuration.getWkPlant());
            List<TransportAgent> transportSaps = new ArrayList<>();
            try {
                session.execute(bapi);
                List<TransportagentGetListStructure> transports = bapi.getEtVendor();
                TransportAgentsConverter transportAgentsConverter = new TransportAgentsConverter();
                transportSaps = transportAgentsConverter.convert(transports);
                //sync SAP <=> DB
                /**
                 * sync DVVC
                 *
                 * @param transportDBs
                 * @param transportSaps
                 */
                // delete data DB not exist SAP
                for (TransportAgent transportAgent : transportDBs) {
                    if (transportSaps.indexOf(transportAgent) == -1) {
                        // delete in table Vehicle
                        List<TransportAgentVehicle> transportAgentVehicles = transportAgentVehicleRepository.findByTransportAgentId(transportAgent.getId());

                        if (!entityTransaction.isActive()) {
                            entityTransaction.begin();
                        }

                        try {
                            for (TransportAgentVehicle transportAgentVehicle : transportAgentVehicles) {
                                entityManager.remove(transportAgentVehicle);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.deleteVehicleFalse"));
                            entityTransaction.rollback();
                            continue;
                        }

                        // delete dvvc
                        try {
                            if (!entityManager.contains(transportAgent)) {
                                transportAgent = entityManager.merge(transportAgent);
                            }
                            entityManager.remove(transportAgent);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.deleteProviderFalse"));
                            entityTransaction.rollback();
                            continue;
                        }

                        entityTransaction.commit();
                        entityManager.clear();
                    }
                }

                try {
                    if (!entityTransaction.isActive()) {
                        entityTransaction.begin();
                    }
                    // update dara SAP -> DB
                    for (TransportAgent transportAgentSAP : transportSaps) {
                        int index = transportDBs.indexOf(transportAgentSAP);
                        if (index == -1) {
                            entityManager.persist(transportAgentSAP);
                        } else {
                            transportAgentSAP.setId(transportDBs.get(index).getId());
                            entityManager.merge(transportAgentSAP);
                        }
                    }

                    entityTransaction.commit();
                    entityManager.clear();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.syncProviderFalse"));
                    if (entityTransaction.isActive()) {
                        entityTransaction.rollback();
                    }
                }
            } catch (Exception ex) {
                // to do
            }
        }
        // return data
        return transportAgentRepository.getListTransportAgent();
    }

    /**
     * get detail Material
     *
     * @param matnr
     * @return
     */
    public Material getMaterialDetail(String matnr) {
        Material result = null;
        MatGetDetailBapi bapi = new MatGetDetailBapi();
        bapi.setId_matnr(matnr);
        try {
            session.execute(bapi);
            MatGetDetailStructure bapiResult = bapi.getEs_makt();
            MaterialConverter materialConverter = new MaterialConverter();
            result = materialConverter.convert(bapiResult);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public OutboundDelivery syncOutboundDelivery(OutboundDelivery sapOutboundDelivery, OutboundDelivery outboundDelivery, String deliveryNum) {

        OutboundDelivery result;
        if (!entityTransaction.isActive()) {
            entityTransaction.begin();
        }

        try {
            if (sapOutboundDelivery != null && outboundDelivery == null) {
                entityManager.persist(sapOutboundDelivery);
                result = sapOutboundDelivery;
            } else if (sapOutboundDelivery != null && outboundDelivery != null) {
                sapOutboundDelivery.setId(outboundDelivery.getId());
                entityManager.merge(sapOutboundDelivery);
                result = sapOutboundDelivery;
            } else {
                if (outboundDelivery != null) {
                    entityManager.remove(outboundDelivery);
                }
                result = null;
            }

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            Logger.getLogger(SAPService.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        }

        return result;
    }

    public List<DOCheckStructure> getDONumber(String[] soNumbers, String bsXe, String soRomoc) {
        SOGetDetailBapi bapi = new SOGetDetailBapi();
        bapi.setWerks(configuration.getWkPlant());
        List<SOCheckStructure> soChecks = new ArrayList<SOCheckStructure>();
        SOCheckStructure soCheck;
        for (int k = 0; k < soNumbers.length; k++) {
            soCheck = new SOCheckStructure();
            soCheck.setVbeln(StringUtil.paddingZero(soNumbers[k].trim(), 10));
            soCheck.setTraid(bsXe);
            soChecks.add(soCheck);
        }

        //soCheck.setVbeln(soNumber);
        bapi.setSOCheck(soChecks);
        WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
        List<DOCheckStructure> dos = bapi.getDOCheck();
        if (dos != null) {
            return dos;
        }
        return null;
    }

    public String validateVendor(String idVendor, String mantr, String vendorType, String wplantPo) {
        VendorValiationCheckBapi bapi = new VendorValiationCheckBapi();
        bapi.setIvVendor(idVendor);
        bapi.setIvMatnr(mantr);
        bapi.setIvWerks(wplantPo);
        bapi.setIvReswk(configuration.getWkPlant());
        bapi.setIvKschl(vendorType);
        try {
            WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
            String bapiResult = bapi.getEvReturn();
            if (bapiResult != null) {
                return bapiResult;
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public SAPSetting syncSapSetting(String mandt, String wplant) {
        try {
            PlantGetDetailBapi plantGetDetailBapi = new PlantGetDetailBapi(configuration.getSapClient(), configuration.getWkPlant());
            session.execute(plantGetDetailBapi);
            HashMap vals = plantGetDetailBapi.getEsPlant();

            SAPSetting localSetting = sapSettingRepository.findByMandtAndWplant(mandt, wplant);

            if (vals.size() > 0) {
                SAPSetting sapSetting = new SAPSetting();
                sapSetting.setName1((String) vals.get(PlantGeDetailConstants.NAME1));
                sapSetting.setName2((String) vals.get(PlantGeDetailConstants.NAME2));
                sapSetting.setMandt(configuration.getSapClient());
                sapSetting.setWplant(configuration.getWkPlant());
                sapSetting.setCheckPov(true);

                if (!entityTransaction.isActive()) {
                    entityTransaction.begin();
                }

                if (localSetting == null) {
                    entityManager.persist(sapSetting);
                } else {
                    localSetting.setName1(sapSetting.getName1());
                    localSetting.setName2(sapSetting.getName2());
                    entityManager.merge(localSetting);

                    sapSetting = localSetting;
                }

                entityTransaction.commit();
                entityManager.clear();

                return sapSetting;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
        }

        return null;
    }

    public List<String> getListPoPostoNumber() {
        List<String> poPostoNumbers = new ArrayList<>();
        try {
            Date now = new Date();
            SimpleDateFormat formatTime = new SimpleDateFormat("hhmmss");

            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(now);
            calendarStart.add(Calendar.DATE, -1);

            PoPostGetListBapi poPostGetListBapi = new PoPostGetListBapi();
            poPostGetListBapi.setIvStartDate(calendarStart.getTime());
            poPostGetListBapi.setIvStartTime(Constants.SyncMasterData.TIME_SYNC);
            poPostGetListBapi.setIvEndDate(now);
            poPostGetListBapi.setIvEndTime(formatTime.format(now));
            session.execute(poPostGetListBapi);

            List<PODataOuboundStructure> listPO = poPostGetListBapi.getListPODataOubound();
            if (listPO != null) {
                poPostoNumbers = listPO.stream()
                        .flatMap(t -> {
                            if (t.getPoHeader() != null) {
                                return t.getPoHeader().stream().map(s -> s.getEbeln());
                            }

                            return null;
                        })
                        .filter(t -> t != null && !t.isEmpty())
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        } catch (Exception ex) {
            System.out.println("PoPostGetListBapi đang lỗi!");
        }

        return poPostoNumbers;
    }

    public void syncPoPostoDatas() {
        List<String> poPostoNumbers = getListPoPostoNumber();

        poPostoNumbers.stream().forEach(number -> {
            try {
                PurchaseOrder sapPurchaseOrder = getPurchaseOrder(number);
                PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPoNumber(number);
                syncPurchaseOrder(sapPurchaseOrder, purchaseOrder);
            } catch (Exception ex) {
                Logger.getLogger(SAPService.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public List<Object> syncListSalesOrder() {
        try {
            SyncContractSOGetListBapi syncContractSOGetListBapi = new SyncContractSOGetListBapi();
            Date dateF = null;
            Date dateT = new Date();
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, -3);
            dateF = calendar.getTime();

            syncContractSOGetListBapi.setIvDateF(dateF);
            syncContractSOGetListBapi.setIvDateT(dateT);
            syncContractSOGetListBapi.setIvOption("WB");
            session.execute(syncContractSOGetListBapi);

            List<SalesOrderStructure> listSO = syncContractSOGetListBapi.getListSalesOrder();

        } catch (Exception ex) {
            System.out.println("SyncContractSOGetListBapi đang lỗi!");
        }

        return null;
    }
}
