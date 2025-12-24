/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.SAPSetting;
import javax.persistence.EntityManager;

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
