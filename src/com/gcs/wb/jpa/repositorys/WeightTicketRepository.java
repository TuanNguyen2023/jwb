/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.enums.MaterialEnum;
import com.gcs.wb.base.enums.ModeEnum;
import com.gcs.wb.base.enums.StatusEnum;
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

    public WeightTicket findByDeliveryOrderNoNotExistEbeln(String deliverNumber) {
        WeightTicket weightTicket = null;
        List<WeightTicket> list = getListByDeliveryOrderNoNotExistEbeln(deliverNumber);
        if (list != null && list.size() > 0) {
            weightTicket = list.get(0);
        }
        return weightTicket;
    }
    
    public WeightTicket findByDeliveryOrderNoNotExistEbelnScale(String deliverNumber) {
        WeightTicket weightTicket = null;
        List<WeightTicket> list = getListByDeliveryOrderNoNotExistEbelnScale(deliverNumber);
        if (list != null && list.size() > 0) {
            weightTicket = list.get(0);
        }
        return weightTicket;
    }
    
    public List<WeightTicket> getListByDeliveryOrderNoNotExistEbelnScale(String deliverNumber) {
        List<WeightTicket> wt = new ArrayList<WeightTicket>();
        TypedQuery<WeightTicket> query = entityManager.createNamedQuery("WeightTicket.findByDeliveryOrderNoNotExistEbelnScale", WeightTicket.class);
        query.setParameter("deliveryOrderNo", "%" + deliverNumber + "%");
        wt = query.getResultList();
        return wt;
    }

    public List<WeightTicket> getListByDeliveryOrderNoNotExistEbeln(String deliverNumber) {
        List<WeightTicket> wt = new ArrayList<WeightTicket>();
        TypedQuery<WeightTicket> query = entityManager.createNamedQuery("WeightTicket.findByDeliveryOrderNoNotExistEbeln", WeightTicket.class);
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

    public List<WeightTicket> findListWeightTicket(String month, String year, String tagent, String matnr, List<Character> modes, StatusEnum status) throws Exception {
        String query = "SELECT DISTINCT w FROM WeightTicket w "
                + " , IN(w.weightTicketDetails) wd "
                + "WHERE FUNC('YEAR', w.createdDate) = :year "
                + " AND FUNC('MONTH', w.createdDate) = :month "
                + " AND w.regType IN :regType "
                + " AND w.mandt = :mandt"
                + "  AND w.wplant = :wplant";

        if (!tagent.equalsIgnoreCase("-2")) {
            query += " AND wd.transVendor = :taAbbr";
        }

        if (!matnr.equalsIgnoreCase("-2")) {
            if (!matnr.equalsIgnoreCase("-1")) {
                query += " AND wd.matnrRef = :matnrRef ";
            } else {
                query += " AND wd.matnrRef IS NULL ";
            }
        }

        switch (status) {
            case POSTED:
                query += "  AND w.status = '" + Constants.WeightTicket.STATUS_POSTED + "'";
                break;
            case UNFINISH:
                query += "  AND w.status IS NULL";
                break;
            case OFFLINE:
                query += "  AND w.offlineMode = 1";
                break;

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

    public List<WeightTicket> findListWeightTicket(Date from, Date to,
            String creator, String driverName, String plateNo,
            String material, StatusEnum status, ModeEnum mode) throws Exception {

        String query = "SELECT DISTINCT w FROM WeightTicket w "
                + " , IN(w.weightTicketDetails) wd "
                + "WHERE w.createdDate BETWEEN :from AND :to"
                + "  AND w.creator LIKE :creator"
                + "  AND (w.plateNo LIKE :plateNo OR w.trailerId LIKE :plateNo)"
                + "  AND w.driverName LIKE :driverName"
                + "  AND w.mandt = :mandt"
                + "  AND w.wplant = :wplant";

        if (mode != ModeEnum.ALL) {
            query += "  AND w.mode = '" + mode + "'";
        }

        switch (status) {
            case POSTED:
                query += "  AND w.status = '" + Constants.WeightTicket.STATUS_POSTED + "'";
                break;
            case UNFINISH:
                query += "  AND w.status IS NULL";
                break;
            case OFFLINE:
                query += "  AND w.offlineMode = 1";
                break;
        }

        if (material.equals(MaterialEnum.OTHER.VALUE)) {
            query += "  AND wd.matnrRef IS NULL";
        } else if (!material.equals(MaterialEnum.ALL.VALUE)) {
            query += "  AND wd.matnrRef = '" + material + "'";
        }

        try {
            TypedQuery<WeightTicket> nq = entityManager.createQuery(query, WeightTicket.class);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("driverName", "%" + driverName + "%");
            nq.setParameter("plateNo", "%" + plateNo + "%");
            nq.setParameter("mandt", configuration.getSapClient());
            nq.setParameter("wplant", configuration.getWkPlant());

            return nq.getResultList();
        } catch (NumberFormatException ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public WeightTicket getWeightTicketByIdAndMandtAndWplant(String id, String mandt, String wplant) {
        WeightTicket weightTicket = null;
        TypedQuery<WeightTicket> query = entityManager.createNamedQuery("WeightTicket.findByIdAndMandtAndWplant", WeightTicket.class);
        query.setParameter("id", id);
        query.setParameter("mandt", mandt);
        query.setParameter("wplant", wplant);
        List<WeightTicket> weightTickets = query.getResultList();
        if (weightTickets != null && weightTickets.size() > 0) {
            weightTicket = weightTickets.get(0);
        }
        return weightTicket;
    }
    
    public List<WeightTicket> findQtyByPoNo(String poNo) {
        String status = "POSTED";
        TypedQuery<WeightTicket> typedQuery = entityManager.createNamedQuery("WeightTicket.findByQtyPOisPOSTED", WeightTicket.class);
        typedQuery.setParameter("ebeln", poNo);
        typedQuery.setParameter("status", status);
        typedQuery.setParameter("mandt", configuration.getSapClient());
        typedQuery.setParameter("wplant", configuration.getWkPlant());
        return typedQuery.getResultList();
    }

    public WeightTicket findByDOFromPO(String doNum) {
        WeightTicket weightTicket = null;
        String modePlant = "OUT_PLANT_PLANT";
        TypedQuery<WeightTicket> typedQuery = entityManager.createNamedQuery("WeightTicket.findByDOFromPO", WeightTicket.class);
        typedQuery.setParameter("deliveryOrderNo", doNum);
        typedQuery.setParameter("mode", modePlant);
        typedQuery.setParameter("mandt", configuration.getSapClient());

        List<WeightTicket> weightTickets = typedQuery.getResultList();
        if (weightTickets != null && weightTickets.size() > 0) {
            weightTicket = weightTickets.get(0);
        }
        return weightTicket;
    }
}
