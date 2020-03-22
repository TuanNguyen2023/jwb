/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.MaterialInterPlant;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author HANGTT
 */
public class MaterialInterPlantRepository {
    EntityManager entityManager = JPAConnector.getInstance();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public MaterialInterPlant findByMatnrAndPlantInOut(String matnr, String plantIn, String plantOut) {
        TypedQuery<MaterialInterPlant> typedQuery = entityManager.createNamedQuery("MaterialInterPlant.findByMatnrAndPlantInOut", MaterialInterPlant.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("matnr", matnr);
        typedQuery.setParameter("plantIn", plantIn);
        typedQuery.setParameter("plantOut", plantOut);
        List<MaterialInterPlant> materials = typedQuery.getResultList();
        if (materials != null && materials.size() > 0) {
            return materials.get(0);
        }

        return null;
    }
}
