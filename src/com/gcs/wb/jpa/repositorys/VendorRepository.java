/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import com.gcs.wb.jpa.entity.Vendor;

/**
 *
 * @author dinhhn.vr
 */
public class VendorRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    /**
     * 
     * Get data for "Vendor boc xep/van chuyen"
     * @return 
     */
    public List<Vendor> getListVendor() {
        TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findAll", Vendor.class);
        return typedQuery.getResultList();
    }

    public Vendor findByLifnr(String lifnr) {
        TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByLifnr", Vendor.class);
        typedQuery.setParameter("lifnr", lifnr);
        List<Vendor> vendors = typedQuery.getResultList();
        if (vendors != null && vendors.size() > 0) {
            return vendors.get(0);
        }

        return null;
    }
    
    public boolean hasData(String mandt) {
        TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandt", Vendor.class);
        typedQuery.setParameter("mandt", mandt);
        List<Vendor> vendors = typedQuery.getResultList();
        if (vendors != null && vendors.size() > 0) {
            return true;
        }

        return false;
    }
}
