/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa;

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
        Map<String, Object> map = new HashMap<>();
        /* map.put("P_CLIENT", "500");
        map.put("P_WPLANT", "1111");
        map.put("P_ID", "1501010035");
        map.put("P_DAYSEQ", 2);
        map.put("P_ADDRESS", 1102);*/
        map.put("P_ADDRESS", 1102);
        map.put("P_MANDT", "500");
        map.put("P_WPlant", "1111");
        map.put("P_ID", "1501010035");
        map.put("P_DAYSEQ", 2);
        map.put("P_PAGE", 1);
        map.put("P_DEL_NUM", "10");
        String reportName = "./rpt/rptPQ/WeightTicket.jasper";
        jreportService.printReport(map, reportName);
    }

    public void storeBqWeightticketNew() {
        Map<String, Object> map = new HashMap<>();
        /*map.put("P_CLIENT", "G111");
        map.put("P_WPLANT", "130");
        map.put("P_ID", "1501010020");
        map.put("P_DAYSEQ", 1);
        map.put("P_ADDRESS", 1102);
        map.put("P_DEL_NUM", "0080180425");
        map.put("P_PAGE",1);*/
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

    public static void main(String[] args) {

        //new BQJreportTest().storeBqRegWt();
        //new BQJreportTest().storeBqWeightTicket();
        new BQJreportTest().storeBqWeightticketNew();

    }
}
