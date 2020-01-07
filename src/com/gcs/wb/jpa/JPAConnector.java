/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa;

import java.util.HashMap;
import java.util.Map;
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
                    getProperties());
            instance = entityManagerFactory.createEntityManager();
            return instance;
        }
    }

    public static Map<String, String> getProperties() {

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlserver://172.16.20.181:1433;databaseName=weighbridge");
        properties.put("javax.persistence.jdbc.user", "sa");
        properties.put("javax.persistence.jdbc.password", "1qaZ2wsX");
        return properties;
    }
}
