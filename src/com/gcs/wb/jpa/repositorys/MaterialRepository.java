/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Material;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class MaterialRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<Material> getListMaterial() {
        TypedQuery<Material> typedQuery = entityManager.createNamedQuery("Material.findAll", Material.class);
        return typedQuery.getResultList();
    }

    public Material CheckPOSTO(String matnr) {
        Material material = new Material();
        try {
            TypedQuery<Material> query = entityManager.createNamedQuery("Material.CheckPOSTO", Material.class);
            query.setParameter("matnr", "%" + matnr + "%");
            List<Material> list = query.getResultList();
            if (list != null && list.size() > 0) {
                material = list.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return material;
    }
}
