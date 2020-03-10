/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.enums.ModeEnum;
import com.gcs.wb.base.enums.StatusEnum;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.*;
import com.gcs.wb.jpa.entity.OutboundDeliveryDetail;
import com.gcs.wb.jpa.repositorys.BatchStockRepository;
import com.gcs.wb.jpa.repositorys.WeightTicketRegistarationRepository;
import com.gcs.wb.jpa.repositorys.CustomerRepository;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.OutboundDeliveryRepository;
import com.gcs.wb.jpa.repositorys.OutboundDetailRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentVehicleRepository;
import com.gcs.wb.jpa.repositorys.UnitRepository;
import com.gcs.wb.jpa.repositorys.VehicleRepository;
import com.gcs.wb.jpa.repositorys.VendorRepository;
import com.gcs.wb.jpa.repositorys.WeightTicketRepository;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author THANGPT
 */
public class WeightTicketRegistrationService {

    EntityManager entityManager = JPAConnector.getInstance();
    CustomerRepository customerRepository = new CustomerRepository();
    VendorRepository vendorRepository = new VendorRepository();
    WeightTicketRegistarationRepository wTRegRepository = new WeightTicketRegistarationRepository();
    MaterialRepository materialRepository = new MaterialRepository();
    WeightTicketRepository weightTicketRepository = new WeightTicketRepository();
    OutboundDeliveryRepository outboundDeliveryRepository = new OutboundDeliveryRepository();
    VehicleRepository vehicleRepository = new VehicleRepository();
    TransportAgentVehicleRepository transportAgentVehicleRepository = new TransportAgentVehicleRepository();
    BatchStockRepository batchStockRepository = new BatchStockRepository();
    UnitRepository unitRepository = new UnitRepository();

    SAPService sAPService = new SAPService();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();

    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<Object[]> checkExistDO(String doNumber) {
        return wTRegRepository.checkDoExist(doNumber, configuration.getWkPlant());
    }

    public int shippingPointVar(String shipPoint, String Matnr) {
        return wTRegRepository.getSPVar(configuration.getWbId(), shipPoint, Matnr);
    }

    public List<Material> getListMaterial() {
        return materialRepository.getListMaterial();
    }

    public WeightTicket findByDeliveryOrderNo(String doNumber) {
        return weightTicketRepository.findByDeliveryOrderNoNotExistEbeln(doNumber);
    }

    public List<WeightTicket> getListByDeliveryOrderNo(String outbNumber) {
        return weightTicketRepository.getListByDeliveryOrderNo(outbNumber);
    }

    public Customer findByKunnr(String kunag) {
        return customerRepository.findByKunnr(kunag);
    }

    public Vendor findByLifnr(String lifnr) {
        return vendorRepository.findByLifnr(lifnr);
    }

    public OutboundDelivery findByDeliveryOrderNumber(String deliveryOrderNo) {
        return outboundDeliveryRepository.findByDeliveryOrderNo(deliveryOrderNo);
    }

    public Vehicle findByPlateNo(String plateNo) {
        return vehicleRepository.findByPlateNo(plateNo);
    }

    public List<TransportAgentVehicle> findByVehicleId(int vehicleId) {
        return transportAgentVehicleRepository.findByVehicleId(vehicleId);
    }

    public List<WeightTicket> findByDateFull(String sfrom, String sto, String creator, String taixe, String loaihang, String bienso) throws Exception {
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return weightTicketRepository.findByDateFull(from, to, creator, taixe, loaihang, bienso);
    }

    public List<WeightTicket> findByDateNull(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return weightTicketRepository.findByDateNull(from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByDateNullAll(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return weightTicketRepository.findByDateNullAll(from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByDateDissolved(String sfrom, String sto,
            String creator, String taixe,
            String loaihang, String bienso) throws Exception {
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return weightTicketRepository.findByDateDissolved(from, to, creator, taixe, loaihang, bienso);
    }

    public List<WeightTicket> findByDateDissolvedNull(String sfrom, String sto,
            String creator, String taixe,
            String bienso) throws Exception {
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return weightTicketRepository.findByDateDissolvedNull(from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByDateDissolvedNullAll(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return weightTicketRepository.findByDateDissolvedNullAll(from, to, creator, taixe, bienso);
    }

    public int getCountTicketMonth(String plant) {
        int count = 0;
        try {
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("[p_get_weight_ticket_seq_month]");
                query.registerStoredProcedureParameter("pWplant", String.class, ParameterMode.IN);
                query.setParameter("pWplant", plant);
                query.execute();
                List<Object[]> list = query.getResultList();
                if (list != null && list.size() > 0) {
                    Object[] firstRow = list.get(0);
                    count = Integer.parseInt(firstRow[0].toString());
                }
                entityTransaction.commit();
            }
        } catch (NumberFormatException e) {
            logger.error(e.toString());

        }
        return count;
    }

    public int getCountTicketDay(String plant) {
        int count = 0;
        try {
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("p_get_weight_ticket_seq_day");
                query.registerStoredProcedureParameter("pWplant", String.class, ParameterMode.IN);
                query.setParameter("pWplant", plant);
                query.execute();
                List<Object[]> list = query.getResultList();
                if (list != null && list.size() > 0) {
                    Object[] firstRow = list.get(0);
                    count = Integer.parseInt(firstRow[0].toString());
                }
                entityTransaction.commit();
            }
        } catch (NumberFormatException e) {
            logger.error(e.toString());
        }
        return count;
    }

    public List<OutboundDeliveryDetail> findByMandtDelivNumb(String deliv_numb) throws Exception {
        //String devNumber = "%" + deliv_numb + "%";
        OutboundDetailRepository repo = new OutboundDetailRepository();
        return repo.findByDeliveryOrderNo(deliv_numb);
    }

    public List<BatchStock> getBatchStocks(SLoc sloc, String[] arr_matnr) {
        List<BatchStock> batchStocks = new ArrayList<>();
        SyncMasterDataService syncData = new SyncMasterDataService();
        List<BatchStock> items = new ArrayList<BatchStock>();
        for (String matnr : arr_matnr) {
            items = batchStockRepository.getListBatchStock(configuration.getWkPlant(),
                    sloc.getLgort(), matnr);

            if (CollectionUtils.isEmpty(items) && !WeighBridgeApp.getApplication().isOfflineMode()) {
                sAPService.syncBatchStocks(sloc.getLgort(), matnr);
                items = batchStockRepository.getListBatchStock(configuration.getWkPlant(),
                        sloc.getLgort(), matnr);
            }

            if (items.size() > 0) {
                batchStocks.addAll(items);
            }
        }

        return batchStocks;
    }

    public void getSyncBatchStocks(SLoc sloc, String[] arr_matnr) {
        for (String matnr : arr_matnr) {
            sAPService.syncBatchStocks(sloc.getLgort(), matnr);
        }
    }

    public Unit getUnit() {
        List<Unit> units = unitRepository.getListUnit();
        return units.get(0);
    }

    public List<WeightTicket> findListWeightTicket(Date from, Date to,
            String creator, String driverName, String plateNo,
            String material, StatusEnum status, ModeEnum mode) throws Exception {
        return weightTicketRepository.findListWeightTicket(from, to, creator, driverName, plateNo, material, status, mode);
    }
}
