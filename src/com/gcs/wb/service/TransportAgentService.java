/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.TransportAgentVehicle;
import com.gcs.wb.jpa.entity.Vehicle;
import com.gcs.wb.jpa.repositorys.TransportAgentVehicleRepository;
import com.gcs.wb.jpa.repositorys.VehicleRepository;
import com.gcs.wb.views.TransportAgentView;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author THANGPT
 */
public class TransportAgentService {

    private VehicleRepository vehicleRepository = new VehicleRepository();
    private TransportAgentVehicleRepository transportAgentVehicleRepository = new TransportAgentVehicleRepository();
    EntityManager entityManager = JPAConnector.getInstance();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    private JFrame mainFrame = WeighBridgeApp.getApplication().getMainFrame();
    public ResourceMap resourceMapMsg = Application.getInstance(WeighBridgeApp.class).getContext().getResourceMap(TransportAgentView.class);
    SAPService sapService = new SAPService();

    public DefaultListModel getTransportAgentsModel() {
        List<TransportAgent> transportAgents = sapService.getTransportAgentList();
        DefaultListModel model = new DefaultListModel();
        for (TransportAgent transportAgent : transportAgents) {
            model.addElement(transportAgent);
        }
        return model;
    }

    public DefaultListModel getVehiclesModel(TransportAgent transportAgentSelected) {
        List<TransportAgentVehicle> transportAgentVehicles = transportAgentVehicleRepository.findByTransportAgentId(transportAgentSelected.getId());
        DefaultListModel model = new DefaultListModel();
        for (TransportAgentVehicle transportAgentVehicle : transportAgentVehicles) {
            model.addElement(transportAgentVehicle.getVehicle());
        }
        return model;
    }

    /**
     * save Vehicle
     * @param entityTransaction
     * @param licensePlate
     * @param vehicle
     * @param transportAgentSelected 
     */
    public void saveVehicle(String licensePlate, TransportAgent transportAgentSelected) {
        Vehicle vehicle = vehicleRepository.findByPlateNo(licensePlate);
        
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
    }

    /**
     * remove Vehicle
     * @param transportId
     * @param vehicleId 
     */
    public void vehicleRemoveActionPerformed(TransportAgent transportAgentSelected, Vehicle vehicleSelected) {
        try {
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            TransportAgentVehicle transportAgentVehicle =
                    transportAgentVehicleRepository.findByTransportAgentIdAndVehicleId(transportAgentSelected.getId(), vehicleSelected.getId());
            entityManager.remove(transportAgentVehicle);

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.deleteVehicleFalse"));
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            entityManager.clear();
        }
    }
    /**
     * action prohibit Vehicle
     * @param vehicleSelected
     * @param prohibit 
     */
    public void prohibitApplyActionPerformed(Vehicle vehicleSelected, boolean isProhibitVehicle) {
        try {
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            vehicleSelected.setProhibit(isProhibitVehicle);
            entityManager.merge(vehicleSelected);
            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.prohibitFalse"));
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            entityManager.clear();
        }
    }
}
