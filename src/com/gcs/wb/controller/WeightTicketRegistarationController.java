/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.constant.Constants.WeighingProcess.MODE_DETAIL;
import com.gcs.wb.base.enums.ModeEnum;
import com.gcs.wb.base.enums.StatusEnum;
import com.gcs.wb.base.util.FunctionalUtil;
import com.gcs.wb.jpa.JReportService;
import com.gcs.wb.jpa.entity.*;
import com.gcs.wb.jpa.entity.OutboundDeliveryDetail;
import com.gcs.wb.jpa.repositorys.CustomerRepository;
import com.gcs.wb.jpa.repositorys.MaterialInternalRepository;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.SLocRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentVehicleRepository;
import com.gcs.wb.jpa.repositorys.VehicleLoadRepository;
import com.gcs.wb.jpa.repositorys.VendorRepository;
import com.gcs.wb.jpa.repositorys.WeightTicketDetailRepository;
import com.gcs.wb.service.WeightTicketRegistrationService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author THANGPT
 */
public class WeightTicketRegistarationController {

    WeightTicketRegistrationService wTRegService = new WeightTicketRegistrationService();
    JReportService jreportService = new JReportService();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    private final Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    TransportAgentVehicleRepository transportAgentVehicleRepository = new TransportAgentVehicleRepository();
    VehicleLoadRepository vehicleLoadRepository = new VehicleLoadRepository();
    VendorRepository vendorRepository = new VendorRepository();
    MaterialInternalRepository materialInternalRepository = new MaterialInternalRepository();
    WeightTicketDetailRepository weightTicketDetailRepository = new WeightTicketDetailRepository();
    MaterialRepository materialRepository = new MaterialRepository();
    SLocRepository sLocRepository = new SLocRepository();
    CustomerRepository customerRepository = new CustomerRepository();

