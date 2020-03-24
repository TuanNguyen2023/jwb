/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.SAPSetting;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class SAPSettingRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = Logger.getLogger(this.getClass());

    public SAPSetting getSAPSetting() {
        try {
            TypedQuery<SAPSetting> typedQuery = entityManager.createNamedQuery("SAPSetting.findAll", SAPSetting.class);
            List<SAPSetting> sapSettings = typedQuery.getResultList();

            if (sapSettings != null && sapSettings.size() >= 1) {
                return sapSettings.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public SAPSetting findByMandtAndWplant(String mandt, String wplant) {
        try {
            TypedQuery<SAPSetting> typedQuery = entityManager.createNamedQuery("SAPSetting.findByMandtAndWplant", SAPSetting.class
            );
            typedQuery.setParameter("mandt", mandt);
            typedQuery.setParameter("wplant", wplant);
            List<SAPSetting> sapSettings = typedQuery.getResultList();

            if (sapSettings.size() > 0) {
                return sapSettings.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }
}
