/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.SaleOrder;
import java.util.ArrayList;
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
    Logger logger = Logger.getLogger(this.getClass());
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();

    public List<SaleOrder> getListSaleOrder() {
        List<SaleOrder> list = new ArrayList<>();
        try {
            TypedQuery<SaleOrder> typedQuery = entityManager.createNamedQuery("SaleOrder.findAll", SaleOrder.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public SaleOrder findBySoNumber(String soNumber) {
        try {
            TypedQuery<SaleOrder> typedQuery = entityManager.createNamedQuery("SaleOrder.findBySoNumber", SaleOrder.class);
            typedQuery.setParameter("soNumber", soNumber);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            List<SaleOrder> sellOrders = typedQuery.getResultList();
            if (sellOrders != null && sellOrders.size() > 0) {
                return sellOrders.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public boolean hasData() {
        return getListSaleOrder().size() > 0;
    }
}
