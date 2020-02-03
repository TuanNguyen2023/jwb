/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl
 */
public class ConfigurationRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public Configuration getConfiguration() {
        TypedQuery<Configuration> typedQuery = entityManager.createNamedQuery("Configuration.findAll", Configuration.class);
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(1);
        return typedQuery.getSingleResult();
    }
}
