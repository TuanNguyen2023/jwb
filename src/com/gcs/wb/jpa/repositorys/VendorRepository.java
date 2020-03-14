/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
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
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();

    /**
     *
     * Get data for "Vendor boc xep/van chuyen"
     *
     * @return
     */
    public List<Vendor> getListVendor() {
        TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandtWplant", Vendor.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        return typedQuery.getResultList();
    }

    public Vendor findByLifnr(String lifnr) {
        TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandtWplantLifnr", Vendor.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        typedQuery.setParameter("lifnr", lifnr);
        List<Vendor> vendors = typedQuery.getResultList();
        if (vendors != null && vendors.size() > 0) {
            return vendors.get(0);
        }

        return null;
    }
    
    public List<Vendor> findByEkorg(String ekorg) {
        TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandtWplantEkorg", Vendor.class);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        typedQuery.setParameter("ekorg", ekorg);
        return typedQuery.getResultList();
    }

    public boolean hasData(String mandt, String wplant) {
        TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandtWplant", Vendor.class);
        typedQuery.setParameter("mandt", mandt);
        typedQuery.setParameter("wplant", wplant);
        List<Vendor> vendors = typedQuery.getResultList();
        if (vendors != null && vendors.size() > 0) {
            return true;
        }

        return false;
    }
}
