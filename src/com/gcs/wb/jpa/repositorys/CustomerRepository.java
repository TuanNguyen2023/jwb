/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Customer;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class CustomerRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<Object[]> getCustomerByMaNdt(String client) {
        List<Object[]> list = new ArrayList<Object[]>();
        Query q = entityManager.createNativeQuery("Select * from Customer where MANDT = ?");
        q.setParameter(1, client);
        list = q.getResultList();
        return list;
    }

    public List<Customer> findCustByMandt(String mandt) {
        List<Customer> result = new ArrayList<Customer>();
        try {
            TypedQuery<Customer> query = entityManager.createNamedQuery("Customer.findByMandt", Customer.class);
            query.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            result = query.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return result;
    }
}
