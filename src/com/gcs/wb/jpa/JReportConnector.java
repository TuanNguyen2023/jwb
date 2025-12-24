/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.eclipse.persistence.config.PersistenceUnitProperties;

/**
 *
 * @author dinhhn.vr
 */
public class JReportConnector {

    private static Connection instance = null;

    private JReportConnector() {
    }

    public static Connection getInstance() throws ClassNotFoundException, SQLException {
        if (instance != null) {
            return instance;
        } else {
            Class.forName((String) DataSources.getJweighbridgeProperties().get(PersistenceUnitProperties.JDBC_DRIVER));

            instance = DriverManager.getConnection(
                    (String) DataSources.getJweighbridgeProperties().get(PersistenceUnitProperties.JDBC_URL),
                    (String) DataSources.getJweighbridgeProperties().get(PersistenceUnitProperties.JDBC_USER),
                    (String) DataSources.getJweighbridgeProperties().get(PersistenceUnitProperties.JDBC_PASSWORD));

            return instance;
        }
    }

    public static void close() {
        if (instance != null) {
            try {
                instance.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(JReportConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            instance = null;
        }
    }
}
