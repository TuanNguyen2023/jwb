/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.OutboundDeliveryDetail;
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

    public List<OutboundDeliveryDetail> findByDeliveryOrderNo(String deliv_numb) {

        List<OutboundDeliveryDetail> result = new ArrayList<>();
        try {
            TypedQuery<OutboundDeliveryDetail> nq = entityManager.createNamedQuery("OutboundDeliveryDetail.findByDeliveryOrderNo", OutboundDeliveryDetail.class);
            nq.setParameter("deliveryOrderNo", "%" + deliv_numb + "%");
            result = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }

    public List<OutboundDeliveryDetail> findByDeliveryOrderNoAndDeliveryOrderItem(String deliv_numb, String item) {
        List<OutboundDeliveryDetail> result = new ArrayList<>();
        try {
            TypedQuery<OutboundDeliveryDetail> nq = entityManager.createNamedQuery("OutboundDeliveryDetail.findByDeliveryOrderNoAndDeliveryOrderItem", OutboundDeliveryDetail.class);
            nq.setParameter("deliveryOrderNo", "%" + deliv_numb + "%");
            nq.setParameter("deliveryOrderItem", item);
            result = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }

    public List<OutboundDeliveryDetail> findByWtId(String wt_id) {
        List<OutboundDeliveryDetail> result = new ArrayList<>();
        try {
            TypedQuery<OutboundDeliveryDetail> nq = entityManager.createNamedQuery("OutboundDeliveryDetail.findByWtId", OutboundDeliveryDetail.class);
            nq.setParameter("wtId", "%" + wt_id + "%");
            result = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }
}
