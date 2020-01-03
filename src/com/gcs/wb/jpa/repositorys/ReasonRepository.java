/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Reason;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author dinhhn.vr
 */
public class ReasonRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public List<Reason> findByMandtBwart(String client, String bwart) {
        TypedQuery<Reason> tReasonQ = entityManager.createNamedQuery("Reason.findByMandtBwart", Reason.class);
        tReasonQ.setParameter("mandt", client);
        tReasonQ.setParameter("bwart", bwart);
        List<Reason> reasons = tReasonQ.getResultList();
        return reasons;
    }
}
