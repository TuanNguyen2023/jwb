/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtDoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtCodeStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtHeaderStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtItemDoStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtItemPoStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import com.gcs.wb.bapi.helper.MatAvailableBapi;
import com.gcs.wb.bapi.helper.structure.MatAvailableStructure;
import com.gcs.wb.bapi.outbdlv.DOCreate2PGIBapi;
import com.gcs.wb.bapi.outbdlv.DORevertBapi;
import com.gcs.wb.bapi.outbdlv.WsDeliveryUpdateBapi;
import com.gcs.wb.bapi.outbdlv.structure.OutbDeliveryCreateStoStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbkokStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbpokStructure;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.util.Conversion_Exit;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.BatchStock;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.OutboundDetail;
import com.gcs.wb.jpa.entity.PurchaseOrder;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.entity.TimeRange;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.procedures.WeightTicketJpaRepository;
import com.gcs.wb.jpa.repositorys.BatchStockRepository;
import com.gcs.wb.jpa.repositorys.CustomerRepository;
import com.gcs.wb.jpa.repositorys.SignalsRepository;
import com.gcs.wb.jpa.repositorys.TimeRangeRepository;
import com.gcs.wb.jpa.procedures.WeightTicketRepository;
import com.gcs.wb.jpa.repositorys.PurchaseOrderRepository;
import com.gcs.wb.jpa.service.JPAService;
import com.gcs.wb.jpa.service.JReportService;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.views.WeightTicketView;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import org.hibersap.session.Session;
import org.hibersap.util.DateUtil;

/**
 *
 * @author THANGPT
 */
public class WeightTicketService {

    WeightTicketRepository weightTicketRepository = new WeightTicketRepository();
    BatchStockRepository batchStocksRepository = new BatchStockRepository();
    TimeRangeRepository timeRangeRepository = new TimeRangeRepository();
    private AppConfig config = null;
    public HashMap hmMsg = new HashMap();
    CustomerRepository customerRepository = new CustomerRepository();
    SignalsRepository noneRepository = new SignalsRepository();
    WeightTicketJpaRepository weightTicketJpaRepository = new WeightTicketJpaRepository();
    EntityManager entityManager = JPAConnector.getInstance();
    String client = WeighBridgeApp.getApplication().getConfig().getsClient();
    WeightTicketJpaController con = new WeightTicketJpaController();
    SAPService sapService = new SAPService();
    JPAService jpaService = new JPAService();
    JReportService jreportService = new JReportService();
    PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();

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

    public TimeRange getTime() {
        return timeRangeRepository.findByMandtWbId(client, WeighBridgeApp.getApplication().getConfig().getWbId());
    }

    public List<BatchStock> getBatchStocks(SLoc selSloc, WeightTicket weightTicket) {
        return batchStocksRepository.getListBatchStock(config.getwPlant(),
                selSloc.getLgort(), weightTicket.getMatnrRef());
    }

    public void getSyncBatchStocks(SLoc selSloc, WeightTicket weightTicket) {
        sapService.syncBatchStocks(selSloc.getLgort(), weightTicket.getMatnrRef(), weightTicket.getLgort());
    }

