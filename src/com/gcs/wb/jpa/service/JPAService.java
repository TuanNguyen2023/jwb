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
import com.gcs.wb.jpa.entity.TransportAgentVehicle;
import com.gcs.wb.jpa.entity.Vehicle;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.repositorys.BatchStocksRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentVehicleRepository;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.views.TransportAgentView;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application;

/**
 *
 * @author HANGTT
 */
public class JPAService {

    EntityManager entityManager = JPAConnector.getInstance();
    BatchStocksRepository batchStocksRepository = new BatchStocksRepository();
    AppConfig config = WeighBridgeApp.getApplication().getConfig();
    private TransportAgentRepository transportAgentRepository = new TransportAgentRepository();
    private TransportAgentVehicleRepository transportAgentVehicleRepository = new TransportAgentVehicleRepository();
    private JFrame mainFrame = WeighBridgeApp.getApplication().getMainFrame();
    public org.jdesktop.application.ResourceMap resourceMapMsg = Application.getInstance(WeighBridgeApp.class).getContext().getResourceMap(TransportAgentView.class);

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

    /**
     * get list "Kho"
     * @return 
     */
    public List<SLoc> getSlocList() {
        List<SLoc> slocs = new ArrayList<SLoc>();
        TypedQuery<SLoc> tSlocQ = entityManager.createNamedQuery("SLoc.findByMandtWPlant", SLoc.class);
        tSlocQ.setParameter("mandt", config.getsClient());
        tSlocQ.setParameter("wPlant", config.getwPlant());
        slocs = tSlocQ.getResultList();
        return slocs;
    }

    /**
     * get list DVVC
     * @return 
     */
    public List<TransportAgent> getTransportAgent() {
        List<TransportAgent> transportAgents = transportAgentRepository.getListTransportAgent();
        return transportAgents;
    }

    /**
     * get list BS Xe
     * @param id
     * @return 
     */
    public List<TransportAgentVehicle> getVehicle(int id) {
        List<TransportAgentVehicle> transportAgentVehicles = transportAgentVehicleRepository.findByTransportAgentId(id);
        return transportAgentVehicles;
    }

    /**
     * remove Vehicle
     * @param transportId
     * @param vehicleId 
     */
    public void removeVehicle(int transportId, int vehicleId) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            TransportAgentVehicle transportAgentVehicle =
                    transportAgentVehicleRepository.findByTransportAgentIdAndVehicleId(transportId, vehicleId);
            entityManager.remove(transportAgentVehicle);

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.deleteVehicleFalse"));
            entityTransaction.rollback();
            entityManager.clear();
        }
    }

    /**
     * action prohibit Vehicle
     * @param vehicleSelected
     * @param prohibit 
     */
    public void prohibitApplyVehicle(Vehicle vehicleSelected, boolean prohibit) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            vehicleSelected.setProhibit(prohibit);
            entityManager.merge(vehicleSelected);
            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.prohibitFalse"));
            entityTransaction.rollback();
            entityManager.clear();
        }
    }

    /**
     * save Vehicle
     * @param entityTransaction
     * @param licensePlate
     * @param vehicle
     * @param transportAgentSelected 
     */
    public void saveVehicle(String licensePlate, Vehicle vehicle, TransportAgent transportAgentSelected) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            TransportAgentVehicle transportAgentVehicle = new TransportAgentVehicle();
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            if (vehicle == null) {
                // insert new vehicle
                vehicle = new Vehicle();
                vehicle.setPlateNo(licensePlate);
                vehicle.setProhibit(false);
                entityManager.persist(vehicle);

                // insert vehicle relationship
                transportAgentVehicle.setTransportAgent(transportAgentSelected);
                transportAgentVehicle.setVehicle(vehicle);
                entityManager.persist(transportAgentVehicle);
            } else {
                // check vehicle has exit in transport agent
                transportAgentVehicle = transportAgentVehicleRepository.findByTransportAgentIdAndVehicleId(transportAgentSelected.getId(), vehicle.getId());
                if (transportAgentVehicle != null) {
                    JOptionPane.showMessageDialog(mainFrame,
                            resourceMapMsg.getString("msg.duplicationPlateNo",
                            licensePlate,
                            transportAgentSelected.getName()));
                    return;
                } else {
                    // insert vehicle relationship
                    transportAgentVehicle = new TransportAgentVehicle();
                    transportAgentVehicle.setTransportAgent(transportAgentSelected);
                    transportAgentVehicle.setVehicle(vehicle);
                    entityManager.persist(transportAgentVehicle);
                }
            }

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.addPlateNoFalse"));

            entityTransaction.rollback();
            entityManager.clear();
        }
    }

    /**
     * sync DVVC
     * @param transportDBs
     * @param transportSaps 
     */
    public void syncTransportAgent(List<TransportAgent> transportDBs, List<TransportAgent> transportSaps) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        // delete data DB not exist SAP
        for (TransportAgent transportAgent : transportDBs) {
            if (transportSaps.indexOf(transportAgent) == -1) {
                // delete in table Vehicle
                List<TransportAgentVehicle> transportAgentVehicles = transportAgentVehicleRepository.findByTransportAgentId(transportAgent.getId());

                if (!entityTransaction.isActive()) {
                    entityTransaction.begin();
                }

                try {
                    for (TransportAgentVehicle transportAgentVehicle : transportAgentVehicles) {
                        entityManager.remove(transportAgentVehicle);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.deleteVehicleFalse"));
                    entityTransaction.rollback();
                    //return null;
                }

                // delete dvvc
                try {
                    if (!entityManager.contains(transportAgent)) {
                        transportAgent = entityManager.merge(transportAgent);
                    }
                    entityManager.remove(transportAgent);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.deleteProviderFalse"));
                    entityTransaction.rollback();
                    //return null;
                }

                entityTransaction.commit();
                entityManager.clear();
            }
        }

        try {
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }
            // update dara SAP -> DB
            for (TransportAgent transportAgentSAP : transportSaps) {
                int index = transportDBs.indexOf(transportAgentSAP);
                if (index == -1) {
                    entityManager.persist(transportAgentSAP);
                } else {
                    //transportAgentSAP.setId(transportDBs.get(index).getId());
                    entityManager.merge(transportAgentSAP);
                }
            }

            entityTransaction.commit();
            entityManager.clear();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.syncProviderFalse"));
            entityTransaction.rollback();
            //return null;
        }
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
