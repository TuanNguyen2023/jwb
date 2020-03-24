/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Vehicle;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.DefaultListModel;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class VehicleRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = Logger.getLogger(this.getClass());

    public DefaultListModel getVModel(String abbr) {
        List<Vehicle> vehicles = getListVehicle(abbr);
        DefaultListModel model = new DefaultListModel();
        for (Vehicle vh : vehicles) {
            model.addElement(vh);
        }
        return model;
    }

    public List<Vehicle> getListVehicle(String abbr) {
        List<Vehicle> list = new ArrayList<>();
        try {
            TypedQuery<Vehicle> typedQuery = entityManager.createNamedQuery("Vehicle.findByAbbr", Vehicle.class);
            typedQuery.setParameter("abbr", abbr);
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public Vehicle findByPlateNo(String plateNo) {
        try {
            TypedQuery<Vehicle> typedQuery = entityManager.createNamedQuery("Vehicle.findByPlateNo", Vehicle.class);
            typedQuery.setParameter("plateNo", plateNo);

            List<Vehicle> vehicles = typedQuery.getResultList();
            if (vehicles != null && vehicles.size() == 1) {
                return vehicles.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }
}
