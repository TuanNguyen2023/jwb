/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.procedures;

import com.gcs.wb.jpa.JPAConnector;
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
public class WeightTicketRepository {

    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List getTicketIndex(String maphieu) {
        List result = new ArrayList();
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pvc_getTicketIndex");
                query.registerStoredProcedureParameter("pmaphieu", String.class, ParameterMode.IN);
                query.setParameter("pmaphieu", maphieu);
                query.execute();
                result = query.getResultList();
                entityTransaction.commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return result;
    }

    public List getDev2(String pWbId) {
        List result = new ArrayList();
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pGetDev2");
                query.registerStoredProcedureParameter("pWbId", String.class, ParameterMode.IN);
                query.setParameter("pWbId", pWbId);
                query.execute();
                result = query.getResultList();
                entityTransaction.commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return result;
    }

    public List<Object[]> getWeighTicketReg(String pRegId) {
        List<Object[]> result = new ArrayList<Object[]>();
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pgetWT_Reg");
                query.registerStoredProcedureParameter("pRegId", String.class, ParameterMode.IN);
                query.setParameter("pRegId", pRegId);
                query.execute();
                result = query.getResultList();
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());

        }
        return result;
    }

    public List<Object[]> getMaxL(String pPlant, String pWbid, String pBs) {
        List<Object[]> result = new ArrayList<Object[]>();
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pGetMaxL");
                query.registerStoredProcedureParameter("pPlant", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pWbid", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pBs", String.class, ParameterMode.IN);
                query.setParameter("pPlant", pPlant);
                query.setParameter("pWbid", pWbid);
                query.setParameter("pBs", pBs);
                query.execute();
                result = query.getResultList();
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return result;
    }

    public List<Object[]> getMaxLLock(String pPlant, String pWbid, String pBs) {
        List<Object[]> result = new ArrayList<Object[]>();
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pGetMaxLLock");
                query.registerStoredProcedureParameter("pPlant", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pWbid", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pBs", String.class, ParameterMode.IN);
                query.setParameter("pPlant", pPlant);
                query.setParameter("pWbid", pWbid);
                query.setParameter("pBs", pBs);
                query.execute();
                result = query.getResultList();
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return result;
    }

    public String getSoNiemXa(String pWtId) {
        String soNiemXa = null;
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pgetWT_NiemXa");
                query.registerStoredProcedureParameter("pWtId", String.class, ParameterMode.IN);
                query.setParameter("pWtId", pWtId);
                query.execute();
                List<Object[]> result = query.getResultList();
                if (result != null && (result.size() > 0)) {
                    Object[] firstRow = result.get(0);
                    soNiemXa = firstRow[0].toString();
                }
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return soNiemXa;
    }
}
