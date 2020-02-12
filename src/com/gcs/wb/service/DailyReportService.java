/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.repositorys.WeightTicketRepository;
import com.gcs.wb.jpa.entity.WeightTicketDetail;
import com.gcs.wb.model.AppConfig;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author THANGPT
 */
public class DailyReportService {

    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    AppConfig appConfig = WeighBridgeApp.getApplication().getConfig();
    Object[] wtColNames = Constants.DailyReport.WT_COL_NAMES;

    public List<WeightTicket> findByCreateDateRange(JXDatePicker dpDateFrom, JXDatePicker dpDateTo) {

        List<WeightTicket> weightTickets = findByCreatedDateRange(dpDateFrom.getDate(), dpDateTo.getDate());
        return weightTickets;
    }
    
    public String getReportName() {
        String reportName = null;
        if (configuration.isModeNormal()) {
            reportName = "./rpt/rptBT/WTList.jasper";
        } else {
            reportName = "./rpt/rptPQ/WTList.jasper";
        }
        return reportName;
    }

    public Map<String, Object> getParamsReport(JXDatePicker dpDateFrom, JXDatePicker dpDateTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_PNAME_RPT", WeighBridgeApp.getApplication().getSapSetting().getNameRpt());
        params.put("P_PADDRESS", WeighBridgeApp.getApplication().getSapSetting().getAddress());
        params.put("P_PPHONE", WeighBridgeApp.getApplication().getSapSetting().getPhone());
        params.put("P_PFAX", WeighBridgeApp.getApplication().getSapSetting().getFax());
        params.put("P_FROM", dpDateFrom.getDate());
        params.put("P_TO", dpDateTo.getDate());
        return params;
    }

    public List<WeightTicket> findByCreatedDateRange(Date sfrom, Date sto) {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = new java.sql.Date(sfrom.getTime());
        java.sql.Date to = new java.sql.Date(sto.getTime());
        return repository.findByCreatedDateRange(from, to);
    }
}
