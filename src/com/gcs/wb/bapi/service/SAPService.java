/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.service;

import com.gcs.wb.bapi.helper.DoGetDetailBapi;
import com.gcs.wb.bapi.helper.MaterialGetListBapi;
import com.gcs.wb.bapi.helper.TransportagentGetListBapi;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.bapi.helper.structure.BatchStocksStructure;
import com.gcs.wb.bapi.helper.structure.DoGetDetailStructure;
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.BatchStocksGetListBapi;
import com.gcs.wb.bapi.helper.CustomerGetDetailBapi;
import com.gcs.wb.bapi.helper.MatGetDetailBapi;
import com.gcs.wb.bapi.helper.PoGetDetailBapi;
import com.gcs.wb.bapi.helper.SLocsGetListBapi;
import com.gcs.wb.bapi.helper.VendorGetDetailBapi;
import com.gcs.wb.bapi.helper.structure.CustomerGetDetailStructure;
import com.gcs.wb.bapi.helper.structure.MatGetDetailStructure;
import com.gcs.wb.bapi.helper.structure.MaterialGetListStructure;
import com.gcs.wb.bapi.helper.structure.PoGetDetailHeaderStructure;
import com.gcs.wb.bapi.helper.structure.PoGetDetailItemStructure;
import com.gcs.wb.bapi.helper.structure.SLocsGetListStructure;
import com.gcs.wb.bapi.helper.structure.TransportagentGetListStructure;
import com.gcs.wb.bapi.helper.structure.VendorGetDetailStructure;
import com.gcs.wb.base.converter.PurOrderConverter;
import com.gcs.wb.base.converter.TransportAgentsConverter;
import com.gcs.wb.base.util.StringUtil;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.BatchStock;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.OutboundDeliveryDetail;
import com.gcs.wb.jpa.entity.PurchaseOrder;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.repositorys.BatchStockRepository;
import com.gcs.wb.jpa.service.JPAService;
import com.gcs.wb.model.AppConfig;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.DefaultComboBoxModel;
import org.hibersap.session.Session;

/**
 *
 * @author HANGTT
 */
public class SAPService {
    BatchStockRepository batchStockRepository = new BatchStockRepository();
    EntityManager entityManager = JPAConnector.getInstance();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    JPAService jpaService = new JPAService();
    AppConfig config = WeighBridgeApp.getApplication().getConfig();
    //org.hibersap.session.Session session = WeighBridgeApp.getApplication().getSAPSession();
    Session session = WeighBridgeApp.getApplication().getSAPSession();

