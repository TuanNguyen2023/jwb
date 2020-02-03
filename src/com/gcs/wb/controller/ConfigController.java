/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.base.enums.ParityEnum;
import com.gcs.wb.base.util.Base64_Utils;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.ConfigService;
import java.math.BigDecimal;
import java.util.Date;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

/**
 *
 * @author THANGPT
 */
public class ConfigController {

    private ConfigService configService = new ConfigService();
    
    public AppConfig objMapping(AppConfig config, String wbId, String dbHost, String dbName, String dbUsr, String dbPwd,
            String sHost, String sRoute, String sNo, String sDClient, Integer speed1, Object port1, Float sbit1,
            Object port2, Integer speed2, Float sbit2, String wPlant, 
            JComboBox cbxDataBits1, JComboBox cbxPControl1, JCheckBox chbMettler1, JComboBox cbxDataBits2, JComboBox cbxPControl2, JCheckBox chbMettler2) {
        
        if (config == null) {
            config = new AppConfig();
        }
        config.setDbHost(Base64_Utils.decodeNTimes(dbHost));
        config.setDbName(Base64_Utils.decodeNTimes(dbName));
        config.setDbUsername(Base64_Utils.decodeNTimes(dbUsr));
        config.setDbPassword(Base64_Utils.decodeNTimes(dbPwd));
        
        Configuration configuration = config.getConfiguration();
        if (configuration == null) {
            configuration = new Configuration();
        } else {
            configuration.setUpdatedDate(new Date());
        }
        
        configuration.setSapHost(Base64_Utils.decodeNTimes(sHost));
        configuration.setSapRouteString(Base64_Utils.decodeNTimes(sRoute));
        configuration.setSapSystemNumber(Base64_Utils.decodeNTimes(sNo));
        configuration.setSapClient(Base64_Utils.decodeNTimes(sDClient));
        
        configuration.setWkPlant(Base64_Utils.decodeNTimes(wPlant));
        configuration.setWbId(Base64_Utils.decodeNTimes(wbId));

        configuration.setWb1Port((String) port1);
        configuration.setWb1BaudRate(speed1);
        configuration.setWb1DataBit(port1 == null ? null : (Short) cbxDataBits1.getSelectedItem());
        configuration.setWb1StopBit(port1 == null ? null : new BigDecimal(sbit1));
        configuration.setWb1ParityControl(port1 == null ? null : (new Integer(((ParityEnum) cbxPControl1.getSelectedItem()).ordinal())).shortValue());
        configuration.setWb1Mettler(port1 == null ? null : chbMettler1.isSelected());

        configuration.setWb2Port((String) port2);
        configuration.setWb2BaudRate(speed2);
        configuration.setWb2DataBit(port2 == null ? null : (Short) cbxDataBits2.getSelectedItem());
        configuration.setWb2StopBit(port2 == null ? null : new BigDecimal(sbit2));
        configuration.setWb2ParityControl(port2 == null ? null : (new Integer(((ParityEnum) cbxPControl2.getSelectedItem()).ordinal())).shortValue());
        configuration.setWb2Mettler(port2 == null ? null : chbMettler2.isSelected());

        return config;
    }
    
    public ParityEnum getPlControl(AppConfig config, int getBPC) {
        
        ParityEnum p1Control = null;

        switch (getBPC) {
            case 0:
                p1Control = ParityEnum.NONE;
                break;
            case 1:
                p1Control = ParityEnum.ODD;
                break;
            case 2:
                p1Control = ParityEnum.EVEN;
                break;
            case 3:
                p1Control = ParityEnum.MARK;
                break;
            case 4:
                p1Control = ParityEnum.SPACE;
                break;
        }
        return p1Control;
        
    }
}
