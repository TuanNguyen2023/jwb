/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

//import com.gcs.wb.jpa.JReportService;
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.service.JReportService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dinhhn.vr
 */
public class BQJreportTest {

    JReportService jreportService = new JReportService();

    public void storeBqRegWt() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("P_CLIENT", "500");
        map.put("P_WPLANT", "1111");
        map.put("P_ID", "1501020619");
        map.put("P_DAYSEQ", 8);
        map.put("P_ADDRESS", 1102);
        String reportName = "./rpt/rptPQ/RegWT.jasper";
        jreportService.printReport(map, reportName);
    }

    public void storeBqWeightTicket() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("P_MANDT", "500");
//        map.put("P_WPlant", "1111");
//        map.put("P_ID", "1000000007");
//        map.put("P_ADDRESS", 1102);
//        map.put("P_DAYSEQ", 1);
//        map.put("P_PAGE", 1);
//        map.put("P_DEL_NUM", "10");
//        String reportName = "./rpt/rptPQ/WeightTicket.jasper";
//        jreportService.printReport(map, reportName);
        
        Map<String, Object> map = new HashMap<>();
        map.put("P_MANDT", "500");
        map.put("P_WPlant", "1111");
        //map.put("P_ID", "1501010035");
        map.put("P_ID", "1000000007");
        map.put("P_REPRINT", true);
        map.put("P_ADDRESS", 1102);
        map.put("P_DAYSEQ", 1);
        map.put("P_PAGE", 1);
        map.put("P_DEL_NUM", "10");
        String reportName = "./rpt/rptPQ/WeightTicket.jasper";
        jreportService.printReport(map, reportName);
    }

    public void storeBqWeightticketNew() {
        Map<String, Object> map = new HashMap<>();
        map.put("P_ADDRESS", 1102);
        map.put("P_MANDT", "500");
        map.put("P_WPlant", "1111");
        map.put("P_ID", "1501010035");
        map.put("P_DAYSEQ", 2);
        map.put("P_PAGE", 1);
        map.put("P_DEL_NUM", "10");
        String reportName = "./rpt/rptPQ/WeightTicket_NEW.jasper";
        jreportService.printReport(map, reportName);
    }

    public void storePQWlist() {
        Map<String, Object> map = new HashMap<>();
//        map.put("P_PNAME_RPT", WeighBridgeApp.getApplication().getSapSetting().getNameRpt());
//        map.put("P_PADDRESS", WeighBridgeApp.getApplication().getSapSetting().getAddress());
//        map.put("P_PPHONE", WeighBridgeApp.getApplication().getSapSetting().getPhone());
//        map.put("P_PFAX", WeighBridgeApp.getApplication().getSapSetting().getFax());
//        map.put("P_FROM", new Date());
//        map.put("P_TO", new Date());
        String reportName = "./rpt/rptBT/WTList.jasper";
        jreportService.printReport(map, reportName);

    }

    public static void main(String[] args) {

        //new BQJreportTest().storeBqRegWt();
        //new BQJreportTest().storePQWlist();
         new BQJreportTest().storeBqWeightTicket();
        //new BQJreportTest().storeBqWeightticketNew();


    }
}
