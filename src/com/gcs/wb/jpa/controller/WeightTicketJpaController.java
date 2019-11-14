/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.OutbDel;
import com.gcs.wb.jpa.entity.Variant;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.WeightTicketPK;
import com.gcs.wb.jpa.entity.OutbDetailsV2;
import com.gcs.wb.jpa.entity.OutbDetailsV2PK;
import com.gcs.wb.jpa.entity.Regvehicle;
import com.gcs.wb.jpa.entity.UserLocal;
 import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.text.SimpleDateFormat;
 import java.util.Calendar;
import java.util.Date;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;
import java.sql.Timestamp;
 import com.gcs.wb.utils.Base64_Utils; 
import com.gcs.wb.model.AppConfig; 
import  java.util.*; 
import java.sql.*;
/**
 *
 * Tuanna modified 30/06/2013
 */
public class WeightTicketJpaController {

    private EntityManager gEM = null;
    private Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public WeightTicketJpaController(EntityManager em) {
        this.gEM = em;
    }

    public EntityManager getEntityManager() {
        return gEM;
    }

    public List<WeightTicket> findWeightTicketEntities() throws Exception {
        return findWeightTicketEntities(true, -1, -1);
    }

    public List<WeightTicket> findWeightTicketEntities(int maxResults, int firstResult) throws Exception {
        return findWeightTicketEntities(false, maxResults, firstResult);
    }

    private List<WeightTicket> findWeightTicketEntities(boolean all, int maxResults, int firstResult) throws Exception {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<WeightTicket> cq = em.getCriteriaBuilder().createQuery(WeightTicket.class);
            cq.select(cq.from(WeightTicket.class));
            TypedQuery<WeightTicket> q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public WeightTicket findWeightTicket(WeightTicketPK id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            return em.find(WeightTicket.class, id);
        } catch (Exception ex) {
            logger.error(id, ex);
            throw ex;
        }
    }

    public List<WeightTicket> findByCreateDateRange(Date from, Date to) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByCreateDateRange", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    //hoangvv modify
  
