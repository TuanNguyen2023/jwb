/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.TransportAgentVehicle;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author thanghl
 */
public class TransportAgentVehicleRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    /**
     * get list BS Xe
     * @param id
     * @return 
     */
    public List<TransportAgentVehicle> findByTransportAgentId(int transportAgentId) {
        TypedQuery<TransportAgentVehicle> typedQuery = entityManager.createNamedQuery("TransportAgentVehicle.findByTransportAgentId", TransportAgentVehicle.class);
        typedQuery.setParameter("transportAgentId", transportAgentId);

        return typedQuery.getResultList();
    }

    public List<TransportAgentVehicle> findByVehicleId(int vehicleId) {
        TypedQuery<TransportAgentVehicle> typedQuery = entityManager.createNamedQuery("TransportAgentVehicle.findByVehicleId", TransportAgentVehicle.class);
        typedQuery.setParameter("vehicleId", vehicleId);

        return typedQuery.getResultList();
    }

    public TransportAgentVehicle findByTransportAgentIdAndVehicleId(int transportAgentId, int vehicleId) {
        TypedQuery<TransportAgentVehicle> typedQuery = entityManager.createNamedQuery("TransportAgentVehicle.findByTransportAgentIdAndVehicleId", TransportAgentVehicle.class);
        typedQuery.setParameter("transportAgentId", transportAgentId);
        typedQuery.setParameter("vehicleId", vehicleId);

        List<TransportAgentVehicle> transportAgentVehicles = typedQuery.getResultList();
        if (transportAgentVehicles != null && transportAgentVehicles.size() == 1) {
            return transportAgentVehicles.get(0);
        }

        return null;
    }
    
    public List<TransportAgentVehicle> findByPlateNo(String plateNo) {
        TypedQuery<TransportAgentVehicle> typedQuery = entityManager.createNamedQuery("TransportAgentVehicle.findByPlateNo", TransportAgentVehicle.class);
        typedQuery.setParameter("plateNo", plateNo);

        return typedQuery.getResultList();
    }
    
    public TransportAgentVehicle findByAbbrAndPlateNo(String abbr, String plateNo) {
        TypedQuery<TransportAgentVehicle> typedQuery = entityManager.createNamedQuery("TransportAgentVehicle.findByAbbrAndPlateNo", TransportAgentVehicle.class);
        typedQuery.setParameter("abbr", abbr);
        typedQuery.setParameter("plateNo", plateNo);

        List<TransportAgentVehicle> transportAgentVehicles = typedQuery.getResultList();
        if (transportAgentVehicles != null && transportAgentVehicles.size() == 1) {
            return transportAgentVehicles.get(0);
        }

        return null;
    }
}