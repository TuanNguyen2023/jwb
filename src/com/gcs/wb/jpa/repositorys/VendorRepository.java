/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import com.gcs.wb.jpa.entity.Vendor;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class VendorRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    Logger logger = Logger.getLogger(this.getClass());

    /**
     *
     * Get data for "Vendor boc xep/van chuyen"
     *
     * @return
     */
    public List<Vendor> getListVendor() {
        List<Vendor> list = new ArrayList<>();
        try {
            TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandtWplant", Vendor.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public Vendor findByLifnr(String lifnr) {
        try {
            TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandtWplantLifnr", Vendor.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            typedQuery.setParameter("lifnr", lifnr);
            List<Vendor> vendors = typedQuery.getResultList();
            if (vendors != null && vendors.size() > 0) {
                return vendors.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }
    
    public Vendor findByLifnrIsCustomer(String lifnr) {
        try {
            TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandtWplantLifnrIsCustomer", Vendor.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            typedQuery.setParameter("lifnr", lifnr);
            typedQuery.setParameter("groupType", Constants.GroupType.CUSTOMER);
            List<Vendor> vendors = typedQuery.getResultList();
            if (vendors != null && vendors.size() > 0) {
                return vendors.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public List<Vendor> findByEkorg(String ekorg) {
        List<Vendor> list = new ArrayList<>();
        try {
            TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandtWplantEkorg", Vendor.class
            );
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            typedQuery.setParameter("ekorg", ekorg);
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }
    
    public List<Vendor> findByGroupType() {
        List<Vendor> list = new ArrayList<>();
        try {
            TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandtWplantIsCustomer", Vendor.class
            );
            typedQuery.setParameter("mandt", configuration.getSapClient());
            typedQuery.setParameter("wplant", configuration.getWkPlant());
            typedQuery.setParameter("groupType", Constants.GroupType.CUSTOMER);
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public boolean hasData(String mandt, String wplant) {
        try {
            TypedQuery<Vendor> typedQuery = entityManager.createNamedQuery("Vendor.findByMandtWplant", Vendor.class
            );
            typedQuery.setParameter("mandt", mandt);
            typedQuery.setParameter("wplant", wplant);
            List<Vendor> vendors = typedQuery.getResultList();
            if (vendors != null && vendors.size() > 0) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return false;
    }
}
