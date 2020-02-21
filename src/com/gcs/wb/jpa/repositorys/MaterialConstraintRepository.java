/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.MaterialConstraint;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author thangtp.nr
 */
public class MaterialConstraintRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    
    public MaterialConstraint findByMatnr(String matnr) {
        TypedQuery<MaterialConstraint> typedQuery = entityManager.createNamedQuery("MaterialConstraint.findByMatnr", MaterialConstraint.class);
        typedQuery.setParameter("matnr", matnr);
        List<MaterialConstraint> materialconstraint = typedQuery.getResultList();
        if (materialconstraint != null && materialconstraint.size() > 0) {
            return materialconstraint.get(0);
        }
        return null;
    }
}
