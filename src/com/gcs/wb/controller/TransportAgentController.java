/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.Vehicle;
import com.gcs.wb.jpa.entity.VehicleType;
import com.gcs.wb.jpa.repositorys.VehicleTypeRepository;
import com.gcs.wb.service.TransportAgentService;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

/**
 *
 * @author THANGPT
 */
public class TransportAgentController {

    private TransportAgentService transportAgentService = new TransportAgentService();
    private VehicleTypeRepository vehicleTypeRepository = new VehicleTypeRepository();

    public DefaultListModel getTransportAgentsModel() {
        return transportAgentService.getTransportAgentsModel();
    }

    public DefaultListModel getVehiclesModel(TransportAgent transportAgentSelected) {
        return transportAgentService.getVehiclesModel(transportAgentSelected);
    }

    public void saveVehicle(String txtLicensePlate, TransportAgent transportAgentSelected, Date validFrom, Date validTo, VehicleType vehicleType) {
        transportAgentService.saveVehicle(txtLicensePlate, transportAgentSelected, validFrom, validTo, vehicleType);
    }

    public void vehicleRemoveActionPerformed(TransportAgent transportAgentSelected, Vehicle vehicleSelected) {
        transportAgentService.vehicleRemoveActionPerformed(transportAgentSelected, vehicleSelected);
    }

    public Vehicle getVehicle(String plateNo) {
        return transportAgentService.findByPlateNo(plateNo);
    }

    public DefaultComboBoxModel getVehicleTypesModel() {
        return new DefaultComboBoxModel(vehicleTypeRepository.getListVehicleType().toArray());
    }
}
