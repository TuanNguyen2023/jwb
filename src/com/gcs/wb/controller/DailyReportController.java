/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.WeightTicketPK;
import com.gcs.wb.model.AppConfig;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author THANGPT
 */
public class DailyReportController {
    AppConfig appConfig = WeighBridgeApp.getApplication().getConfig();
    public List<WeightTicket> findByCreateDateRange(JXDatePicker dpDateFrom, JXDatePicker dpDateTo) {
        WeightTicketJpaController weightTicketJpaController = new WeightTicketJpaController();
        List<WeightTicket> weightTickets = weightTicketJpaController.findByCreateDateRange(dpDateFrom.getDate(), dpDateTo.getDate());
        return weightTickets;
    }

    public Object[][] handleWtDatas(Object[][] wtDatas, List<WeightTicket> weightTicketList, Object[] wtColNames) {
        
        wtDatas = new Object[weightTicketList.size()][wtColNames.length];

        for (int i = 0; i < weightTicketList.size(); i++) {
            WeightTicket weightTicket = weightTicketList.get(i);
            WeightTicketPK weightTicketPK = weightTicket.getWeightTicketPK();
            if (!weightTicketPK.getMandt().equalsIgnoreCase(appConfig.getsClient()) && !weightTicketPK.getWPlant().equalsIgnoreCase(appConfig.getwPlant().toString())) {
                continue;
            }
            String hh = weightTicket.getCreateTime().substring(0, 2);
            String mm = weightTicket.getCreateTime().substring(2, 4);
            String ss = weightTicket.getCreateTime().substring(4);
            Calendar create_date = Calendar.getInstance();
            create_date.setTime(weightTicket.getCreateDate());
            create_date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hh));
            create_date.set(Calendar.MINUTE, Integer.valueOf(mm));
            create_date.set(Calendar.SECOND, Integer.valueOf(ss));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String createdDateTime = dateFormat.format(create_date.getTime());
            wtDatas[i][0] = i + 1;
            wtDatas[i][1] = weightTicket.getWeightTicketPK().getSeqByDay();
            wtDatas[i][2] = weightTicket.getTenTaiXe();
            wtDatas[i][3] = weightTicket.getCmndBl();
            wtDatas[i][4] = weightTicket.getSoXe();
            wtDatas[i][5] = weightTicket.getSoRomooc();
            wtDatas[i][6] = weightTicket.getCreator();
            wtDatas[i][7] = createdDateTime;
            wtDatas[i][8] = weightTicket.getRegCategory();
            wtDatas[i][9] = weightTicket.getRegItemText();
            if (weightTicket.getFTime() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(weightTicket.getFTime());
                String inDateTime = dateFormat.format(calendar.getTime());
                wtDatas[i][10] = inDateTime;//item.getFTime();
            } else {
                wtDatas[i][10] = weightTicket.getFTime();
            }
            wtDatas[i][11] = weightTicket.getFScale() == null ? weightTicket.getFScale() : weightTicket.getFScale().doubleValue() / 1000d;
            if (weightTicket.getSTime() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(weightTicket.getSTime());
                String outDateTime = dateFormat.format(calendar.getTime());
                wtDatas[i][12] = outDateTime;//item.getSTime();
            } else {
                wtDatas[i][12] = weightTicket.getSTime();
            }
            wtDatas[i][13] = weightTicket.getSScale() == null ? weightTicket.getSScale() : weightTicket.getSScale().doubleValue() / 1000d;
            wtDatas[i][14] = weightTicket.getGQty();
            wtDatas[i][15] = weightTicket.getDelivNumb();
            wtDatas[i][16] = weightTicket.getMatDoc();
            wtDatas[i][17] = weightTicket.getDissolved();
            if (weightTicket.getPosted() == 1) {
                wtDatas[i][18] = true;
            } else {
                wtDatas[i][18] = false;
            }
            wtDatas[i][19] = weightTicket.getEbeln();
            wtDatas[i][20] = "";
        }
        return wtDatas;
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
    
    public Map<String, Object> getParamsReport(JXDatePicker dpDateFrom, JXDatePicker dpDateTo){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("P_PNAME_RPT", WeighBridgeApp.getApplication().getSapSetting().getNameRpt());
        params.put("P_PADDRESS", WeighBridgeApp.getApplication().getSapSetting().getAddress());
        params.put("P_PPHONE", WeighBridgeApp.getApplication().getSapSetting().getPhone());
        params.put("P_PFAX", WeighBridgeApp.getApplication().getSapSetting().getFax());
        params.put("P_FROM", dpDateFrom.getDate());
        params.put("P_TO", dpDateTo.getDate());
        return params;
    }
}
