/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.service.DailyReportService;
import java.util.List;
import java.util.Map;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author THANGPT
 */
public class DailyReportController {
    
    DailyReportService dailyReportService = new DailyReportService();
    
    public List<WeightTicket> findByCreateDateRange(JXDatePicker dpDateFrom, JXDatePicker dpDateTo) {
        return dailyReportService.findByCreateDateRange(dpDateFrom, dpDateTo);
    }

    public Object[][] handleWtDatas(Object[][] wtDatas, List<WeightTicket> weightTicketList, Object[] wtColNames) {
        return dailyReportService.handleWtDatas(wtDatas, weightTicketList);
    }

    public String getReportName() {
        return dailyReportService.getReportName();
    }
    
    public Map<String, Object> getParamsReport(JXDatePicker dpDateFrom, JXDatePicker dpDateTo){
        return dailyReportService.getParamsReport(dpDateFrom, dpDateTo);
    }
}
