/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.WTRegService;
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
public class WTRegController {

    private WTRegService wTRegService = new WTRegService();
    
    public List<WeightTicket> listWeightTicketsDoInBackground(JXDatePicker dpFrom, JXDatePicker dpTo, JComboBox cbxType, JComboBox cbxTimeFrom, JComboBox cbxTimeTo, JTextField txtNguoitao, JRadioButton rbtDissolved, JRadioButton rbtPosted, JRadioButton rbtStateAll, JTextField txtTaixe, JTextField txtBienSo) throws Exception {
        return wTRegService.listWeightTicketsDoInBackground(dpFrom, dpTo, cbxType, cbxTimeFrom, cbxTimeTo, txtNguoitao, rbtDissolved, rbtPosted, rbtStateAll, txtTaixe, txtBienSo);
    }

    public Object[][] handleWtData(String getMode, Object[][] wtData, List<WeightTicket> weightTicketList, AppConfig config, Object[] wtCols, String sVendor) {
        return wTRegService.handleWtData(getMode, wtData, weightTicketList, config, wtCols, sVendor);
    }

    public String printReportDoInBackground(JXDatePicker dpFrom, JXDatePicker dpTo) {

        return wTRegService.printReportDoInBackground(dpFrom, dpTo);
    }

    public Map<String, Object> getPrintReport(JXDatePicker dpFrom, JXDatePicker dpTo) {
        return wTRegService.getPrintReport(dpFrom, dpTo);
    }

    public void hanldeCheckDOOutbDel(OutboundDelivery sapOutb, Customer kunnr, Customer kunag, Vendor lifnr, Customer sapKunnr, Customer sapKunag, Vendor sapLifnr) {
        wTRegService.hanldeCheckDOOutbDel(sapOutb, kunnr, kunag, lifnr, sapKunnr, sapKunag, sapLifnr);
    }
}