    /**
     * sync Material
     */
    public void syncMaterialMaster() {
        List<Material> result = new ArrayList<>();
        //get data from DB
        List<Material> materialsDB = new ArrayList<>();
        materialsDB = jpaService.getListMaterial();
        // get data SAP
        List<Material> matsSap = new ArrayList<>();
        MaterialGetListBapi bapi = new MaterialGetListBapi();
        try {
            session.execute(bapi);
            List<MaterialGetListStructure> mats = bapi.getEtMaterial();
            
            for (MaterialGetListStructure mat : mats) {
                if (config.getwPlant().toString().equalsIgnoreCase(mat.getWerks())) {
                    Material m = null;
                    m = new Material(config.getsClient(), mat.getWerks(), mat.getMatnr());
                    m.setMaktx(mat.getMaktx());
                    m.setMaktg(mat.getMaktxLong());
                    //m.setXchpf(mat.getXchpf() != null && mat.getXchpf().equalsIgnoreCase("X") ? 'X' : ' ');
                    matsSap.add(m);
                }
            }
        } catch (Exception ex) {
            return;
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
        for (Material mSap : result) {
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
    }

    /**
     * sync Vendor
     */
    public DefaultComboBoxModel getVendorList() {
        List<Vendor> vendorDBs = new ArrayList<>();
        vendorDBs = jpaService.getVendorList();

        TransportagentGetListBapi bapi = new TransportagentGetListBapi();
        bapi.setIvEkorg(config.getwPlant());
        List<Vendor> venSaps = new ArrayList<>();
        try {
            session.execute(bapi);

            List<TransportagentGetListStructure> etVendors = bapi.getEtVendor();

            for (TransportagentGetListStructure vens : etVendors) {
                Vendor ven = new Vendor();
                ven.setMandt(config.getsClient());
                ven.setLifnr(vens.getLifnr());
                ven.setName1(vens.getName1());
                ven.setName2(vens.getName2());
                venSaps.add(ven);
            }
        } catch (Exception ex) {
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
        vendorDBs = jpaService.getVendorList();
        
        return new DefaultComboBoxModel(vendorDBs.toArray());
    }

    /**
     * get Outbound Delivery follow DO number
     * @param number
     * @param refresh
     * @return 
     */
    public OutboundDelivery getOutboundDelivery(String number, boolean refresh) {
        OutboundDelivery outb = null;
        OutboundDeliveryDetail outb_details = null;
        String item_cat = "";
        String item_num = null;
        String item_num_free = null;
        boolean flag_free = true;
        boolean flag = true;
        boolean flag_detail = true;
        BigDecimal item_qty = BigDecimal.ZERO;
        BigDecimal item_qty_free = BigDecimal.ZERO;
        DoGetDetailBapi bapiDO = new DoGetDetailBapi();
        bapiDO.setId_do(StringUtil.paddingZero(number, 10));
        session.execute(bapiDO);
        List<DoGetDetailStructure> dos = bapiDO.getTd_dos();
        if (dos.size() > 0) {
            // <editor-fold defaultstate="collapsed" desc="Fill D.O Data">
            //check do detail exist
            entityTransaction = entityManager.getTransaction();
            WeightTicketJpaController con_check = new WeightTicketJpaController();
            List<OutboundDeliveryDetail> outb_detail_check;
            if (refresh == true) {
                try {
                    outb_detail_check = con_check.findByMandtDelivNumb(number);
                    if (outb_detail_check.size() > 0) {
                        entityTransaction.begin();
                        
                        for (int i = 0; i < outb_detail_check.size(); i++) {
                            entityManager.remove(outb_detail_check.get(i));
                        }
                        entityTransaction.commit();
                        entityManager.clear();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SAPService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //end check
            outb = new OutboundDelivery(number);
            outb.setShipPoint(bapiDO.getEs_vstel());
            for (int i = 0; i < dos.size(); i++) {
                DoGetDetailStructure doItem = dos.get(i);
                try {
                    outb_detail_check = con_check.findByMandtDelivNumbItem(number, doItem.getPosnr().substring(4, 5));
                    if (outb_detail_check.size() > 0) {
                        outb_details = outb_detail_check.get(0);
                    } else {
                        outb_details = new OutboundDeliveryDetail(number, doItem.getPosnr().substring(4, 5));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SAPService.class.getName()).log(Level.SEVERE, null, ex);
                }
                // free goods processing
                item_cat = doItem.getPstyv();
                //set DO number cho details
                if (flag_detail == true) {
                }
                //end set
                if (item_cat.equals("ZTNN")) {
                    outb.setDeliveryItemFree(doItem.getPosnr());
                    outb.setMatnrFree(doItem.getMatnr());
                    //set data cho details free goods
                    if (flag_detail == true) {
                        outb_details.setFreeItem('X');
                        outb_details.setLfimg(doItem.getLfimg());
                        outb_details.setMeins(doItem.getMeins());
                        String split[] = doItem.getArktx().split("-");
                        outb_details.setArktx(split[0].toString());
                        outb_details.setMatnr(doItem.getMatnr());
                        outb_details.setVgbel(doItem.getVgbel());
                        outb_details.setBzirk(bapiDO.getEs_bzirk());
                        outb_details.setBztxt(bapiDO.getEs_text());
                    }
                    //end set data
                    if (flag_free) {
                        item_num_free = doItem.getPosnr();
                        flag_free = false;
                    }
                    item_qty_free = item_qty_free.add(doItem.getLfimg());
//                    continue;
                }

                outb.setDeliveryItem(doItem.getPosnr()); //Get position
                //set data out details hang thuong
                if (flag_detail == true) {
                    outb_details.setLfimg(doItem.getLfimg());

                    if ((!outb_details.isPosted())
                            || (outb_details.getLfimg() == null)
                            || (outb_details.getLfimg().equals(BigDecimal.ZERO))) {
                        outb_details.setLfimg(doItem.getLfimg());
                    }
                    outb_details.setMeins(doItem.getMeins());
                    String split[] = doItem.getArktx().split("-");
                    outb_details.setArktx(split[0].toString());
                    outb_details.setMatnr(doItem.getMatnr());
                    outb_details.setVgbel(doItem.getVgbel());
                    outb_details.setBzirk(bapiDO.getEs_bzirk());
                    outb_details.setBztxt(bapiDO.getEs_text());
                }
                //end set data
                //save database
                if (flag_detail == true) {
                    entityTransaction = entityManager.getTransaction();
                    entityTransaction.begin();
                    entityManager.merge(outb_details);
                    entityTransaction.commit();
                    entityManager.clear();
                }
                //end
                //only get number item dong dau
                if (!item_cat.equals("ZTNN")) {
                    item_qty = item_qty.add(doItem.getLfimg());
                    if (flag) {
                        item_num = doItem.getPosnr();
                        flag = false;
                    }
                }

                outb.setErdat(new java.sql.Date(doItem.getErdat().getTime()));
                outb.setLfart(doItem.getLfart());

                outb.setWadat(new java.sql.Date(doItem.getWadat().getTime()));
                outb.setLddat(new java.sql.Date(doItem.getLddat().getTime()));
                outb.setKodat(new java.sql.Date(doItem.getKodat().getTime()));
                outb.setLifnr(doItem.getLifnr());
                outb.setKunnr(doItem.getKunnr());
                outb.setKunag(doItem.getKunag());
                outb.setTraty(doItem.getTraty());
                outb.setTraid(doItem.getTraid());

                outb.setBldat(new java.sql.Date(doItem.getBldat().getTime()));
                if(outb.getMatnr() == null || outb.getMatnr().trim().isEmpty()) {
                    outb.setMatnr(doItem.getMatnr());
                }
                outb.setWerks(doItem.getWerks());
                outb.setLgort(doItem.getLgort());
                outb.setCharg(doItem.getCharg());
                outb.setLichn(doItem.getLichn());
                outb.setMeins(doItem.getMeins());
                outb.setVrkme(doItem.getVrkme());
                outb.setUebtk(doItem.getUebtk() == null || doItem.getUebtk().trim().isEmpty() ? ' ' : 'X');
                outb.setUebto(doItem.getUebto());
                outb.setUntto(doItem.getUntto());
                outb.setArktx(doItem.getArktx());
                outb.setVgbel(doItem.getVgbel());
                outb.setVgpos(doItem.getVgpos());
                outb.setBwart(doItem.getBwart());
                outb.setBwtar(doItem.getBwtar());
                if (outb.getBwtar() == null || outb.getBwtar().trim().isEmpty()) {
                    outb.setBwtar("PURC");
                }
                outb.setRecvPlant(doItem.getRecv_plant());
                outb.setKoquk(doItem.getKoquk() == null || doItem.getKoquk().trim().isEmpty() || doItem.getKoquk().trim().charAt(0) != 'C' ? ' ' : 'X');
                outb.setKostk(doItem.getKostk() == null || doItem.getKostk().trim().isEmpty() || doItem.getKostk().trim().charAt(0) != 'C' ? ' ' : 'X');
                outb.setWbstk(doItem.getWbstk() == null || doItem.getWbstk().trim().isEmpty() || doItem.getWbstk().trim().charAt(0) != 'C' ? ' ' : 'X');
//            }
                if (item_cat.equals("ZTNN")) {
                    outb.setFreeQty(item_qty_free);
                }
                outb.setLfimg(item_qty);
            }
            //set lai item number thanh number dau tien

            if (outb.getDeliveryItem() != null) {
                if (!outb.getDeliveryItem().equals(item_num)) {
                    outb.setDeliveryItem(item_num);
                }
            }
            if (item_num_free != null) {

                if (!outb.getDeliveryItemFree().equals(item_num_free)) {
                    outb.setDeliveryItemFree(item_num_free);
                }
            }
            //th chi co hang free goods

            if (outb.getDeliveryItem() == null) {
                outb.setDeliveryItem(outb.getDeliveryItemFree());
                outb.setLfimg(outb.getFreeQty());
                outb.setDeliveryItemFree(null);
                outb.setFreeQty(null);
            }

        }
        return outb;
    }

    /**
     * get purchase order follow PO
     * @param poNum
     * @return
     * @throws Exception 
     */
    public PurchaseOrder getPurchaseOrder(String poNum) throws Exception {
        PoGetDetailBapi bPO = new PoGetDetailBapi();
        bPO.setPURCHASEORDER(poNum);
        session.execute(bPO);
        PurOrderConverter purOrderConverter = new PurOrderConverter();
        return purOrderConverter.convertHasParameter(bPO, poNum);
    }

    /**
     * sync Batch Stocks
     * @param lgortSloc
     * @param matnr
     * @param lgortWT 
     */
    public void syncBatchStocks(String lgortSloc, String matnr, String lgortWT) {
        // get data DB
        List<BatchStock> batchs = jpaService.getBatchStocks(config.getwPlant(), lgortSloc, matnr);
        // get data SAP
        BatchStocksGetListBapi bBatch = new BatchStocksGetListBapi();
        List<BatchStock> batchStockSaps = new ArrayList<>();
        bBatch.setIdMandt(config.getsClient());
        bBatch.setIdWerks(config.getwPlant());
        bBatch.setIdLgort(lgortSloc);
        bBatch.setIdMatnr(matnr);
        try {
            session.execute(bBatch);
            List<BatchStocksStructure> bBatchStocks = bBatch.getBatchStocks();
            for (BatchStocksStructure b : bBatchStocks) {
                BatchStock bs = batchStockRepository.findByWerksLgortMatnrCharg(config.getwPlant(), b.getLgort(), b.getMatnr(), b.getCharg());
                bs.setLvorm(b.getLvorm() == null || b.getLvorm().trim().isEmpty() ? ' ' : b.getLvorm().charAt(0));
                batchStockSaps.add(bs);
            }
        } catch (Exception ex) {
        }
        //sync data
        entityTransaction = entityManager.getTransaction();
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
    }

    /**
     * get data Customer detail
     * @param kunnr
     * @return 
     */
    public Customer getCustomer(String kunnr) {
        Customer result = null;
        CustomerGetDetailBapi bapiCust = new CustomerGetDetailBapi();
        bapiCust.setIdKunnr(kunnr);
        session.execute(bapiCust);
        CustomerGetDetailStructure strucCust = bapiCust.getEsKna1();
        if (strucCust != null && (strucCust.getKunnr() != null && !strucCust.getKunnr().trim().isEmpty())) {
            result = new Customer();
            result.setMandt(strucCust.getMandt());
            result.setKunnr(strucCust.getKunnr());
            result.setName1(strucCust.getName1());
            result.setName2(strucCust.getName2());
        }
        return result;
    }
    
    /**
     * get data Vendor detail
     * @param lifnr
     * @return 
     */
    public Vendor getVendor(String lifnr) {
        Vendor result = null;
        VendorGetDetailBapi bapiCust = new VendorGetDetailBapi();
        bapiCust.setIdLifnr(lifnr);
        session.execute(bapiCust);
        VendorGetDetailStructure strucVendor = bapiCust.getEsLfa1();
        if (strucVendor != null && (strucVendor.getLifnr() != null && !strucVendor.getLifnr().trim().isEmpty())) {
            result = new Vendor();
            result.setMandt(strucVendor.getMandt());
            result.setLifnr(strucVendor.getLifnr().trim());
            result.setName1(strucVendor.getName1());
            result.setName2(strucVendor.getName2());
        }
        return result;
    }
    
    public DefaultComboBoxModel getSlocModel() {
        // get data DB
        List<SLoc> slocDBs = jpaService.getSlocList();
        String mandt = config.getsClient();
        String wplant = config.getwPlant();
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
            int index = slocDBs.indexOf(sloc);
            sloc.setMandt(mandt);
            sloc.setWplant(wplant);
            if (index == -1) {
                entityManager.persist(sloc);
            } else {
                sloc.setId(slocDBs.get(index).getId());
                entityManager.merge(sloc);
            }
        }
        
        entityTransaction.commit();
        entityManager.clear();
        // set data
        slocDBs = jpaService.getSlocList();
        if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
            return new DefaultComboBoxModel(slocDBs.toArray());
        } else {

            //filter sloc theo user
            String[] sloc1 = WeighBridgeApp.getApplication().getSloc().split("-");
            List<SLoc> result = new ArrayList<>();
            if (sloc1.length > 0) {
                SLoc item = null;

                for (int i = 0; i < slocDBs.size(); i++) {
                    item = slocDBs.get(i);
                    for (int j = 0; j < sloc1.length; j++) {
                        if (item.getLgort().equals(sloc1[j])) {
                            result.add(item);
                        }
                    }
                }
            }

            return new DefaultComboBoxModel(result.toArray());
        }
    }
    
    public List<TransportAgent> getTransportAgentList() {
        List<TransportAgent> result = new ArrayList<>();
        // get data DB
        List<TransportAgent> transportDBs = new ArrayList<>();
        transportDBs = jpaService.getTransportAgent();
        // get data SAP
        TransportagentGetListBapi bapi = new TransportagentGetListBapi();
        bapi.setIvEkorg(config.getwPlant());
        List<TransportAgent> transportSaps = new ArrayList<>();
        try {
            session.execute(bapi);
            List<TransportagentGetListStructure> transports = bapi.getEtVendor();
            TransportAgentsConverter transportAgentsConverter = new TransportAgentsConverter();
            transportSaps = transportAgentsConverter.convert(transports);
            //sync SAP <=> DB
            jpaService.syncTransportAgent(transportDBs, transportSaps);
        } catch (Exception ex) {
            // to do
        }
        // end get data SAP


        // return data
        result = jpaService.getTransportAgent();

        return result;
    }
    
    /**
     * get detail Material
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
            if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                result = new Material(bapiResult.getMandt(), config.getwPlant(), bapiResult.getMatnr());
            } else {
                result = new Material(bapiResult.getMandt(), bapiResult.getMatnr());
            }
            result.setMaktx(bapiResult.getMaktx());
            result.setMaktg(bapiResult.getMaktg());
            result.setXchpf(bapiResult.getXchpf() != null && bapiResult.getXchpf().equalsIgnoreCase("X") ? 'X' : ' ');
        } catch (Exception e) {
            return null;
        }
        return result;
    }
}
