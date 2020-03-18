/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtDoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPOSTOCreatePGIBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.structure.*;
import com.gcs.wb.bapi.helper.MatAvailableBapi;
import com.gcs.wb.bapi.helper.structure.MatAvailableStructure;
import com.gcs.wb.bapi.outbdlv.DOCreate2PGIBapi;
import com.gcs.wb.bapi.outbdlv.DOPostingPGIBapi;
import com.gcs.wb.bapi.outbdlv.DORevertBapi;
import com.gcs.wb.bapi.outbdlv.WsDeliveryUpdateBapi;
import com.gcs.wb.bapi.outbdlv.structure.OutbDeliveryCreateStoStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbkokStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbpokStructure;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.controller.WeightTicketController;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.JReportService;
import com.gcs.wb.jpa.entity.*;
import com.gcs.wb.jpa.repositorys.*;
import com.gcs.wb.views.WeightTicketView;
import org.apache.log4j.Logger;
import org.hibersap.session.Session;
import org.hibersap.util.DateUtil;

import javax.persistence.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;

/**
 *
 * @author THANGPT
 */
public class WeightTicketService {

    WeightTicketRepository weightTicketRepository = new WeightTicketRepository();
    BatchStockRepository batchStocksRepository = new BatchStockRepository();
    public HashMap hmMsg = new HashMap();
    VendorRepository vendorRepository = new VendorRepository();
    CustomerRepository customerRepository = new CustomerRepository();
    BatchStockRepository batchStockRepository = new BatchStockRepository();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    SLocRepository sLocRepository = new SLocRepository();
    VariantRepository variantRepository = new VariantRepository();
    MaterialConstraintRepository materialConstraintRepository = new MaterialConstraintRepository();
    EntityManager entityManager = JPAConnector.getInstance();
    String client = configuration.getSapClient();
    SAPService sapService = new SAPService();
    JReportService jreportService = new JReportService();
    PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    Session session = WeighBridgeApp.getApplication().getSAPSession();
    private final Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    WeightTicketRegistrationService weightTicketRegistrationService = new WeightTicketRegistrationService();

    public DefaultComboBoxModel getCustomerByMaNdt() {
        List<Customer> customers = this.customerRepository.getListCustomer();
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        result.addElement("");
        for (Customer customer : customers) {
            if (result.getIndexOf(customer) < 0) {
                result.addElement(customer);
            }
        }
        return result;
    }

    public List<BatchStock> getBatchStocks(SLoc selSloc, WeightTicket weightTicket) {
        return batchStocksRepository.getListBatchStock(configuration.getWkPlant(),
                selSloc.getLgort(), weightTicket.getWeightTicketDetail().getMatnrRef());
    }

    public void getSyncBatchStocks(SLoc selSloc, WeightTicket weightTicket) {
        sapService.syncBatchStocks(selSloc.getLgort(), weightTicket.getWeightTicketDetail().getMatnrRef());
    }

    public DefaultComboBoxModel setCbxBatch(List<BatchStock> batchs) {
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        for (BatchStock b : batchs) {
            if (b.getLvorm() == null || b.getLvorm().toString().trim().isEmpty()) {
                // Fillter BATCH not contain "-" by Tuanna -10.01.2013 
                if (configuration.getWkPlant().contains("1311")) {
                    result.addElement(b.getCharg());
                } else if (!b.getCharg().contains("-")) {
                    result.addElement(b.getCharg());
                }
            }
        }
        return result;

    }

