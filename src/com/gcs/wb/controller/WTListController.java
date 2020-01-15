/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.service.WTListService;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

/**
 *
 * @author THANGPT
 */
public class WTListController {

    private WTListService wTListService = new WTListService();

    public DefaultComboBoxModel getMatsModel() {
        return wTListService.getMatsModel();
    }

    public Object[][] findWTsDoIn(String month, String year, String tagent, String matnr, List<Character> modes,
            JRadioButton rbtDissolved, JRadioButton rbtPosted, JComboBox cbxTAgent) throws Exception {
        return wTListService.findWTsDoIn(month, year, tagent, matnr, modes, rbtDissolved, rbtPosted, cbxTAgent);
    }

    public Map<String, Object> getParamPrintReport(JComboBox cbxTAgent, JComboBox cbxMonth, JComboBox cbxYear) {
        return wTListService.getParamPrintReport(cbxTAgent, cbxMonth, cbxYear);
    }

    public String getReportName() {
        return wTListService.getReportName();
    }
    
    public DefaultComboBoxModel getTAgentsModel() {
        return wTListService.getTAgentsModel();
    }
}
