/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.model.AppConfig;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import org.hibersap.session.Session;

/**
 *
 * @author thangtp.nr
 */
public class LookupMaterialService {

    EntityManager entityManager = JPAConnector.getInstance();
    AppConfig config = WeighBridgeApp.getApplication().getConfig();
    Session session = WeighBridgeApp.getApplication().getSAPSession();
    EntityTransaction entityTransaction = entityManager.getTransaction();

    /**
     * Get data material
     * @param desc
     * @return 
     */
    public List<Material> getListMaterial() {
        List<Material> materials = new ArrayList<>();
        TypedQuery<Material> tMaterial =
                entityManager.createQuery("SELECT m FROM Material m WHERE m.wplant = :wplant order by m.matnr asc", Material.class);
        tMaterial.setParameter("wplant", config.getwPlant());
        materials = tMaterial.getResultList();
        return materials;
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
    
}
