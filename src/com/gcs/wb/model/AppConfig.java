/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.model;

import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.util.Base64_Utils;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.repositorys.ConfigurationRepository;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl
 */
public class AppConfig {

    private PropertiesConfiguration config = null;
    private Configuration configuration = null;
    private boolean fullyConfigured = false;
    private boolean hasDatabaseConfig = false;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public AppConfig() {
        config = new PropertiesConfiguration();
        config.setAutoSave(false);
        config.setFileName(Constants.Configuration.FILE_NAME);
        try {
            config.load();
            
            getDbHost();
            getDbName();
            getDbUsername();
            getDbPassword();
            getAutoSync();
        } catch (ConfigurationException ex) {
            try {
                config.save();
            } catch (ConfigurationException ex1) {
                Logger.getLogger(this.getClass()).error(null, ex1);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Keys">
    /**
     * Configuration Key: DB_HOST
     */
    public static final String DB_HOST = "DB_HOST";
    /**
     * Configuration Key: DB_NAME
     */
    public static final String DB_NAME = "DB_NAME";
    /**
     * Configuration Key: DB_USR
     */
    public static final String DB_USR = "DB_USR";
    /**
     * Configuration Key: DB_PWD
     */
    public static final String DB_PWD = "DB_PWD";
    /**
     * Configuration Key: WB_ID
     */
    public static final String WB_ID = "WB_ID";
    /**
     * Configuration Key: AUTO_SYNC
     */
    public static final String AUTO_SYNC = "AUTO_SYNC";
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Configuration Attributes">
    /**
     * DB Host String
     */
    private String dbHost = null;
    /**
     * DB Name
     */
    private String dbName = null;
    /**
     * DB login id
     */
    private String dbUsername = null;
    /**
     * DB login password
     */
    private String dbPassword = null;
    /**
     * ma cau can
     */
    private String wbId = null;
    /**
     * Auto sync flag
     */
    private boolean autoSync = false;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Attribute's Getter Setter">
    /**
     * @return the hasDatabaseConfig
     */
    public boolean isHasDatabaseConfig() {
        hasDatabaseConfig = dbHost != null && dbName != null && dbUsername != null && dbPassword != null;
        return hasDatabaseConfig;
    }

    /**
     * @return the fullyConfigured
     */
    public boolean isFullyConfigured() {
        if (configuration == null) {
            fullyConfigured = false;
        } else {
            boolean b1Configured = configuration.getWb1Port() != null && configuration.getWb1BaudRate()!= null
                    && configuration.getWb1DataBit()!= null && configuration.getWb1StopBit()!= null
                    && configuration.getWb1ParityControl()!= null && configuration.getWb1Mettler()!= null;
            boolean b2Configured = configuration.getWb2Port() != null && configuration.getWb2BaudRate()!= null
                    && configuration.getWb2DataBit()!= null && configuration.getWb2StopBit()!= null
                    && configuration.getWb2ParityControl()!= null && configuration.getWb2Mettler()!= null;
            boolean otherConfigured = configuration.getSapHost() != null && configuration.getSapSystemNumber() != null
                    && configuration.getSapClient() != null && configuration.getWkPlant() != null;

            fullyConfigured = (b1Configured || b2Configured) && otherConfigured;
        }
        return fullyConfigured;
    }

    /**
     * DB Host String
     * @return the dbHost
     */
    public String getDbHost() {
        if (dbHost == null) {
            dbHost = Base64_Utils.decodeNTimes(config.getString(DB_HOST, ""));
        }
        return dbHost;
    }

    /**
     * DB Host String
     * @param dbHost the dbHost to set
     */
    public void setDbHost(String dbHost) {
        config.setProperty(DB_HOST, Base64_Utils.encodeNTimes(dbHost));
        this.dbHost = dbHost;
    }

    /**
     * DB Name
     * @return the dbName
     */
    public String getDbName() {
        if (dbName == null) {
            dbName = Base64_Utils.decodeNTimes(config.getString(DB_NAME, ""));
        }
        return dbName;
    }

    /**
     * DB Name
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        config.setProperty(DB_NAME, Base64_Utils.encodeNTimes(dbName));
        this.dbName = dbName;
    }

    /**
     * DB login id
     * @return the dbUsername
     */
    public String getDbUsername() {
        if (dbUsername == null) {
            dbUsername = Base64_Utils.decodeNTimes(config.getString(DB_USR, ""));
        }
        return dbUsername;
    }

    /**
     * DB login id
     * @param dbUsername the dbUsername to set
     */
    public void setDbUsername(String dbUsername) {
        config.setProperty(DB_USR, Base64_Utils.encodeNTimes(dbUsername));
        this.dbUsername = dbUsername;
    }

    /**
     * DB login password
     * @return the dbPassword
     */
    public String getDbPassword() {
        if (dbPassword == null) {
            dbPassword = Base64_Utils.decodeNTimes(config.getString(DB_PWD, ""));
        }
        return dbPassword;
    }

    /**
     * DB login password
     * @param dbPassword the dbPassword to set
     */
    public void setDbPassword(String dbPassword) {
        config.setProperty(DB_PWD, Base64_Utils.encodeNTimes(dbPassword));
        this.dbPassword = dbPassword;
    }
    
    /**
     * WB_ID
     * @return the wbId
     */
    public String getWbId() {
        if (wbId == null) {
            wbId = Base64_Utils.decodeNTimes(config.getString(WB_ID, ""));
        }
        return wbId;
    }

    /**
     * WB_ID
     * @param wbId
     */
    public void setWbId(String wbId) {
        config.setProperty(WB_ID, Base64_Utils.encodeNTimes(wbId));
        this.wbId = wbId;
    }

    /**
     * Auto Sync Boolean
     * @return the Auto Sync
     */
    public boolean getAutoSync() {
        autoSync = config.getBoolean(AUTO_SYNC, false);
        return autoSync;
    }

    /**
     * Auto Sync Boolean
     * @param isAutoSync the flag to set
     */
    public void setAutoSync(boolean isAutoSync) {
        config.setProperty(AUTO_SYNC, isAutoSync);
        this.autoSync = isAutoSync;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void save() throws ConfigurationException {
        // save to application.properties
        config.save();
        
        // save to db
        ConfigurationRepository configurationRepository = new ConfigurationRepository();
        configurationRepository.saveConfiguration(configuration);
    }
}
