/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.base.enums.ParityEnum;
import com.gcs.wb.base.util.Base64_Utils;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.ConfigService;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

/**
 *
 * @author THANGPT
 */
public class ConfigController {

    private final ConfigService configService = new ConfigService();
    
    public AppConfig objMapping(AppConfig config, String wbId, String dbHost, String dbName, String dbUsr, String dbPwd,
            String sHost, String sRoute, String sNo, String sDClient, Integer speed1, Object port1, Float sbit1,
            Object port2, Integer speed2, Float sbit2, String wPlant, 
            JComboBox cbxDataBits1, JComboBox cbxPControl1, JCheckBox chbMettler1, JComboBox cbxDataBits2, JComboBox cbxPControl2, JCheckBox chbMettler2) {
        
        if (config == null) {
            config = new AppConfig();
        }
        config.setWbId(Base64_Utils.decodeNTimes(wbId));
        config.setDbHost(Base64_Utils.decodeNTimes(dbHost));
        config.setDbName(Base64_Utils.decodeNTimes(dbName));
        config.setDbUsr(Base64_Utils.decodeNTimes(dbUsr));
        config.setDbPwd(Base64_Utils.decodeNTimes(dbPwd));
        config.setsHost(Base64_Utils.decodeNTimes(sHost));
        config.setsRoute(Base64_Utils.decodeNTimes(sRoute));
        config.setsNumber(Base64_Utils.decodeNTimes(sNo));
        config.setsClient(Base64_Utils.decodeNTimes(sDClient));

        config.setB1Port((String) port1);
        config.setB1Speed(speed1);
        config.setB1DBits(port1 == null ? null : (Short) cbxDataBits1.getSelectedItem());
        
        config.setB1SBits(port1 == null ? null : sbit1);
        config.setB1PC(port1 == null ? null : (new Integer(((ParityEnum) cbxPControl1.getSelectedItem()).ordinal())).shortValue());
        config.setB1Mettler(port1 == null ? null : chbMettler1.isSelected());

        
        config.setB2Port((String) port2);
        config.setB2Speed(speed2);
        config.setB2DBits(port2 == null ? null : (Short) cbxDataBits2.getSelectedItem());
        
        config.setB2SBits(port2 == null ? null : sbit2);
        config.setB2PC(port2 == null ? null : (new Integer(((ParityEnum) cbxPControl2.getSelectedItem()).ordinal())).shortValue());
        config.setB2Mettler(port2 == null ? null : chbMettler2.isSelected());
        config.setwPlant(Base64_Utils.decodeNTimes(wPlant));

        return config;
    }
    
    public ParityEnum getPlControl(AppConfig config, Short getBPC) {
        
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
