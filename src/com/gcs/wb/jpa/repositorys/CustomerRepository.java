/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Customer;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class CustomerRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    Logger logger = Logger.getLogger(this.getClass());

    public List<Customer> getListCustomer() {
        List<Customer> list = new ArrayList<>();
        try {
            TypedQuery<Customer> typedQuery = entityManager.createNamedQuery("Customer.findByMandt", Customer.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public Customer findByKunnr(String kunnr) {
        try {
            TypedQuery<Customer> typedQuery = entityManager.createNamedQuery("Customer.findByKunnr", Customer.class);
            typedQuery.setParameter("kunnr", kunnr);
            List<Customer> customers = typedQuery.getResultList();
            if (customers != null && customers.size() > 0) {
                return customers.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }
}
