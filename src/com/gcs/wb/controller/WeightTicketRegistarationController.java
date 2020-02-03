/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.service.JReportService;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.WeightTicketRegistarationService;
import com.gcs.wb.service.WeightTicketService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author THANGPT
 */
public class WeightTicketRegistarationController {

    private WeightTicketRegistarationService wTRegService = new WeightTicketRegistarationService();
    private WeightTicketService weightTicketService = new WeightTicketService();
    JReportService jreportService = new JReportService();

    public List<WeightTicket> listWeightTicketsDoInBackground(JXDatePicker dpFrom, JXDatePicker dpTo, JComboBox cbxType, JComboBox cbxTimeFrom, JComboBox cbxTimeTo, JTextField txtNguoitao, JRadioButton rbtDissolved, JRadioButton rbtPosted, JRadioButton rbtStateAll, JTextField txtTaixe, JTextField txtBienSo) throws Exception {
        return wTRegService.listWeightTicketsDoInBackground(dpFrom, dpTo, cbxType, cbxTimeFrom, cbxTimeTo, txtNguoitao, rbtDissolved, rbtPosted, rbtStateAll, txtTaixe, txtBienSo);
    }

    public Object[][] handleWtData(String getMode, Object[][] wtData, List<WeightTicket> weightTicketList, AppConfig config, Object[] wtCols, String sVendor) {
        return wTRegService.handleWtData(getMode, wtData, weightTicketList, config, wtCols, sVendor);
    }

    public String getReportName() {
        String reportName = null;
        if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
            reportName = "./rpt/rptBT/WTList.jasper";
        } else {
            reportName = "./rpt/rptPQ/WTList.jasper";
        }
        return reportName;
    }

    public Map<String, Object> getPrintReport(String kFrom, String kTo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("P_PNAME_RPT", WeighBridgeApp.getApplication().getSapSetting().getNameRpt());
        params.put("P_PADDRESS", WeighBridgeApp.getApplication().getSapSetting().getAddress());
        params.put("P_PPHONE", WeighBridgeApp.getApplication().getSapSetting().getPhone());
        params.put("P_PFAX", WeighBridgeApp.getApplication().getSapSetting().getFax());
        params.put("P_FROM", kFrom);
        params.put("P_TO", kTo);
        return params;
    }

    public void hanldeCheckDOOutbDel(OutboundDelivery sapOutb, Customer kunnr, Customer kunag, Vendor lifnr, Customer sapKunnr, Customer sapKunag, Vendor sapLifnr) {
        wTRegService.hanldeCheckDOOutbDel(sapOutb, kunnr, kunag, lifnr, sapKunnr, sapKunag, sapLifnr);
    }

    public void printReport(Map<String, Object> map, String reportName) {
        jreportService.printReport(map, reportName);
    }

    public void printRegWT(WeightTicket wt, boolean reprint) {
        AppConfig config = WeighBridgeApp.getApplication().getConfig();
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("P_CLIENT", WeighBridgeApp.getApplication().getConfig().getsClient());
        map.put("P_WPLANT", WeighBridgeApp.getApplication().getConfig().getwPlant());
        map.put("P_ID", wt.getId());
        map.put("P_DAYSEQ", wt.getSeqDay());
        map.put("P_REPRINT", reprint);
        map.put("P_ADDRESS", config.getRptId());
        String reportName = null;
        if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
            //reportName = "./rpt/rptBT/RegWT_HP.jasper";
            reportName = "./rpt/rptBT/RegWT_HP.jasper";
        } else {
            //reportName = "./rpt/rptPQ/RegWT.jasper";
            reportName = "./rpt/rptPQ/RegWT.jasper";
        }
        jreportService.printReport(map, reportName);
    }
}
