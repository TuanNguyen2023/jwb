/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl
 */
public class SPValidateWTDataRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();

    public void beforeRegistrationWT(String para1) {
        execute(null, para1, null, null, null, null, null, null, null, null);
    }

    public void afterRegistrationWT(String regId, String para1) {
        execute(regId, para1, null, null, null, null, null, null, null, null);
    }

    public void inScaleWT(String para1, Date para7) {
        execute(null, para1, null, null, null, null, null, para7, null, null);
    }

    public void outScaleWT(String para1, Date para8) {
        execute(null, para1, null, null, null, null, null, null, para8, null);
    }

    public void execute(String regId, String para1, String para2, String para3,
            BigDecimal para4, BigDecimal para5, BigDecimal para6,
            Date para7, Date para8, Date para9) {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_ValidateWTData");
            query.registerStoredProcedureParameter(
                    "RegID",
                    String.class,
                    ParameterMode.IN
            ).registerStoredProcedureParameter(
                    "Para1",
                    String.class,
                    ParameterMode.IN
            ).registerStoredProcedureParameter(
                    "Para2",
                    String.class,
                    ParameterMode.IN
            ).registerStoredProcedureParameter(
                    "Para3",
                    String.class,
                    ParameterMode.IN
            ).registerStoredProcedureParameter(
                    "Para4",
                    BigDecimal.class,
                    ParameterMode.IN
            ).registerStoredProcedureParameter(
                    "Para5",
                    BigDecimal.class,
                    ParameterMode.IN
            ).registerStoredProcedureParameter(
                    "Para6",
                    BigDecimal.class,
                    ParameterMode.IN
            ).registerStoredProcedureParameter(
                    "Para7",
                    Date.class,
                    ParameterMode.IN
            ).registerStoredProcedureParameter(
                    "Para8",
                    Date.class,
                    ParameterMode.IN
            ).registerStoredProcedureParameter(
                    "Para9",
                    Date.class,
                    ParameterMode.IN
            );

            query.setParameter("RegID", regId)
                    .setParameter("Para1", para1)
                    .setParameter("Para2", para2)
                    .setParameter("Para3", para3)
                    .setParameter("Para4", para4)
                    .setParameter("Para5", para5)
                    .setParameter("Para6", para6)
                    .setParameter("Para7", para7)
                    .setParameter("Para8", para8)
                    .setParameter("Para9", para9);

            query.execute();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }
    }
}
