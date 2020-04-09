/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.SchedulerSync;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author TaiTQ
 */
public class SchedulerSyncRepository {
    private Logger logger = Logger.getLogger(this.getClass());
    EntityManager entityManager = JPAConnector.getInstance();
    EntityTransaction entityTransaction = entityManager.getTransaction();

    public SchedulerSync findByParamMandtWplant(String mandt, String wplant) {
        SchedulerSync result = null;
        try {
            TypedQuery<SchedulerSync> query = entityManager.createNamedQuery("SchedulerSync.findByMandtWplant", SchedulerSync.class);
            query.setParameter("mandt", mandt);
            query.setParameter("wplant", wplant);
            List<SchedulerSync> schedulerSync = query.getResultList();
            if (schedulerSync != null && schedulerSync.size() > 0) {
                return schedulerSync.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return result;
    }
        
        public void updateLastSync(SchedulerSync newSchedulerSync) {
        try {
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }
            entityManager.merge(newSchedulerSync);
            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            throw ex;
        }
    }
}
