/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.WeightTicketDetail;
import java.util.ArrayList;
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
    Logger logger = Logger.getLogger(this.getClass());

    public List<WeightTicketDetail> findByPoNo(String poNo) {
        List<WeightTicketDetail> list = new ArrayList<>();
        try {
            String status = "POSTED";
            TypedQuery<WeightTicketDetail> typedQuery = entityManager.createNamedQuery("WeightTicketDetail.findByPoNo", WeightTicketDetail.class);
            typedQuery.setParameter("poNo", poNo);
            typedQuery.setParameter("status", status);
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public WeightTicketDetail findBydeliveryOrderNo(String doNumber) {
        try {
            TypedQuery<WeightTicketDetail> typedQuery = entityManager.createNamedQuery("WeightTicketDetail.findByDoNo", WeightTicketDetail.class);
            typedQuery.setParameter("deliveryOrderNo", doNumber);
            List<WeightTicketDetail> wtDetail = typedQuery.getResultList();

            if (wtDetail != null && wtDetail.size() > 0) {
                return wtDetail.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }
}
