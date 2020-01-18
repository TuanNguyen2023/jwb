/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.service.SettingService;
import javax.persistence.EntityManager;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

/**
 *
 * @author THANGPT
 */
public class SettingController {

    private SettingService settingService = new SettingService();
    
    public SAPSetting saveDoInBackground(SAPSetting sapSetting) {
        return settingService.saveDoInBackground(sapSetting);
    }
}
