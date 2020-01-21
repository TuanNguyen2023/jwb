/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.BatchStocksStructure;
import com.gcs.wb.bapi.helper.structure.DoGetDetailStructure;
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.CustomerGetDetailStructure;
import com.gcs.wb.bapi.helper.structure.MatGetDetailStructure;
import com.gcs.wb.bapi.helper.structure.MatLookupStructure;
import com.gcs.wb.bapi.helper.structure.MaterialGetListStructure;
import com.gcs.wb.bapi.helper.structure.SLocsGetListStructure;
import com.gcs.wb.bapi.helper.structure.TransportagentGetListStructure;
import com.gcs.wb.bapi.helper.structure.VendorGetDetailStructure;
import com.gcs.wb.base.converter.CustomerConverter;
import com.gcs.wb.base.converter.MaterialConverter;
import com.gcs.wb.base.converter.MaterialsV1Converter;
import com.gcs.wb.base.converter.MaterialsV2Converter;
import com.gcs.wb.base.converter.OutboundDeliveryConverter;
import com.gcs.wb.base.converter.PurOrderConverter;
import com.gcs.wb.base.converter.TransportAgentsConverter;
import com.gcs.wb.base.converter.VendorConverter;
import com.gcs.wb.base.converter.VendorsConverter;
import com.gcs.wb.base.util.StringUtil;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.PurchaseOrder;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.model.AppConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.DefaultComboBoxModel;

/** 
 *
 * @author vunguyent
 */
public class SAP2Local {

