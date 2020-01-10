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
 * @author dinhhn.vr
 */
public class JPAConnector {

    private static EntityManager instance = null;

    private JPAConnector() {
    }

    public static EntityManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JWeighBridgePU",
                    DataSources.getJweighbridgeProperties());
            instance = entityManagerFactory.createEntityManager();
            return instance;
        }
    }
}
