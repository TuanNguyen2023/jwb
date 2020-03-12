/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Material;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class MaterialRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    Logger logger = Logger.getLogger(this.getClass());

    public List<Material> getListMaterial() {
        TypedQuery<Material> typedQuery = entityManager.createNamedQuery("Material.findByMandtWplant", Material.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        return typedQuery.getResultList();
    }

    public Material CheckPOSTO(String matnr) {
        Material material = new Material();
        try {
            TypedQuery<Material> query = entityManager.createNamedQuery("Material.CheckPOSTO", Material.class);
            query.setParameter("mandt", configuration.getSapClient());
            query.setParameter("wplant", configuration.getWkPlant());
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

    public boolean hasData(String mandt, String wplant) {
        TypedQuery<Material> typedQuery = entityManager.createNamedQuery("Material.findByMandtWplant", Material.class);
        typedQuery.setParameter("mandt", mandt);
        typedQuery.setParameter("wplant", wplant);
        List<Material> materials = typedQuery.getResultList();
        if (materials != null && materials.size() > 0) {
            return true;
        }

        return false;
    }

    public Material findByMatnr(String matnr) {
        TypedQuery<Material> typedQuery = entityManager.createNamedQuery("Material.findByMatnr", Material.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        typedQuery.setParameter("matnr", matnr);
        List<Material> materials = typedQuery.getResultList();
        if (materials != null && materials.size() > 0) {
            return materials.get(0);
        }

        return null;
    }

    public List<String> getListLgortByMatnr(String matnr) {
        TypedQuery<Material> typedQuery = entityManager.createNamedQuery("Material.findByMatnr", Material.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        typedQuery.setParameter("matnr", matnr);
        List<Material> materials = typedQuery.getResultList();
        return materials.stream()
                .map(t -> t.getLgort())
                .filter(t -> t != null && !t.isEmpty())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String> getListLgortByMatnr(List<String> matnrs) {
        if (matnrs == null || matnrs.isEmpty()) {
            return new ArrayList<>();
        }

        TypedQuery<Material> typedQuery = entityManager.createNamedQuery("Material.findByMatnrs", Material.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        typedQuery.setParameter("matnrs", matnrs);
        List<Material> materials = typedQuery.getResultList();
        return materials.stream()
                .map(t -> t.getLgort())
                .filter(t -> t != null && !t.isEmpty())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
