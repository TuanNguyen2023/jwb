/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Movement;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author dinhhn.vr
 */
public class MovementRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public Movement findByMandtBwartSpras(String mandt, String language) {
        Movement movement = null;
        TypedQuery<Movement> query = entityManager.createNamedQuery("Movement.findByMandtBwartSpras", Movement.class);
        query.setParameter("mandt", mandt);
        query.setParameter("bwart", "101");
        query.setParameter("spras", language);
        List<Movement> list = query.getResultList();
        if (list != null && list.size() > 0) {
            movement = list.get(0);
        }
        return movement;
    }
}
