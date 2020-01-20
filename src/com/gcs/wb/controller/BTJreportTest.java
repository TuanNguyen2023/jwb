/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.jpa.service.JReportService;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dinhhn.vr
 */
public class BTJreportTest {

    JReportService jreportService = new JReportService();

    public void storeBtRegwt() {
        Map<String, Object> map = new HashMap<>();
        map.put("P_CLIENT", "500");
        map.put("P_WPLANT", "1111");
//        map.put("P_ID", "1501020619");
//        map.put("P_DAYSEQ", 8);
        map.put("P_ID", "1000000007");
        map.put("P_DAYSEQ", 1);
        map.put("P_ADDRESS", 1102);
        String reportName = "./rpt/rptBT/RegWT.jasper";
        jreportService.printReport(map, reportName);

    }

    public void storeBtRegwtHp() {
        Map<String, Object> map = new HashMap<>();
        map.put("P_CLIENT", "500");
        map.put("P_WPLANT", "1111");
//        map.put("P_ID", "1501010035");
//        map.put("P_DAYSEQ", 2);
        map.put("P_ID", "1000000007");
        map.put("P_DAYSEQ", 1);
        map.put("P_ADDRESS", 1102);
        String reportName = "./rpt/rptBT/RegWT_HP.jasper";
        jreportService.printReport(map, reportName);

    }

    public void storeBtWeightTicket() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("P_MANDT", "500");
            map.put("P_WPlant", "1111");
            //map.put("P_ID", "1501010035");
            map.put("P_ID", "1000000007");
            //map.put("P_DAYSEQ", 2);
            map.put("P_DAYSEQ", 1);
            map.put("P_ADDRESS", 1102);
            String reportName1 = "./rpt/rptBT/WeightTicket.jasper";
            jreportService.printReport(map, reportName1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void storeBtWeightTicketNew() {
        Map<String, Object> map = new HashMap<>();
        map.put("P_ADDRESS", 1102);
        map.put("P_MANDT", "500");
        map.put("P_WPlant", "1111");
        map.put("P_ID", "1501010035");
        map.put("P_DAYSEQ", 2);
        map.put("P_PAGE", 1);
        map.put("P_DEL_NUM", "10");
        String reportName1 = "./rpt/rptBT/WeightTicket_NEW.jasper";
        jreportService.printReport(map, reportName1);

    }

    public void storeBtWlist() {
        Map<String, Object> map = new HashMap<>();
        map.put("P_PNAME_RPT", "test1");
        map.put("P_PADDRESS", "test1");
        map.put("P_PPHONE", "test1");
        map.put("P_PFAX", "test1");
        map.put("P_FROM", new Date());
        map.put("P_TO", new Date());
        String reportName = "./rpt/rptBT/WTList.jasper";
        jreportService.printReport(map, reportName);

    }

    public static void main(String[] args) {

        //new BTJreportTest().storeBtRegwt();
        //new BTJreportTest().storeBtRegwtHp();
        new BTJreportTest().storeBtWlist();
        //new BTJreportTest().storeBtWeightTicket();
        //new BTJreportTest().storeBtWeightTicketNew();


    }
}