/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.SAP2Local;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.TransportAgentVehicle;
import com.gcs.wb.jpa.entity.Vehicle;
import com.gcs.wb.jpa.repositorys.TransportAgentRepository;
import com.gcs.wb.jpa.repositorys.TransportAgentVehicleRepository;
import com.gcs.wb.jpa.repositorys.VehicleRepository;
import com.gcs.wb.jpa.service.JPAService;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.views.TransportAgentView;
import java.awt.Color;
import java.util.List;
import java.util.regex.Matcher;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author THANGPT
 */
public class TransportAgentController {

    private EntityManager entityManager = JPAConnector.getInstance();
    private TransportAgentVehicleRepository transportAgentVehicleRepository = new TransportAgentVehicleRepository();
    private TransportAgentRepository transportAgentRepository = new TransportAgentRepository();
    private VehicleRepository vehicleRepository = new VehicleRepository();
    private JFrame mainFrame = WeighBridgeApp.getApplication().getMainFrame();
    public ResourceMap resourceMapMsg = Application.getInstance(WeighBridgeApp.class).getContext().getResourceMap(TransportAgentView.class);
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

    public void saveVehicle(JTextField txtLicensePlate, TransportAgent transportAgentSelected) {
        Vehicle vehicle = vehicleRepository.findByPlateNo(txtLicensePlate.getText().trim());
        jpaService.saveVehicle(txtLicensePlate.getText().trim(), vehicle, transportAgentSelected);
    }

    public void vehicleRemoveActionPerformed(TransportAgent transportAgentSelected, Vehicle vehicleSelected) {
        jpaService.removeVehicle(transportAgentSelected.getId(), vehicleSelected.getId());
    }

    public void prohibitApplyActionPerformed(Vehicle vehicleSelected, JCheckBox chkProhibitVehicle) {
        jpaService.prohibitApplyVehicle(vehicleSelected, chkProhibitVehicle.isSelected());
    }

    public boolean validateLicensePlate(JTextField txtLicensePlate, JLabel lblLicensePlate) {
        boolean isLicensePlate = false;
        String licensePlateStr = txtLicensePlate.getText().trim();

        Matcher matcher = Constants.TransportAgent.LICENSE_PLATE_PATTERN.matcher(licensePlateStr);
        isLicensePlate = !licensePlateStr.isEmpty() && matcher.matches();
        lblLicensePlate.setForeground(isLicensePlate ? Color.black : Color.red);

        return isLicensePlate;
    }
}
