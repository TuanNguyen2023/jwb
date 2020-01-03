/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Material;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class MaterialRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<Object[]> getListMaterial(String client, String plant) {
        List<Object[]> list = new ArrayList<Object[]>();
        Query q = (Query) entityManager.createNativeQuery("select * from Material where MANDT = ? and WPLANT = ?");
        q.setParameter(1, client);
        q.setParameter(2, plant);
        list = q.getResultList();
        return list;

    }

    public Material CheckPOSTO(String client, String plant, String matnr) {
        Material material = new Material();
        try {
            TypedQuery<Material> query = entityManager.createNamedQuery("Material.CheckPOSTO", Material.class);
            query.setParameter("mandt", client);
            query.setParameter("wplant", plant);
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
