/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.NiemXa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author thanghl
 */
public class NiemXaRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public List<NiemXa> getListNiemXa() {
        TypedQuery<NiemXa> query = entityManager.createNamedQuery("NiemXa.findAll", NiemXa.class);
        return query.getResultList();
    }

    public NiemXa findByWtid(String wtid) {
        TypedQuery<NiemXa> query = entityManager.createNamedQuery("NiemXa.findByWtid", NiemXa.class);
        query.setParameter("wtid", wtid);
        List<NiemXa> niemXas = query.getResultList();
        if (niemXas != null && niemXas.size() > 0) {
            return niemXas.get(0);
        }

        return null;
    }
}
