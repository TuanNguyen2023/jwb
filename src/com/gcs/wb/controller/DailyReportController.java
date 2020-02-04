/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.service.JReportService;
import com.gcs.wb.service.DailyReportService;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author THANGPT
 */
public class DailyReportController {

    DailyReportService dailyReportService = new DailyReportService();
    Object[] wtColNames = Constants.DailyReport.WT_COL_NAMES;
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    JReportService jreportService = new JReportService();

    public List<WeightTicket> findByCreateDateRange(JXDatePicker dpDateFrom, JXDatePicker dpDateTo) {
        return dailyReportService.findByCreateDateRange(dpDateFrom, dpDateTo);
    }

    public String getReportName() {
        String reportName;
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

    public void printReport(Map<String, Object> map, String reportName, JRTableModelDataSource data){
        jreportService.printReportDataSource(map, reportName, data);
    }
    
    public Object[][] handleWtDatas(Object[][] wtDatas, List<WeightTicket> weightTicketList) {

        wtDatas = new Object[weightTicketList.size()][wtColNames.length];

        for (int i = 0; i < weightTicketList.size(); i++) {
            WeightTicket weightTicket = weightTicketList.get(i);
            String time = weightTicket.getCreatedTime().replaceAll(":","");
            String hh = time.substring(0, 2);
            String mm = time.substring(2, 4);
            String ss = time.substring(4, 6);
            Calendar create_date = Calendar.getInstance();
            create_date.setTime(weightTicket.getCreatedDate());
            create_date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hh));
            create_date.set(Calendar.MINUTE, Integer.valueOf(mm));
            create_date.set(Calendar.SECOND, Integer.valueOf(ss));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String createdDateTime = dateFormat.format(create_date.getTime());
            wtDatas[i][0] = i + 1;
            wtDatas[i][1] = weightTicket.getSeqDay();
            wtDatas[i][2] = weightTicket.getDriverName();
            wtDatas[i][3] = weightTicket.getDriverIdNo();
            wtDatas[i][4] = weightTicket.getPlateNo();
            wtDatas[i][5] = weightTicket.getTrailerId();
            wtDatas[i][6] = weightTicket.getCreator();
            wtDatas[i][7] = create_date.getTime();
            wtDatas[i][8] = weightTicket.getRegType();
            wtDatas[i][9] = weightTicket.getRegItemDescription();
            if (weightTicket.getFTime() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(weightTicket.getFTime());
                String inDateTime = dateFormat.format(calendar.getTime());
                wtDatas[i][10] = calendar.getTime();//item.getFTime();
            } else {
                wtDatas[i][10] = weightTicket.getFTime();
            }
            wtDatas[i][11] = weightTicket.getFScale() == null ? weightTicket.getFScale() : weightTicket.getFScale().doubleValue() / 1000d;
            if (weightTicket.getSTime() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(weightTicket.getSTime());
                String outDateTime = dateFormat.format(calendar.getTime());
                wtDatas[i][12] = calendar.getTime();//item.getSTime();
            } else {
                wtDatas[i][12] = weightTicket.getSTime();
            }
            wtDatas[i][13] = weightTicket.getSScale() == null ? weightTicket.getSScale() : weightTicket.getSScale().doubleValue() / 1000d;
            wtDatas[i][14] = weightTicket.getGQty();
            wtDatas[i][15] = weightTicket.getDeliveryOrderNo();
            wtDatas[i][16] = weightTicket.getMatDoc();
            if (weightTicket.isPosted()) {
                wtDatas[i][17] = true;
            } else {
                wtDatas[i][17] = false;
            }
            wtDatas[i][18] = weightTicket.getEbeln();
            wtDatas[i][19] = "";
        }
        return wtDatas;
    }
}
