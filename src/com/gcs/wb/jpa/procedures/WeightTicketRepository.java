/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.procedures;

import com.gcs.wb.jpa.JPAConnector;
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

    public String getSoNiemXa(String pWtId) {
        String soNiemXa = null;
        EntityManager entityManager = JPAConnector.getInstance();
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("p_get_niem_xa");
            query.registerStoredProcedureParameter("pWtId", String.class, ParameterMode.IN);
            query.setParameter("pWtId", pWtId);
            query.execute();
            List<Object[]> result = query.getResultList();
            if (result != null && (result.size() > 0)) {
                Object[] firstRow = result.get(0);
                soNiemXa = firstRow[0].toString();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return soNiemXa;
    }
}
