/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.MaterialPK;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.repositorys.WeightTicketRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentRepository;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

/**
 *
 * @author THANGPT
 */
public class WTListService {
    
    WeightTicketRepository weightTicketRepository = new WeightTicketRepository();
    TransportAgentRepository transportAgentRepository = new TransportAgentRepository();
    WeightTicketJpaController wCon = new WeightTicketJpaController();
    Object[] wtCols = Constants.WTList.wtCols;
    
    public DefaultComboBoxModel getMatsModel() {
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        List<Object[]> wts = weightTicketRepository.getMatsModel(client, plant);
        if (wts != null) {
            for (Object obj : wts) {
                Object[] wt = (Object[]) obj;
                MaterialPK matPK = new MaterialPK();
                Material mat = new Material();
                matPK.setMandt(WeighBridgeApp.getApplication().getConfig().getsClient());
                if (wt[0] == null) {
                    matPK.setMatnr("-1");
                    mat.setMaktx("Linh tinh");
                } else {
                    matPK.setMatnr(wt[0].toString());
                    mat.setMaktx(wt[1].toString());
                }
                mat.setMaterialPK(matPK);
                if (result.getIndexOf(mat) < 0) {
                    result.addElement(mat);
                }
            }
        }
        return result;
    }

    public Object[][] findWTsDoIn(String month, String year, String tagent, String matnr, List<Character> modes,
            JRadioButton rbtDissolved, JRadioButton rbtPosted, JComboBox cbxTAgent) throws Exception {

        List<WeightTicket> wts = wCon.findListWTs(month, year, tagent, matnr, modes, rbtDissolved.isSelected(), rbtPosted.isSelected());
        Object[][] wtData = new Object[wts.size()][wtCols.length];
        for (int i = 0; i < wts.size(); i++) {
            WeightTicket item = wts.get(i);
            String hh;
            String mm;
            String ss;
            hh = item.getCreateTime().substring(0, 2);
            mm = item.getCreateTime().substring(2, 4);
            ss = item.getCreateTime().substring(4);
            Calendar create_date = Calendar.getInstance();
            create_date.setTime(item.getCreateDate());
            create_date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hh));
            create_date.set(Calendar.MINUTE, Integer.valueOf(mm));
            create_date.set(Calendar.SECOND, Integer.valueOf(ss));
            wtData[i][0] = i + 1;// item.getSeqByMonth();
            wtData[i][1] = item.getWeightTicketPK().getSeqByDay();
            wtData[i][2] = item.getTenTaiXe();
            wtData[i][3] = item.getCmndBl();
            wtData[i][4] = item.getSoXe();
            wtData[i][5] = item.getSoRomooc();
            wtData[i][6] = item.getCreator();
            wtData[i][7] = create_date.getTime();
            wtData[i][8] = item.getRegCategory();
            wtData[i][9] = item.getRegItemText();
            wtData[i][10] = item.getFTime();
            wtData[i][11] = item.getFScale() == null ? item.getFScale() : item.getFScale().doubleValue() / 1000d;
            wtData[i][12] = item.getSTime();
            wtData[i][13] = item.getSScale() == null ? item.getSScale() : item.getSScale().doubleValue() / 1000d;
            wtData[i][14] = item.getGQty();
            wtData[i][15] = item.getDelivNumb();
            wtData[i][16] = item.getMatDoc();
            wtData[i][17] = item.getDissolved();
            if (item.getPosted() == 1) {
                wtData[i][18] = true;
            } else {
                wtData[i][18] = false;
            }
            wtData[i][19] = ((TransportAgent) cbxTAgent.getSelectedItem()).getName();
            wtData[i][20] = item.getEbeln();
        }
        return wtData;
    }

    public Map<String, Object> getParamPrintReport(JComboBox cbxTAgent, JComboBox cbxMonth, JComboBox cbxYear) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("P_PNAME_RPT", WeighBridgeApp.getApplication().getSapSetting().getNameRpt());
        params.put("P_PADDRESS", WeighBridgeApp.getApplication().getSapSetting().getAddress());
        params.put("P_PPHONE", WeighBridgeApp.getApplication().getSapSetting().getPhone());
        params.put("P_PFAX", WeighBridgeApp.getApplication().getSapSetting().getFax());
        params.put("P_TAGENT", ((TransportAgent) cbxTAgent.getSelectedItem()).getName());
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
    
    public DefaultComboBoxModel getTAgentsModel() {
        return transportAgentRepository.getTAgentsModel();
    }
}
