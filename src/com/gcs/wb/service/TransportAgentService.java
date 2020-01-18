/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.TransportAgentVehicle;
import com.gcs.wb.jpa.entity.Vehicle;
import com.gcs.wb.jpa.repositorys.VehicleRepository;
import com.gcs.wb.jpa.service.JPAService;
import java.awt.Color;
import java.util.List;
import java.util.regex.Matcher;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author THANGPT
 */
public class TransportAgentService {
    
    private VehicleRepository vehicleRepository = new VehicleRepository();
    
    SAPService sapService = new SAPService();
    JPAService jpaService = new JPAService();
    
    public DefaultListModel getTransportAgentsModel() {
        List<TransportAgent> transportAgents = sapService.getTransportAgentList();
        DefaultListModel model = new DefaultListModel();
        for (TransportAgent transportAgent : transportAgents) {
            model.addElement(transportAgent);
        }
        return model;
    }

    public DefaultListModel getVehiclesModel(TransportAgent transportAgentSelected) {
        List<TransportAgentVehicle> transportAgentVehicles = jpaService.getVehicle(transportAgentSelected.getId());
        DefaultListModel model = new DefaultListModel();
        for (TransportAgentVehicle transportAgentVehicle : transportAgentVehicles) {
            model.addElement(transportAgentVehicle.getVehicle());
        }
        return model;
    }

    public void saveVehicle(String txtLicensePlate, TransportAgent transportAgentSelected) {
        Vehicle vehicle = vehicleRepository.findByPlateNo(txtLicensePlate);
        jpaService.saveVehicle(txtLicensePlate, vehicle, transportAgentSelected);
    }

    public void vehicleRemoveActionPerformed(TransportAgent transportAgentSelected, Vehicle vehicleSelected) {
        jpaService.removeVehicle(transportAgentSelected.getId(), vehicleSelected.getId());
    }

    public void prohibitApplyActionPerformed(Vehicle vehicleSelected, boolean isProhibitVehicle) {
        jpaService.prohibitApplyVehicle(vehicleSelected, isProhibitVehicle);
    }
}
