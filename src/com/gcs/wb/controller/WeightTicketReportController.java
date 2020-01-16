/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentRepository;
import com.gcs.wb.jpa.repositorys.WeightTicketRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author THANGPT
 */
public class WeightTicketReportController {

    private TransportAgentRepository transportAgentRepository = new TransportAgentRepository();
    private WeightTicketRepository weightTicketRepository = new WeightTicketRepository();
    private MaterialRepository materialRepository = new MaterialRepository();
    private Object[] wtColNames = new String[]{
        "STT",
        "S.Đ.Tài",
        "Tên tài xế",
        "CMND/BL",
        "Số Xe",
        "Số Rơmoóc",
        "Người tạo",
        "Ngày giờ tạo",
        "Nhập/Xuất(I/O)",
        "Loại hàng",
        "Ngày giờ vào",
        "T.L vào",
        "Ngày giờ ra",
        "T.L ra",
        "T.L Hàng",
        "Số D.O",
        "Số chứng từ SAP",
        "Hủy",
        "SAP Posted",
        "DVVC",
        "Số P.O"};

    public List<Character> getModeItemStateChanged(List<Character> modes, JComboBox cbxMode) {
        modes = new ArrayList<Character>();
        switch (cbxMode.getSelectedIndex()) {
            case 0:
                modes.add('I');
                modes.add('O');
                break;
            case 1:
                modes.add('I');
                break;
            case 2:
                modes.add('O');
                break;
        }
        return modes;
    }

    public DefaultComboBoxModel getMaterialsModel() {
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        List<Material> materials = materialRepository.getListMaterial();
        Material mat = new Material();
        mat.setMatnr("-1");
        mat.setMaktx("Tất cả");
        result.addElement(mat);

        for (Material material : materials) {
            if (result.getIndexOf(material) < 0 && material.getMatnr() != null
                    && material.getMaktx() != null && !material.getMaktx().isEmpty()) {
                result.addElement(material);
            }
        }
        return result;
    }

    public Object[][] findWeightTickets(Object[][] wtDatas, String month, String year, String tAgent, String matnr, List<Character> modes, JComboBox cbxStatus, JComboBox cbxTransportAgent) throws Exception {
        WeightTicketJpaController weightTicketJpaController = new WeightTicketJpaController();
        List<WeightTicket> weightTickets = weightTicketJpaController.findListWTs(month, year, tAgent, matnr, modes, cbxStatus.getSelectedIndex() == 1, cbxStatus.getSelectedIndex() == 2);
        wtDatas = new Object[weightTickets.size()][wtColNames.length];
        for (int i = 0; i < weightTickets.size(); i++) {
            WeightTicket item = weightTickets.get(i);
            String hh = item.getCreatedTime().substring(0, 2);
            String mm = item.getCreatedTime().substring(2, 4);
            String ss = item.getCreatedTime().substring(4);
            Calendar create_date = Calendar.getInstance();
            create_date.setTime(item.getCreatedDate());
            create_date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hh));
            create_date.set(Calendar.MINUTE, Integer.valueOf(mm));
            create_date.set(Calendar.SECOND, Integer.valueOf(ss));
            wtDatas[i][0] = i + 1;// item.getSeqByMonth();
            wtDatas[i][1] = item.getSeqDay();
            wtDatas[i][2] = item.getDriverName();
            wtDatas[i][3] = item.getDriverIdNo();
            wtDatas[i][4] = item.getPlateNo();
            wtDatas[i][5] = item.getTrailerId();
            wtDatas[i][6] = item.getCreator();
            wtDatas[i][7] = create_date.getTime();
            wtDatas[i][8] = item.getRegType();
            wtDatas[i][9] = item.getRegItemDescription();
            wtDatas[i][10] = item.getFTime();
            wtDatas[i][11] = item.getFScale() == null ? item.getFScale() : item.getFScale().doubleValue() / 1000d;
            wtDatas[i][12] = item.getSTime();
            wtDatas[i][13] = item.getSScale() == null ? item.getSScale() : item.getSScale().doubleValue() / 1000d;
            wtDatas[i][14] = item.getGQty();
            wtDatas[i][15] = item.getDeliveryOrderNo();
            wtDatas[i][16] = item.getMatDoc();
            wtDatas[i][17] = item.isDissolved();
            if (item.isPosted()) {
                wtDatas[i][18] = true;
            } else {
                wtDatas[i][18] = false;
            }
            wtDatas[i][19] = ((TransportAgent) cbxTransportAgent.getSelectedItem()).getName();
            wtDatas[i][20] = item.getEbeln();
        }
        return wtDatas;
    }

    public Map<String, Object> getParamReport(JComboBox cbxTransportAgent, JComboBox cbxMonth, JComboBox cbxYear) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("P_PNAME_RPT", WeighBridgeApp.getApplication().getSapSetting().getNameRpt());
        params.put("P_PADDRESS", WeighBridgeApp.getApplication().getSapSetting().getAddress());
        params.put("P_PPHONE", WeighBridgeApp.getApplication().getSapSetting().getPhone());
        params.put("P_PFAX", WeighBridgeApp.getApplication().getSapSetting().getFax());
        params.put("P_TAGENT", ((TransportAgent) cbxTransportAgent.getSelectedItem()).getName());
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
}
