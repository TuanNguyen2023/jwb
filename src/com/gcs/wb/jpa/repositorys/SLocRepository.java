/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.SLoc;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author dinhhn.vr
 */
public class SLocRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public List<SLoc> getListSLoc() {
        TypedQuery<SLoc> typedQuery = entityManager.createNamedQuery("SLoc.findAll", SLoc.class);
        return typedQuery.getResultList();
    }

    public SLoc findByLgort(String lgort) {
        TypedQuery<SLoc> typedQuery = entityManager.createNamedQuery("SLoc.findByLgort", SLoc.class);
        typedQuery.setParameter("lgort", lgort);
        List<SLoc> sLocs = typedQuery.getResultList();
        if (sLocs != null && sLocs.size() > 0) {
            return sLocs.get(0);
        }

        return null;
    }
}
