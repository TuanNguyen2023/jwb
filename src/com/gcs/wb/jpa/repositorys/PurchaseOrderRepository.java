/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.PurchaseOrder;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class PurchaseOrderRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = Logger.getLogger(this.getClass());

    public List<PurchaseOrder> getListPurchaseOrder() {
        List<PurchaseOrder> list = new ArrayList<>();
        try {
            TypedQuery<PurchaseOrder> query = entityManager.createNamedQuery("PurchaseOrder.findAll", PurchaseOrder.class);
            list = query.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public PurchaseOrder findByPoNumber(String poNumber) {
        try {
            TypedQuery<PurchaseOrder> query = entityManager.createNamedQuery("PurchaseOrder.findByPoNumber", PurchaseOrder.class);
            query.setParameter("poNumber", poNumber);
            List<PurchaseOrder> purchaseOrders = query.getResultList();
            if (purchaseOrders != null && purchaseOrders.size() > 0) {
                return purchaseOrders.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public boolean hasData() {
        return getListPurchaseOrder().size() > 0;
    }
}
