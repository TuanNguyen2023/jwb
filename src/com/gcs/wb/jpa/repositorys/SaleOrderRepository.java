/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.SaleOrder;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author THANGLH
 */
public class SaleOrderRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<SaleOrder> getListSaleOrder() {
        TypedQuery<SaleOrder> typedQuery = entityManager.createNamedQuery("SaleOrder.findAll", SaleOrder.class);
        return typedQuery.getResultList();
    }

    public SaleOrder findBySoNumber(String soNumber) {
        TypedQuery<SaleOrder> typedQuery = entityManager.createNamedQuery("SaleOrder.findBySoNumber", SaleOrder.class);
        typedQuery.setParameter("soNumber", soNumber);
        List<SaleOrder> sellOrders = typedQuery.getResultList();
        if (sellOrders != null && sellOrders.size() > 0) {
            return sellOrders.get(0);
        }

        return null;
    }

    public boolean hasData() {
        return getListSaleOrder().size() > 0;
    }
}
