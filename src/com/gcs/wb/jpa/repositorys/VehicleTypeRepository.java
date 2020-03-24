/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.VehicleType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author THANGHL
 */
public class VehicleTypeRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = Logger.getLogger(this.getClass());

    public List<VehicleType> getListVehicleType() {
        List<VehicleType> list = new ArrayList<>();
        try {
            TypedQuery<VehicleType> typedQuery = entityManager.createNamedQuery("VehicleType.findAll", VehicleType.class);
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }
}
