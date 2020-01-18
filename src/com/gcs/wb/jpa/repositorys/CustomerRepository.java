/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Customer;
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
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List<Customer> getListCustomer() {
        TypedQuery<Customer> typedQuery = entityManager.createNamedQuery("Customer.findAll", Customer.class);
        return typedQuery.getResultList();
    }

    public Customer findByKunnr(String kunnr) {
        TypedQuery<Customer> typedQuery = entityManager.createNamedQuery("Customer.findKunnr", Customer.class);
        typedQuery.setParameter("kunnr", kunnr);
        List<Customer> customers = typedQuery.getResultList();
        if (customers != null && customers.size() > 0) {
            return customers.get(0);
        }

        return null;
    }
}
