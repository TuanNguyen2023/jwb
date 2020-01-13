/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.Variant;
import com.gcs.wb.jpa.entity.VariantPK;
import com.gcs.wb.model.AppConfig;
import javax.persistence.EntityManager;

/**
 *
 * @author THANGPT
 */
public class SettingController {

    public void handleDoInBackground(EntityManager entityManager, SAPSetting sapSetting) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.merge(sapSetting);
        entityManager.getTransaction().commit();
        entityManager.clear();
        sapSetting = entityManager.find(SAPSetting.class, sapSetting.getId());
        entityManager.clear();

        //{+20110119#01
        //Save process order checking
        Variant vari = new Variant();
        VariantPK variPK = new VariantPK();
        try {
            AppConfig lconfig = WeighBridgeApp.getApplication().getConfig();
            variPK.setMandt(lconfig.getsClient().toString());
            variPK.setWPlant(lconfig.getwPlant().toString());
            variPK.setParam("PROCESS_ORDER_CF");
            vari.setVariantPK(variPK);
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.merge(vari);
            entityManager.getTransaction().commit();
            entityManager.clear();
        } catch (Exception e) {
        }
    }
}
