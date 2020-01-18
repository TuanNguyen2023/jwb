/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.model.AppConfig;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.persistence.config.PersistenceUnitProperties;

/**
 *
 * @author Dinh Nguyen
 */
public class DataSources {

    public static Map getOrProperties() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlserver://172.16.20.181:1433;databaseName=ors");
        properties.put("javax.persistence.jdbc.user", "sa");
        properties.put("javax.persistence.jdbc.password", "1qaZ2wsX");
        return properties;
    }

    public static Map getJweighbridgeProperties() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        properties.put("javax.persistence.jdbc.url", "jdbc:sqlserver://172.16.20.181:1433;databaseName=jweighbridge");
        properties.put("javax.persistence.jdbc.user", "sa");
        properties.put("javax.persistence.jdbc.password", "1qaZ2wsX");
        return properties;
    }
}
