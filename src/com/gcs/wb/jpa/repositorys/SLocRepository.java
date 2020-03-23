/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.SLoc;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class SLocRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    Logger logger = Logger.getLogger(this.getClass());

    /**
     * get list "Kho"
     *
     * @return
     */
    public List<SLoc> getListSLoc() {
        List<SLoc> list = new ArrayList<>();
        try {
            TypedQuery<SLoc> typedQuery = entityManager.createNamedQuery("SLoc.findByMandtWplant", SLoc.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public SLoc findByLgort(String lgort) {
        try {
            TypedQuery<SLoc> typedQuery = entityManager.createNamedQuery("SLoc.findByLgort", SLoc.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            typedQuery.setParameter("lgort", lgort);
            List<SLoc> sLocs = typedQuery.getResultList();
            if (sLocs != null && sLocs.size() > 0) {
                return sLocs.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public boolean hasData(String mandt, String wplant) {
        try {
            TypedQuery<SLoc> typedQuery = entityManager.createNamedQuery("SLoc.findByMandtWplant", SLoc.class
            );
            typedQuery.setParameter("mandt", mandt);
            typedQuery.setParameter("wplant", wplant);
            List<SLoc> sLoc = typedQuery.getResultList();
            if (sLoc != null && sLoc.size() > 0) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return false;
    }

    public List<SLoc> getListSloc(List<String> lgorts) {
        List<SLoc> list = new ArrayList<>();
        if (lgorts == null || lgorts.isEmpty()) {
            return list;
        }

        try {
            TypedQuery<SLoc> typedQuery = entityManager.createNamedQuery("SLoc.findByLgorts", SLoc.class
            );
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            typedQuery.setParameter("lgorts", lgorts);
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }
}
