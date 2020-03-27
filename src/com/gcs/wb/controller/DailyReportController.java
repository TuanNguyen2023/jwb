/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.JReportService;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.WeightTicketDetail;
import com.gcs.wb.jpa.repositorys.TransportAgentRepository;
import com.gcs.wb.service.DailyReportService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private TransportAgentRepository transportAgentRepository = new TransportAgentRepository();

    public List<WeightTicket> findByCreateDateRange(JXDatePicker dpDateFrom, JXDatePicker dpDateTo) {
        return dailyReportService.findByCreateDateRange(dpDateFrom, dpDateTo);
    }

    public String getReportName() {
        String reportName;
//        if (configuration.isModeNormal()) {
//            reportName = "./rpt/rptBT/WTList.jasper";
//        } else {
//            reportName = "./rpt/rptPQ/WTList.jasper";
//        }
        reportName = "./rpt/rptBT/WTList.jasper";
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
        params.put("P_HEADER_RPT", WeighBridgeApp.getApplication().getSapSetting().getHeaderRpt());
        return params;
    }

    public void printReport(Map<String, Object> map, String reportName, JRTableModelDataSource data){
        jreportService.printReportDataSource(map, reportName, data);
    }
    
    public Object[][] handleWtDatas(Object[][] wtDatas, List<WeightTicket> weightTicketList) {
        List<TransportAgent> transportAgents = transportAgentRepository.getListTransportAgent();
        wtDatas = new Object[weightTicketList.size()][wtColNames.length];

        for (int i = 0; i < weightTicketList.size(); i++) {
            WeightTicket weightTicket = weightTicketList.get(i);
            WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
            List<WeightTicketDetail> weightTicketDetails = weightTicket.getWeightTicketDetails();
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
            wtDatas[i][7] = createdDateTime;
            wtDatas[i][8] = weightTicket.getRegType();
            String[] regItemDescriptions = weightTicketDetails.stream()
                    .map(t -> t.getRegItemDescription())
                    .filter(t -> t != null)
                    .toArray(String[]::new);
            wtDatas[i][9] = regItemDescriptions.length > 0 ? String.join(" - ", regItemDescriptions) : "";
            if (weightTicket.getFTime() != null) {
                wtDatas[i][10] = dateFormat.format(weightTicket.getFTime());
            } else {
                wtDatas[i][10] = weightTicket.getFTime();
            }
            wtDatas[i][11] = weightTicket.getFScale() == null ? weightTicket.getFScale() : weightTicket.getFScale().doubleValue() / 1000d;
            if (weightTicket.getSTime() != null) {
                wtDatas[i][12] = dateFormat.format(weightTicket.getSTime());
            } else {
                wtDatas[i][12] = weightTicket.getSTime();
            }
            wtDatas[i][13] = weightTicket.getSScale() == null ? weightTicket.getSScale() : weightTicket.getSScale().doubleValue() / 1000d;
            wtDatas[i][14] = weightTicket.getGQty();
            String[] doNums = weightTicketDetails.stream()
                    .map(t -> t.getDeliveryOrderNo())
                    .filter(t -> t != null)
                    .toArray(String[]::new);
            wtDatas[i][15] = doNums.length > 0 ? String.join(" - ", doNums) : "";
            wtDatas[i][16] = weightTicketDetail.getMatDoc();
            if (weightTicket.isPosted()) {
                wtDatas[i][17] = true;
            } else {
                wtDatas[i][17] = false;
            }
            TransportAgent transportAgent = transportAgents.stream()
                    .filter(t -> {
                        String abbr = weightTicketDetail.getTransVendor();
                        return abbr != null && abbr.equals(t.getAbbr());
                    })
                    .findAny()
                    .orElse(null);
            wtDatas[i][18] = transportAgent != null ? transportAgent.getName() : "";
            //wtDatas[i][18] = weightTicketDetail.getEbeln();
            String[] poNums = weightTicketDetails.stream()
                    .map(t -> t.getEbeln())
                    .filter(t -> t != null)
                    .toArray(String[]::new);
            wtDatas[i][19] = poNums.length > 0 ? String.join(" - ", poNums) : "";
            //wtDatas[i][19] = "";
        }
        return wtDatas;
    }
}
