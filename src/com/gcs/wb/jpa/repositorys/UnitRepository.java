/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Unit;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author THANGPT
 */
public class UnitRepository {
    EntityManager entityManager = JPAConnector.getInstance();
    
    public List<Unit> getListUnit() {
        TypedQuery<Unit> typedQuery = entityManager.createNamedQuery("Unit.findAll", Unit.class);
        return typedQuery.getResultList();
    }
}
