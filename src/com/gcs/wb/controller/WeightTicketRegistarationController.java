/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.JReportService;
import com.gcs.wb.jpa.entity.*;
import com.gcs.wb.jpa.entity.OutboundDeliveryDetail;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.WeightTicketRegistarationService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author THANGPT
 */
public class WeightTicketRegistarationController {

    WeightTicketRegistarationService wTRegService = new WeightTicketRegistarationService();
    JReportService jreportService = new JReportService();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    private final Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());


    public String getReportName() {
        String reportName = null;
        if (configuration.isModeNormal()) {
            reportName = "./rpt/rptBT/WTList.jasper";
        } else {
            reportName = "./rpt/rptPQ/WTList.jasper";
        }
        return reportName;
    }

    public Map<String, Object> getPrintReport(String kFrom, String kTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_PNAME_RPT", WeighBridgeApp.getApplication().getSapSetting().getNameRpt());
        params.put("P_PADDRESS", WeighBridgeApp.getApplication().getSapSetting().getAddress());
        params.put("P_PPHONE", WeighBridgeApp.getApplication().getSapSetting().getPhone());
        params.put("P_PFAX", WeighBridgeApp.getApplication().getSapSetting().getFax());
        params.put("P_FROM", kFrom);
        params.put("P_TO", kTo);
        return params;
    }

    public void printReport(Map<String, Object> map, String reportName) {
        jreportService.printReport(map, reportName);
    }

    public void printRegWT(WeightTicket wt, boolean reprint) {
        Map<String, Object> map = new HashMap<>();
        map.put("P_CLIENT", configuration.getSapClient());
        map.put("P_WPLANT", configuration.getWkPlant());
        map.put("P_ID", wt.getId());
        map.put("P_DAYSEQ", wt.getSeqDay());
        map.put("P_REPRINT", reprint);
        map.put("P_ADDRESS", configuration.getRptId());
        String reportName;
        if (configuration.isModeNormal()) {
            //reportName = "./rpt/rptBT/RegWT_HP.jasper";
            reportName = "./rpt/rptBT/RegWT_HP.jasper";
        } else {
            //reportName = "./rpt/rptPQ/RegWT.jasper";
            reportName = "./rpt/rptPQ/RegWT.jasper";
        }
        jreportService.printReport(map, reportName);
    }

    public boolean checkExistDO(String doNumber) {
        List<Object[]> wts1 = wTRegService.checkExistDO(doNumber);
        boolean isInUsedDO = false;
        try {
            if (CollectionUtils.isNotEmpty(wts1)) {
                isInUsedDO = Float.parseFloat(wts1.get(0)[0].toString()) > 0;
            }
        } catch (Throwable cause) {
            // NOP
        }
        return isInUsedDO;
    }

    public int shippingPointVar(String shipPoint, String Matnr) {
        return wTRegService.shippingPointVar(shipPoint, Matnr);
    }

    public DefaultComboBoxModel getListMaterial() {

        List<Material> materials = wTRegService.getListMaterial();
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        for (Material material : materials) {
            if (result.getIndexOf(material) < 0 && material.getMatnr() != null
                    && material.getMaktx() != null && !material.getMaktx().isEmpty()) {
                result.addElement(material.getMaktx());
            }
        }
        return result;
    }

    public DefaultComboBoxModel getMatsModel() {
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        List<Material> materials = wTRegService.getListMaterial();
        Material mat = new Material();
        mat.setMatnr("-2");
        mat.setMaktx(Constants.Label.LABEL_ALL);
        result.addElement(mat);

        mat = new Material();
        mat.setMatnr("-1");
        mat.setMaktx(Constants.Label.LABEL_OTHER);
        result.addElement(mat);
        for (Material material : materials) {
            if (result.getIndexOf(material) < 0) {
                result.addElement(material);
            }
        }
        return result;
    }

    public WeightTicket findByDeliveryOrderNo(String doNumber) {
        return wTRegService.findByDeliveryOrderNo(doNumber);
    }

    public List<WeightTicket> getListByDeliveryOrderNo(String outbNumber) {
        return wTRegService.getListByDeliveryOrderNo(outbNumber);
    }

    public Customer findByKunnr(String kunag) {
        return wTRegService.findByKunnr(kunag);
    }

    public Vendor findByLifnr(String lifnr) {
        return wTRegService.findByLifnr(lifnr);
    }

    public OutboundDelivery findByDeliveryOrderNumber(String deliveryOrderNo) {
        return wTRegService.findByDeliveryOrderNumber(deliveryOrderNo);
    }

    public Vehicle findByPlateNo(String plateNo) {
        return wTRegService.findByPlateNo(plateNo);
    }

    public List<TransportAgentVehicle> findByVehicleId(int vehicleId) {
        return wTRegService.findByVehicleId(vehicleId);
    }

    public List<WeightTicket> findByDatePostedNull(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        return wTRegService.findByDatePostedNull(sfrom, sto, creator, taixe, bienso);
    }

    public List<WeightTicket> findByDatePostedNullAll(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        return wTRegService.findByDatePostedNullAll(sfrom, sto, creator, taixe, bienso);
    }

    public List<WeightTicket> findByDatePosted(String sfrom, String sto, String creator, String taixe, String loaihang, String bienso) throws Exception {
        return wTRegService.findByDatePosted(sfrom, sto, creator, taixe, loaihang, bienso);
    }

    public List<WeightTicket> findByDateAll(String sfrom, String sto, String creator, String taixe, String loaihang, String bienso) throws Exception {
        return wTRegService.findByDateAll(sfrom, sto, creator, taixe, loaihang, bienso);
    }

    public List<WeightTicket> findByDateAllNull(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {

        return wTRegService.findByDateAllNull(sfrom, sto, creator, taixe, bienso);
    }

    public Date getServerDate() {
        Date date = null;
        try {
            date = new Date();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return date;
    }

    public int getNewSeqBMonth() {
        String plant = configuration.getWkPlant();
        int count = wTRegService.getCountTicketMonth(plant);
        return count;
    }

    public int getNewSeqBDay() {
        String plant = configuration.getWkPlant();
        int count = wTRegService.getCountTicketDay(plant);
        return count;
    }

    public List<WeightTicket> findByDateAllNullAll(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        return wTRegService.findByDateAllNullAll(sfrom, sto, creator, taixe, bienso);
    }

    public List<OutboundDeliveryDetail> findByMandtDelivNumb(String deliv_numb) throws Exception {
        return wTRegService.findByMandtDelivNumb(deliv_numb);

    }
}
