/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.OutbDetailsV2;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class OutbDetailsV2Repository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<OutbDetailsV2> findByMandtDelivNumb(String mandt, String deliv_numb) {

        List<OutbDetailsV2> result = new ArrayList<OutbDetailsV2>();
        try {
            TypedQuery<OutbDetailsV2> nq = entityManager.createNamedQuery("OutbDetailsV2.findByMandtDelivNumb", OutbDetailsV2.class);
            nq.setParameter("mandt", mandt);
            nq.setParameter("delivNumb", "%" + deliv_numb + "%");
            result = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }

    public List<OutbDetailsV2> findByMandtDelivNumbItem(String mandt, String deliv_numb, String item) {
        List<OutbDetailsV2> result = new ArrayList<OutbDetailsV2>();
        try {
            TypedQuery<OutbDetailsV2> nq = entityManager.createNamedQuery("OutbDetailsV2.findByMandtDelivNumbItem", OutbDetailsV2.class);
            nq.setParameter("mandt", mandt);
            nq.setParameter("delivNumb", "%" + deliv_numb + "%");
            nq.setParameter("delivItem", item);
            result = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }

    public List<OutbDetailsV2> findByMandtWTID(String client, String wt_id) {
        List<OutbDetailsV2> result = new ArrayList<OutbDetailsV2>();
        try {
            TypedQuery<OutbDetailsV2> nq = entityManager.createNamedQuery("OutbDetailsV2.findByMandtWTID", OutbDetailsV2.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wtId", "%" + wt_id + "%");
            result = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }
}
