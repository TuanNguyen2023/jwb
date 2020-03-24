/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
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
public class WeightTicketRegistarationRepository {

    Logger logger = Logger.getLogger(this.getClass());

    public List<Object[]> getDangtaiV2(String pRegId) {
        List<Object[]> result = new ArrayList<>();
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pGetDangtaiV2");
                query.registerStoredProcedureParameter("pRegId", String.class, ParameterMode.IN);
                query.setParameter("pRegId", pRegId);
                query.execute();
                result = query.getResultList();
                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return result;
    }

    public List<Object[]> checkDoExist(String pDoNumber, String pWplant) {
        List<Object[]> result = new ArrayList<>();
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("p_check_delivery_order_no_exist");
                query.registerStoredProcedureParameter("pDoNumber", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pWplant", String.class, ParameterMode.IN);
                query.setParameter("pDoNumber", pDoNumber);
                query.setParameter("pWplant", pWplant);
                query.execute();
                result = query.getResultList();
                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return result;
    }

    public int getSPVar(String pwbid, String pspid, String pmatid) {
        int count = 0;
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("p_get_spvar");
                query.registerStoredProcedureParameter("pwbid", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pspid", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pmatid", String.class, ParameterMode.IN);
                query.setParameter("pwbid", pwbid);
                query.setParameter("pspid", pspid);
                query.setParameter("pmatid", pmatid);
                query.execute();
                List<Object[]> result = query.getResultList();
                if (result != null && result.size() > 0) {
                    Object[] ob = result.get(0);
                    count = Integer.parseInt(ob[0].toString());
                }
                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return count;
    }

    public int getTicketInCre(String pWbId) {
        int iplus = 0;
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("getTicketInCre");
                query.registerStoredProcedureParameter("pWbId", String.class, ParameterMode.IN);
                query.setParameter("pWbId", pWbId);
                query.execute();
                List<Object[]> result = query.getResultList();
                if (result != null && result.size() > 0) {
                    Object[] object = result.get(0);
                    iplus = Integer.parseInt(object[0].toString());
                }
                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return iplus;
    }

    public int checkExist(String pWT) {
        int count = 0;
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("p_check_weight_ticket_exist");
                query.registerStoredProcedureParameter("pWT", String.class, ParameterMode.IN);
                query.setParameter("pWT", pWT);
                query.execute();
                List<Object[]> result = query.getResultList();
                if (result != null && result.size() > 0) {
                    Object[] object = result.get(0);
                    count = Integer.parseInt(object[0].toString());
                }
                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return count;
    }

    public List<Object> updateWTReg(String pRegId, String pWT) {
        List<Object> result = new ArrayList<>();
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("p_check_weight_ticket_exist");
                query.registerStoredProcedureParameter("pRegId", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pWT", String.class, ParameterMode.IN);
                query.setParameter("pRegId", pWT);
                query.setParameter("pWT", pWT);
                query.execute();
                result = query.getResultList();
                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return result;
    }

    public List<Object[]> updateRegIdForTicket(
            String pTicket,
            String pRegID,
            String pPOSTO,
            String pTRANSVEN,
            String pTRANSPLANT) {

        List<Object[]> result = new ArrayList<>();
        try {
            EntityManager entityManager = JPAConnector.getInstance();
            if (entityManager != null) {
                EntityTransaction entityTransaction = entityManager.getTransaction();
                entityTransaction.begin();
                StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pUpdateRegIdForTicket");
                query.registerStoredProcedureParameter("pTicket", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pRegID", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pPOSTO", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pTRANSVEN", String.class, ParameterMode.IN);
                query.registerStoredProcedureParameter("pTRANSPLANT", String.class, ParameterMode.IN);
                query.setParameter("pTicket", pTicket);
                query.setParameter("pRegID", pRegID);
                query.setParameter("pPOSTO", pPOSTO);
                query.setParameter("pTRANSVEN", pTRANSVEN);
                query.setParameter("pTRANSPLANT", pTRANSPLANT);
                query.execute();
                result = query.getResultList();
                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return result;
    }
}
