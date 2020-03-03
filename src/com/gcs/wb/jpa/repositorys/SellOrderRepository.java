/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.MaterialGroup;
import com.gcs.wb.jpa.entity.SellOrder;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author THANGLH
 */
public class SellOrderRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<SellOrder> getListSellOrder() {
        TypedQuery<SellOrder> typedQuery = entityManager.createNamedQuery("SellOrder.findAll", SellOrder.class);
        return typedQuery.getResultList();
    }

    public SellOrder findBySoNumber(String soNumber) {
        TypedQuery<SellOrder> typedQuery = entityManager.createNamedQuery("SellOrder.findBySoNumber", SellOrder.class);
        typedQuery.setParameter("soNumber", soNumber);
        List<SellOrder> sellOrders = typedQuery.getResultList();
        if (sellOrders != null && sellOrders.size() > 0) {
            return sellOrders.get(0);
        }

        return null;
    }
}
