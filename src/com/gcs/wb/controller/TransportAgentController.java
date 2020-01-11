/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.SAP2Local;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.Vehicle;
import com.gcs.wb.model.AppConfig;
import java.awt.Color;
import java.util.List;
import java.util.regex.Matcher;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author THANGPT
 */
public class TransportAgentController {

    public DefaultListModel getTransportAgentsModel(EntityManager entityManager, ResourceMap resourceMapMsg) {
        AppConfig config = WeighBridgeApp.getApplication().getConfig();
        TypedQuery<TransportAgent> typedQuery = entityManager.createNamedQuery("TransportAgent.findAll", TransportAgent.class);
        List<TransportAgent> transportAgents = typedQuery.getResultList();

        // get from SAP
        List<com.gcs.wb.jpa.entity.TransportAgent> transportAgentsSAP = SAP2Local.getTransportAgentList(config.getwPlant());
        //sync SAP <=> DB
        // delete data DB not exist SAP
        for (TransportAgent transportAgent : transportAgents) {
            if (transportAgentsSAP.indexOf(transportAgent) == -1) {
                // delete in table Vehicle
                TypedQuery<Vehicle> vehicleTypedQuery = entityManager.createNamedQuery("Vehicle.findByTaAbbr", Vehicle.class);
                vehicleTypedQuery.setParameter("taAbbr", transportAgent.getAbbr());
                List<Vehicle> vehicles = vehicleTypedQuery.getResultList();

                if (!entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().begin();
                }
                try {
                    for (Vehicle vehicle : vehicles) {
                        entityManager.remove(vehicle);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), resourceMapMsg.getString("msg.deleteVehicleFalse"));
                    entityManager.getTransaction().rollback();
                }
                // delete dvvc
                try {
                    if (!entityManager.contains(transportAgent)) {
                        transportAgent = entityManager.merge(transportAgent);
                    }
                    entityManager.remove(transportAgent);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), resourceMapMsg.getString("msg.deleteProviderFalse"));
                    entityManager.getTransaction().rollback();
                }

                entityManager.getTransaction().commit();
                entityManager.clear();
            }
        }

        try {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            // update dara SAP -> DB
            for (com.gcs.wb.jpa.entity.TransportAgent transportAgentSAP : transportAgentsSAP) {

                if (transportAgents.indexOf(transportAgentSAP) == -1) {
                    entityManager.persist(transportAgentSAP);
                } else {
                    entityManager.merge(transportAgentSAP);
                }
            }
            entityManager.getTransaction().commit();
            entityManager.clear();

        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
        }
        // get dvvc
        transportAgents = typedQuery.getResultList();
        DefaultListModel model = new DefaultListModel();
        for (TransportAgent transportAgent : transportAgents) {
            model.addElement(transportAgent);
        }
        return model;
    }

    public DefaultListModel getVehiclesModel(EntityManager entityManager, TransportAgent transportAgentSelected) {
        TypedQuery<Vehicle> typedQuery = entityManager.createNamedQuery("Vehicle.findByTaAbbr", Vehicle.class);
        typedQuery.setParameter("taAbbr", transportAgentSelected.getAbbr());
        List<Vehicle> vehicles = typedQuery.getResultList();
        DefaultListModel model = new DefaultListModel();
        for (Vehicle vehicle : vehicles) {
            model.addElement(vehicle);
        }
        return model;
    }

    public Vehicle saveVehicle(EntityManager entityManager, TransportAgent transportAgentSelected, JTextField txtLicensePlate,
            Vehicle vehicleSelected, JList lstVehicle, JRootPane rootPane, DefaultListModel getVehiclesModel) {
        Vehicle vehicle = entityManager.find(Vehicle.class, txtLicensePlate.getText().trim());
        if (vehicle == null) {
            vehicle = new Vehicle();
            vehicle.setSoXe(txtLicensePlate.getText().trim());
            vehicle.setTaAbbr(transportAgentSelected.getAbbr());
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(vehicle);
            entityManager.getTransaction().commit();
            entityManager.clear();
            //setVehicleCreatable(false);
            vehicleSelected = null;
            txtLicensePlate.setText("");
            lstVehicle.clearSelection();
            lstVehicle.setModel(getVehiclesModel);
        } else {
            TransportAgent transportAgent = entityManager.find(TransportAgent.class, vehicle.getTaAbbr());
            if (transportAgent != null) {
                JOptionPane.showMessageDialog(rootPane, "Số xe "
                        + txtLicensePlate.getText().trim()
                        + " đã được đăng ký cho đơn vị vận chuyển "
                        + transportAgent.getName());
            }
        }
        return vehicle;
    }

    public void vehicleRemoveActionPerformed(EntityManager entityManager, JList lstVehicle,
            Vehicle vehicleSelected, DefaultListModel getVehiclesModel) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.remove(vehicleSelected);
        entityManager.getTransaction().commit();

        entityManager.clear();
        lstVehicle.clearSelection();
        lstVehicle.setModel(getVehiclesModel);
        vehicleSelected = null;
    }

    public void prohibitApplyActionPerformed(EntityManager entityManager, Vehicle vehicleSelected,
            JCheckBox chkProhibitVehicle, JList lstVehicle, DefaultListModel getVehiclesModel) {
        vehicleSelected.setProhibit(chkProhibitVehicle.isSelected());

        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }

        entityManager.merge(vehicleSelected);
        entityManager.getTransaction().commit();
        entityManager.clear();

        chkProhibitVehicle.setSelected(false);
        vehicleSelected = null;
        lstVehicle.clearSelection();
        lstVehicle.setModel(getVehiclesModel);
    }
    
    public boolean validateLicensePlate(JTextField txtLicensePlate, JLabel lblLicensePlate){
        boolean isLicensePlate = false;
        String licensePlateStr = txtLicensePlate.getText().trim();

        Matcher matcher = Constants.TransportAgent.LICENSE_PLATE_PATTERN.matcher(licensePlateStr);
        isLicensePlate = !licensePlateStr.isEmpty() && matcher.matches();
        lblLicensePlate.setForeground(isLicensePlate ? Color.black : Color.red);

        return isLicensePlate;
    }
}
