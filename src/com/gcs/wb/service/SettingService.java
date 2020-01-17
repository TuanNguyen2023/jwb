/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.Variant;
import com.gcs.wb.model.AppConfig;
import javax.persistence.EntityManager;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

/**
 *
 * @author THANGPT
 */
public class SettingService {

    private EntityManager entityManager = JPAConnector.getInstance();

    public void handleDoInBackground(SAPSetting sapSetting) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.merge(sapSetting);
        entityManager.getTransaction().commit();
        entityManager.clear();
        sapSetting = entityManager.find(SAPSetting.class, sapSetting.getId());
        entityManager.clear();
        Variant vari = new Variant();
        try {
            AppConfig lconfig = WeighBridgeApp.getApplication().getConfig();
            vari.setMandt(lconfig.getsClient().toString());
            vari.setWplant(lconfig.getwPlant().toString());
            vari.setParam("PROCESS_ORDER_CF");
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.merge(vari);
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception e) {
        }
    }

    public SAPSetting saveDoInBackground(SAPSetting sapSetting, JTextField txtNameRPT, JTextField txtAddress, JTextField txtPhone, JTextField txtFax, JCheckBox chkPOV) {
        String tmp = txtNameRPT.getText().trim();
        sapSetting.setNameRpt(tmp.isEmpty() ? null : tmp);
        tmp = txtAddress.getText().trim();
        sapSetting.setAddress(tmp.isEmpty() ? null : tmp);
        tmp = txtPhone.getText().trim();
        sapSetting.setPhone(tmp.isEmpty() ? null : tmp);
        tmp = txtFax.getText().trim();
        sapSetting.setFax(tmp.isEmpty() ? null : tmp);
        sapSetting.setCheckPov(chkPOV.isSelected() ? true : false);

        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }

        // save sap setting
        entityManager.merge(sapSetting);
        entityManager.getTransaction().commit();
        entityManager.clear();

        sapSetting = entityManager.find(SAPSetting.class, sapSetting.getId());
        entityManager.clear();
        return sapSetting;
    }
}
