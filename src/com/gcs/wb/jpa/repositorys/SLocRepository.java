/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.SLoc;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author dinhhn.vr
 */
public class SLocRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();

    /**
     * get list "Kho"
     *
     * @return
     */
    public List<SLoc> getListSLoc() {
        TypedQuery<SLoc> typedQuery = entityManager.createNamedQuery("SLoc.findByMandtWplant", SLoc.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        return typedQuery.getResultList();
    }

    public SLoc findByLgort(String lgort) {
        TypedQuery<SLoc> typedQuery = entityManager.createNamedQuery("SLoc.findByLgort", SLoc.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        typedQuery.setParameter("lgort", lgort);
        List<SLoc> sLocs = typedQuery.getResultList();
        if (sLocs != null && sLocs.size() > 0) {
            return sLocs.get(0);
        }

        return null;
    }

    public boolean hasData(String mandt, String wplant) {
        TypedQuery<SLoc> typedQuery = entityManager.createNamedQuery("SLoc.findByMandtWplant", SLoc.class);
        typedQuery.setParameter("mandt", mandt);
        typedQuery.setParameter("wplant", wplant);
        List<SLoc> sLoc = typedQuery.getResultList();
        if (sLoc != null && sLoc.size() > 0) {
            return true;
        }

        return false;
    }

    public List<SLoc> getListSloc(List<String> lgorts) {
        if (lgorts == null || lgorts.isEmpty()) {
            return new ArrayList<>();
        }

        TypedQuery<SLoc> typedQuery = entityManager.createNamedQuery("SLoc.findByLgorts", SLoc.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        typedQuery.setParameter("lgorts", lgorts);
        return typedQuery.getResultList();
    }
}
