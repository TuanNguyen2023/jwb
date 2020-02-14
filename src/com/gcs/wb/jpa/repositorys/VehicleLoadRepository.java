package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.VehicleLoad;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author thanghl
 */
public class VehicleLoadRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public VehicleLoad findByPlateNo(String plateNo) {
        TypedQuery<VehicleLoad> typedQuery = entityManager.createNamedQuery("VehicleLoad.findByPlateNo", VehicleLoad.class);
        typedQuery.setParameter("plateNo", plateNo);

        List<VehicleLoad> vehicleLoads = typedQuery.getResultList();
        if (vehicleLoads != null && vehicleLoads.size() == 1) {
            return vehicleLoads.get(0);
        }

        return null;
    }
}