    public static OutboundDelivery getOutboundDelivery(String number, boolean refresh) {
        OutboundDelivery outb = null;
        DoGetDetailBapi bapiDO = new DoGetDetailBapi();
        bapiDO.setId_do(StringUtil.paddingZero(number, 10));
        WeighBridgeApp.getApplication().getSAPSession().execute(bapiDO);
        List<DoGetDetailStructure> dos = bapiDO.getTd_dos();
        OutboundDeliveryConverter outbDelConverter = new OutboundDeliveryConverter();
        try {
            outb = outbDelConverter.convertsHasParameter(bapiDO, number, refresh);
        } catch (Exception ex) {
            Logger.getLogger(SAP2Local.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return outb;
    }

    public static PurchaseOrder getPurchaseOrder(String poNum) throws Exception {
        PurchaseOrder result = null;
        org.hibersap.session.Session session = WeighBridgeApp.getApplication().getSAPSession();
        PoGetDetailBapi bPO = new PoGetDetailBapi();
        bPO.setPURCHASEORDER(poNum);
        session.execute(bPO);
        PurOrderConverter purOrderConverter = new PurOrderConverter();
        result = purOrderConverter.convertHasParameter(bPO, poNum);

        return result;
    }

    public static List<BatchStocksStructure> getBatchStocks(String lgort, String matnr) {
        org.hibersap.session.Session session = WeighBridgeApp.getApplication().getSAPSession();
        AppConfig config = WeighBridgeApp.getApplication().getConfig();
        BatchStocksGetListBapi bBatch = new BatchStocksGetListBapi();
        bBatch.setIdMandt(config.getsClient());
        bBatch.setIdWerks(config.getwPlant());
        bBatch.setIdLgort(lgort);
        bBatch.setIdMatnr(matnr);
        try {
            session.execute(bBatch);
        } catch (Exception ex) {
        }
        List<BatchStocksStructure> bBatchStocks = bBatch.getBatchStocks();
        return bBatchStocks;
    }

    public static Customer getCustomer(String kunnr) {
        CustomerGetDetailBapi bapiCust = new CustomerGetDetailBapi();
        bapiCust.setIdKunnr(kunnr);
        WeighBridgeApp.getApplication().getSAPSession().execute(bapiCust);
        CustomerGetDetailStructure strucCust = bapiCust.getEsKna1();
        CustomerConverter customerConverter = new CustomerConverter();
        return customerConverter.convert(strucCust);
    }

    public static Vendor getVendor(String lifnr) {
        VendorGetDetailBapi bapiCust = new VendorGetDetailBapi();
        bapiCust.setIdLifnr(lifnr);
        WeighBridgeApp.getApplication().getSAPSession().execute(bapiCust);
        VendorGetDetailStructure strucVendor = bapiCust.getEsLfa1();
        VendorConverter vendorConverter = new VendorConverter();
        return vendorConverter.convert(strucVendor);
    }

    public static DefaultComboBoxModel getSlocModel(AppConfig config, EntityManager entityManager) {
        config = WeighBridgeApp.getApplication().getConfig();
        TypedQuery<SLoc> tSlocQ = entityManager.createNamedQuery("SLoc.findByMandtWPlant", SLoc.class);
        tSlocQ.setParameter("mandt", config.getsClient());
        tSlocQ.setParameter("wPlant", config.getwPlant());
        List<SLoc> slocs = tSlocQ.getResultList();
        SLocsGetListBapi bSloc = new SLocsGetListBapi(config.getsClient(), config.getwPlant());
        try {
            WeighBridgeApp.getApplication().getSAPSession().execute(bSloc);

            List<SLocsGetListStructure> bSlocs = bSloc.getTdSLocs();
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            for (SLocsGetListStructure s : bSlocs) {
                SLoc sloc = new SLoc(s.getLgort());
                sloc.setLgobe(s.getLgobe());
                if (slocs.indexOf(sloc) == -1) {
                    entityManager.persist(sloc);
                    slocs.add(sloc);
                } else {
                    entityManager.merge(sloc);
                }
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex) {
            //Logger.getLogger(SAP2Local.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
            return new DefaultComboBoxModel(slocs.toArray());
        } else {
            //filter sloc theo user
            String[] sloc1 = WeighBridgeApp.getApplication().getSloc().split("-");
            List<SLoc> result = new ArrayList<SLoc>();
            if (sloc1.length > 0) {
                SLoc item = null;

                for (int i = 0; i < slocs.size(); i++) {
                    item = slocs.get(i);
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

    public static List<Material> lookupMaterialsByDesc(String desc) {
        AppConfig config = WeighBridgeApp.getApplication().getConfig();
        MatLookupBapi bapi = new MatLookupBapi();
        bapi.setPlant(config.getwPlant());
        bapi.setSdesc(desc);
        WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
        List<MatLookupStructure> mats = bapi.getListMats();
        MaterialsV1Converter materialsV1Converter = new MaterialsV1Converter();

        return materialsV1Converter.convert(mats);
    }
    
//    public static List<Material> getLookUpMaterialsList(String wplant) {
//        List<Material> result = new ArrayList<Material>();
//        AppConfig config = WeighBridgeApp.getApplication().getConfig();
//        MatLookupBapi bapi = new MatLookupBapi();
//        bapi.setPlant(wplant);
//        WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
//        List<MatLookupStructure> mats = bapi.getListMats();
//        for (MatLookupStructure mat : mats) {
//            Material m = null;
//            m = new Material(config.getsClient(), mat.getMaterial());
//            if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
//                m = new Material(config.getsClient(), config.getwPlant(), mat.getMaterial());
//            } else {
//                m = new Material(config.getsClient(), mat.getMaterial());
//            }
//            //m.setWplant(wplant);
//            m.setMaktx(mat.getDesc());
//            m.setMaktg(m.getMaktx().toUpperCase());
//            m.setXchpf(mat.getXchpf() != null && mat.getXchpf().equalsIgnoreCase("X") ? 'X' : ' ');
//            result.add(m);
//        }
//        return result;
//    }
    
        public static List<Material> getMaterialsList() {
        List<Material> result = new ArrayList<Material>();

        MaterialGetListBapi bapi = new MaterialGetListBapi();
        try {
            WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
            List<MaterialGetListStructure> mats = bapi.getEtMaterial();
            MaterialsV2Converter materialsV2Converter = new MaterialsV2Converter();
            result = materialsV2Converter.convert(mats);

        } catch (Exception ex) {
        }

        return result;

    }
    
    public static List<TransportAgent> getTransportAgentList(String wplant) {
        List<TransportAgent> result = new ArrayList<TransportAgent>();
        TransportagentGetListBapi bapi = new TransportagentGetListBapi();
        bapi.setIvEkorg(wplant);
        try {
            WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
            List<TransportagentGetListStructure> transports = bapi.getEtVendor();
            TransportAgentsConverter transportAgentsConverter = new TransportAgentsConverter();
            result = transportAgentsConverter.convert(transports);
        } catch (Exception ex) {
            Logger.getLogger(SAP2Local.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    // Get data for Vendor van chuyen/boc xep
    public static List<Vendor> getVendorList(String wplant) {
        List<Vendor> result = new ArrayList<Vendor>();
        TransportagentGetListBapi bapi = new TransportagentGetListBapi();
        bapi.setIvEkorg(wplant);
        try {
            WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
            List<TransportagentGetListStructure> vendors = bapi.getEtVendor();
            VendorsConverter vendorsConverter = new VendorsConverter();
            vendorsConverter.convertHasParameter(vendors, wplant);
        } catch (Exception ex) {
            Logger.getLogger(SAP2Local.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

        // Get data for Vendor van chuyen/boc xep
    public static DefaultComboBoxModel getVendorList(AppConfig config, EntityManager entityManager) {
        config = WeighBridgeApp.getApplication().getConfig();
        
        TypedQuery<Vendor> tVendor = entityManager.createNamedQuery("Vendor.findByMandt", Vendor.class);
            tVendor.setParameter("mandt", config.getsClient());
        List<Vendor> vendors = tVendor.getResultList();
        
        TransportagentGetListBapi bapi = new TransportagentGetListBapi();
        bapi.setIvEkorg(config.getwPlant());
        try {
            WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
            
            List<TransportagentGetListStructure> venSaps = bapi.getEtVendor();
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            for (TransportagentGetListStructure venSap : venSaps) {
                Vendor ven = new Vendor();
                ven.setMandt(config.getsClient());
                ven.setLifnr(venSap.getLifnr());
                ven.setName1(venSap.getName1());
                ven.setName2(venSap.getName2());
                 if(vendors.indexOf(ven)==-1) {
                     entityManager.persist(ven);
                     vendors.add(ven);
                 } else {
                     entityManager.merge(ven);
                 }
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception ex) {  
        }
        
         return new DefaultComboBoxModel(vendors.toArray());
        }
    
    public static Material getMaterialDetail(String matnr) {
        MatGetDetailBapi bapi = new MatGetDetailBapi();
        bapi.setId_matnr(matnr);
        WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
        MatGetDetailStructure bapiResult = bapi.getEs_makt();
        MaterialConverter materialConverter  = new MaterialConverter();
        
        return materialConverter.convert(bapiResult);
    }
}
