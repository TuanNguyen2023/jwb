/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.TransportAgentVehicle;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl
 */
public class TransportAgentVehicleRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = Logger.getLogger(this.getClass());

    /**
     * get list BS Xe
     *
     * @param transportAgentId
     * @return
     */
    public List<TransportAgentVehicle> findByTransportAgentId(int transportAgentId) {
        List<TransportAgentVehicle> list = new ArrayList<>();
        try {
            TypedQuery<TransportAgentVehicle> typedQuery = entityManager.createNamedQuery("TransportAgentVehicle.findByTransportAgentId", TransportAgentVehicle.class);
            typedQuery.setParameter("transportAgentId", transportAgentId);

            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public List<TransportAgentVehicle> findByVehicleId(int vehicleId) {
        List<TransportAgentVehicle> list = new ArrayList<>();
        try {
            TypedQuery<TransportAgentVehicle> typedQuery = entityManager.createNamedQuery("TransportAgentVehicle.findByVehicleId", TransportAgentVehicle.class);
            typedQuery.setParameter("vehicleId", vehicleId);

            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public TransportAgentVehicle findByTransportAgentIdAndVehicleId(int transportAgentId, int vehicleId) {
        try {
            TypedQuery<TransportAgentVehicle> typedQuery = entityManager.createNamedQuery("TransportAgentVehicle.findByTransportAgentIdAndVehicleId", TransportAgentVehicle.class);
            typedQuery.setParameter("transportAgentId", transportAgentId);
            typedQuery.setParameter("vehicleId", vehicleId);

            List<TransportAgentVehicle> transportAgentVehicles = typedQuery.getResultList();
            if (transportAgentVehicles != null && transportAgentVehicles.size() == 1) {
                return transportAgentVehicles.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public List<TransportAgentVehicle> findByPlateNo(String plateNo) {
        List<TransportAgentVehicle> list = new ArrayList<>();
        try {
            TypedQuery<TransportAgentVehicle> typedQuery = entityManager.createNamedQuery("TransportAgentVehicle.findByPlateNo", TransportAgentVehicle.class
            );
            typedQuery.setParameter("plateNo", plateNo);

            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public TransportAgentVehicle findByAbbrAndPlateNo(String abbr, String plateNo) {
        try {
            TypedQuery<TransportAgentVehicle> typedQuery = entityManager.createNamedQuery("TransportAgentVehicle.findByAbbrAndPlateNo", TransportAgentVehicle.class
            );
            typedQuery.setParameter("abbr", abbr);
            typedQuery.setParameter("plateNo", plateNo);

            List<TransportAgentVehicle> transportAgentVehicles = typedQuery.getResultList();
            if (transportAgentVehicles != null && transportAgentVehicles.size() == 1) {
                return transportAgentVehicles.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }
}
