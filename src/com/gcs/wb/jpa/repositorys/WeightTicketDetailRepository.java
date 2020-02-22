/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.WeightTicketDetail;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl
 */
public class WeightTicketDetailRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<WeightTicketDetail> findByPoNo(String poNo) {
        String status = "POSTED";
        TypedQuery<WeightTicketDetail> typedQuery = entityManager.createNamedQuery("WeightTicketDetail.findByPoNo", WeightTicketDetail.class);
        typedQuery.setParameter("poNo", poNo);
        typedQuery.setParameter("status", status);
        return typedQuery.getResultList();
    }
}
