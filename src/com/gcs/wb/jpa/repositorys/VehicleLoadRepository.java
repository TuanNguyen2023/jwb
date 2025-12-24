package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.VehicleLoad;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl
 */
public class VehicleLoadRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = Logger.getLogger(this.getClass());

    public VehicleLoad findByPlateNo(String plateNo) {
        try {
            TypedQuery<VehicleLoad> typedQuery = entityManager.createNamedQuery("VehicleLoad.findByPlateNo", VehicleLoad.class);
            typedQuery.setParameter("plateNo", plateNo);

            List<VehicleLoad> vehicleLoads = typedQuery.getResultList();
            if (vehicleLoads != null && vehicleLoads.size() == 1) {
                return vehicleLoads.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }
}
