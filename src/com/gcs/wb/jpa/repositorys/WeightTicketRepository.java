/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.WeightTicket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class WeightTicketRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();

    public List<WeightTicket> getListWeightTicket() {
        TypedQuery<WeightTicket> query = entityManager.createNamedQuery("WeightTicket.findAll", WeightTicket.class);
        return query.getResultList();
    }
    
    public WeightTicket findByIdSeqDay(int id, int seqDay) {
        WeightTicket weightTicket = null;
        TypedQuery<WeightTicket> query = entityManager.createNamedQuery("WeightTicket.findByIdSeqDay", WeightTicket.class);
        query.setParameter("id", id);
        query.setParameter("seqDay", seqDay);
        List<WeightTicket> weightTickets = query.getResultList();
        if (weightTickets != null && weightTickets.size() > 0) {
            weightTicket = weightTickets.get(0);
        }
        return weightTicket;
    }

    public WeightTicket findByDeliveryOrderNo(String deliverNumber) {
        WeightTicket weightTicket = null;
        List<WeightTicket> list = getListByDeliveryOrderNo(deliverNumber);
        if (list != null && list.size() > 0) {
            weightTicket = list.get(0);
        }
        return weightTicket;
    }

    public List<WeightTicket> getListByDeliveryOrderNo(String deliverNumber) {
        List<WeightTicket> wt = new ArrayList<WeightTicket>();
        TypedQuery<WeightTicket> query = entityManager.createNamedQuery("WeightTicket.findByDeliveryOrderNo", WeightTicket.class);
        query.setParameter("deliveryOrderNo", "%" + deliverNumber + "%");
        wt = query.getResultList();
        return wt;
    }

    public List<WeightTicket> findByCreatedDateRange(Date from, Date to) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByCreatedDateRange", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByDateFull(
            Date from, Date to,
            String creator, String driverName,
            String loaihang, String plateNo) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDateFull", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByDateNull(
            Date from, Date to,
            String creator, String driverName,
            String plateNo) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDateNull", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByDateNullAll(
            Date from, Date to,
            String creator, String driverName,
            String plateNo) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDateNullAll", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByDateDissolved(
            Date from, Date to,
            String creator, String driverName,
            String loaihang, String plateNo) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDateDissolved", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);

        }
        return list;
    }

    public List<WeightTicket> findByDateDissolvedNull(
            Date from, Date to,
            String creator, String driverName,
            String plateNo) throws Exception {
        List<WeightTicket> list = new ArrayList<WeightTicket>();

        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDateDissolvedNull", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
        return list;
    }

    public List<WeightTicket> findByDateDissolvedNullAll(
            Date from, Date to,
            String creator, String driverName,
            String plateNo) throws Exception {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDateDissolvedNullAll", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByDatePosted(
            Date from, Date to,
            String creator, String driverName,
            String loaihang, String plateNo) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDatePosted", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByDatePostedNull(
            Date from, Date to,
            String creator, String driverName,
            String plateNo) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDatePostedNull", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByDatePostedNullAll(
            Date from, Date to,
            String creator, String driverName,
            String plateNo) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDatePostedNullAll", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByDateAll(
            Date from, Date to,
            String creator, String driverName,
            String loaihang, String plateNo) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDateAll", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByDateAllNull(
            Date from, Date to,
            String creator, String driverName,
            String plateNo) throws Exception {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDateAllNull", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByDateAllNullAll(
            Date from, Date to,
            String creator, String driverName,
            String plateNo) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();

        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByDateAllNullAll", WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }
    
    public List<WeightTicket> findListWeightTicket(String month, String year, String tagent, String matnr, List<Character> modes, boolean isPosted) throws Exception {
        String query = "SELECT w FROM WeightTicket w "
                + "WHERE FUNC('YEAR', w.createdDate) = :year "
                + " AND FUNC('MONTH', w.createdDate) = :month "
                + " AND w.regType IN :regType "
                + " AND w.mandt = :mandt"
                + "  AND w.wplant = :wplant";
        if (!tagent.equalsIgnoreCase("-2")) {
            query += " AND w.plateNo IN ( SELECT tv.vehicle.plateNo FROM TransportAgentVehicle tv WHERE tv.transportAgent.abbr = :taAbbr ) ";
        }
        if (!matnr.equalsIgnoreCase("-2")) {
            if (!matnr.equalsIgnoreCase("-1")) {
                query += " AND w.matnrRef = :matnrRef ";
            } else {
                query += " AND w.matnrRef IS NULL ";
            }
        }
        if (isPosted) {
            query += " AND w.status = '" + Constants.WeightTicket.STATUS_POSTED + "' ";
        }
        try {
            TypedQuery<WeightTicket> nq = entityManager.createQuery(query, WeightTicket.class);
            nq.setParameter("year", Integer.parseInt(year));
            nq.setParameter("month", Integer.parseInt(month));
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());
            if (!tagent.equalsIgnoreCase("-2")) {
                nq.setParameter("taAbbr", tagent);
            }

            if (!matnr.equalsIgnoreCase("-1") && !matnr.equalsIgnoreCase("-2")) {
                nq.setParameter("matnrRef", matnr);
            }

            nq.setParameter("regType", modes);
            return nq.getResultList();
        } catch (NumberFormatException ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
}
