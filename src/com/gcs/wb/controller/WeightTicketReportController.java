/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.service.JReportService;
import com.gcs.wb.service.WeightTicketReportService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

/**
 *
 * @author THANGPT
 */
public class WeightTicketReportController {

    private WeightTicketReportService weightTicketReportService = new WeightTicketReportService();
    JReportService jreportService = new JReportService();

    public List<Character> getModeItemStateChanged(List<Character> modes, int mode) {
        return weightTicketReportService.getModeItemStateChanged(modes, mode);
    }

    public DefaultComboBoxModel getMaterialsModel() {
        return weightTicketReportService.getMaterialsModel();
    }

    public Object[][] findWeightTickets(Object[][] wtDatas, String month, String year, String tAgent, String matnr, List<Character> modes, int status, String transportAgentName) throws Exception {
        return weightTicketReportService.findWeightTickets(wtDatas, month, year, tAgent, matnr, modes, status, transportAgentName);
    }

    public Map<String, Object> getParamReport(JComboBox cbxTransportAgent, JComboBox cbxMonth, JComboBox cbxYear) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_PNAME_RPT", WeighBridgeApp.getApplication().getSapSetting().getNameRpt());
        params.put("P_PADDRESS", WeighBridgeApp.getApplication().getSapSetting().getAddress());
        params.put("P_PPHONE", WeighBridgeApp.getApplication().getSapSetting().getPhone());
        params.put("P_PFAX", WeighBridgeApp.getApplication().getSapSetting().getFax());
        params.put("P_TAGENT", ((TransportAgent) cbxTransportAgent.getSelectedItem()).getName());
        params.put("P_MONTH", cbxMonth.getSelectedItem().toString());
        params.put("P_YEAR", cbxYear.getSelectedItem().toString());
        return params;
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

    public DefaultComboBoxModel getTransportAgentsModel() {
        return weightTicketReportService.getTransportAgentsModel();
    }
    
    public void printReport(Map<String, Object> map, String reportName, JRTableModelDataSource data){
        jreportService.printReportDataSource(map, reportName, data);
    }
}
