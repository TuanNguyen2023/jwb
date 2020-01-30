/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.WeightTicketDetail;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author THANGPT
 */
public class WeightTicketReportService {

    private TransportAgentRepository transportAgentRepository = new TransportAgentRepository();
    private MaterialRepository materialRepository = new MaterialRepository();
    private Object[] wtColNames = Constants.WeightTicketReport.WT_COL_NAMES;

    public List<Character> getModeItemStateChanged(List<Character> modes, int mode) {
        modes = new ArrayList<>();
        switch (mode) {
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
        mat.setMatnr("-2");
        mat.setMaktx(Constants.Label.LABEL_ALL);
        result.addElement(mat);

        mat = new Material();
        mat.setMatnr("-1");
        mat.setMaktx(Constants.Label.LABEL_OTHER);
        result.addElement(mat);

        for (Material material : materials) {
            if (result.getIndexOf(material) < 0 && material.getMatnr() != null
                    && material.getMaktx() != null && !material.getMaktx().isEmpty()) {
                result.addElement(material);
            }
        }

        return result;
    }

    public Object[][] findWeightTickets(Object[][] wtDatas, String month, String year, String tAgent, String matnr, List<Character> modes, int status, String transportAgentName) throws Exception {
        WeightTicketJpaController weightTicketJpaController = new WeightTicketJpaController();
        List<WeightTicket> weightTickets = weightTicketJpaController.findListWTs(month, year, tAgent, matnr, modes, status == 1);
        wtDatas = new Object[weightTickets.size()][wtColNames.length];
        for (int i = 0; i < weightTickets.size(); i++) {
            WeightTicket item = weightTickets.get(i);   
            WeightTicketDetail weightTicketDetail = item.getWeightTicketDetail();
            String time = item.getCreatedTime().replaceAll(":","");
            String hh = time.substring(0, 2);
            String mm = time.substring(2, 4);
            String ss = time.substring(4, 6);
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
            wtDatas[i][9] = weightTicketDetail.getRegItemDescription();
            wtDatas[i][10] = item.getFTime();
            wtDatas[i][11] = item.getFScale() == null ? item.getFScale() : item.getFScale().doubleValue() / 1000d;
            wtDatas[i][12] = item.getSTime();
            wtDatas[i][13] = item.getSScale() == null ? item.getSScale() : item.getSScale().doubleValue() / 1000d;
            wtDatas[i][14] = item.getGQty();
            wtDatas[i][15] = weightTicketDetail.getDeliveryOrderNo();
            wtDatas[i][16] = weightTicketDetail.getMatDoc();
            if (item.isPosted()) {
                wtDatas[i][17] = true;
            } else {
                wtDatas[i][17] = false;
            }
            wtDatas[i][18] = transportAgentName;
            wtDatas[i][19] = weightTicketDetail.getEbeln();
        }
        return wtDatas;
    }

    public DefaultComboBoxModel getTransportAgentsModel() {
        List<TransportAgent> transportAgents = transportAgentRepository.getListTransportAgent();
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(transportAgents.toArray());
        TransportAgent transportAgent = new TransportAgent("-2");
        transportAgent.setName(Constants.Label.LABEL_ALL);
        comboBoxModel.insertElementAt(transportAgent, 0);
        comboBoxModel.setSelectedItem(transportAgent);
        return comboBoxModel;
    }
}
