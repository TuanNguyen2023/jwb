/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.repositorys.ConfigurationRepository;

/**
 *
 * @author THANGPT
 */
public class ConfigService {

    ConfigurationRepository configurationRepository = new ConfigurationRepository();

    public Configuration getConfiguration() {

        return configurationRepository.getConfiguration(WeighBridgeApp.getApplication().getConfig().getWbId());
    }
    
    public Configuration getConfiguration(String wbId) {
        return configurationRepository.getConfiguration(wbId);
    }
}
