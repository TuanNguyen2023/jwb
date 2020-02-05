/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class OutboundDeliveryRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public OutboundDelivery findByDeliveryOrderNo(String deliveryOrderNo) {
        OutboundDelivery outbDel = null;
        try {
            TypedQuery<OutboundDelivery> nq = entityManager.createNamedQuery("OutboundDelivery.findByDeliveryOrderNo", OutboundDelivery.class);
            nq.setParameter("deliveryOrderNo", "%" + deliveryOrderNo + "%");
            List<OutboundDelivery> list = nq.getResultList();
            if (list != null & list.size() > 0) {
                outbDel = list.get(0);
            }

        } catch (Exception ex) {
            logger.error(null, ex);

        }
        return outbDel;
    }
}
