/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Partner;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author thanghl
 */
public class PartnerRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    Logger logger = Logger.getLogger(this.getClass());

    public List<Partner> getListPartner() {
        List<Partner> list = new ArrayList<>();
        try {
            TypedQuery<Partner> typedQuery = entityManager.createNamedQuery("Partner.findByMandt", Partner.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public Partner findByKunnr(String kunnr) {
        try {
            TypedQuery<Partner> typedQuery = entityManager.createNamedQuery("Partner.findByKunnr", Partner.class);
            typedQuery.setParameter("kunnr", kunnr);
            List<Partner> partners = typedQuery.getResultList();
            if (partners != null && partners.size() > 0) {
                return partners.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public List<Partner> findByMandtKunnr(String mandt, String kunnr) {
        List<Partner> list = new ArrayList<>();
        try {
            TypedQuery<Partner> typedQuery = entityManager.createNamedQuery("Partner.findByMandtKunnr", Partner.class);
            typedQuery.setParameter("mandt", mandt);
            typedQuery.setParameter("kunnr", kunnr);
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public Partner findByMandtKunnrKunn2(String mandt, String kunnr, String kunn2) {
        try {
            TypedQuery<Partner> typedQuery = entityManager.createNamedQuery("Partner.findByMandtKunnrKunn2", Partner.class);
            typedQuery.setParameter("mandt", mandt);
            typedQuery.setParameter("kunnr", kunnr);
            typedQuery.setParameter("kunn2", kunn2);
            List<Partner> partners = typedQuery.getResultList();
            if (partners != null && partners.size() > 0) {
                return partners.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public Partner findPartner(String mandt, String kunnr, String vkorg, String vtweg, String spart, String parvw, String parza) {
        try {
            TypedQuery<Partner> typedQuery = entityManager.createNamedQuery("Partner.findPartner", Partner.class);
            typedQuery.setParameter("mandt", mandt);
            typedQuery.setParameter("kunnr", kunnr);
            typedQuery.setParameter("vkorg", vkorg);
            typedQuery.setParameter("vtweg", vtweg);
            typedQuery.setParameter("spart", spart);
            typedQuery.setParameter("parvw", parvw);
            typedQuery.setParameter("parza", parza);
            List<Partner> partners = typedQuery.getResultList();
            if (partners != null && partners.size() > 0) {
                return partners.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public boolean hasData() {
        try {
            TypedQuery<Partner> typedQuery = entityManager.createNamedQuery("Partner.findByMandt", Partner.class);
            typedQuery.setParameter("mandt", configuration.getSapClient());
            List<Partner> partners = typedQuery.getResultList();
            if (partners != null && partners.size() > 0) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return false;
    }
}
