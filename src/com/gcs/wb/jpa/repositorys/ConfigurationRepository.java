/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl
 */
public class ConfigurationRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = Logger.getLogger(this.getClass());

    public Configuration getConfiguration(String wbId) {
        try {
            TypedQuery<Configuration> typedQuery = entityManager.createNamedQuery("Configuration.findByWbId", Configuration.class);
            typedQuery.setParameter("wbId", wbId);
            List<Configuration> configurations = typedQuery.getResultList();
            if (configurations != null && configurations.size() > 0) {
                return configurations.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public void saveConfiguration(Configuration configuration) throws ConfigurationException {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        if (!entityTransaction.isActive()) {
            entityTransaction.begin();
        }

        try {
            entityManager.merge(configuration);

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw new ConfigurationException(ex);
        }
    }
}