    public String getReportName() {
        String reportName = null;
//        if (configuration.isModeNormal()) {
//            reportName = "./rpt/rptBT/WTList.jasper";
//        } else {
//            reportName = "./rpt/rptPQ/WTList.jasper";
//        }
        reportName = "./rpt/rptBT/WTList.jasper";
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
        List<WeightTicketDetail> weightTicketDetails = new ArrayList<>();
        weightTicketDetails = wt.getWeightTicketDetails();
        for (WeightTicketDetail weightTicketDetail : weightTicketDetails) {
            Map<String, Object> map = new HashMap<>();
            map.put("P_CLIENT", wt.getMandt());
            map.put("P_WPLANT", wt.getWplant());
            map.put("P_ID", wt.getId());
            map.put("P_DAYSEQ", wt.getSeqDay());
            map.put("P_REPRINT", reprint);
            map.put("P_ADDRESS", configuration.getRptId());
            map.put("P_DO", weightTicketDetail.getDeliveryOrderNo());
            String reportName;
            //        if (configuration.isModeNormal()) {
            //            //reportName = "./rpt/rptBT/RegWT_HP.jasper";
            //            reportName = "./rpt/rptBT/RegWT_HP.jasper";
            //        } else {
            //            //reportName = "./rpt/rptPQ/RegWT.jasper";
            //            reportName = "./rpt/rptPQ/RegWT.jasper";
            //        }
            reportName = "./rpt/rptBT/RegWT_HP.jasper";
            jreportService.printReport(map, reportName);
        }

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

        materials = materials.stream()
                .filter(FunctionalUtil.distinctByKey(p -> p.getMatnr()))
                .collect(Collectors.toList());

        for (Material material : materials) {
            if (result.getIndexOf(material) < 0 && material.getMatnr() != null
                    && material.getMaktx() != null && !material.getMaktx().isEmpty()) {
                result.addElement(material);
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

        materials = materials.stream()
                .filter(FunctionalUtil.distinctByKey(p -> p.getMatnr()))
                .collect(Collectors.toList());

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
    
    public WeightTicket findByDeliveryOrderNoScale(String doNumber) {
        return wTRegService.findByDeliveryOrderNoScale(doNumber);
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

    public List<OutboundDeliveryDetail> findByMandtDelivNumb(String deliv_numb) throws Exception {
        return wTRegService.findByMandtDelivNumb(deliv_numb);
    }

    public DefaultComboBoxModel getModeTypeModel(Constants.WeighingProcess.MODE mode) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        List<MODE_DETAIL> permissions = configuration.getListModePermissions();

        if (mode == Constants.WeighingProcess.MODE.INPUT) {
            Constants.WeighingProcess.getInputModeList().forEach(item -> {
                if (permissions.contains(item.getModeDetail())) {
                    model.addElement(item);
                }
            });
        } else if (mode == Constants.WeighingProcess.MODE.OUTPUT) {
            Constants.WeighingProcess.getOutputModeList().forEach(item -> {
                if (permissions.contains(item.getModeDetail())) {
                    model.addElement(item);
                }
            });
        }

        return model;
    }

    public String loadTransportAgentAbbr(String plateNo) {
        List<TransportAgentVehicle> transportAgentVehicles = transportAgentVehicleRepository.findByPlateNo(plateNo);
        if (transportAgentVehicles.size() > 0) {
            return transportAgentVehicles.get(0).getTransportAgent().getAbbr();
        }

        return "";
    }

    public Float loadVehicleLoading(String plateNo) {
        VehicleLoad vehicleLoad = vehicleLoadRepository.findByPlateNo(plateNo);
        if (vehicleLoad != null) {
            return vehicleLoad.getVehicleLoad();
        }

        return 0f;
    }

    public List<BatchStock> getBatchStocks(SLoc sloc, String[] arr_matnr) {
        return wTRegService.getBatchStocks(sloc, arr_matnr);
    }

    public void getSyncBatchStocks(SLoc sloc, String[] arr_matnr) {
        wTRegService.getSyncBatchStocks(sloc, arr_matnr);
    }

    public DefaultComboBoxModel getBatchStockModel(List<BatchStock> batchStocks) {
        return new DefaultComboBoxModel(batchStocks.toArray());
    }

    public Vendor getVendor(String strVendor) {
        return vendorRepository.findByLifnr(strVendor);
    }

    public boolean checkPlateNoInVendor(String abbr, String plateNo) {
        return transportAgentVehicleRepository.findByAbbrAndPlateNo(abbr, plateNo) != null;
    }

    public MaterialInternal getMaterialInternal(String matnr) {
        return materialInternalRepository.findByMatnr(matnr);
    }

    public Material getMaterial(String matnr) {
        return materialRepository.findByMatnr(matnr);
    }

    public BigDecimal getSumQuantityWithPoNo(String poNo) {
        BigDecimal result = BigDecimal.ZERO;

        List<WeightTicketDetail> weightTicketDetails = weightTicketDetailRepository.findByPoNo(poNo);
        for (WeightTicketDetail weightTicketDetail : weightTicketDetails) {
            result = result.add(weightTicketDetail.getRegItemQuantity());
        }

        return result;
    }

    /**
     * Get list material inter for mode Other
     *
     * @return
     */
    public DefaultComboBoxModel getListMaterialInternal() {

        List<MaterialInternal> materials = materialInternalRepository.getMaterialInternals();
        DefaultComboBoxModel result = new DefaultComboBoxModel();

        materials = materials.stream()
                .filter(FunctionalUtil.distinctByKey(p -> p.getMatnr()))
                .collect(Collectors.toList());

        for (MaterialInternal material : materials) {
            if (result.getIndexOf(material) < 0 && material.getMatnr() != null
                    && material.getMaktx() != null && !material.getMaktx().isEmpty()) {
                result.addElement(material);
            }
        }
        return result;
    }

    public DefaultComboBoxModel getSlocModel() {
        return new DefaultComboBoxModel(sLocRepository.getListSLoc().toArray());
    }

    public DefaultComboBoxModel getVendorModel() {
        return new DefaultComboBoxModel(vendorRepository.getListVendor().toArray());
    }
    
    public DefaultComboBoxModel getVendorModelByEkorg(String ekorg) {
        return new DefaultComboBoxModel(vendorRepository.findByEkorg(ekorg).toArray());
    }

    public DefaultComboBoxModel getCustomerModel() {
        return new DefaultComboBoxModel(customerRepository.getListCustomer().toArray());
    }

    public Unit getUnit() {
        return wTRegService.getUnit();
    }

    public List<WeightTicket> findListWeightTicket(String sfrom, String sto,
            String creator, String driverName, String plateNo,
            String material, StatusEnum status, ModeEnum mode) throws Exception {
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return wTRegService.findListWeightTicket(from, to, creator, driverName, plateNo, material, status, mode);
    }

    public SLoc getSloc(String sloc) {
        return sLocRepository.findByLgort(sloc);
    }

    public List<String> getListLgortByMatnr(String matnr, boolean isInternal) {
        if (isInternal) {
            return materialInternalRepository.getListLgortByMatnr(matnr);
        } else {
            return materialRepository.getListLgortByMatnr(matnr);
        }
    }

    public List<String> getListLgortByMatnr(List<String> matnrs, boolean isInternal) {
        if (isInternal) {
            return materialInternalRepository.getListLgortByMatnr(matnrs);
        } else {
            return materialRepository.getListLgortByMatnr(matnrs);
        }
    }

    public DefaultComboBoxModel getSlocModel(List<String> lgorts) {
        return new DefaultComboBoxModel(getListSLoc(lgorts).toArray());
    }
    
    public List<SLoc> getListSLoc(List<String> lgorts) {
        return wTRegService.getListSLoc(lgorts);
    }
    
    public Customer getCustomer(String kunnr) {
        return customerRepository.findByKunnr(kunnr);
    }
}
