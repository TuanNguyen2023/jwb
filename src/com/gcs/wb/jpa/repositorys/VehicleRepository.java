/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.JpaProperties;
import com.gcs.wb.jpa.entity.Vehicle;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.DefaultListModel;

/**
 *
 * @author dinhhn.vr
 */
public class VehicleRepository {

    EntityManager entityManager = JpaProperties.getEntityManager();

    public DefaultListModel getVModel(String abbr) {
        List<Vehicle> vehicles = getListVehicle(abbr);
        DefaultListModel model = new DefaultListModel();
        for (Vehicle vh : vehicles) {
            model.addElement(vh);
        }
        return model;
    }

    public List<Vehicle> getListVehicle(String abbr) {
        TypedQuery<Vehicle> typedQuery = entityManager.createNamedQuery("Vehicle.findByAbbr", Vehicle.class);
        typedQuery.setParameter("abbr", abbr);
        return typedQuery.getResultList();
    }
}
