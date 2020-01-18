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
    
    public void handleDoInBackground(EntityManager entityManager, SAPSetting sapSetting) {
        settingService.handleDoInBackground(sapSetting);
    }

    public SAPSetting saveDoInBackground(SAPSetting sapSetting, JTextField txtNameRPT, JTextField txtAddress, JTextField txtPhone, JTextField txtFax,JCheckBox chkPOV) {
        return settingService.saveDoInBackground(sapSetting, txtNameRPT, txtAddress, txtPhone, txtFax, chkPOV);
    }
}
