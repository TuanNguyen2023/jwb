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
import com.gcs.wb.bapi.helper.structure.PoGetDetailHeaderStructure;
import com.gcs.wb.bapi.helper.structure.PoGetDetailItemStructure;
import com.gcs.wb.bapi.helper.structure.SLocsGetListStructure;
import com.gcs.wb.bapi.helper.structure.TransportagentGetListStructure;
import com.gcs.wb.bapi.helper.structure.VendorGetDetailStructure;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.CustomerPK;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.OutbDel;
import com.gcs.wb.jpa.entity.OutbDelPK;
import com.gcs.wb.jpa.entity.OutbDetailsV2;
import com.gcs.wb.jpa.entity.OutbDetailsV2PK;
import com.gcs.wb.jpa.entity.PurOrder;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.entity.VendorPK;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.utils.Conversion_Exit;
import java.math.BigDecimal;
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

    public static OutbDel getOutboundDelivery(String number, boolean refresh) {
        OutbDel outb = null;
        OutbDetailsV2 outb_details = null;
        String item_cat = "";
        String item_num = null;
        String item_num_free = null;
        boolean flag_free = true;
        boolean flag = true;
        boolean flag_detail = true;
        BigDecimal item_qty = BigDecimal.ZERO;
        BigDecimal item_qty_free = BigDecimal.ZERO;
        DoGetDetailBapi bapiDO = new DoGetDetailBapi();
        bapiDO.setId_do(Conversion_Exit.Conv_output_num(number, 10));
        WeighBridgeApp.getApplication().getSAPSession().execute(bapiDO);
        List<DoGetDetailStructure> dos = bapiDO.getTd_dos();
        if (dos.size() > 0) {
            // <editor-fold defaultstate="collapsed" desc="Fill D.O Data">
            //check do detail exist
            EntityManager em_check = WeighBridgeApp.getApplication().getEm();
            WeightTicketJpaController con_check = new WeightTicketJpaController();
            List<OutbDetailsV2> outb_detail_check;
            if (refresh == true) {
                try {
                    outb_detail_check = con_check.findByMandtDelivNumb(number);
                    if (outb_detail_check.size() > 0) {
//                    flag_detail = false; //hauld removed 20120713#01
                        //EntityManager em = WeighBridgeApp.getApplication().getEm();
                        em_check.getTransaction().begin();
                        //em_check.persist(outb_details);
                        for (int i = 0; i < outb_detail_check.size(); i++) {
                            em_check.remove(outb_detail_check.get(i));
                        }
                        em_check.getTransaction().commit();
                        em_check.clear();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SAP2Local.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
//            System.out.println(flag_detail);
            //end check
            outb = new OutbDel(new OutbDelPK(WeighBridgeApp.getApplication().getConfig().getsClient(), number));
            outb.setShipPoint(bapiDO.getEs_vstel()); //set shipping point 20120712#01
            for (int i = 0; i < dos.size(); i++) {
                DoGetDetailStructure doItem = dos.get(i);
                try {
                    outb_detail_check = con_check.findByMandtDelivNumbItem(number, doItem.getPosnr().substring(4, 5));
                    if (outb_detail_check.size() > 0) {
                        //outb_details = new OutbDetailsV2();
                        outb_details = outb_detail_check.get(0);
                    } else {
                        outb_details = new OutbDetailsV2(new OutbDetailsV2PK(WeighBridgeApp.getApplication().getConfig().getsClient(), number, doItem.getPosnr().substring(4, 5)));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SAP2Local.class.getName()).log(Level.SEVERE, null, ex);
                }
                //{-20100212#01 free goods processing
                item_cat = doItem.getPstyv();
                //set DO number cho details
                if (flag_detail == true) {
                    // outb_details.setDelivNumb(number);
                }
                //end set
                if (item_cat.equals("ZTNN")) {
                    outb.setDelivItemFree(doItem.getPosnr());
                    outb.setMatnrFree(doItem.getMatnr());
                    //set data cho details free goods
                    if (flag_detail == true) {
                        // outb_details.setDelivItem(doItem.getPosnr().substring(4, 5));
                        outb_details.setFreeItem("X");
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
//                    outb.setFreeQty(doItem.getLfimg());
                    item_qty_free = item_qty_free.add(doItem.getLfimg());
//                    continue;
                }
                //}-20100212#01 free goods processing

                outb.setDelivItem(doItem.getPosnr()); //Get position
                //set data out details hang thuong
                if (flag_detail == true) {
                    // outb_details.setDelivItem(doItem.getPosnr().substring(4, 5));
                    outb_details.setLfimg(doItem.getLfimg());

                    if ((outb_details.getPosted() == null)
                            || (!outb_details.getPosted().trim().equals("1"))
                            || (outb_details.getLfimgOri() == null)
                            || (outb_details.getLfimgOri().equals(BigDecimal.ZERO))) {
                        outb_details.setLfimgOri(doItem.getLfimg());
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
                //luu xuong database
                if (flag_detail == true) {
                    EntityManager em = WeighBridgeApp.getApplication().getEm();
                    em.getTransaction().begin();
                    //em.persist(outb_details);
                    em.merge(outb_details);
                    em.getTransaction().commit();
                    em.clear();
                }
                //end
                //chi lay number item dong dau
                if (!item_cat.equals("ZTNN")) {
                    item_qty = item_qty.add(doItem.getLfimg());
                    if (flag) {
                        item_num = doItem.getPosnr();
                        flag = false;
                    }
                }
                //{-20100212#01 comment out
                //Get free goods items
                /*
                if (i == 1) {
                outb.setDelivItemFree(doItem.getPosnr());
                outb.setFreeQty(doItem.getLfimg());
                }*/
                //{-20100212#01 comment out

                outb.setErdat(doItem.getErdat());
                outb.setLfart(doItem.getLfart());
                //autlf
                outb.setWadat(doItem.getWadat());
                outb.setLddat(doItem.getLddat());
                outb.setKodat(doItem.getKodat());
                //outb.setShipPoint(doItem.getVstel());
                outb.setLifnr(doItem.getLifnr());
                outb.setKunnr(doItem.getKunnr());
                outb.setKunag(doItem.getKunag());
                outb.setTraty(doItem.getTraty());
                outb.setTraid(doItem.getTraid());
                outb.setBldat(doItem.getBldat());
                if(outb.getMatnr() == null || outb.getMatnr().trim().isEmpty()) {
                    outb.setMatnr(doItem.getMatnr());
                }
                outb.setWerks(doItem.getWerks());
                outb.setLgort(doItem.getLgort());
                outb.setCharg(doItem.getCharg());
                outb.setLichn(doItem.getLichn());
//                outb.setLfimg(doItem.getLfimg());
//                item_qty = item_qty.add(doItem.getLfimg());
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
//                    outb.setDelivItemFree(doItem.getPosnr());
                    outb.setFreeQty(item_qty_free);
                }
                outb.setLfimg(item_qty);
            }
            //set lai item number thanh number dau tien
            if (outb.getDelivItem() != null) {
                if (!outb.getDelivItem().equals(item_num)) {
                    outb.setDelivItem(item_num);
                }
            }
            if (item_num_free != null) {
                if (!outb.getDelivItemFree().equals(item_num_free)) {
                    outb.setDelivItemFree(item_num_free);
                }
            }
            //th chi co hang free goods
            if (outb.getDelivItem() == null) {
                outb.setDelivItem(outb.getDelivItemFree());
                outb.setLfimg(outb.getFreeQty());
                outb.setDelivItemFree(null);
                outb.setFreeQty(null);
            }

        }
        return outb;
    }

    public static PurOrder getPurchaseOrder(String poNum) throws Exception {
        String item_num = null;
        String item_num_free = null;
        boolean flag_free = true;
        boolean flag = true;
        BigDecimal item_qty = BigDecimal.ZERO;
        BigDecimal item_qty_free = BigDecimal.ZERO;
        PurOrder result = null;
        org.hibersap.session.Session session = WeighBridgeApp.getApplication().getSAPSession();
        PoGetDetailBapi bPO = new PoGetDetailBapi();
        bPO.setPURCHASEORDER(poNum);
        session.execute(bPO);
        PoGetDetailHeaderStructure header = bPO.getPoHeader();
        List<PoGetDetailItemStructure> items = bPO.getPoItems();
        if (items.size() == 2 && !items.get(0).getMATERIAL().equalsIgnoreCase(items.get(1).getMATERIAL())) {
            throw new Exception("Không hỗ trợ P.O số: " + poNum + "!");
        }

        result = new PurOrder(WeighBridgeApp.getApplication().getConfig().getsClient(), poNum);
        result.setDocType(header.getDOC_TYPE());
        result.setDeleteInd(header.getDELETE_IND() == null || header.getDELETE_IND().trim().isEmpty() ? ' ' : header.getDELETE_IND().charAt(0));
        result.setStatus(header.getSTATUS() == null || header.getSTATUS().trim().isEmpty() ? ' ' : header.getSTATUS().charAt(0));
        result.setCreatDate(header.getCREAT_DATE());
        result.setVendor(header.getVENDOR());
        result.setSupplVend(header.getSUPPL_VEND());
        result.setCustomer(header.getCUSTOMER());
        result.setSupplPlnt(header.getSUPPL_PLNT());
        result.setPoRelInd(header.getPO_REL_IND() == null || header.getPO_REL_IND().trim().isEmpty() ? ' ' : header.getPO_REL_IND().charAt(0));
        result.setRelStatus(header.getREL_STATUS());
        for (int i = 0; i < items.size(); i++) {
            PoGetDetailItemStructure item = items.get(i);
            if (item.getFREE_ITEM() == null || item.getFREE_ITEM().trim().isEmpty()) {
//                result.setPoItem(item.getPO_ITEM());
                if (flag == true) {
                    item_num = item.getPO_ITEM();
                    flag = false;
                }
                result.setIDeleteInd(item.getDELETE_IND() == null || item.getDELETE_IND().trim().isEmpty() ? ' ' : item.getDELETE_IND().charAt(0));
                result.setShortText(item.getSHORT_TEXT());
                result.setMaterial(item.getMATERIAL());
                result.setPlant(item.getPLANT());
                result.setStgeLoc(item.getSTGE_LOC());
                result.setVendMat(item.getVEND_MAT());
//                result.setQuantity(item.getQUANTITY());
                item_qty = item_qty.add(item.getQUANTITY());
                result.setPoUnit(item.getPO_UNIT());
                result.setPoUnitIso(item.getPO_UNIT_ISO());
                result.setQualInsp(item.getQUAL_INSP() == null || item.getQUAL_INSP().trim().isEmpty() ? ' ' : item.getQUAL_INSP().charAt(0));
                result.setOverDlvTol(item.getOVER_DLV_TOL());
                result.setUnlimitedDlv(item.getUNLIMITED_DLV() == null || item.getUNLIMITED_DLV().trim().isEmpty() ? ' ' : item.getUNLIMITED_DLV().charAt(0));
                result.setValType(item.getVAL_TYPE());
                result.setNoMoreGr(item.getNO_MORE_GR() == null || item.getNO_MORE_GR().trim().isEmpty() ? ' ' : item.getNO_MORE_GR().charAt(0));
                result.setFinalInv(item.getFINAL_INV() == null || item.getFINAL_INV().trim().isEmpty() ? ' ' : item.getFINAL_INV().charAt(0));
                result.setItemCat(item.getITEM_CAT() == null || item.getITEM_CAT().trim().isEmpty() ? ' ' : item.getITEM_CAT().charAt(0));
                result.setGrInd(item.getGR_IND() == null || item.getGR_IND().trim().isEmpty() ? ' ' : item.getGR_IND().charAt(0));
                result.setGrNonVal(item.getGR_NON_VAL() == null || item.getGR_NON_VAL().trim().isEmpty() ? ' ' : item.getGR_NON_VAL().charAt(0));
                result.setDelivCompl(item.getDELIV_COMPL() == null || item.getDELIV_COMPL().trim().isEmpty() ? ' ' : item.getDELIV_COMPL().charAt(0));
                result.setPartDeliv(item.getPART_DELIV() == null || item.getPART_DELIV().trim().isEmpty() ? ' ' : item.getPART_DELIV().charAt(0));
            } else {
//                result.setPoItemFree(item.getPO_ITEM());
                if (flag_free == true) {
                    item_num_free = item.getPO_ITEM();
                    flag_free = false;
                }
                result.setIfDeleteInd(item.getDELETE_IND() == null || item.getDELETE_IND().trim().isEmpty() ? ' ' : item.getDELETE_IND().charAt(0));
//                result.setQuantityFree(item.getQUANTITY());
                item_qty_free = item_qty_free.add(item.getQUANTITY());
                result.setItemFreeCat(item.getITEM_CAT() == null || item.getITEM_CAT().trim().isEmpty() ? ' ' : item.getITEM_CAT().charAt(0));
                result.setShortText(item.getSHORT_TEXT());
                result.setMaterial(item.getMATERIAL());
                result.setPlant(item.getPLANT());
                result.setStgeLoc(item.getSTGE_LOC());
                result.setVendMat(item.getVEND_MAT());
                result.setPoUnit(item.getPO_UNIT());
                result.setPoUnitIso(item.getPO_UNIT_ISO());
                result.setQualInsp(item.getQUAL_INSP() == null || item.getQUAL_INSP().trim().isEmpty() ? ' ' : item.getQUAL_INSP().charAt(0));
                result.setOverDlvTol(item.getOVER_DLV_TOL());
                result.setUnlimitedDlv(item.getUNLIMITED_DLV() == null || item.getUNLIMITED_DLV().trim().isEmpty() ? ' ' : item.getUNLIMITED_DLV().charAt(0));
                result.setValType(item.getVAL_TYPE());
                result.setNoMoreGr(item.getNO_MORE_GR() == null || item.getNO_MORE_GR().trim().isEmpty() ? ' ' : item.getNO_MORE_GR().charAt(0));
                result.setFinalInv(item.getFINAL_INV() == null || item.getFINAL_INV().trim().isEmpty() ? ' ' : item.getFINAL_INV().charAt(0));
                result.setGrInd(item.getGR_IND() == null || item.getGR_IND().trim().isEmpty() ? ' ' : item.getGR_IND().charAt(0));
                result.setGrNonVal(item.getGR_NON_VAL() == null || item.getGR_NON_VAL().trim().isEmpty() ? ' ' : item.getGR_NON_VAL().charAt(0));
                result.setDelivCompl(item.getDELIV_COMPL() == null || item.getDELIV_COMPL().trim().isEmpty() ? ' ' : item.getDELIV_COMPL().charAt(0));
                result.setPartDeliv(item.getPART_DELIV() == null || item.getPART_DELIV().trim().isEmpty() ? ' ' : item.getPART_DELIV().charAt(0));
            }
        }
        result.setPoItem(item_num);
        result.setPoItemFree(item_num_free);
        result.setQuantity(item_qty);
        result.setQuantityFree(item_qty_free);
        //neu chi co free goods
        if (result.getPoItem() == null) {
            if (result.getPoItemFree() != null) {
                result.setPoItem(result.getPoItemFree());
                result.setQuantity(result.getQuantityFree());
                result.setPoItemFree(null);
                result.setQuantityFree(null);
            }
        }
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
        Customer result = null;
        CustomerGetDetailBapi bapiCust = new CustomerGetDetailBapi();
        bapiCust.setIdKunnr(kunnr);
        WeighBridgeApp.getApplication().getSAPSession().execute(bapiCust);
        CustomerGetDetailStructure strucCust = bapiCust.getEsKna1();
        if (strucCust != null && (strucCust.getKunnr() != null && !strucCust.getKunnr().trim().isEmpty())) {
            CustomerPK pk = new CustomerPK(strucCust.getMandt(), strucCust.getKunnr());
            result = new Customer(pk);
            result.setName1(strucCust.getName1());
            result.setName2(strucCust.getName2());
        }
        return result;
    }

    public static Vendor getVendor(String lifnr) {
        Vendor result = null;
        VendorGetDetailBapi bapiCust = new VendorGetDetailBapi();
        bapiCust.setIdLifnr(lifnr);
        WeighBridgeApp.getApplication().getSAPSession().execute(bapiCust);
        VendorGetDetailStructure strucVendor = bapiCust.getEsLfa1();
        if (strucVendor != null && (strucVendor.getLifnr() != null && !strucVendor.getLifnr().trim().isEmpty())) {
            VendorPK pk = new VendorPK(strucVendor.getMandt(), strucVendor.getLifnr().trim());
            result = new Vendor(pk);
            result.setName1(strucVendor.getName1());
            result.setName2(strucVendor.getName2());
        }
        return result;
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
                SLoc sloc = new SLoc(s.getMandt(), s.getWerks(), s.getLgort());
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
                        if (item.getSLocPK().getLgort().equals(sloc1[j])) {
                            result.add(item);
                        }
                    }
                }
            }

//        return new DefaultComboBoxModel(slocs.toArray());
            return new DefaultComboBoxModel(result.toArray());
        }
    }

    public static List<Material> lookupMaterialsByDesc(String desc) {
        List<Material> result = new ArrayList<Material>();
        AppConfig config = WeighBridgeApp.getApplication().getConfig();
        MatLookupBapi bapi = new MatLookupBapi();
        bapi.setPlant(config.getwPlant());
        bapi.setSdesc(desc);
        WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
        List<MatLookupStructure> mats = bapi.getListMats();
        for (MatLookupStructure mat : mats) {
            Material m = null;
            if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                m = new Material(config.getsClient(), config.getwPlant(), mat.getMaterial());
            } else {
                m = new Material(config.getsClient(), mat.getMaterial());
            }
            m.setMaktx(mat.getDesc());
            m.setMaktg(m.getMaktx().toUpperCase());
            m.setXchpf(mat.getXchpf() != null && mat.getXchpf().equalsIgnoreCase("X") ? 'X' : ' ');
            result.add(m);
        }
        return result;
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
        AppConfig config = WeighBridgeApp.getApplication().getConfig();

        MaterialGetListBapi bapi = new MaterialGetListBapi();
        try {
            WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
            List<MaterialGetListStructure> mats = bapi.getEtMaterial();
            for (MaterialGetListStructure mat : mats) {
                if(config.getwPlant().toString().equalsIgnoreCase(mat.getWerks()))
                {
                    Material m = null;
                    m = new Material(config.getsClient(), mat.getWerks(), mat.getMatnr());
                    m.setMaktx(mat.getMaktx());
                    m.setMaktg(mat.getMaktxLong());
                    //m.setXchpf(mat.);
                    result.add(m);
                }
               
            }
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
            for (TransportagentGetListStructure transport : transports) {
                 TransportAgent tAgent = null;
                tAgent = new TransportAgent(transport.getLifnr());
                if (transport.getName1() != null || transport.getName1().length() <= 35) 
                {
                tAgent.setName(transport.getName1());
            } else
                {
                    tAgent.setName(transport.getName2());
                }
                result.add(tAgent);
            }
        } catch (Exception ex) {
            
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
            for (TransportagentGetListStructure vendor : vendors) {
                Vendor v = null;
                VendorPK vpk = new VendorPK(wplant, vendor.getLifnr());
                v = new Vendor(vpk);
                v.setName1(vendor.getName1());
                v.setName2(vendor.getName2());
                
                result.add(v);
            }
        } catch (Exception ex) {  
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
                Vendor ven = null;
                VendorPK vpk = new VendorPK(config.getsClient(), venSap.getLifnr());
                ven = new Vendor(vpk);
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
        Material result = null;
        MatGetDetailBapi bapi = new MatGetDetailBapi();
        bapi.setId_matnr(matnr);
        WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
        MatGetDetailStructure bapiResult = bapi.getEs_makt();
        if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
            AppConfig config = WeighBridgeApp.getApplication().getConfig();
            result = new Material(bapiResult.getMandt(), config.getwPlant(), bapiResult.getMatnr());
        } else {
            result = new Material(bapiResult.getMandt(), bapiResult.getMatnr());
        }
        result.setMaktx(bapiResult.getMaktx());
        result.setMaktg(bapiResult.getMaktg());
        result.setXchpf(bapiResult.getXchpf() != null && bapiResult.getXchpf().equalsIgnoreCase("X") ? 'X' : ' ');
        return result;
    }
}
