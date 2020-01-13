/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.SAPSetting;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author dinhhn.vr
 */
public class SAPSettingRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public SAPSetting getSAPSetting() {
        TypedQuery<SAPSetting> typedQuery = entityManager.createNamedQuery("SAPSetting.findAll", SAPSetting.class);
        List<SAPSetting> sapSettings = typedQuery.getResultList();

        if (sapSettings != null && sapSettings.size() >= 1) {
            return sapSettings.get(0);
        }

        return null;
    }
}
