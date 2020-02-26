/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.WeightTicketDetail;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentRepository;
import com.gcs.wb.jpa.repositorys.WeightTicketRepository;
import java.text.SimpleDateFormat;
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
    List<TransportAgent> transportAgents = new ArrayList<>();

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
        List<WeightTicket> weightTickets = findListWeightTicket(month, year, tAgent, matnr, modes, status == 1);
        wtDatas = new Object[weightTickets.size()][wtColNames.length];
        for (int i = 0; i < weightTickets.size(); i++) {
            WeightTicket item = weightTickets.get(i);
            WeightTicketDetail weightTicketDetail = item.getWeightTicketDetail();
            List<WeightTicketDetail> weightTicketDetails = item.getWeightTicketDetails();
            
            String time = item.getCreatedTime().replaceAll(":", "");
            String hh = time.substring(0, 2);
            String mm = time.substring(2, 4);
            String ss = time.substring(4, 6);
            Calendar create_date = Calendar.getInstance();
            create_date.setTime(item.getCreatedDate());
            create_date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hh));
            create_date.set(Calendar.MINUTE, Integer.valueOf(mm));
            create_date.set(Calendar.SECOND, Integer.valueOf(ss));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String createdDateTime = dateFormat.format(create_date.getTime());
            wtDatas[i][0] = i + 1;// item.getSeqByMonth();
            wtDatas[i][1] = item.getSeqDay();
            wtDatas[i][2] = item.getDriverName();
            wtDatas[i][3] = item.getDriverIdNo();
            wtDatas[i][4] = item.getPlateNo();
            wtDatas[i][5] = item.getTrailerId();
            wtDatas[i][6] = item.getCreator();
            wtDatas[i][7] = createdDateTime;
            wtDatas[i][8] = item.getRegType();
            String[] regItemDescriptions = weightTicketDetails.stream()
                    .map(t -> t.getRegItemDescription())
                    .filter(t -> t != null)
                    .toArray(String[]::new);
            wtDatas[i][9] = regItemDescriptions.length > 0 ? String.join(" - ", regItemDescriptions) : "";
            if (item.getFTime() != null) {
                wtDatas[i][10] = dateFormat.format(item.getFTime());
            } else {
                wtDatas[i][10] = item.getFTime();
            }
            wtDatas[i][11] = item.getFScale() == null ? item.getFScale() : item.getFScale().doubleValue() / 1000d;
            if (item.getFTime() != null) {
                wtDatas[i][12] = dateFormat.format(item.getFTime());
            } else {
                wtDatas[i][12] = item.getFTime();
            }
            wtDatas[i][13] = item.getSScale() == null ? item.getSScale() : item.getSScale().doubleValue() / 1000d;
            wtDatas[i][14] = item.getGQty();
            String[] doNums = weightTicketDetails.stream()
                    .map(t -> t.getDeliveryOrderNo())
                    .filter(t -> t != null)
                    .toArray(String[]::new);
            wtDatas[i][15] = doNums.length > 0 ? String.join(" - ", doNums) : "";
            wtDatas[i][16] = weightTicketDetail.getMatDoc();
            if (item.isPosted()) {
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
            String[] poNums = weightTicketDetails.stream()
                    .map(t -> t.getEbeln())
                    .filter(t -> t != null)
                    .toArray(String[]::new);
            wtDatas[i][19] = poNums.length > 0 ? String.join(" - ", poNums) : "";
        }
        return wtDatas;
    }

    public DefaultComboBoxModel getTransportAgentsModel() {
        transportAgents = transportAgentRepository.getListTransportAgent();
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(transportAgents.toArray());
        TransportAgent transportAgent = new TransportAgent("-2");
        transportAgent.setName(Constants.Label.LABEL_ALL);
        comboBoxModel.insertElementAt(transportAgent, 0);
        comboBoxModel.setSelectedItem(transportAgent);
        return comboBoxModel;
    }

    public List<WeightTicket> findListWeightTicket(String month, String year, String tagent, String matnr, List<Character> modes, boolean isPosted) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        return repository.findListWeightTicket(month, year, tagent, matnr, modes, isPosted);
    }
}
