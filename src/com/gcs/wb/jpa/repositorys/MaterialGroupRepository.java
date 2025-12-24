/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.MaterialGroup;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author THANGLH
 */
public class MaterialGroupRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    Logger logger = Logger.getLogger(this.getClass());

    public List<MaterialGroup> getListMaterialGroup() {
        List<MaterialGroup> list = new ArrayList<>();
        try {
            TypedQuery<MaterialGroup> typedQuery = entityManager.createNamedQuery("MaterialGroup.findAll", MaterialGroup.class);
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public boolean hasData(String mandt, String wplant, String matnr) {
        try {
            TypedQuery<MaterialGroup> typedQuery = entityManager.createNamedQuery("MaterialGroup.findByMandtWplantMatnr", MaterialGroup.class);
            typedQuery.setParameter("mandt", mandt);
            typedQuery.setParameter("wplant", wplant);
            typedQuery.setParameter("matnr", matnr);
            List<MaterialGroup> materials = typedQuery.getResultList();
            if (materials != null && materials.size() > 0) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return false;
    }

    public MaterialGroup findByMatnr(String matnr) {
        try {
            TypedQuery<MaterialGroup> typedQuery = entityManager.createNamedQuery("MaterialGroup.findByMatnr", MaterialGroup.class);
            typedQuery.setParameter("matnr", matnr);
            List<MaterialGroup> materials = typedQuery.getResultList();
            if (materials != null && materials.size() > 0) {
                return materials.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public boolean checkMaterialTogether(List<String> matnrs) {
        if (matnrs == null || matnrs.isEmpty()) {
            return false;
        }

        try {
            TypedQuery<Object> typedQuery = entityManager.createNamedQuery("MaterialGroup.checkMaterialTogether", Object.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            typedQuery.setParameter("matnrs", matnrs);

            Object[] info = (Object[]) typedQuery.getResultList().get(0);
            Long sumRecord = (Long) info[0];
            Long sumType = (Long) info[1];

            return matnrs.size() == sumRecord && sumType == 1;
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return false;
    }
}
