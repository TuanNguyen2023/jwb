/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.User;
import javax.swing.JLabel;

/**
 *
 * @author thangtp.nr
 */
public class WeighBridgeService {
    
    private SAPSetting sapSetting = WeighBridgeApp.getApplication().getSapSetting();
    
    public void setStatus(User login,JLabel txt_status){
        if (WeighBridgeApp.getApplication().isOfflineMode()) {
            if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                txt_status.setText(sapSetting.getName1() + " - User:" + login.getFullname() + " - Offline Mode");
            } else {
                txt_status.setText(sapSetting.getName1() + " - User:" + WeighBridgeApp.getApplication().getCurrent_user_name() + " - Offline Mode");
            }
        } else {
            if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                txt_status.setText(sapSetting.getName1() + " - User:" + login.getFullname());
            } else {
                txt_status.setText(sapSetting.getName1() + " - User:" + WeighBridgeApp.getApplication().getCurrent_user_name());
            }
        }
    }
}
