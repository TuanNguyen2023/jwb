/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.jpa.entity.TransportAgent;
import com.gcs.wb.jpa.entity.Vehicle;
import com.gcs.wb.service.TransportAgentService;
import java.util.Date;
import javax.swing.DefaultListModel;

/**
 *
 * @author THANGPT
 */
public class TransportAgentController {
    
    private TransportAgentService transportAgentService = new TransportAgentService();
    
    public DefaultListModel getTransportAgentsModel() {
        return transportAgentService.getTransportAgentsModel();
    }
    
    public DefaultListModel getVehiclesModel(TransportAgent transportAgentSelected) {
        return transportAgentService.getVehiclesModel(transportAgentSelected);
    }
    
    public void saveVehicle(String txtLicensePlate, TransportAgent transportAgentSelected, Date validFrom, Date validTo) {
        transportAgentService.saveVehicle(txtLicensePlate, transportAgentSelected,validFrom, validTo);
    }
    
    public void vehicleRemoveActionPerformed(TransportAgent transportAgentSelected, Vehicle vehicleSelected) {
        transportAgentService.vehicleRemoveActionPerformed(transportAgentSelected, vehicleSelected);
    }
    
//    public void prohibitApplyActionPerformed(Vehicle vehicleSelected, boolean isProhibitVehicle) {
//        transportAgentService.prohibitApplyActionPerformed(vehicleSelected, isProhibitVehicle);
//    }
}
