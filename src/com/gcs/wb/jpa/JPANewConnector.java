/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author thanghl
 */
// TODO: for new database
public class JPANewConnector {

    private static EntityManager instance = null;

    private JPANewConnector() {
    }

    public static EntityManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JWeighBridgePU",
                    DataSources.getJweighbridgePropertiesNew());
            instance = entityManagerFactory.createEntityManager();
            return instance;
        }
    }
}
