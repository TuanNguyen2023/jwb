/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.WeightTicket;
import java.sql.Date;
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
public class WeightTicketRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public List getMatsModel(String client, String plant) {
        Query q = entityManager.createNativeQuery("select distinct MATNR_REF, REG_ITEM_TEXT from WeightTicket "
                + " where MANDT = ? and WPlant = ?");
        q.setParameter(1, client);
        q.setParameter(2, plant);
        List list = q.getResultList();
        return list;
    }

    public WeightTicket findByMandtWPlantDelivNumb(String client, String plant, String deliverNumber) {
        WeightTicket weightTicket = new WeightTicket();
        List<WeightTicket> list = getListByMandtWPlantDelivNumb(client, plant, deliverNumber);
        if (list != null) {
            weightTicket = list.get(0);
        }
        return weightTicket;
    }

    public List<WeightTicket> getListByMandtWPlantDelivNumb(String client, String plant, String deliverNumber) {
        List<WeightTicket> wt = new ArrayList<WeightTicket>();
        TypedQuery<WeightTicket> query = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDelivNumb", WeightTicket.class);
        query.setParameter("mandt", client);
        query.setParameter("wPlant", plant);
        query.setParameter("delivNumb", deliverNumber);
        wt = query.getResultList();
        return wt;
    }

    public List<WeightTicket> findByCreateDateRange(String client, String plant, Date from, Date to) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByCreateDateRange", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDateFull(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String loaihang, String bienso) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDateFull", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDateNull(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String bienso) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDateNull", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDateNullAll(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String bienso) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDateNullAll", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDateDissolved(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String loaihang, String bienso) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDateDissolved", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);

        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDateDissolvedNull(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String bienso) throws Exception {
        List<WeightTicket> list = new ArrayList<WeightTicket>();

        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDateDissolvedNull", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDateDissolvedNullAll(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String bienso) throws Exception {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDateDissolvedNullAll", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDatePosted(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String loaihang, String bienso) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDatePosted", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDatePostedNull(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String bienso) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDatePostedNull", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDatePostedNullAll(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String bienso) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDatePostedNullAll", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDateAll(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String loaihang, String bienso) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDateAll", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDateAllNull(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String bienso) throws Exception {
        List<WeightTicket> list = new ArrayList<WeightTicket>();
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDateAllNull", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }

    public List<WeightTicket> findByMandtWPlantDateAllNullAll(String client, String plant,
            Date from, Date to,
            String creator, String taixe,
            String bienso) {
        List<WeightTicket> list = new ArrayList<WeightTicket>();

        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery("WeightTicket.findByMandtWPlantDateAllNullAll", WeightTicket.class);
            nq.setParameter("mandt", client);
            nq.setParameter("wPlant", plant);
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
            list = nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return list;
    }
}
