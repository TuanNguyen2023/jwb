/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import com.gcs.wb.base.enums.ModeEnum;
import com.gcs.wb.jpa.entity.BatchStock;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.MaterialConstraint;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.OutboundDeliveryDetail;
import com.gcs.wb.jpa.entity.PurchaseOrder;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.entity.Variant;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.service.WeightTicketService;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import org.hibersap.session.Session;

/**
 *
 * @author THANGPT
 */
public class WeightTicketController {

    WeightTicketService weightTicketService = new WeightTicketService();

    public DefaultComboBoxModel getCustomerByMaNdt() {
        return weightTicketService.getCustomerByMaNdt();
    }

    public List<BatchStock> getBatchStocks(SLoc selSloc, WeightTicket weightTicket) {
        return weightTicketService.getBatchStocks(selSloc, weightTicket);
    }

    public void getSyncBatchStocks(SLoc selSloc, WeightTicket weightTicket) {
        weightTicketService.getSyncBatchStocks(selSloc, weightTicket);
    }

    public DefaultComboBoxModel setCbxBatch(List<BatchStock> batchs) {
        return weightTicketService.setCbxBatch(batchs);

    }

    public void savePostAgainActionPerformed(WeightTicket weightTicket) {
        weightTicketService.savePostAgainActionPerformed(weightTicket);
    }

    public void saveKunnrItemStateChanged(WeightTicket weightTicket) {
        weightTicketService.saveKunnrItemStateChanged(weightTicket);
    }

    public Date setTimeWeightTicket(String[] time) {
        return weightTicketService.setTimeWeightTicket(time);

    }

    public String getMsg() {
        return weightTicketService.getMsg();
    }

    public DefaultComboBoxModel getMaterialList() {
        return weightTicketService.getMaterialList();
    }

    public WeightTicket findWeightTicket(WeightTicket weightTicket, String id) {
        return weightTicketService.findWeightTicket(weightTicket, id);
    }

    public PurchaseOrder findPurOrder(String poNum) {
        return weightTicketService.findPurOrder(poNum);
    }

    public PurchaseOrder getSapPurOrder(String poNum) throws Exception {
        return weightTicketService.getSapPurOrder(poNum);
    }

    public void revertCompletedDO(List<String> completedDOs, List<OutboundDeliveryDetail> OutbDetailsV2, List<OutboundDelivery> outbDels, WeightTicket weightTicket, List<OutboundDeliveryDetail> outDetails_lits, Session sapSession) {
        weightTicketService.revertCompletedDO(completedDOs, OutbDetailsV2, outbDels, weightTicket, outDetails_lits, sapSession);
    }

    public GoodsMvtWeightTicketStructure fillWTStructure(WeightTicket wt,
            OutboundDelivery od, List<OutboundDeliveryDetail> od_v2_list, WeightTicket weightTicket) {
        return weightTicketService.fillWTStructure(wt, od, od_v2_list, weightTicket);
    }

    public Date getServerTime() {
        return weightTicketService.getServerTime();
    }

    public Material checkPOSTO(PurchaseOrder purOrder) throws Exception {
        return weightTicketService.checkPOSTO(purOrder);
    }

    public Double CheckMatStock(String matnr, String plant, String sloc, String batch) {
        return weightTicketService.CheckMatStock(matnr, plant, sloc, batch);
    }

    public Object getGrDoMigoBapi(WeightTicket wt, WeightTicket weightTicket, OutboundDelivery outbDel, List<OutboundDeliveryDetail> outDetails_lits, int timeFrom, int timeTo) {
        return weightTicketService.getGrDoMigoBapi(wt, weightTicket, outbDel, outDetails_lits, timeFrom, timeTo);
    }

    public Object getGrPoMigoBapi(WeightTicket wt, WeightTicket weightTicket, PurchaseOrder purchaseOrder, int timeFrom, int timeTo) {
        return weightTicketService.getGrPoMigoBapi(wt, weightTicket, purchaseOrder, timeFrom, timeTo);
    }

    public Object getGi541MigoBapi(WeightTicket wt, WeightTicket weightTicket, int timeFrom, int timeTo, PurchaseOrder purOrder, JRadioButton rbtOutward) {
        return weightTicketService.getGi541MigoBapi(wt, weightTicket, timeFrom, timeTo, purOrder, rbtOutward);
    }

    public Object getGiMB1BBapi(WeightTicket wt, WeightTicket weightTicket, int timeFrom, int timeTo, JRadioButton rbtOutward) {
        return weightTicketService.getGiMB1BBapi(wt, weightTicket, timeFrom, timeTo, rbtOutward);
    }

    public Object getDoCreate2PGI(WeightTicket wt, OutboundDelivery outbDel, WeightTicket weightTicket, int timeFrom, int timeTo, List<OutboundDeliveryDetail> outDetails_lits) {
        return weightTicketService.getDoCreate2PGI(wt, outbDel, weightTicket, timeFrom, timeTo, outDetails_lits);
    }

    public Object getPgmVl02nBapi(WeightTicket wt, OutboundDelivery outbDel, WeightTicket weightTicket, String modeFlg, int timeFrom, int timeTo, List<OutboundDeliveryDetail> outDetails_lits) {
        return weightTicketService.getPgmVl02nBapi(wt, outbDel, weightTicket, modeFlg, timeFrom, timeTo, outDetails_lits);
    }

    public Object getMvtPOSTOCreatePGI(WeightTicket wt, WeightTicket weightTicket, String posto, int timeFrom, int timeTo) {
        return weightTicketService.getMvtPOSTOCreatePGI(wt, weightTicket, posto, timeFrom, timeTo);
    }

    public void printWT(WeightTicket wt, boolean reprint, String ximang, List<OutboundDelivery> outbDel_list, List<OutboundDeliveryDetail> outDetails_lits,
            OutboundDelivery outbDel, String txtPONo, boolean isStage1, JRootPane rootPane) {
        weightTicketService.printWT(wt, reprint, ximang, outbDel_list, outDetails_lits, outbDel, txtPONo, isStage1, rootPane);
    }

//    public int getCountSingal() {
//        return weightTicketService.getCountSingal();
//    }
    public SLoc findByLgort(String lgort) {
        return weightTicketService.findByLgort(lgort);
    }

    public Variant findByParam(String param) {
        return weightTicketService.findByParam(param);
    }

    public BatchStock findByWerksLgortMatnrCharg(String werks, String lgort, String matnr, String charg) {
        return weightTicketService.findByWerksLgortMatnrCharg(werks, lgort, matnr, charg);
    }

    public PurchaseOrder findByPoNumber(String poNumber) {
        return weightTicketService.findByPoNumber(poNumber);
    }

    public Material checkPOSTO(String matnr) throws Exception {
        return weightTicketService.CheckPOSTO(matnr);
    }

    public OutboundDelivery findByMandtOutDel(String delnum) throws Exception {
        return weightTicketService.findByMandtOutDel(delnum);
    }

    public String getModeProcedure(String mode) {
        for (ModeEnum v : ModeEnum.values()) {
            if (v.name().equals(mode)) {
                return v.getName();
            }
        }
        return null;
    }
    
    public MaterialConstraint getMaterialConstraintByMatnr(String matnr){
        return weightTicketService.getMaterialConstraintByMatnr(matnr);
    }
}
