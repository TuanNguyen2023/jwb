/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Variant;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class VariantRepository {

    private Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    EntityManager entityManager = JPAConnector.getInstance();

    public Variant findByParam(String param) {
        Variant result = null;
        try {
            TypedQuery<Variant> query = entityManager.createNamedQuery("Variant.findByParam", Variant.class);
            query.setParameter("param", param);
            List<Variant> variants = query.getResultList();
            if (variants != null && variants.size() > 0) {
                return variants.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }
}