    public DefaultComboBoxModel setCbxBatch(List<BatchStock> batchs) {
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        for (BatchStock b : batchs) {
            if (b.getLvorm() == null || b.getLvorm().toString().trim().isEmpty()) {
                // Fillter BATCH not contain "-" by Tuanna -10.01.2013 
                if (WeighBridgeApp.getApplication().getConfig().getwPlant().indexOf("1311") >= 0) {
                    result.addElement(b.getCharg());
                } else if (b.getCharg().indexOf("-") < 0) {
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

    public String readWT(String txt) {
        List wts = weightTicketRepository.getWeighTicketReg(txt);

        String kq = null;
        if (wts != null && wts.size() > 0) {
            kq = wts.get(0).toString();
        }
        if (kq != null && kq.length() > 0) {
            txt = kq.trim();
        }
        return txt;
    }

    public String getMsg() {
        return weightTicketJpaRepository.getMsg("6");
    }

    public List getMaxL(String pBs) {
        return weightTicketRepository.getMaxL(config.getwPlant().toString().trim(), config.getWbId().toString().trim(), pBs);
    }

    public List getMaxLLock(String pBs) {
        return weightTicketRepository.getMaxLLock(config.getwPlant().toString().trim(), config.getWbId().toString().trim(), pBs);
    }

    public DefaultComboBoxModel getMaterialList() {
        List<Material> materials = null;
        //get data from DB
        TypedQuery<Material> tMaterial = entityManager.createQuery("SELECT m FROM Material m WHERE m.materialPK.wplant = :wplant order by m.materialPK.matnr asc", Material.class);
        tMaterial.setParameter("wplant", config.getwPlant());
        materials = tMaterial.getResultList();

        DefaultComboBoxModel result = new DefaultComboBoxModel();
        for (Material m : materials) {
            result.addElement(m);
        }
        return result;
    }

    public WeightTicket findWeightTicket(WeightTicket weightTicket, int id, Integer seq) {
        com.gcs.wb.jpa.repositorys.WeightTicketRepository repository = new com.gcs.wb.jpa.repositorys.WeightTicketRepository();
        return repository.findByIdSeqDay(id, seq);
    }

    public String getSoNiemXa(String pWtId) {
        return weightTicketRepository.getSoNiemXa(pWtId);
    }

    public void getTicketIndex(String maphieu, JTextField txtPONum, WeightTicket weightTicket, JRadioButton rbtPO) {
        String Posto = "";
        List wtxxx = weightTicketRepository.getTicketIndex(maphieu);
        if (wtxxx != null) {
            for (Object obj : wtxxx) {
                Object[] wt = (Object[]) obj;
                Posto = wt[2].toString().trim();
                txtPONum.setText(Posto);
                weightTicket.setAbbr(wt[3].toString().trim());
                rbtPO.setSelected(true);
            }

        }
    }


    public PurchaseOrder findPurOrder(String poNum) {
        return purchaseOrderRepository.findByPoNumber(poNum);
    }


    public PurchaseOrder getSapPurOrder(String poNum) throws Exception {
        return sapService.getPurchaseOrder(poNum);
    }

    public void revertCompletedDO(List<String> completedDOs, List<OutboundDetail> OutbDetailsV2, List<OutboundDelivery> outbDels, WeightTicket weightTicket, List<OutboundDetail> outDetails_lits, Session sapSession) {
        DORevertBapi bapi = null;
        for (String item : completedDOs) {
            bapi = new DORevertBapi(item);

            OutboundDelivery od_temp = new OutboundDelivery(item);
            GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, od_temp, outDetails_lits, weightTicket);

            bapi.setWeightticket(stWT);
            try {
                sapSession.execute(bapi);
            } catch (Exception ex) {
                //do nothing
            }
        }

        // active entityManager
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        if (OutbDetailsV2 != null) {
            for (OutboundDetail outbDetail : OutbDetailsV2) {
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
            OutboundDelivery od, List<OutboundDetail> od_v2_list, WeightTicket weightTicket) {
        GoodsMvtWeightTicketStructure stWT = null;
        if (wt == null) {
            return stWT;
        }
        String tempWTID = new Integer(wt.getId()).toString();
        tempWTID = tempWTID.concat(Conversion_Exit.Conv_output_num(String.valueOf(weightTicket.getSeqDay()), 3));
        stWT = new GoodsMvtWeightTicketStructure(weightTicket.getWplant(),
                weightTicket.getWbId(),
                tempWTID);
//          outb_details_v2
        OutboundDetail od_v2 = null;
        if (od != null && od_v2_list != null) {
            for (OutboundDetail od_v2_tmp : od_v2_list) {
                if (od_v2_tmp.getDeliveryOrderNo() != null
                        && od_v2_tmp.getDeliveryOrderNo().equals(od.getDeliveryOrderNo())) {
                    od_v2 = od_v2_tmp;
                    break;
                }
            }
        }

        stWT.setCAT_TYPE(String.valueOf(wt.getRegType()));
        stWT.setDO_WT(wt.getDeliveryOrderNo());
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
        stWT.setKUNNR(wt.getKunnr());
        stWT.setLIFNR(wt.getAbbr());
        stWT.setMATID(wt.getMatnrRef());
        stWT.setMATNAME(wt.getRegItemDescription());
        stWT.setMVT_TYPE(wt.getMoveType());
        if ((stWT.getMVT_TYPE() == null || stWT.getMVT_TYPE().isEmpty())
                && od != null && od.getBwart() != null) {
            stWT.setMVT_TYPE(od.getBwart());
        }
        stWT.setNTEXT(wt.getText());
        stWT.setPO_NUMBER(wt.getEbeln());
        stWT.setREGQTY_WT(wt.getRegItemQuantity());
        stWT.setREGQTY(od != null ? od.getLfimg() : BigDecimal.ZERO);
        stWT.setSALEDT(od_v2 != null ? od_v2.getBzirk() : "");
        stWT.setSDATE(wt.getSTime());
        stWT.setSHIPTYPE(od != null ? od.getTraty() : "");
        stWT.setSLOC(wt.getLgort());
        stWT.setSSCALE(wt.getSScale());
        stWT.setSTIME(wt.getSTime());
        stWT.setTRANSID(wt.getPlateNo());
        stWT.setUNIT(wt.getUnit());
        stWT.setUSERNAME(wt.getSCreator());
        stWT.setVTYPE(od != null ? od.getBwtar() : "");
        stWT.setBatch(od != null ? od.getCharg() : "");
        stWT.setLfart(od != null ? od.getLfart() : "");

        return stWT;
    }

    public Date getServerTime() {
        Date now = null;

        try {
            Object rs = con.get_server_time();

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
        return con.CheckPOSTO(purOrder.getMaterial());
    }

    public Double CheckMatStock(String matnr, String plant, String sloc, String batch) {
        Double remaining = 0d;

        MatAvailableBapi bapi = new MatAvailableBapi();
        bapi.setMaterial(matnr);
        bapi.setPlant(plant);
        bapi.setSloc(sloc);
        bapi.setBatch(batch);
        bapi.setUnit("TO");
        WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
        List<MatAvailableStructure> stocks = bapi.getWmdvex();
        if (!stocks.isEmpty() && stocks.get(0).getCom_qty() != null) {
            remaining = stocks.get(0).getCom_qty().doubleValue();
        }
        return remaining;
    }

    public Object getGrDoMigoBapi(WeightTicket wt, WeightTicket weightTicket, OutboundDelivery outbDel, List<OutboundDetail> outDetails_lits, int timeFrom, int timeTo) {
        String doNum = null;
        if (outbDel != null) {
            doNum = outbDel.getDeliveryOrderNo();
        }
        config = WeighBridgeApp.getApplication().getConfig();
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += wt.getTrailerId();
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
        Date stime = null;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        header.setPstngDate(DateUtil.stripTime(stime));
        // >> end of modified      

        if (outbDel == null) {
            header.setRefDocNo(wt.getDeliveryOrderNo());
        } else {
            header.setRefDocNo(doNum);
        }
        header.setBillOfLading(plateCombine);
        header.setGrGiSlipNo(wt.getDriverIdNo());
        if (outbDel == null) {
            header.setHeaderText(wt.getDeliveryOrderNo());
        } else {
            header.setHeaderText(doNum);
        }

        GoodsMvtItemDoStructure tab_wa = new GoodsMvtItemDoStructure();
        List<GoodsMvtItemDoStructure> tab = new ArrayList<GoodsMvtItemDoStructure>();

        //get do details for current do
        OutboundDetail item = null;
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
            tab_wa.setDeliv_numb(wt.getDeliveryOrderNo());
            tab_wa.setDeliv_numb_to_search(wt.getDeliveryOrderNo());
        } else {
            tab_wa.setDeliv_numb(doNum);
            tab_wa.setDeliv_numb_to_search(doNum);
        }
        tab_wa.setDeliv_item(wt.getItem());
        tab_wa.setDeliv_item_to_search(wt.getItem());
        tab_wa.setMove_type("101");
        tab_wa.setPlant(config.getwPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setGr_rcpt(wt.getSCreator());
        if (outbDel == null) {
            tab_wa.setEntry_qnt(wt.getGQty());
        } else {
            tab_wa.setEntry_qnt(kl_total);
        }
        tab_wa.setEntry_uom(wt.getUnit());
//        tab_wa.setEntry_uom_iso(wt.getUnit());
        if (wt.getNoMoreGr() != null && wt.getNoMoreGr() == '2') {
            tab_wa.setNo_more_gr("X");
        } else {
            tab_wa.setNo_more_gr(null);
        }
        tab_wa.setMove_reas(wt.getMoveReas());
        tab_wa.setItem_text(wt.getText());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    public Object getGrPoMigoBapi(WeightTicket wt, WeightTicket weightTicket, int timeFrom, int timeTo) {
        config = WeighBridgeApp.getApplication().getConfig();
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += wt.getTrailerId();
        }
        GoodsMvtPoCreateBapi bapi = new GoodsMvtPoCreateBapi();
        GoodsMvtHeaderStructure header = new GoodsMvtHeaderStructure();

        header.setDocDate(DateUtil.stripTime(wt.getSTime()));

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, null, null, weightTicket);
        bapi.setWeightticket(stWT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime = null;

        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        header.setPstngDate(DateUtil.stripTime(stime));

        header.setBillOfLading(plateCombine);
        header.setGrGiSlipNo(wt.getDriverIdNo());

        List<GoodsMvtItemPoStructure> tab = new ArrayList<GoodsMvtItemPoStructure>();
        GoodsMvtItemPoStructure tab_wa = new GoodsMvtItemPoStructure();

        tab_wa.setPo_number(wt.getEbeln());
        tab_wa.setPo_item(wt.getItem());
        tab_wa.setMove_type("101");
        tab_wa.setPlant(config.getwPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setGr_rcpt(wt.getSCreator());
        tab_wa.setEntry_qnt(wt.getGQty());
        tab_wa.setEntry_uom(wt.getUnit());

        if (wt.getNoMoreGr() != null && wt.getNoMoreGr() == '2') {
            tab_wa.setNo_more_gr("X");
        } else {
            tab_wa.setNo_more_gr(null);
        }
        tab_wa.setMove_reas(wt.getMoveReas());
        tab_wa.setItem_text(wt.getText());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }


    public Object getGi541MigoBapi(WeightTicket wt, WeightTicket weightTicket, int timeFrom, int timeTo, PurchaseOrder purOrder, JRadioButton rbtOutward) {
        config = WeighBridgeApp.getApplication().getConfig();
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
        Date stime = null;
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

        List<GoodsMvtItemPoStructure> tab = new ArrayList<GoodsMvtItemPoStructure>();
        GoodsMvtItemPoStructure tab_wa = new GoodsMvtItemPoStructure();

        tab_wa.setMaterial(wt.getMatnrRef());
        tab_wa.setPlant(config.getwPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setMove_type("541");
        tab_wa.setMvt_ind(null);
        tab_wa.setEntry_qnt(wt.getGQty());

        tab_wa.setEntry_uom(wt.getUnit());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setVendor(purOrder.getVendor());

        tab_wa.setNo_more_gr(null);

        tab_wa.setItem_text(wt.getText());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    public Object getGiMB1BBapi(WeightTicket wt, WeightTicket weightTicket, int timeFrom, int timeTo, JRadioButton rbtOutward) {
        config = WeighBridgeApp.getApplication().getConfig();
        String vendorNo = null;
        String headertxt = null;
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
        Date stime = null;
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

        List<GoodsMvtItemPoStructure> tab = new ArrayList<GoodsMvtItemPoStructure>();
        GoodsMvtItemPoStructure tab_wa = new GoodsMvtItemPoStructure();

        tab_wa.setMaterial(wt.getMatnrRef());
        tab_wa.setPlant(config.getwPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setMove_type(wt.getMoveType());
        tab_wa.setMvt_ind(null);
        tab_wa.setEntry_qnt(wt.getGQty());
        tab_wa.setEntry_uom(wt.getUnit());

        tab_wa.setMoveMat(wt.getRecvMatnr());
        tab_wa.setMovePlant(wt.getRecvPlant());
        tab_wa.setMoveSLoc(wt.getRecvLgort());
        tab_wa.setMoveBatch(wt.getRecvCharg());

        tab_wa.setMove_reas(wt.getMoveReas());
        tab_wa.setItem_text(wt.getText());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    public Object getDoCreate2PGI(WeightTicket wt, OutboundDelivery outbDel, WeightTicket weightTicket, int timeFrom, int timeTo, List<OutboundDetail> outDetails_lits) {
        String doNum = null;
        if (outbDel != null) {
            doNum = outbDel.getDeliveryOrderNo();
        }
        config = WeighBridgeApp.getApplication().getConfig();
        DOCreate2PGIBapi bapi = new DOCreate2PGIBapi();

        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += "|" + wt.getTrailerId();
        }
        VbkokStructure wa = new VbkokStructure();
        if (outbDel == null) {
            wa.setVbeln_vl(wt.getDeliveryOrderNo());
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
        Date stime = null;

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
        wa.setTraty("0004");
        wa.setTraid(plateCombine);
        wa.setLifex(wt.getDriverName());
        bapi.setVbkok_wa(wa);

        //get do details for current do
        OutboundDetail item = null;
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
                && wt.getRegItemDescription() != null
                && wt.getRegItemDescription().toLowerCase().indexOf("bao") >= 0
                && wt.getMatnrRef() != null) {
            _StockTransItems.add(new OutbDeliveryCreateStoStructure(wt.getEbeln(), wt.getItem(), wt.getRegItemQuantity(), wt.getUnit()));
        } else if (outbDel == null) {
            _StockTransItems.add(new OutbDeliveryCreateStoStructure(wt.getEbeln(), wt.getItem(), wt.getGQty(), wt.getUnit()));
        } else {
            _StockTransItems.add(new OutbDeliveryCreateStoStructure(wt.getEbeln(), wt.getItem(), kl_total, wt.getUnit()));
        }
        bapi.setStockTransItems(_StockTransItems);

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, outbDel, outDetails_lits, weightTicket);
        bapi.setWeightticket(stWT);
        List<VbpokStructure> tab = new ArrayList<VbpokStructure>();
        VbpokStructure tab_wa = new VbpokStructure();
        if (outbDel == null) {
            tab_wa.setVbeln_vl(wt.getDeliveryOrderNo());
        } else {
            tab_wa.setVbeln_vl(doNum);
        }
        tab_wa.setPosnr_vl(wt.getItem());
        tab_wa.setVbeln(tab_wa.getVbeln_vl());
        tab_wa.setPosnn(tab_wa.getPosnr_vl());
        tab_wa.setMatnr(wt.getMatnrRef());
        tab_wa.setWerks(config.getwPlant());
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
        tab_wa.setVrkme(wt.getUnit());
        tab_wa.setMeins(wt.getUnit());
        tab.add(tab_wa);
        bapi.setVbpok_tab(tab);

        return bapi;
    }

    public Object getPgmVl02nBapi(WeightTicket wt, OutboundDelivery outbDel, WeightTicket weightTicket, int timeFrom, int timeTo, List<OutboundDetail> outDetails_lits) {
        String doNum = null;
        if (outbDel != null) {
            doNum = outbDel.getDeliveryOrderNo();
        }
        config = WeighBridgeApp.getApplication().getConfig();
        String plateCombine = wt.getPlateNo();
        if (wt.getTrailerId() != null && !wt.getTrailerId().trim().isEmpty()) {
            plateCombine += "|" + wt.getTrailerId();
        }
        WsDeliveryUpdateBapi bapi = new WsDeliveryUpdateBapi();
        if (outbDel == null) {
            bapi.setDelivery(wt.getDeliveryOrderNo());
        } else {
            bapi.setDelivery(doNum);
        }
        bapi.setUpdate_picking("X");

        //get do details for current do
        OutboundDetail item = null;
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

        if (wt.getPpProcord() != null && wt.getPpProcord().length() == 12) {
            bapi.setProc_ord_id(wt.getPpProcord());
            if (outbDel == null) {
                bapi.setYield(wt.getGQty());
            } else {
                bapi.setYield(kl_total);
            }
        }
        bapi.setYield(kl_total);
        VbkokStructure wa = new VbkokStructure();
        if (outbDel == null) {
            wa.setVbeln_vl(wt.getDeliveryOrderNo());
        } else {
            wa.setVbeln_vl(doNum);
        }
        wa.setKodat(DateUtil.stripTime(wt.getFTime()));
        wa.setKouhr(DateUtil.stripDate(wt.getFTime()));
        wa.setKomue("X");
        wa.setWabuc("X");

        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime = null;

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
        wa.setTraty("0004");
        wa.setTraid(plateCombine);
        wa.setLifex(wt.getDriverName());
        bapi.setVbkok_wa(wa);

        List<VbpokStructure> tab = new ArrayList<VbpokStructure>();
        VbpokStructure tab_wa = new VbpokStructure();
        if (outbDel == null) {
            tab_wa.setVbeln_vl(wt.getDeliveryOrderNo());
        } else {
            tab_wa.setVbeln_vl(doNum);
        }
        tab_wa.setPosnr_vl(wt.getItem());
        tab_wa.setVbeln(tab_wa.getVbeln_vl());
        tab_wa.setPosnn(tab_wa.getPosnr_vl());
        tab_wa.setMatnr(wt.getMatnrRef());
        tab_wa.setWerks(config.getwPlant());
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

        tab_wa.setVrkme(wt.getUnit());
        tab_wa.setMeins(outbDel.getMeins());
        tab.add(tab_wa);

        VbpokStructure tab_wa_f = new VbpokStructure();
        tab_wa_f.setWerks(config.getwPlant());
        tab_wa_f.setLgort(wt.getLgort());
        tab_wa_f.setCharg(wt.getCharg());
        tab_wa_f.setLianp("X");
        tab_wa_f.setVrkme(wt.getUnit());
        tab_wa_f.setMeins(outbDel.getMeins());

        if (outbDel.getDeliveryItemFree() != null && (outbDel.getDeliveryItemFree() == null ? "" != null : !outbDel.getDeliveryItemFree().equals(""))) {
            if (outbDel == null) {
                tab_wa_f.setVbeln_vl(wt.getDeliveryOrderNo());
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

//    public DefaultComboBoxModel getReasonModel() {
//        Movement mvt = new Movement();
//
//        try {
//            MovementRepository movementRepository = new MovementRepository();
//            String language = WeighBridgeApp.getApplication().getLogin().getLang().toString();
//            mvt = movementRepository.findByMandtBwartSpras(client, language);
//
//        } catch (NoResultException ex) {
//            MvtGetDetailBapi bMvt = new MvtGetDetailBapi(client, "101");
//            WeighBridgeApp.getApplication().getSAPSession().execute(bMvt);
//            mvt = new Movement(new MovementPK(client, bMvt.getItem().getBwart()), bMvt.getItem().getSpras());
//            mvt.setBtext(bMvt.getItem().getBtext());
//            if (!entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().begin();
//            }
//            entityManager.persist(mvt);
//            entityManager.getTransaction().commit();
//            entityManager.clear();
//        }
//        String bwart = mvt.getMovementPK().getBwart().trim();
//        ReasonRepository reasonRepository = new ReasonRepository();
//
//        List<Reason> reasons = reasonRepository.findByMandtBwart(client, bwart);
//
//        if (reasons.isEmpty()) {
//            MvtReasonsGetListBapi bReason = new MvtReasonsGetListBapi(client, "101");
//            WeighBridgeApp.getApplication().getSAPSession().execute(bReason);
//            List<MvtReasonsGetListStructure> brReasons = bReason.getTdMvtsReasons();
//            if (!entityManager.getTransaction().isActive()) {
//                entityManager.getTransaction().begin();
//            }
//            for (MvtReasonsGetListStructure r : brReasons) {
//                Reason reason = new Reason(r.getMandt(), r.getBwart(), r.getGrund());
//                reason.setGrtxt(r.getGrtxt());
//                entityManager.persist(reason);
//                reasons.add(reason);
//            }
//            entityManager.getTransaction().commit();
//            entityManager.clear();
//        }
//
//
//        return new DefaultComboBoxModel(reasons.toArray());
//    }
    public void printWT(WeightTicket wt, boolean reprint, String ximang, List<OutboundDelivery> outbDel_list, List<OutboundDetail> outDetails_lits,
            OutboundDelivery outbDel, JRadioButton rbtMisc, JRadioButton rbtPO, boolean isStage1, JRootPane rootPane) {
        config = WeighBridgeApp.getApplication().getConfig();
        OutboundDelivery item = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Long bags = null;
            if (outbDel_list == null || outbDel_list.isEmpty() || rbtMisc.isSelected() || rbtPO.isSelected()) {
                // can posto xi mang 
                map.put("P_MANDT", WeighBridgeApp.getApplication().getConfig().getsClient());
                map.put("P_WPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
                map.put("P_ID", wt.getId());
                map.put("P_DAYSEQ", wt.getSeqDay());
                map.put("P_REPRINT", reprint);
                map.put("P_ADDRESS", config.getRptId());
                if (!wt.isDissolved()) {
                    Double tmp;

                    if (wt.getMatnrRef() != null && wt.getMatnrRef().equalsIgnoreCase("000000101130400008")) // Tuanna - for bag 40K 27.04.2013
                    {
                        tmp = ((wt.getRegItemQuantity().doubleValue()) * 1000d) / 40d;
                    } else {
                        tmp = ((wt.getRegItemQuantity().doubleValue()) * 1000d) / 50d;
                    }

                    if ((tmp == null || tmp == 0) && wt.getRegItemQuantity() != null) {
                        if (wt.getMatnrRef() != null && wt.getMatnrRef().equalsIgnoreCase("000000101130400008")) // Tuanna - for bag 40K 27.04.2013                           
                        {
                            tmp = ((wt.getRegItemQuantity().doubleValue()) * 1000d) / 40d;
                        } else {
                            tmp = ((wt.getRegItemQuantity().doubleValue()) * 1000d) / 50d;
                        }
                    }
                    bags = Math.round(tmp);
                    if (bags != null) {
                        map.put("P_PCB40BAG", bags);
                    }
                }
                String reportName1 = "";
                if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                    reportName1 = "./rpt/rptBT/WeightTicket.jasper";
                } else {
                    reportName1 = "./rpt/rptPQ/WeightTicket.jasper";
                }

                jreportService.printReport(map, reportName1);


            } else {
                for (int i = 0; i < outbDel_list.size(); i++) {
                    item = outbDel_list.get(i);
                    OutboundDetail out_detail = null;
                    BigDecimal sscale = BigDecimal.ZERO;
                    BigDecimal gqty = BigDecimal.ZERO;
                    BigDecimal lfimg_ori = BigDecimal.ZERO;
                    for (int k = 0; k < outDetails_lits.size(); k++) {
                        out_detail = outDetails_lits.get(k);
                        if (out_detail.getDeliveryOrderNo().contains(item.getDeliveryOrderNo())) {
                            if (out_detail.getLfimg() != null) {
                                lfimg_ori = lfimg_ori.add(out_detail.getLfimg());
                            }
                        }
                    }
                    map.put("P_PAGE", "Trang ".concat(String.valueOf(i + 1).concat("/").concat(String.valueOf(outbDel_list.size()))));
                    map.put("P_TOTAL_QTY_ORI", String.valueOf(lfimg_ori));
                    if (wt.getFScale() != null) {
                        outbDel = item;
                    }
                    map.put("P_MANDT", WeighBridgeApp.getApplication().getConfig().getsClient());
                    map.put("P_WPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
                    map.put("P_ID", wt.getId());
                    map.put("P_DAYSEQ", wt.getSeqDay());
                    map.put("P_REPRINT", reprint);
                    map.put("P_ADDRESS", config.getRptId());
                    map.put("P_DEL_NUM", outbDel.getDeliveryOrderNo());
                    if (wt.getSScale() != null) {
                        BigDecimal n_1000 = new BigDecimal(1000);
                        sscale = wt.getSScale().divide(n_1000);
                    }
                    if (wt.getGQty() != null) {
                        gqty = wt.getGQty();
                    }
                    if (reprint && wt.isPosted()) {
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
                        map.put("P_TOTAL_QTY", String.valueOf(outbDel.getLfimg().add(outbDel.getFreeQty())));
                        if (outbDel != null && (outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF") || outbDel.getLfart().equalsIgnoreCase("NL"))) {
                            Double tmp;
                            // Double tmp = ((outbDel.getLfimg().doubleValue() + outbDel.getFreeQty().doubleValue()) * 1000d) / 50d;
                            if (outbDel.getMatnr().equalsIgnoreCase("000000101130400008")) // Tuanna - crazy lắm lun hix ai chơi hardcode  for bag 40K 27.04.2013
                            //  Double tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 40d;
                            {
                                tmp = ((outbDel.getLfimg().doubleValue() + outbDel.getFreeQty().doubleValue()) * 1000d) / 40d;
                            } else //  Double tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 50d;
                            {
                                tmp = ((outbDel.getLfimg().doubleValue() + outbDel.getFreeQty().doubleValue()) * 1000d) / 50d;
                            }
                            bags = Math.round(tmp);
                        }
                    } else {
                        map.put("P_TOTAL_QTY", String.valueOf(outbDel.getLfimg()));
                        Double tmp;
                        if (outbDel != null && (outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF") || outbDel.getLfart().equalsIgnoreCase("NL"))) {
                            if (outbDel.getMatnr().equalsIgnoreCase("000000101130400008")) // Tuanna - for bag 40K  27.04.2013
                            {
                                tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 40d;
                            } else {
                                tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 50d;
                            }

                            bags = Math.round(tmp);
                        }

                    }

                    if (bags != null) {
                        map.put("P_PCB40BAG", bags);
                    }
                    if (outbDel.getMatDoc() != null) {
                        map.put("P_MAT_DOC", outbDel.getMatDoc());
                    }

                    String reportName = null;
                    String path = "";
                    if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                        path = "./rpt/rptBT/";  // ->> DO cai nay ne e
                    } else {
                        path = "./rpt/rptPQ/";
                    }
                    if (rbtMisc.isSelected() || rbtPO.isSelected()) {

                        reportName = path.concat("WeightTicket.jasper");
                        //reportName = path.concat("WeightTicket.jasper");
                    } else {

                        reportName = path.concat("WeightTicket_NEW.jasper");
                        //reportName = path.concat("WeightTicket.jasper");
                    }

                    jreportService.printReport(map, reportName);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, ex);
        }
    }
}
