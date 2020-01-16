/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.OutboundDetail;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class OutboundDetailRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<OutboundDetail> findByDeliveryOrderNo(String deliv_numb) {

        List<OutboundDetail> result = new ArrayList<OutboundDetail>();
        try {
            TypedQuery<OutboundDetail> nq = entityManager.createNamedQuery("OutboundDetail.findByDeliveryOrderNo", OutboundDetail.class);
            nq.setParameter("deliveryOrderNo", "%" + deliv_numb + "%");
            result = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }

    public List<OutboundDetail> findByDeliveryOrderNoAndDeliveryOrderItem(String deliv_numb, String item) {
        List<OutboundDetail> result = new ArrayList<OutboundDetail>();
        try {
            TypedQuery<OutboundDetail> nq = entityManager.createNamedQuery("OutboundDetail.findByDeliveryOrderNoAndDeliveryOrderItem", OutboundDetail.class);
            nq.setParameter("deliveryOrderNo", "%" + deliv_numb + "%");
            nq.setParameter("deliveryOrderItem", item);
            result = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }

    public List<OutboundDetail> findByWtId(String wt_id) {
        List<OutboundDetail> result = new ArrayList<OutboundDetail>();
        try {
            TypedQuery<OutboundDetail> nq = entityManager.createNamedQuery("OutboundDetail.findByWtId", OutboundDetail.class);
            nq.setParameter("wtId", "%" + wt_id + "%");
            result = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }
}
