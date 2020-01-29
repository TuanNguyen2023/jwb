/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.procedures;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.OrsJpaConnector;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */

public class WeightTicketJpaRepository {

    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public String getMsg(String pid) {
        String msg = null;
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pgetmsg");
                query.registerStoredProcedureParameter("pid", String.class, ParameterMode.IN);
                query.setParameter("pid", pid);
                query.execute();
                List<Object[]> list = query.getResultList();
                if (list != null && list.size() > 0) {
                    Object[] firstRow = list.get(0);
                    msg = firstRow[0].toString();
                }
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());

        }
        return msg;
    }

    public int getCountTicketDay(String plant) {
        int count = 0;
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("p_get_weight_ticket_seq_day");
                query.registerStoredProcedureParameter("pWplant", String.class, ParameterMode.IN);
                query.setParameter("pWplant", plant);
                query.execute();
                List<Object[]> list = query.getResultList();
                if (list != null && list.size() > 0) {
                    Object[] firstRow = list.get(0);
                    count = Integer.parseInt(firstRow[0].toString());
                }
                entityTransaction.commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return count;
    }

    public int getCountTicketMonth(String plant) {
        int count = 0;
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("[p_get_weight_ticket_seq_month]");
                query.registerStoredProcedureParameter("pWplant", String.class, ParameterMode.IN);
                query.setParameter("pWplant", plant);
                query.execute();
                List<Object[]> list = query.getResultList();
                if (list != null && list.size() > 0) {
                    Object[] firstRow = list.get(0);
                    count = Integer.parseInt(firstRow[0].toString());
                }
                entityTransaction.commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());

        }
        return count;
    }

    public Object getTime() {
        Object time = null;
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pgettime");
                query.execute();
                List<Object[]> list = query.getResultList();
                if (list != null && list.size() > 0) {
                    Object[] firstRow = list.get(0);
                    time = firstRow[0];
                }
                entityTransaction.commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return time;
    }

    public Object getTaiTrong(String xe, String roMooc) {
        Object result = null;
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pGetTaiTrong");
                query.registerStoredProcedureParameter("pXe", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pRomooc", String.class, ParameterMode.IN);
                query.setParameter("pXe", xe);
                query.setParameter("pRomooc", roMooc);
                query.execute();
                List<Object[]> list = query.getResultList();
                if (list != null && list.size() > 0) {
                    Object[] firstRow = list.get(0);
                    result = firstRow[0];
                }
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());

        }
        return result;
    }

    public List<Object[]> getOrgDev2(String pWbId) {
        List<Object[]> list = new ArrayList<Object[]>();
        try {
            EntityManager entityManager = OrsJpaConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pGetDev2");
                query.registerStoredProcedureParameter("pWbId", String.class, ParameterMode.IN);
                query.setParameter("pWbId", pWbId);
                query.execute();
                list = query.getResultList();
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return list;
    }
}
