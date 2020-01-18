/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.PurchaseOrder;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author dinhhn.vr
 */
public class PurchaseOrderRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public List<PurchaseOrder> getListPurchaseOrder() {
        TypedQuery<PurchaseOrder> query = entityManager.createNamedQuery("PurchaseOrder.findAll", PurchaseOrder.class);
        return query.getResultList();
    }

    public PurchaseOrder findByPoNumber(String poNumber) {
        TypedQuery<PurchaseOrder> query = entityManager.createNamedQuery("PurchaseOrder.findByPoNumber", PurchaseOrder.class);
        query.setParameter("poNumber", poNumber);
        List<PurchaseOrder> purchaseOrders = query.getResultList();
        if (purchaseOrders != null && purchaseOrders.size() > 0) {
            return purchaseOrders.get(0);
        }

        return null;
    }
}
