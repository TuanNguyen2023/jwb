/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.Variant;
import com.gcs.wb.jpa.entity.VariantPK;
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

    public SAPSetting saveDoInBackground(SAPSetting sapSetting) {
        
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
