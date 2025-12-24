/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.MaterialInternal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl
 */
public class MaterialInternalRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    Logger logger = Logger.getLogger(this.getClass());

    public List<MaterialInternal> getMaterialInternals() {
        List<MaterialInternal> list = new ArrayList<>();
        try {
            TypedQuery<MaterialInternal> typedQuery = entityManager.createNamedQuery("MaterialInternal.findByMandtWplant", MaterialInternal.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public MaterialInternal findByMatnr(String matnr) {
        try {
            TypedQuery<MaterialInternal> typedQuery = entityManager.createNamedQuery("MaterialInternal.findByMatnr", MaterialInternal.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            typedQuery.setParameter("matnr", matnr);
            List<MaterialInternal> materialInternals = typedQuery.getResultList();
            if (materialInternals != null && materialInternals.size() > 0) {
                return materialInternals.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public List<String> getListLgortByMatnr(String matnr) {
        List<String> list = new ArrayList<>();
        try {
            TypedQuery<MaterialInternal> typedQuery = entityManager.createNamedQuery("MaterialInternal.findByMatnr", MaterialInternal.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            typedQuery.setParameter("matnr", matnr);
            List<MaterialInternal> materials = typedQuery.getResultList();
            list = materials.stream()
                    .map(t -> t.getLgort())
                    .filter(t -> t != null && !t.isEmpty())
                    .distinct()
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public List<String> getListLgortByMatnr(List<String> matnrs) {
        List<String> list = new ArrayList<>();
        if (matnrs == null || matnrs.isEmpty()) {
            return list;
        }

        try {
            TypedQuery<MaterialInternal> typedQuery = entityManager.createNamedQuery("MaterialInternal.findByMatnrs", MaterialInternal.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            typedQuery.setParameter("matnrs", matnrs);
            List<MaterialInternal> materials = typedQuery.getResultList();
            list = materials.stream()
                    .map(t -> t.getLgort())
                    .filter(t -> t != null && !t.isEmpty())
                    .distinct()
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }
}