    public void savePostAgainActionPerformed(WeightTicket weightTicket) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.merge(weightTicket);
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    public void saveKunnrItemStateChanged(WeightTicket weightTicket) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.merge(weightTicket);
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    public Date setTimeWeightTicket(String[] time) {
        String[] date = time[0].split("/");
        String[] hour = time[1].split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]), Integer.parseInt(hour[0]), Integer.parseInt(hour[1]), Integer.parseInt(hour[2]));
        Date stime = cal.getTime();
        System.out.println(stime);
        return stime;

    }

    public String getMsg() {
        return getMsg("6");
    }

    public DefaultComboBoxModel getMaterialList() {
        //get data from DB
        TypedQuery<Material> tMaterial = entityManager.createQuery("SELECT m FROM Material m WHERE m.wplant = :wplant order by m.matnr asc", Material.class);
        tMaterial.setParameter("wplant", configuration.getWkPlant());
        List<Material> materials = tMaterial.getResultList();

        DefaultComboBoxModel result = new DefaultComboBoxModel();
        for (Material m : materials) {
            result.addElement(m);
        }
        return result;
    }

    public WeightTicket findWeightTicket(WeightTicket weightTicket, String id, String mandt, String wplant) {
        //return entityManager.find(WeightTicket.class, id);
        return weightTicketRepository.getWeightTicketByIdAndMandtAndWplant(id, mandt, wplant);
    }

    public PurchaseOrder findPurOrder(String poNum) {
        return purchaseOrderRepository.findByPoNumber(poNum);
    }


    public PurchaseOrder getSapPurOrder(String poNum) throws Exception {
        return sapService.getPurchaseOrder(poNum);
    }

    public void revertCompletedDO(List<String> completedDOs, List<OutboundDeliveryDetail> OutbDetailsV2, List<OutboundDelivery> outbDels, WeightTicket weightTicket, List<OutboundDeliveryDetail> outDetails_lits, Session sapSession) {
        DORevertBapi bapi = null;
        for (String item : completedDOs) {
            bapi = new DORevertBapi(item);

            OutboundDelivery od_temp = new OutboundDelivery(item);
            GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, od_temp, outDetails_lits, weightTicket);

            bapi.setWeightticket(stWT);
            try {
                logger.info("[SAP] Check Revert DO: " + bapi.toString());
                sapSession.execute(bapi);
            } catch (Exception ex) {
                logger.error(ex);
            }
        }

        // active entityManager
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        if (OutbDetailsV2 != null) {
            for (OutboundDeliveryDetail outbDetail : OutbDetailsV2) {
                outbDetail.setPosted(false);
                entityManager.merge(outbDetail);
            }
        }
        if (outbDels != null) {
            for (OutboundDelivery outbD : outbDels) {
                outbD.setPosted(false);
                entityManager.merge(outbD);
            }
        }
        entityManager.getTransaction().commit();
    }

    public GoodsMvtWeightTicketStructure fillWTStructure(WeightTicket wt,
            OutboundDelivery od, List<OutboundDeliveryDetail> od_v2_list, WeightTicket weightTicket) {
        GoodsMvtWeightTicketStructure stWT = null;
        if (wt == null) {
            return stWT;
        }
        String tempWTID = wt.getId();
        //tempWTID = tempWTID.concat(StringUtil.paddingZero(String.valueOf(weightTicket.getSeqDay()), 3));
        stWT = new GoodsMvtWeightTicketStructure(weightTicket.getWplant(),
                weightTicket.getWbId(),
                tempWTID);
//          outb_details_v2
        OutboundDeliveryDetail od_v2 = null;
        if (od != null && od_v2_list != null) {
            for (OutboundDeliveryDetail od_v2_tmp : od_v2_list) {
                if (od_v2_tmp.getDeliveryOrderNo() != null
                        && od_v2_tmp.getDeliveryOrderNo().equals(od.getDeliveryOrderNo())) {
                    od_v2 = od_v2_tmp;
                    break;
                }
            }
        }

        WeightTicketDetail weightTicketDetail = wt.getWeightTicketDetail();

        stWT.setCAT_TYPE(String.valueOf(wt.getRegType()));
        stWT.setDO_WT(weightTicketDetail.getDeliveryOrderNo());
        stWT.setDO_NUMBER(od != null
                && od.getDeliveryOrderNo() != null
                ? od.getDeliveryOrderNo() : "");
        stWT.setDRIVERID(wt.getDriverIdNo());
        stWT.setDRIVERN(wt.getDriverName());
        stWT.setFDATE(wt.getFTime());
        stWT.setFSCALE(wt.getFScale());
        stWT.setFTIME(wt.getFTime());
        stWT.setGQTY_WT(wt.getGQty());
        stWT.setGQTY(wt.getGQty());
        stWT.setKUNNR(weightTicketDetail.getKunnr());
        stWT.setLIFNR(weightTicketDetail.getAbbr());
        if(wt.getMode().equals("OUT_SLOC_SLOC")) {
            stWT.setMATID(weightTicketDetail.getRecvMatnr());
            stWT.setMATNAME(weightTicketDetail.getRegItemDescription());
            stWT.setMVT_TYPE("311");
            stWT.setPO_NUMBER(null);
        } else {
            stWT.setMATID(weightTicketDetail.getMatnrRef());
            stWT.setMATNAME(weightTicketDetail.getRegItemDescription());
            stWT.setMVT_TYPE(wt.getMoveType());
            if ((stWT.getMVT_TYPE() == null || stWT.getMVT_TYPE().isEmpty())
                    && od != null && od.getBwart() != null) {
                stWT.setMVT_TYPE(od.getBwart());
            }
            stWT.setPO_NUMBER(weightTicketDetail.getEbeln());
        }
        if(wt.getMode().equals("IN_WAREHOUSE_TRANSFER")) {
            stWT.setwtIdRef(wt.getWeightTicketIdRef());
        }
        stWT.setNTEXT(wt.getNote());
        
        stWT.setREGQTY_WT(weightTicketDetail.getRegItemQuantity());
        stWT.setREGQTY(od != null ? od.getLfimg() : BigDecimal.ZERO);
        stWT.setSALEDT(od_v2 != null ? od_v2.getBzirk() : "");
        stWT.setSDATE(wt.getSTime());
        stWT.setSHIPTYPE(od != null ? od.getTraty() : "");
        stWT.setSLOC(wt.getLgort());
        stWT.setSSCALE(wt.getSScale());
        stWT.setSTIME(wt.getSTime());
        stWT.setTRANSID(wt.getPlateNo());
        stWT.setUNIT(weightTicketDetail.getUnit());
        stWT.setUSERNAME(wt.getSCreator());
        stWT.setVTYPE(od != null ? od.getBwtar() : "");
        stWT.setBatch(od != null ? od.getCharg() : "");
        stWT.setLfart(od != null ? od.getLfart() : "");

        return stWT;
    }

    public Date getServerTime() {
        Date now = null;

        try {
            Object rs = new Date();

            if (rs instanceof Timestamp) {
                Timestamp time1 = (Timestamp) rs;
                now = new Date(time1.getTime());
            } else {
                now = (Date) rs;
            }

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return now;
    }

    public Material checkPOSTO(PurchaseOrder purOrder) throws Exception {
        PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
        return CheckPOSTO(purchaseOrderDetail.getMaterial());
    }

    public Double CheckMatStock(String matnr, String plant, String sloc, String batch) {
        Double remaining = 0d;

        MatAvailableBapi bapi = new MatAvailableBapi();
        bapi.setMaterial(matnr);
        bapi.setPlant(plant);
        bapi.setSloc(sloc);
        bapi.setBatch(batch);
        bapi.setUnit(weightTicketRegistrationService.getUnit().getPurchaseUnit());
        logger.info("[SAP] Check Mat Stock: " + bapi.toString());
        WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
        List<MatAvailableStructure> stocks = bapi.getWmdvex();
        if (!stocks.isEmpty() && stocks.get(0).getCom_qty() != null) {
            remaining = stocks.get(0).getCom_qty().doubleValue();
        }
        return remaining;
    }

     public Object getGrDoMigoBapi(WeightTicket wt, WeightTicket weightTicket, OutboundDelivery outbDel, List<OutboundDeliveryDetail> outDetails_lits, int timeFrom, int timeTo) {
        String doNum = null;
        if (outbDel != null) {
            doNum = outbDel.getDeliveryOrderNo();
        }
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += "|" + wt.getTrailerId();
        }

        GoodsMvtDoCreateBapi bapi = new GoodsMvtDoCreateBapi();
        GoodsMvtHeaderStructure header = new GoodsMvtHeaderStructure();

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket,
                (outbDel != null && outbDel.getDeliveryOrderNo() != null
                && !outbDel.getDeliveryOrderNo().isEmpty() ? outbDel : null),
                outDetails_lits, weightTicket);
        bapi.setWeightticket(stWT);
        header.setDocDate(DateUtil.stripTime(wt.getSTime()));

        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        header.setPstngDate(DateUtil.stripTime(stime));
        // >> end of modified      

        WeightTicketDetail weightTicketDetail = wt.getWeightTicketDetail();
        if (outbDel == null) {
            header.setRefDocNo(weightTicketDetail.getDeliveryOrderNo());
        } else {
            header.setRefDocNo(doNum);
        }
        header.setBillOfLading(plateCombine);
        header.setGrGiSlipNo(wt.getDriverIdNo());
        if (outbDel == null) {
            header.setHeaderText(weightTicketDetail.getDeliveryOrderNo());
        } else {
            header.setHeaderText(doNum);
        }

        GoodsMvtItemDoStructure tab_wa = new GoodsMvtItemDoStructure();
        List<GoodsMvtItemDoStructure> tab = new ArrayList<>();

        //get do details for current do
        OutboundDeliveryDetail item = null;
        BigDecimal kl = BigDecimal.ZERO;
        BigDecimal kl_km = BigDecimal.ZERO;
        BigDecimal kl_total = BigDecimal.ZERO;
        for (int i = 0; i < outDetails_lits.size(); i++) {
            item = outDetails_lits.get(i);
            if (item.getDeliveryOrderNo().contains(doNum)) {
                if (item.getFreeItem() == null || item.getFreeItem().equals("")) {
                    kl = kl.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                } else {
                    kl_km = kl_km.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                }
            }
        }
        kl_total = kl.add(kl_km);
        if (outbDel == null) {
            tab_wa.setDeliv_numb(weightTicketDetail.getDeliveryOrderNo());
            tab_wa.setDeliv_numb_to_search(weightTicketDetail.getDeliveryOrderNo());
        } else {
            tab_wa.setDeliv_numb(doNum);
            tab_wa.setDeliv_numb_to_search(doNum);
        }
        tab_wa.setDeliv_item(weightTicketDetail.getItem());
        tab_wa.setDeliv_item_to_search(weightTicketDetail.getItem());
        tab_wa.setMove_type("101");
        tab_wa.setPlant(configuration.getWkPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setGr_rcpt(wt.getSCreator());
        if (outbDel == null || kl_total == BigDecimal.ZERO) {
            tab_wa.setEntry_qnt(wt.getGQty());
        } else {
            tab_wa.setEntry_qnt(kl_total);
        }
        tab_wa.setEntry_uom(weightTicketDetail.getUnit());
//        tab_wa.setEntry_uom_iso(wt.getUnit());
        if (wt.getNoMoreGr() != null && wt.getNoMoreGr() == '2') {
            tab_wa.setNo_more_gr("X");
        } else {
            tab_wa.setNo_more_gr(null);
        }
        tab_wa.setMove_reas(wt.getMoveReas());
        tab_wa.setItem_text(wt.getNote());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    public Object getGrPoMigoBapi(WeightTicket wt, WeightTicket weightTicket, PurchaseOrder purchaseOrder, int timeFrom, int timeTo) {
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += "|" + wt.getTrailerId();
        }
        GoodsMvtPoCreateBapi bapi = new GoodsMvtPoCreateBapi();
        GoodsMvtHeaderStructure header = new GoodsMvtHeaderStructure();

        if(wt.getMode().equals("OUT_SLOC_SLOC")) {
            bapi.setIvWbidNoSave("X");
        }
        header.setDocDate(DateUtil.stripTime(wt.getSTime()));

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, null, null, weightTicket);
        bapi.setWeightticket(stWT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        header.setPstngDate(DateUtil.stripTime(stime));

        header.setBillOfLading(plateCombine);
        header.setGrGiSlipNo(wt.getDriverIdNo());

        List<GoodsMvtItemPoStructure> tab = new ArrayList<>();
        GoodsMvtItemPoStructure tab_wa = new GoodsMvtItemPoStructure();
        WeightTicketDetail weightTicketDetail = wt.getWeightTicketDetail();
        tab_wa.setPo_number(purchaseOrder.getPoNumber());
        tab_wa.setPo_item(purchaseOrder.getPurchaseOrderDetail().getPoItem());
        tab_wa.setMove_type("101");
        tab_wa.setPlant(configuration.getWkPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setGr_rcpt(wt.getSCreator());
        tab_wa.setEntry_qnt(wt.getGQty());
        tab_wa.setEntry_uom(weightTicketDetail.getUnit());

        if (wt.getNoMoreGr() != null && wt.getNoMoreGr() == '2') {
            tab_wa.setNo_more_gr("X");
        } else {
            tab_wa.setNo_more_gr(null);
        }
        tab_wa.setMove_reas(wt.getMoveReas());
        tab_wa.setItem_text(wt.getNote());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    public Object getGi541MigoBapi(WeightTicket wt, WeightTicket weightTicket, int timeFrom, int timeTo, PurchaseOrder purOrder, JRadioButton rbtOutward) {
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += wt.getTrailerId();
        }
        GoodsMvtPoCreateBapi bapi = new GoodsMvtPoCreateBapi(new GoodsMvtCodeStructure("04"));
        GoodsMvtHeaderStructure header = new GoodsMvtHeaderStructure();

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, null, null, weightTicket);
        bapi.setWeightticket(stWT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1) && rbtOutward.isSelected()) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }
        header.setDocDate(DateUtil.stripTime(stime));
        header.setPstngDate(DateUtil.stripTime(stime));
        header.setBillOfLading(plateCombine);
        header.setGrGiSlipNo(wt.getDriverIdNo());

        List<GoodsMvtItemPoStructure> tab = new ArrayList<>();
        GoodsMvtItemPoStructure tab_wa = new GoodsMvtItemPoStructure();

        WeightTicketDetail weightTicketDetail = wt.getWeightTicketDetail();
        tab_wa.setMaterial(weightTicketDetail.getMatnrRef());
        tab_wa.setPlant(configuration.getWkPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setMove_type("541");
        tab_wa.setMvt_ind(null);
        tab_wa.setEntry_qnt(wt.getGQty());

        tab_wa.setEntry_uom(weightTicketDetail.getUnit());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setVendor(purOrder.getVendor());

        tab_wa.setNo_more_gr(null);

        tab_wa.setItem_text(wt.getNote());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    public Object getGiMB1BBapi(WeightTicket wt, WeightTicket weightTicket, int timeFrom, int timeTo, JRadioButton rbtOutward) {
        String vendorNo = null;
        String headertxt;
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += "|" + wt.getTrailerId();
        }
        GoodsMvtPoCreateBapi bapi = new GoodsMvtPoCreateBapi(new GoodsMvtCodeStructure("04"));
        GoodsMvtHeaderStructure header = new GoodsMvtHeaderStructure();

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, null, null, weightTicket);
        bapi.setWeightticket(stWT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1) && rbtOutward.isSelected()) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }
        header.setDocDate(DateUtil.stripTime(stime));
        header.setPstngDate(DateUtil.stripTime(stime));
        header.setRefDocNo(weightTicket.getRecvPo());
        header.setGrGiSlipNo(wt.getDriverIdNo());
        if (vendorNo != null) {
            headertxt = plateCombine + "|" + vendorNo;
        } else {
            headertxt = plateCombine;
        }

        header.setHeaderText(headertxt);

        List<GoodsMvtItemPoStructure> tab = new ArrayList<>();
        GoodsMvtItemPoStructure tab_wa = new GoodsMvtItemPoStructure();
        WeightTicketDetail weightTicketDetail = wt.getWeightTicketDetail();
        tab_wa.setMaterial(wt.getRecvMatnr());
        tab_wa.setPlant(configuration.getWkPlant());
        // kho xuat
        tab_wa.setStge_loc(wt.getLgort());
         // lo xuat
        tab_wa.setBatch(wt.getCharg());
        //tab_wa.setMove_type(wt.getMoveType());
        tab_wa.setMove_type("311");
        tab_wa.setMvt_ind(null);
        tab_wa.setEntry_qnt(wt.getGQty());
        tab_wa.setEntry_uom(weightTicketDetail.getUnit());

        //vat tu, kho, lo nhan
        tab_wa.setMoveMat(wt.getRecvMatnr());
        tab_wa.setMovePlant(wt.getRecvPlant());
        tab_wa.setMoveSLoc(wt.getRecvLgort());
        tab_wa.setMoveBatch(wt.getRecvCharg());

        tab_wa.setMove_reas(wt.getMoveReas());
        tab_wa.setItem_text(wt.getNote());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    public Object getDoCreate2PGI(WeightTicket wt, OutboundDelivery outbDel, WeightTicket weightTicket, int timeFrom, int timeTo, List<OutboundDeliveryDetail> outDetails_lits) {
        String doNum = null;
        if (outbDel != null) {
            doNum = outbDel.getDeliveryOrderNo();
        }
        DOCreate2PGIBapi bapi = new DOCreate2PGIBapi();
        WeightTicketDetail weightTicketDetail = wt.getWeightTicketDetail();
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += "|" + wt.getTrailerId();
        }
        VbkokStructure wa = new VbkokStructure();
        if (outbDel == null) {
            wa.setVbeln_vl(weightTicketDetail.getDeliveryOrderNo());
        } else {
            wa.setVbeln_vl(doNum);
        }
        wa.setKodat(DateUtil.stripTime(wt.getFTime()));
        wa.setKouhr(DateUtil.stripDate(wt.getFTime()));

        wa.setKomue("X");
        wa.setWabuc("X");

        //modify lui ngay
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        wa.setWadat_ist(DateUtil.stripTime(stime));
        wa.setWadat(DateUtil.stripTime(stime));
        wa.setWauhr(DateUtil.stripDate(stime));
        wa.setLfdat(DateUtil.stripTime(stime));
        wa.setLfuhr(DateUtil.stripDate(stime));
        wa.setTraty("Z001");
        wa.setTraid(plateCombine);
        wa.setLifex(wt.getDriverName());
        bapi.setVbkok_wa(wa);
        bapi.setIvCVendor(weightTicketDetail.getLoadVendor());
        bapi.setIvTVendor(weightTicketDetail.getTransVendor());

        //get do details for current do
        OutboundDeliveryDetail item = null;
        BigDecimal kl = BigDecimal.ZERO;
        BigDecimal kl_km = BigDecimal.ZERO;
        BigDecimal kl_total = BigDecimal.ZERO;
        for (int i = 0; i < outDetails_lits.size(); i++) {
            item = outDetails_lits.get(i);
            if (item.getDeliveryOrderNo().contains(doNum)) {
                if (item.getFreeItem() == null || item.getFreeItem().equals("")) {
                    kl = kl.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                } else {
                    kl_km = kl_km.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                }
            }
        }
        kl_total = kl.add(kl_km);

        List<OutbDeliveryCreateStoStructure> _StockTransItems = new ArrayList<OutbDeliveryCreateStoStructure>();
        if (wt.getRegType() == 'O'
                && weightTicketDetail.getRegItemDescription() != null
                && weightTicketDetail.getRegItemDescription().toLowerCase().indexOf("bao") >= 0
                && weightTicketDetail.getMatnrRef() != null) {
            _StockTransItems.add(new OutbDeliveryCreateStoStructure(weightTicketDetail.getEbeln(), weightTicketDetail.getItem(), weightTicketDetail.getRegItemQuantity(), weightTicketDetail.getUnit()));
        } else if (outbDel == null) {
            _StockTransItems.add(new OutbDeliveryCreateStoStructure(weightTicketDetail.getEbeln(), weightTicketDetail.getItem(), wt.getGQty(), weightTicketDetail.getUnit()));
        } else {
            _StockTransItems.add(new OutbDeliveryCreateStoStructure(weightTicketDetail.getEbeln(), weightTicketDetail.getItem(), kl_total, weightTicketDetail.getUnit()));
        }
        bapi.setStockTransItems(_StockTransItems);

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, outbDel, outDetails_lits, weightTicket);
        bapi.setWeightticket(stWT);
        List<VbpokStructure> tab = new ArrayList<>();
        VbpokStructure tab_wa = new VbpokStructure();
        if (outbDel == null) {
            tab_wa.setVbeln_vl(weightTicketDetail.getDeliveryOrderNo());
        } else {
            tab_wa.setVbeln_vl(doNum);
        }
        tab_wa.setPosnr_vl(weightTicketDetail.getItem());
        tab_wa.setVbeln(tab_wa.getVbeln_vl());
        tab_wa.setPosnn(tab_wa.getPosnr_vl());
        tab_wa.setMatnr(weightTicketDetail.getMatnrRef());
        tab_wa.setWerks(configuration.getWkPlant());
        tab_wa.setLgort(wt.getLgort());
        tab_wa.setCharg(wt.getCharg());
        tab_wa.setLianp("X");
        if (outbDel == null) {
            tab_wa.setPikmg(wt.getGQty());
            tab_wa.setLfimg(wt.getGQty());
        } else {
            tab_wa.setPikmg(kl_total);
            tab_wa.setLfimg(kl_total);
        }
        tab_wa.setVrkme(weightTicketDetail.getUnit());
        tab_wa.setMeins(weightTicketDetail.getUnit());
        tab.add(tab_wa);
        bapi.setVbpok_tab(tab);

        return bapi;
    }

    public Object getDOPostingPGI(WeightTicket wt, OutboundDelivery outbDel,WeightTicket weightTicket, int timeFrom, int timeTo, List<OutboundDeliveryDetail> outDetails_lits, String deliveryNum) {
        String doNum = null;
        if (outbDel != null) {
            doNum = outbDel.getDeliveryOrderNo();
        }
        DOPostingPGIBapi bapi = new DOPostingPGIBapi();
        bapi.setDelivery(deliveryNum);
        WeightTicketDetail weightTicketDetail = wt.getWeightTicketDetail();
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += "|" + wt.getTrailerId();
        }
        VbkokStructure wa = new VbkokStructure();
        if (outbDel == null) {
            wa.setVbeln_vl(weightTicketDetail.getDeliveryOrderNo());
        } else {
            wa.setVbeln_vl(doNum);
        }
        wa.setKodat(DateUtil.stripTime(wt.getFTime()));
        wa.setKouhr(DateUtil.stripDate(wt.getFTime()));

        wa.setKomue("X");
        wa.setWabuc("X");

        //modify lui ngay
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        wa.setWadat_ist(DateUtil.stripTime(stime));
        wa.setWadat(DateUtil.stripTime(stime));
        wa.setWauhr(DateUtil.stripDate(stime));
        wa.setLfdat(DateUtil.stripTime(stime));
        wa.setLfuhr(DateUtil.stripDate(stime));
        wa.setTraty("Z001");
        wa.setTraid(plateCombine);
        wa.setLifex(wt.getDriverName());
        bapi.setVbkok_wa(wa);
        bapi.setIvCVendor(weightTicketDetail.getLoadVendor());
        bapi.setIvTVendor(weightTicketDetail.getTransVendor());

        //get do details for current do
        OutboundDeliveryDetail item = null;
        BigDecimal kl = BigDecimal.ZERO;
        BigDecimal kl_km = BigDecimal.ZERO;
        BigDecimal kl_total = BigDecimal.ZERO;
        for (int i = 0; i < outDetails_lits.size(); i++) {
            item = outDetails_lits.get(i);
            if (item.getDeliveryOrderNo().contains(doNum)) {
                if (item.getFreeItem() == null || item.getFreeItem().equals("")) {
                    kl = kl.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                } else {
                    kl_km = kl_km.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                }
            }
        }
        kl_total = kl.add(kl_km);

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, outbDel, outDetails_lits, weightTicket);
        bapi.setWeightticket(stWT);
        List<VbpokStructure> tab = new ArrayList<>();
        VbpokStructure tab_wa = new VbpokStructure();
        if (outbDel == null) {
            tab_wa.setVbeln_vl(weightTicketDetail.getDeliveryOrderNo());
        } else {
            tab_wa.setVbeln_vl(doNum);
        }
        tab_wa.setPosnr_vl(weightTicketDetail.getItem());
        tab_wa.setVbeln(tab_wa.getVbeln_vl());
        tab_wa.setPosnn(tab_wa.getPosnr_vl());
        tab_wa.setMatnr(weightTicketDetail.getMatnrRef());
        tab_wa.setWerks(configuration.getWkPlant());
        tab_wa.setLgort(wt.getLgort());
        tab_wa.setCharg(wt.getCharg());
        tab_wa.setLianp("X");
        if (outbDel == null) {
            tab_wa.setPikmg(wt.getGQty());
            tab_wa.setLfimg(wt.getGQty());
        } else {
            tab_wa.setPikmg(kl_total);
            tab_wa.setLfimg(kl_total);
        }
        tab_wa.setVrkme(weightTicketDetail.getUnit());
        tab_wa.setMeins(weightTicketDetail.getUnit());
        tab.add(tab_wa);
        bapi.setVbpok_tab(tab);

        return bapi;
    }

    public Object getPgmVl02nBapi(WeightTicket wt, OutboundDelivery outbDel,
            WeightTicket weightTicket,String modeFlg, int timeFrom, int timeTo,
            List<OutboundDeliveryDetail> outDetails_lits, String ivWbidNosave, BigDecimal sumQtyReg) {
        String doNum = null;
        if (outbDel != null) {
            doNum = outbDel.getDeliveryOrderNo();
        }
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += "|" + wt.getTrailerId();
        }
        WsDeliveryUpdateBapi bapi = new WsDeliveryUpdateBapi();
        WeightTicketDetail weightTicketDetail = wt.getWeightTicketDetail();
        if (outbDel == null) {
            bapi.setDelivery(weightTicketDetail.getDeliveryOrderNo());
        } else {
            bapi.setDelivery(doNum);
        }
        bapi.setUpdate_picking("X");

        //get do details for current do
        OutboundDeliveryDetail item = null;
        BigDecimal kl = BigDecimal.ZERO;
        BigDecimal kl_km = BigDecimal.ZERO;
        BigDecimal kl_total = BigDecimal.ZERO;
        BigDecimal sumQtyRegWT = BigDecimal.ZERO;
        
        // check dung sai -> set Qty
        String material = (outbDel != null && outbDel.getMatnr() != null) ? outbDel.getMatnr().toString() : "";

        for (int i = 0; i < outDetails_lits.size(); i++) {
            item = outDetails_lits.get(i);
            if (item.getDeliveryOrderNo().contains(doNum)) {
                if (item.getFreeItem() == null || item.getFreeItem().equals("")) {
                    kl = kl.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                } else {
                    kl_km = kl_km.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                }
            }
        }
        kl_total = kl.add(kl_km);

        bapi.setYield(kl_total);
        VbkokStructure wa = new VbkokStructure();
        if (outbDel == null) {
            wa.setVbeln_vl(weightTicketDetail.getDeliveryOrderNo());
        } else {
            wa.setVbeln_vl(doNum);
        }
        wa.setKodat(DateUtil.stripTime(wt.getFTime()));
        wa.setKouhr(DateUtil.stripDate(wt.getFTime()));
        wa.setKomue("X");
        wa.setWabuc("X");

        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime;

        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        wa.setWadat_ist(DateUtil.stripTime(stime));
        wa.setWadat(DateUtil.stripTime(stime));
        wa.setWauhr(DateUtil.stripDate(stime));
        wa.setLfdat(DateUtil.stripTime(stime));
        wa.setLfuhr(DateUtil.stripDate(stime));
        wa.setTraty(modeFlg);
        wa.setTraid(plateCombine);
        wa.setLifex(wt.getDriverName());
        bapi.setVbkok_wa(wa);

        List<VbpokStructure> tab = new ArrayList<>();
        VbpokStructure tab_wa = new VbpokStructure();
        if (outbDel == null) {
            tab_wa.setVbeln_vl(weightTicketDetail.getDeliveryOrderNo());
        } else {
            tab_wa.setVbeln_vl(doNum);
        }
        tab_wa.setPosnr_vl(weightTicketDetail.getItem());
        tab_wa.setVbeln(tab_wa.getVbeln_vl());
        tab_wa.setPosnn(tab_wa.getPosnr_vl());
        tab_wa.setMatnr(outbDel.getMatnr());
        tab_wa.setWerks(configuration.getWkPlant());
        tab_wa.setLgort(wt.getLgort());
        tab_wa.setCharg(wt.getCharg());
        tab_wa.setLianp("X");
        if (outbDel != null && (outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF")) //                && wt.getMatnrRef().equalsIgnoreCase(setting.getMatnrPcb40())
                ) {
            tab_wa.setPikmg(outbDel.getLfimg());
            tab_wa.setLfimg(outbDel.getLfimg());
        } else {
            BigDecimal qty = new BigDecimal(0);
            BigDecimal qtyfree = new BigDecimal(0);
            try {
                qtyfree = (BigDecimal) outbDel.getFreeQty();
            } catch (Exception ex) {
                qtyfree = new BigDecimal(0);
            }
            if (qtyfree == null) {
                qtyfree = new BigDecimal(0);
            }
            qty = wt.getGQty().subtract(qtyfree);
            tab_wa.setPikmg(kl);
            tab_wa.setLfimg(kl);
        }

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, outbDel, outDetails_lits, weightTicket);
        stWT.setGQTY(tab_wa.getLfimg());
        bapi.setWeightticket(stWT);

        // add for check ghep ma
        bapi.setIvWbidNosave(ivWbidNosave);

        tab_wa.setVrkme(weightTicketDetail.getUnit());
        tab_wa.setMeins(outbDel.getMeins());
        tab.add(tab_wa);

        VbpokStructure tab_wa_f = new VbpokStructure();
        tab_wa_f.setWerks(configuration.getWkPlant());
        tab_wa_f.setLgort(wt.getLgort());
        tab_wa_f.setCharg(wt.getCharg());
        tab_wa_f.setLianp("X");
        tab_wa_f.setVrkme(weightTicketDetail.getUnit());
        tab_wa_f.setMeins(outbDel.getMeins());

        if (outbDel.getDeliveryItemFree() != null && (outbDel.getDeliveryItemFree() == null ? "" != null : !outbDel.getDeliveryItemFree().equals(""))) {
            if (outbDel == null) {
                tab_wa_f.setVbeln_vl(weightTicketDetail.getDeliveryOrderNo());
            } else {
                tab_wa_f.setVbeln_vl(doNum);
            }
            tab_wa_f.setMatnr(outbDel.getMatnrFree());
            tab_wa_f.setPosnr_vl(outbDel.getDeliveryItemFree());
            tab_wa_f.setVbeln(tab_wa_f.getVbeln_vl());
            tab_wa_f.setPosnn(tab_wa_f.getPosnr_vl());
            tab_wa_f.setPikmg(outbDel.getFreeQty());
            tab_wa_f.setLfimg(outbDel.getFreeQty());
            tab.add(tab_wa_f);
        }
        bapi.setVbpok_tab(tab);
        return bapi;
    }
    
    public boolean checkVariantByMaterial(WeightTicket wt, String material, BigDecimal gQty, BigDecimal sumQtyReg) {
        Variant vari = findByParamMandtWplant(material, configuration.getSapClient(), configuration.getWkPlant());
        double valueUp = 0;
        double valueDown = 0;
        double resultReg = sumQtyReg.doubleValue();
        double result = gQty.doubleValue();

        if (vari != null) {
            if (vari.getValueUp() != null && !vari.getValueUp().isEmpty()) {
                valueUp = Double.parseDouble(vari.getValueUp());
            }

            if (vari.getValueDown() != null && !vari.getValueDown().isEmpty()) {
                valueDown = Double.parseDouble(vari.getValueDown());
            }

            double upper = resultReg + (resultReg * valueUp) / 100;
            double lower = resultReg - (resultReg * valueDown) / 100;

            if ((lower <= result && result <= upper)) {
                return true;
            }
        }
        return false;
    }

    public void printWT(WeightTicket wt, boolean reprint, String ximang, List<OutboundDelivery> outbDel_list, List<OutboundDeliveryDetail> outDetails_lits,
            OutboundDelivery outbDel, String txtPONo, boolean isStage1, JRootPane rootPane) {
        OutboundDelivery item;
        try {
            boolean isOffline = WeighBridgeApp.getApplication().isOfflineMode();
            Map<String, Object> map = new HashMap<>();
            Long bags = null;
            if ((outbDel_list == null || outbDel_list.isEmpty()) && (isOffline || (txtPONo != null || !"".equals(txtPONo)))) {
                // can posto xi mang 
                map.put("P_MANDT", wt.getMandt());
                map.put("P_WPlant", wt.getWplant());
                map.put("P_ID", wt.getId());
                map.put("P_DAYSEQ", wt.getSeqDay());
                map.put("P_REPRINT", reprint);
                map.put("P_ADDRESS", configuration.getRptId());

                WeightTicketDetail weightTicketDetail = wt.getWeightTicketDetail();
                if (!wt.isDissolved()) {
                    Double tmp = null;
                    if (weightTicketDetail.getRegItemDescription() != null
                            && (weightTicketDetail.getRegItemDescription().contains("Bag")) || weightTicketDetail.getRegItemDescription().contains("bao")) {
                        tmp = ((weightTicketDetail.getRegItemQuantity().doubleValue()) * 1000d) / 50d;
                    }
                    
                    if (tmp != null) {
                        bags = Math.round(tmp);
                        map.put("P_PCB40BAG", bags);
                    }
                }
                String reportName1 = "";
                reportName1 = "./rpt/rptBT/WeightTicket.jasper";

                jreportService.printReport(map, reportName1);

            } else {
                for (int i = 0; i < outbDel_list.size(); i++) {
                    item = outbDel_list.get(i);
                    List<WeightTicketDetail> weightTicketDetails = wt.getWeightTicketDetails();
                    
                    OutboundDeliveryDetail out_detail = null;
                    BigDecimal sscale = BigDecimal.ZERO;
                    BigDecimal gqty = BigDecimal.ZERO;
                    BigDecimal lfimg_ori = BigDecimal.ZERO;
                    BigDecimal totalQtyReality = BigDecimal.ZERO;
                    for (int k = 0; k < outDetails_lits.size(); k++) {
                        out_detail = outDetails_lits.get(k);
                        if (out_detail.getDeliveryOrderNo().contains(item.getDeliveryOrderNo())) {
                            if (out_detail.getLfimg() != null) {
                                lfimg_ori = lfimg_ori.add(out_detail.getLfimg());
                            }
                            if (out_detail.getGoodsQty() != null) {
                                totalQtyReality = totalQtyReality.add(out_detail.getGoodsQty().setScale(3, RoundingMode.HALF_UP));
                            } else {
                                totalQtyReality = totalQtyReality.add(out_detail.getLfimg().setScale(3, RoundingMode.HALF_UP));
                            }
                        }
                    }
                    map.put("P_PAGE", "Trang ".concat(String.valueOf(i + 1).concat("/").concat(String.valueOf(outbDel_list.size()))));
                    map.put("P_TOTAL_QTY_ORI", String.valueOf(lfimg_ori));
                    if (wt.getFScale() != null) {
                        outbDel = item;
                    }
                    map.put("P_MANDT", wt.getMandt());
                    map.put("P_WPlant", wt.getWplant());
                    map.put("P_ID", wt.getId());
                    map.put("P_DAYSEQ", wt.getSeqDay());
                    map.put("P_REPRINT", reprint);
                    map.put("P_ADDRESS", configuration.getRptId());
                    map.put("P_DEL_NUM", outbDel.getDeliveryOrderNo());
                    if (wt.getSScale() != null) {
                        BigDecimal n_1000 = new BigDecimal(1000);
                        sscale = wt.getSScale().divide(n_1000);
                    }
                    if (wt.getGQty() != null) {
                        gqty = wt.getGQty();
                    }
                    if (reprint && wt.isDissolved()) {
                        sscale = BigDecimal.ZERO;
                        gqty = BigDecimal.ZERO;
                    }

                    map.put("P_SSCALE", sscale);
                    map.put("P_GQTY", gqty);
                    BigDecimal n_0 = new BigDecimal(0);
                    map.put("P_STAGE1", isStage1);

                    if (wt.getFScale() != null && wt.getSScale() != null && wt.getSScale() != n_0) {
                        map.put("P_STAGE2", true);
                    } else {
                        map.put("P_STAGE2", false);
                    }

                    if (outbDel.getFreeQty() != null) {
                        map.put("P_FREE", String.valueOf(outbDel.getFreeQty()));
                        // set qty for Ghep ma
                        BigDecimal totalQtyReg = BigDecimal.ZERO;
                        BigDecimal totalQty = BigDecimal.ZERO;
                        for(WeightTicketDetail wtDetail: weightTicketDetails) {
                            if(outbDel.getDeliveryOrderNo().equals(wtDetail.getDeliveryOrderNo())) {
                                totalQtyReg = wtDetail.getRegItemQuantity().subtract(outbDel.getFreeQty());
                                totalQty = wtDetail.getRegItemQuantity();
                                break;
                            }
                        }
                        
                        map.put("P_TOTAL_QTY_REG", String.valueOf(totalQtyReg));        
                        map.put("P_TOTAL_QTY", String.valueOf(totalQty));
                        map.put("P_TOTAL_QTY_REALITY", String.valueOf(totalQtyReality));
                        Double tmp = null;
                        if ((outbDel != null) && ((outbDel.getArktx().contains("Bag")) || (outbDel.getArktx().contains("bao")))) {
                            tmp = ((outbDel.getLfimg().doubleValue() + outbDel.getFreeQty().doubleValue()) * 1000d) / 50d;
                            bags = Math.round(tmp);
                        }
                    } else {
                        BigDecimal totalQtyReg = BigDecimal.ZERO;
                        BigDecimal totalQty = BigDecimal.ZERO;
                        for(WeightTicketDetail wtDetail: weightTicketDetails) {
                            if(outbDel.getDeliveryOrderNo().equals(wtDetail.getDeliveryOrderNo())) {
                                totalQtyReg = wtDetail.getRegItemQuantity();
                                totalQty = wtDetail.getRegItemQuantity();
                                break;
                            }
                        }
                        map.put("P_TOTAL_QTY_REG", String.valueOf(totalQtyReg));
                        map.put("P_TOTAL_QTY", String.valueOf(totalQty));
                        map.put("P_TOTAL_QTY_REALITY", String.valueOf(totalQtyReality));
                        Double tmp = null;
                        if ((outbDel != null) && ((outbDel.getArktx().contains("Bag")) || (outbDel.getArktx().contains("bao")))) {
                            tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 50d;
                            bags = Math.round(tmp);
                        }

                    }

                    if (bags != null) {
                        map.put("P_PCB40BAG", bags);
                    }
                    for (WeightTicketDetail wtDetail : weightTicketDetails) {
                        if (outbDel.getDeliveryOrderNo().equals(wtDetail.getDeliveryOrderNo())) {
                            if (wtDetail.getMatDoc() != null) {
                                map.put("P_MAT_DOC", wtDetail.getMatDoc());
                            }
                        }
                    }
                    
                    String reportName = null;
                    String path = "";
                    path = "./rpt/rptBT/"; 
                    reportName = path.concat("WeightTicket_NEW.jasper");
                    jreportService.printReport(map, reportName);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, ex);
        }
    }


    public SLoc findByLgort(String lgort) {
        return sLocRepository.findByLgort(lgort);
    }

    public Variant findByParamMandtWplant(String param, String mandt, String wplant) {
        return variantRepository.findByParamMandtWplant(param, mandt, wplant);
    }

    public BatchStock findByWerksLgortMatnrCharg(String werks, String lgort, String matnr, String charg) {
        return batchStockRepository.findByWerksLgortMatnrCharg(werks, lgort, matnr, charg);
    }

    public PurchaseOrder findByPoNumber(String poNumber) {
        return purchaseOrderRepository.findByPoNumber(poNumber);
    }

    public Material CheckPOSTO(String matnr) throws Exception {
        Material material = new Material();
        MaterialRepository repository = new MaterialRepository();
        material = repository.CheckPOSTO(matnr);
        return material;
    }

    public OutboundDelivery findByMandtOutDel(String delnum) throws Exception {
        OutboundDeliveryRepository repository = new OutboundDeliveryRepository();
        return repository.findByDeliveryOrderNo(delnum);
    }

    public String getMsg(String pid) {
        String msg = null;
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pgetmsg");
                query.registerStoredProcedureParameter("pid", String.class, ParameterMode.IN);
                query.setParameter("pid", pid);
                query.execute();
                List<Object[]> list = query.getResultList();
                if (list != null && list.size() > 0) {
                    Object[] firstRow = list.get(0);
                    msg = firstRow[0].toString();
                }
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());

        }
        return msg;
    }
    
    public MaterialConstraint getMaterialConstraintByMatnr( String matnr){
        return materialConstraintRepository.findByMatnr(matnr);
    }

    //nhap xuat dong thoi
    public Object getMvtPOSTOCreatePGI(WeightTicket wt, WeightTicket weightTicket, String posto, int timeFrom, int timeTo, boolean flgPost) {
        //config = WeighBridgeApp.getApplication().getConfig();
        GoodsMvtPOSTOCreatePGIBapi bapi = new GoodsMvtPOSTOCreatePGIBapi();
        PurchaseOrder purOrderPosto = purchaseOrderRepository.findByPoNumber(posto);
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId()!= null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += "|" + wt.getTrailerId();
        }
        if(flgPost) {
            bapi.setIvReType("X");
            bapi.setIvMaterialDocument(wt.getWeightTicketDetail().getIvMaterialDocument());
            bapi.setIvMatDocumentYear(wt.getWeightTicketDetail().getIvMatDocumentYear());
        }
        //API ZJBAPI_GOODSMVT_CREATE_V2_2606 - Nhap (posto)
        bapi.setGmCode(new GoodsMvtCodeStructure("01"));
        GoodsMvtHeaderStructure header = new GoodsMvtHeaderStructure();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime = null;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        header.setDocDate(DateUtil.stripTime(stime));
        header.setPstngDate(DateUtil.stripTime(stime));

        header.setBillOfLading(plateCombine);
        header.setGrGiSlipNo(wt.getDriverIdNo());

        List<GoodsMvtItemPoStructure> tab = new ArrayList<GoodsMvtItemPoStructure>();
        GoodsMvtItemPoStructure tab_wa = new GoodsMvtItemPoStructure();
        WeightTicketDetail weightTicketDetail = wt.getWeightTicketDetail();
        tab_wa.setPo_number(purOrderPosto.getPoNumber());
        tab_wa.setPo_item(purOrderPosto.getPurchaseOrderDetail().getPoItem());
        tab_wa.setMove_type("101");
        tab_wa.setMaterial(weightTicketDetail.getMatnrRef());
        tab_wa.setPlant(configuration.getWkPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setVendor(purOrderPosto.getVendor());
        tab_wa.setGr_rcpt(wt.getSCreator());
        tab_wa.setEntry_qnt(wt.getGQty());
        tab_wa.setEntry_uom(weightTicketDetail.getUnit());

        if (wt.getNoMoreGr() != null && wt.getNoMoreGr() == '2') {
            tab_wa.setNo_more_gr("X");
        } else {
            tab_wa.setNo_more_gr(null);
        }
        tab_wa.setMove_reas(wt.getMoveReas());
        tab_wa.setItem_text(wt.getNote());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        //End - API ZJBAPI_GOODSMVT_CREATE_V2_2606
        
        // API ZJBAPI_WB2_DO_CR_N_PGI_V2_0207 - xuat (po)
        PurchaseOrder purOrder = purchaseOrderRepository.findByPoNumber(weightTicketDetail.getEbeln());
        bapi.setIvCVendor(weightTicketDetail.getLoadVendor());
        bapi.setIvTVendor(weightTicketDetail.getTransVendor());

        VbkokStructure wa = new VbkokStructure();
        
        wa.setVbeln_vl(weightTicketDetail.getDeliveryOrderNo());
        wa.setKodat(DateUtil.stripTime(wt.getFTime()));
        wa.setKouhr(DateUtil.stripDate(wt.getFTime()));

        wa.setKomue("X");
        wa.setWabuc("X");
    
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        wa.setWadat_ist(DateUtil.stripTime(stime));
        wa.setWadat(DateUtil.stripTime(stime));
        wa.setWauhr(DateUtil.stripDate(stime));
        wa.setLfdat(DateUtil.stripTime(stime));
        wa.setLfuhr(DateUtil.stripDate(stime));
        wa.setTraty("Z001");
        wa.setTraid(plateCombine);
        wa.setLifex(wt.getDriverName());
        bapi.setVbkok_wa(wa);

        //get do details for current do
        BigDecimal kl = BigDecimal.ZERO;
        BigDecimal kl_km = BigDecimal.ZERO;
        BigDecimal kl_total = BigDecimal.ZERO;
        kl_total = kl.add(kl_km);

        List<OutbDeliveryCreateStoStructure> _StockTransItems = new ArrayList<OutbDeliveryCreateStoStructure>();
       
       _StockTransItems.add(new OutbDeliveryCreateStoStructure(weightTicketDetail.getEbeln(), purOrder.getPurchaseOrderDetail().getPoItem(), wt.getGQty(), purOrder.getPurchaseOrderDetail().getPoUnit()));
        

        bapi.setStockTransItems(_StockTransItems);

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, null, null, weightTicket);
        bapi.setWeightticket(stWT);
        List<VbpokStructure> tabVp = new ArrayList<VbpokStructure>();
        VbpokStructure tab_waVp = new VbpokStructure();
        
        tab_waVp.setVbeln_vl(weightTicketDetail.getDeliveryOrderNo());
            
        tab_waVp.setPosnr_vl(purOrder.getPurchaseOrderDetail().getPoItem());
        tab_waVp.setVbeln(tab_waVp.getVbeln_vl());
        tab_waVp.setPosnn(tab_waVp.getPosnr_vl());
        tab_waVp.setMatnr(weightTicketDetail.getMatnrRef());
        tab_waVp.setWerks(configuration.getWkPlant());
        tab_waVp.setLgort(wt.getLgort());
        tab_waVp.setCharg(wt.getCharg());
        tab_waVp.setLianp("X");
        tab_waVp.setPikmg(wt.getGQty());
        tab_waVp.setLfimg(wt.getGQty());
        tab_waVp.setVrkme(purOrder.getPurchaseOrderDetail().getPoUnit());
        tab_waVp.setMeins(purOrder.getPurchaseOrderDetail().getPoUnit());
        tabVp.add(tab_waVp);
        bapi.setVbpok_tab(tabVp);

        return bapi;
    }
    
//    public boolean checkVariantByMaterial(WeightTicket wt, String material, BigDecimal gQty) {
//        Variant vari = findByParamMandtWplant(material, configuration.getSapClient(), configuration.getWkPlant());
//        double valueUp = 0;
//        double valueDown = 0;
//        double result = gQty.doubleValue();
//
//        if (vari != null) {
//            if (vari.getValueUp() != null && !vari.getValueUp().isEmpty()) {
//                valueUp = Double.parseDouble(vari.getValueUp());
//            }
//
//            if (vari.getValueDown() != null && !vari.getValueDown().isEmpty()) {
//                valueDown = Double.parseDouble(vari.getValueDown());
//            }
//
//            double upper = result + (result * valueUp) / 100;
//            double lower = result - (result * valueDown) / 100;
//
//            if ((lower <= result && result <= upper)) {hc
//                return true;
//            }
//        }
//        return false;
//    }
}
