/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.MaterialInternal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author thangtp.nr
 */
public class LookupMaterialService {

    EntityManager entityManager = JPAConnector.getInstance();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    EntityTransaction entityTransaction = entityManager.getTransaction();

    /**
     * Get data material
     * @param desc
     * @return 
     */
    public List<Material> getListMaterial() {
        TypedQuery<Material> tMaterial =
                entityManager.createQuery("SELECT m FROM Material m WHERE m.wplant = :wplant order by m.matnr asc", Material.class);
        tMaterial.setParameter("wplant", configuration.getWkPlant());
        return tMaterial.getResultList();
    }

    /**
     * Get data for "Vat tu"
     * @param desc
     * @return 
     */
    public List<Material> getListMaterialByDesc(String desc) {
        List<Material> result = new ArrayList<>();
        List<Material> materials = getListMaterial();
        for (Material m : materials) {
            if (m.getMaktx().contains(desc) || m.getMaktg().contains(desc)) {
                result.add(m);
            }
        }
        return result;
    }
    
    /**
     * Get data material
     * @param desc
     * @return 
     */
    public List<MaterialInternal> getListMaterialInternal() {
        TypedQuery<MaterialInternal> tMaterial =
                entityManager.createQuery("SELECT m FROM MaterialInternal m WHERE m.wplant = :wplant order by m.matnr asc", MaterialInternal.class);
        tMaterial.setParameter("wplant", configuration.getWkPlant());
        return tMaterial.getResultList();
    }
    
}
