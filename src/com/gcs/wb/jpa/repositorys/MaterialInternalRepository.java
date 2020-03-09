/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.MaterialInternal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl
 */
public class MaterialInternalRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<MaterialInternal> getMaterialInternals() {
        TypedQuery<MaterialInternal> typedQuery = entityManager.createNamedQuery("MaterialInternal.findAll", MaterialInternal.class);
        return typedQuery.getResultList();
    }

    public MaterialInternal findByMatnr(String matnr) {
        TypedQuery<MaterialInternal> typedQuery = entityManager.createNamedQuery("MaterialInternal.findByMatnr", MaterialInternal.class);
        typedQuery.setParameter("matnr", matnr);
        List<MaterialInternal> materialInternals = typedQuery.getResultList();
        if (materialInternals != null && materialInternals.size() > 0) {
            return materialInternals.get(0);
        }

        return null;
    }

    public List<String> getListLgortByMatnr(String matnr) {
        TypedQuery<MaterialInternal> typedQuery = entityManager.createNamedQuery("MaterialInternal.findByMatnr", MaterialInternal.class);
        typedQuery.setParameter("matnr", matnr);
        List<MaterialInternal> materials = typedQuery.getResultList();
        return materials.stream()
                .map(t -> t.getLgort())
                .filter(t -> t != null && !t.isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<String> getListLgortByMatnr(List<String> matnrs) {
        if (matnrs == null || matnrs.isEmpty()) {
            return new ArrayList<>();
        }

        TypedQuery<MaterialInternal> typedQuery = entityManager.createNamedQuery("MaterialInternal.findByMatnrs", MaterialInternal.class);
        typedQuery.setParameter("matnrs", matnrs);
        List<MaterialInternal> materials = typedQuery.getResultList();
        return materials.stream()
                .map(t -> t.getLgort())
                .filter(t -> t != null && !t.isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
