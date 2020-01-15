/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.service.WeightTicketReportService;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author THANGPT
 */
public class WeightTicketReportController {
    
    private WeightTicketReportService weightTicketReportService = new WeightTicketReportService();
    
    public List<Character> getModeItemStateChanged(List<Character> modes, JComboBox cbxMode) {
        return weightTicketReportService.getModeItemStateChanged(modes, cbxMode);
    }

    public DefaultComboBoxModel getMaterialsModel() {
        return weightTicketReportService.getMaterialsModel();
    }

    public Object[][] findWeightTickets(Object[][] wtDatas, String month, String year, String tAgent, String matnr, List<Character> modes, JComboBox cbxStatus, JComboBox cbxTransportAgent) throws Exception {
        return weightTicketReportService.findWeightTickets(wtDatas, month, year, tAgent, matnr, modes, cbxStatus, cbxTransportAgent);
    }

    public Map<String, Object> getParamReport(JComboBox cbxTransportAgent, JComboBox cbxMonth, JComboBox cbxYear) {
        return weightTicketReportService.getParamReport(cbxTransportAgent, cbxMonth, cbxYear);
    }

    public String getReportName() {
        return weightTicketReportService.getReportName();
    }
    
    public DefaultComboBoxModel getTransportAgentsModel() {
        return weightTicketReportService.getTransportAgentsModel();
    }
}
