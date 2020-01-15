/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.SAP2Local;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.CustomerPK;
import com.gcs.wb.jpa.entity.OutbDel;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.entity.VendorPK;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.WeightTicketPK;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.WTRegService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
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

    public Object[][] handleWtData(String getMode, Object[][] wtData, Object[][] wtRptData, List<WeightTicket> weightTicketList, AppConfig config, Object[] wtCols, Object[] wtRptCols, String sVendor) {
        return wTRegService.handleWtData(getMode, wtData, wtRptData, weightTicketList, config, wtCols, wtRptCols, sVendor);
    }

    public String printReportDoInBackground(JXDatePicker dpFrom, JXDatePicker dpTo) {

        return wTRegService.printReportDoInBackground(dpFrom, dpTo);
    }

    public Map<String, Object> getPrintReport(JXDatePicker dpFrom, JXDatePicker dpTo) {
        return wTRegService.getPrintReport(dpFrom, dpTo);
    }

    public void hanldeCheckDOOutbDel(EntityManager entityManager, OutbDel sapOutb, Customer kunnr, Customer kunag, Vendor lifnr, Customer sapKunnr, Customer sapKunag, Vendor sapLifnr) {
        wTRegService.hanldeCheckDOOutbDel(entityManager, sapOutb, kunnr, kunag, lifnr, sapKunnr, sapKunag, sapLifnr);
    }
}
