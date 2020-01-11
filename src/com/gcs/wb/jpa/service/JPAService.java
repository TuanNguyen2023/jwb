/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.BatchStocks;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.Vehicle;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.repositorys.BatchStocksRepository;
import com.gcs.wb.model.AppConfig;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author HANGTT
 */
public class JPAService {
    EntityManager entityManager = JPAConnector.getInstance();
    BatchStocksRepository batchStocksRepository = new BatchStocksRepository();
    AppConfig config = WeighBridgeApp.getApplication().getConfig();
    
    /**
     * Get data material
     * @param desc
     * @return 
     */
    public List<Material> getListMaterial() {
        List<Material> materials = new ArrayList<Material>();
        TypedQuery<Material> tMaterial =
                entityManager.createQuery("SELECT m FROM Material m WHERE m.materialPK.wplant = :wplant order by m.materialPK.matnr asc", Material.class);
        tMaterial.setParameter("wplant", config.getwPlant());
        materials = tMaterial.getResultList();
        return materials;
    }
    
    /**
     * Get data for "Vat tu"
     * @param desc
     * @return 
     */
    public List<Material> getListMaterialByDesc(String desc) {
        List<Material> result = new ArrayList<Material>();
        List<Material> materials = getListMaterial();
        for (Material m : materials) {
            if (m.getMaktx().contains(desc) || m.getMaktg().contains(desc)) {
                result.add(m);
            }
        }
        return result;
    }
    
    /**
     * 
     * Get data for "Vendor boc xep/van chuyen"
     * @return 
     */
    public List<Vendor> getVendorList() {
        TypedQuery<Vendor> tVendor = entityManager.createNamedQuery("Vendor.findByMandt", Vendor.class);
        tVendor.setParameter("mandt", config.getsClient());
        List<Vendor> vendors = new ArrayList<Vendor>();
        vendors = tVendor.getResultList();
        return vendors;
    }
    
    public List<SLoc> getSlocList() {
        List<SLoc> slocs = new ArrayList<SLoc>();
        TypedQuery<SLoc> tSlocQ = entityManager.createNamedQuery("SLoc.findByMandtWPlant", SLoc.class);
        tSlocQ.setParameter("mandt", config.getsClient());
        tSlocQ.setParameter("wPlant", config.getwPlant());
        slocs = tSlocQ.getResultList();
        return slocs;
    }
    
    public List<TransportAgent> getTransportAgent() {
        List<TransportAgent> transportAgents = new ArrayList<TransportAgent>();
        TypedQuery<TransportAgent> typedQuery = entityManager.createNamedQuery("TransportAgent.findAll", TransportAgent.class);
        transportAgents = typedQuery.getResultList();
        return transportAgents;
    }
    
    public List<Vehicle> getVehicle(String abbr) {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        TypedQuery<Vehicle> typedQuery = entityManager.createNamedQuery("Vehicle.findByTaAbbr", Vehicle.class);
        typedQuery.setParameter("taAbbr", abbr);
        vehicles = typedQuery.getResultList();
        return vehicles;
    }
    
    /**
     * get list batch stock
     * @param client
     * @param plant
     * @param lgort
     * @param matnrRef
     * @return 
     */
    public List<BatchStocks> getBatchStocks(String client, String plant, String lgort, String matnrRef) {
        List<BatchStocks> batchStocks = new ArrayList<BatchStocks>();
        batchStocks = batchStocksRepository.getList(client, plant, lgort, matnrRef);
        return batchStocks;
    }
}
