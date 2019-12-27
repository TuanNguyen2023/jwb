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
 * @author Tran-Vu
 */
public class JpaProperties {

    public static Map getPropertiesOld() {
        AppConfig appConf = WeighBridgeApp.getApplication().getConfig();
        /*URL Template | jdbc:mysql://localhost:3306/jWeighBridge?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8*/
        StringBuilder sbURL = new StringBuilder();
        sbURL.append("jdbc:mysql://");
        sbURL.append(appConf.getDbHost());
        sbURL.append("/");
        sbURL.append(appConf.getDbName());
        sbURL.append("?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8");
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(PersistenceUnitProperties.JDBC_DRIVER, "com.mysql.jdbc.Driver");
        properties.put(PersistenceUnitProperties.JDBC_URL, sbURL.toString());
        properties.put(PersistenceUnitProperties.JDBC_USER, appConf.getDbUsr());
        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, appConf.getDbPwd());
        return properties;
    }
    // conect DB customer
    public static Map getProperties() {
        AppConfig appConf = WeighBridgeApp.getApplication().getConfig();
        StringBuilder sbURL = new StringBuilder();
        sbURL.append("jdbc:sqlserver://");
        sbURL.append(appConf.getDbHost());
        sbURL.append("/");
        sbURL.append(appConf.getDbName());
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(PersistenceUnitProperties.JDBC_DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        properties.put(PersistenceUnitProperties.JDBC_URL, "jdbc:sqlserver://172.16.20.181:1433;databaseName=jweighbridge");
        properties.put(PersistenceUnitProperties.JDBC_USER, appConf.getDbUsr());
        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, appConf.getDbPwd());
        return properties;
    }
}
