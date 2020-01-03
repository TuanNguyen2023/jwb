/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Regvehicle;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class RegvehicleRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<Regvehicle> findRegVehicle(String soXe, String taAbbr) {
        List<Regvehicle> list = new ArrayList<Regvehicle>();
        try {
            TypedQuery<Regvehicle> nq = entityManager.createNamedQuery("Regvehicle.newSQL", Regvehicle.class);
            nq.setParameter("soXe", soXe);
            nq.setParameter("taAbbr", taAbbr);
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }
}
