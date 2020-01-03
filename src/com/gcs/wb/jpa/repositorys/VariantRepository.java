/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Variant;
import java.util.ArrayList;
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

    public List<Variant> findVariant(String param, String client, String plant) {
        List<Variant> result = new ArrayList<Variant>();
        try {
            TypedQuery<Variant> query = entityManager.createNamedQuery("Variant.findByFullParam", Variant.class);
            query.setParameter("mandt", client);
            query.setParameter("wPlant", plant);
            query.setParameter("param", param);
            result = query.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }
}