    public List<WeightTicket> findByMandtWPlantDateFull(Date from, Date to, String creator, String taixe, String loaihang, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDateFull", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public List<WeightTicket> findByMandtWPlantDateNull(Date from, Date to, String creator, String taixe, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDateNull", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    public List<WeightTicket> findByMandtWPlantDateNullAll(Date from, Date to, String creator, String taixe, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDateNullAll", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public List<WeightTicket> findByMandtWPlantDateDissolved(Date from, Date to, String creator, String taixe, String loaihang, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDateDissolved", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public List<WeightTicket> findByMandtWPlantDateDissolvedNull(Date from, Date to, String creator, String taixe, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDateDissolvedNull", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    public List<WeightTicket> findByMandtWPlantDateDissolvedNullAll(Date from, Date to, String creator, String taixe, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDateDissolvedNullAll", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public List<WeightTicket> findByMandtWPlantDatePosted(Date from, Date to, String creator, String taixe, String loaihang, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDatePosted", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("soXe", "%" + bienso + "%");
//           int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public List<WeightTicket> findByMandtWPlantDatePostedNull(Date from, Date to, String creator, String taixe, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDatePostedNull", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    public List<WeightTicket> findByMandtWPlantDatePostedNullAll(Date from, Date to, String creator, String taixe, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDatePostedNullAll", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
  public String getMsg( String id  )throws Exception {

    String msg ="" ; 
    EntityManager em = getEntityManager();
        em.clear();
    try{
        
         Query q = em.createNativeQuery("call pgetmsg(?)");
         
         q.setParameter(1, id);
         List<Timestamp> lst =  q.getResultList();
         msg = Base64_Utils.decodeNTimes ( lst.get(0).toString() ) ;
         
       //      
        return msg ; 
      } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }   
    
}
    public List<WeightTicket> findByMandtWPlantDateAll(Date from, Date to, String creator, String taixe, String loaihang, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDateAll", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("loaihang", loaihang);
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public List<WeightTicket> findByMandtWPlantDateAllNull(Date from, Date to, String creator, String taixe, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDateAllNull", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    public List<WeightTicket> findByMandtWPlantDateAllNullAll(Date from, Date to, String creator, String taixe, String bienso) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery("WeightTicket.findByMandtWPlantDateAllNullAll", WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("from", from);
            nq.setParameter("to", to);
            nq.setParameter("creator", "%" + creator + "%");
            nq.setParameter("tenTaiXe", "%" + taixe + "%");
            nq.setParameter("soXe", "%" + bienso + "%");
//            int n1 = Integer.parseInt(timefrom+"0000");
//            int n2 = Integer.parseInt(timeto+"9999");
//            nq.setParameter("timefrom", timefrom+"0000");
//            nq.setParameter("timeto", timeto+"9999");
//            nq.setParameter("dissolved", isDissolved);
//            nq.setParameter("posted", isPosted);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public List<WeightTicket> findListWTs(String month, String year, String tagent, String matnr, List<Character> modes, boolean isDissovled, boolean isPosted) throws Exception {
        String id = year.substring(2) + month + "%";
        String name_query = null;
        if (isDissovled) {
            if (matnr == null) {
                name_query = "WeightTicket.findByMandtWPlantIdSoxeRegcatDissovled";
            } else {
                name_query = "WeightTicket.findByMandtWPlantIdSoxeMatnrRegcatDissovled";
            }
        } else if (isPosted) {
            if (matnr == null) {
                name_query = "WeightTicket.findByMandtWPlantIdSoxeRegcatPosted";
            } else {
                name_query = "WeightTicket.findByMandtWPlantIdSoxeMatnrRegcatPosted";
            }
        } else {
            if (matnr.equalsIgnoreCase("-1")) {
                name_query = "WeightTicket.findByMandtWPlantIdSoxeRegcat";
            } else {
                name_query = "WeightTicket.findByMandtWPlantIdSoxeMatnrRegcat";
            }
        }
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<WeightTicket> nq = em.createNamedQuery(name_query, WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("id", id);
            nq.setParameter("taAbbr", tagent);
            if (!matnr.equalsIgnoreCase("-1")) {
                nq.setParameter("matnrRef", matnr);
            }
            nq.setParameter("regCategory", modes);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public int getNewSeqBDay() {
        EntityManager em = getEntityManager();
        em.clear();
   //     Query q = em.createNativeQuery("SELECT MAX(SEQ_BY_DAY) FROM WeightTicket WHERE MANDT = ? AND WPLANT = ? AND ID LIKE ?");
      //   Query q = em.createNativeQuery("SELECT Count(*) FROM WeightTicket WHERE MANDT = ? AND WPLANT = ? AND  MONTH(f_time) = MONTH(NOW()) AND YEAR(f_time) = YEAR(NOW()) AND DAY(f_time) = DAY(NOW()) " );
        Query q = em.createNativeQuery(" call jweighbridge.getCountTicketDay( ? ) " );        
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        String qSeq = formatter.format(Calendar.getInstance().getTime()) + "%";

      //  q.setParameter(1, WeighBridgeApp.getApplication().getConfig().getsClient());
        q.setParameter(1, WeighBridgeApp.getApplication().getConfig().getwPlant());
   //     q.setParameter(3, qSeq);
 
        Integer lNumber = 0;
        try {
            Object obj = q.getSingleResult();
            if (obj != null) {
                lNumber = ((Long) obj).intValue();
            }
        } catch (NoResultException ex) {
            lNumber = 0;
        } finally {
            lNumber++;
        }
        return lNumber;
    }

    public int getNewSeqBMonth() {
        EntityManager em = getEntityManager();
        em.clear();
      //  Query q = em.createNativeQuery("SELECT MAX(SEQ_BY_MONTH) FROM WeightTicket WHERE MANDT = ? AND WPLANT = ? AND ID LIKE ?");
      //     Query q = em.createNativeQuery("SELECT Count(*) FROM WeightTicket WHERE MANDT = ? AND WPLANT = ? AND  MONTH(f_time) = MONTH(NOW()) AND YEAR(f_time) = YEAR(NOW())  " );
           Query q = em.createNativeQuery(" call jweighbridge.getCountTicketMonth( ? ) " );   
        SimpleDateFormat formatter = new SimpleDateFormat("yyMM");
        String qSeq = formatter.format(Calendar.getInstance().getTime()) + "%";

 //       q.setParameter(1, WeighBridgeApp.getApplication().getConfig().getsClient());
        q.setParameter(1, WeighBridgeApp.getApplication().getConfig().getwPlant());
       // q.setParameter(3, qSeq);

        Integer lNumber = 0;
        try {
            Object obj = q.getSingleResult();
            if (obj != null) {
                lNumber = ((Long) obj).intValue();
            }
        } catch (NoResultException ex) {
            lNumber = 0;
        } finally {
            lNumber++;
        }
        return lNumber;
    }

    public List<Variant> findVariant(String param) throws Exception{
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<Variant> nq = em.createNamedQuery("Variant.findByFullParam", Variant.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("param", param);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    
    public List<OutbDetailsV2> findByMandtDelivNumb(String deliv_numb) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<OutbDetailsV2> nq = em.createNamedQuery("OutbDetailsV2.findByMandtDelivNumb", OutbDetailsV2.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("delivNumb", "%"+deliv_numb+"%");
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    public List<OutbDetailsV2> findByMandtDelivNumbItem(String deliv_numb, String item) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<OutbDetailsV2> nq = em.createNamedQuery("OutbDetailsV2.findByMandtDelivNumbItem", OutbDetailsV2.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("delivNumb", "%"+deliv_numb+"%");
            nq.setParameter("delivItem", item);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    
    public List<OutbDetailsV2> findByMandtWTID(String wt_id) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<OutbDetailsV2> nq = em.createNamedQuery("OutbDetailsV2.findByMandtWTID", OutbDetailsV2.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wtID", "%"+wt_id+"%");
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    
    public OutbDel findByMandtOutDel(String delnum) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<OutbDel> nq = em.createNamedQuery("OutbDel.findByMandtOutDel", OutbDel.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("delivNumb", "%"+delnum+"%");
            return nq.getSingleResult();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    
    public List<Customer> findCustByMandt(String mandt) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<Customer> nq = em.createNamedQuery("Customer.findByMandt", Customer.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    
    public UserLocal login (String username, String password) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<UserLocal> nq = em.createNamedQuery("UserLocal.login", UserLocal.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wplant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("id", username);
            nq.setParameter("pwd", password);
            return nq.getSingleResult();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    public Material CheckPOSTO(String matnr) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<Material> nq = em.createNamedQuery("Material.CheckPOSTO", Material.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wplant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("matnr", "%"+matnr+"%");
            return nq.getSingleResult();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    public Object get_server_time() throws Exception{
        /*
        EntityManager em = getEntityManager();
        em.clear();
        Query q = em.createNativeQuery("call pgettime()");
        return q.getSingleResult();     
        */
        return new Date();
   
    }
       
        public String get_server_stime() throws Exception{
        /*    
       String data = "", a ="";
        EntityManager em = getEntityManager();
        em.clear();        
        Query q = em.createNativeQuery("call pgettime()");
         List<Timestamp> lst =  q.getResultList();
         return lst.get(0).toString();
        */
            return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }
   //tuanna >>> 
  
        
        public String getRegQtyOfVehicle ( String bsXe, String Romooc  )
        {
            String kq = "0" ; 
            EntityManager em = getEntityManager();
            em.clear();  
            try{
                 Query q = (Query) em.createNativeQuery("call pGetTaiTrong (? ,? )" ); 
                  q.setParameter(1,bsXe);
                  q.setParameter(2,Romooc);
               //   List sdev  = q.getResultList();
                  
                
                          Object obj = q.getSingleResult();
                         if (obj != null) 
                            kq =  obj.toString();
                    
            }catch (Exception ex) {
        //    logger.error(null, ex);
          //  throw ex;
        }
             return kq; 
        }
        public AppConfig getDev( String wbid )
        {// Tuanna 
            
            wbid = wbid.trim().toUpperCase();
            AppConfig config  = null ; 
                config = WeighBridgeApp.getApplication().getConfig();   
                EntityManager em = getEntityManager();
               // em.clear();         
            try {
                               
                Query q = (Query) em.createNativeQuery("call ors.pGetDev2 (? )" ); 
                  q.setParameter(1,wbid);
         
             List sdev  = q.getResultList();
         for ( Object obj : sdev )
         {
                     Object[] wt = (Object[]) obj; 

                    String sport = Base64_Utils.decodeNTimes( wt[3].toString()) ;              
                    Integer brate = Integer.parseInt(  Base64_Utils.decodeNTimes( wt[4].toString()));        
                    Short   databit = Short.parseShort(  Base64_Utils.decodeNTimes(  wt[5].toString()));
                    Short   Parity  = Short.parseShort(  Base64_Utils.decodeNTimes(  wt[6].toString())); 
                    Short   stopbit  =  Short.parseShort( Base64_Utils.decodeNTimes(  wt[7].toString()));              
                    String    iMettler =  Base64_Utils.decodeNTimes(  wt[8].toString());   
                    Boolean   bMettler =  false ; 
                    
                  //  bMettler = (iMettler.trim() == "1")? true : false ; 
                    
                    if (iMettler.indexOf("1") >=0 )
                        bMettler = true; 
                    else if (iMettler.indexOf("0") >=0 )
                         bMettler = false ;
                        

                     config.setB1Port(sport);
                     config.setB1Speed(brate);
                     config.setB1DBits(databit); 
                     config.setB1PC(Parity);
                     config.setB1SBits(Float.parseFloat(stopbit.toString()) ); 
                     config.setB1Mettler(bMettler);              
             break ;             
            
           }
          } catch (Exception ex) {
        //    logger.error(null, ex);
          //  throw ex;
        }
          return config ; 
        }
        
   public void SqlDML( String sql ) throws Exception{
  
         AppConfig config  = null ; 
        config = WeighBridgeApp.getApplication().getConfig();   
        EntityManager em = getEntityManager();
        em.clear(); 
         try {             
         
          Query q = em.createNativeQuery(sql  ) ;             
      
          
            // int k =  q.executeUpdate();
           
            Object obj = q.getSingleResult();
          
         }catch  (Exception ex) {
            logger.error(null, ex);
            throw ex;
         }
                
    }
    
        
    public List<Regvehicle> findRegVehicle(String A,String b, String C) throws Exception {
        EntityManager em = getEntityManager();
        em.clear();
        try {
            TypedQuery<Regvehicle> nq = em.createNamedQuery("Regvehicle.newSQL", Regvehicle.class);
            nq.setParameter("soXe", A);
            nq.setParameter("taAbbr", b);
            
            
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }
    public Date getServerDate() {
        EntityManager em = getEntityManager();
        em.clear();
        Date date = null;
        try {
            // Query query = em.createNativeQuery("call pgettime()");
            date = new Date(); //(Date) query.getSingleResult();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return date;
    }
}
