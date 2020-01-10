/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import org.eclipse.persistence.config.PersistenceUnitProperties;

/**
 *
 * @author dinhhn.vr
 */
public class JReportConnector {

    private static Connection instance = null;

    private JReportConnector() {
    }

    public static Connection getInstance() {
        if (instance != null) {
            return instance;
        } else {
            try {
                Class.forName((String) DataSources.getJweighbridgeProperties().get(PersistenceUnitProperties.JDBC_DRIVER));
                instance = DriverManager.getConnection(
                        (String) DataSources.getJweighbridgeProperties().get(PersistenceUnitProperties.JDBC_URL),
                        (String) DataSources.getJweighbridgeProperties().get(PersistenceUnitProperties.JDBC_USER),
                        (String) DataSources.getJweighbridgeProperties().get(PersistenceUnitProperties.JDBC_PASSWORD));
            } catch (Exception e) {
            }
            return instance;
        }
    }
}
