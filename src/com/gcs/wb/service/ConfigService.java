/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.fazecast.jSerialComm.SerialPort;
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.serials.SerialHelper;
import java.util.HashSet;

import com.gcs.wb.base.enums.ParityEnum;
import com.gcs.wb.base.util.Base64_Utils;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.repositorys.ConfigurationRepository;
import com.gcs.wb.model.AppConfig;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author THANGPT
 */
public class ConfigService {

    ConfigurationRepository configurationRepository = new ConfigurationRepository();

    public Configuration getConfiguration() {

        return configurationRepository.getConfiguration();
    }
}
