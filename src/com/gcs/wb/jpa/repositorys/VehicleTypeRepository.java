/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.VehicleType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author THANGHL
 */
public class VehicleTypeRepository {
    EntityManager entityManager = JPAConnector.getInstance();
    
    public List<VehicleType> getListVehicleType() {
        TypedQuery<VehicleType> typedQuery = entityManager.createNamedQuery("VehicleType.findAll", VehicleType.class);
        return typedQuery.getResultList();
    }
}
