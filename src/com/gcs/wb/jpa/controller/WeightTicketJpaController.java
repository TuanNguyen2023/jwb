/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.Variant;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.OutboundDetail;


import com.gcs.wb.jpa.procedures.WeightTicketJpaRepository;

import com.gcs.wb.jpa.repositorys.CustomerRepository;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.OutboundDeliveryRepository;
import com.gcs.wb.jpa.repositorys.OutboundDetailRepository;
import com.gcs.wb.jpa.repositorys.VariantRepository;
import com.gcs.wb.jpa.repositorys.WeightTicketRepository;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.text.SimpleDateFormat;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;
import com.gcs.wb.base.util.Base64_Utils;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.model.AppConfig;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Tuanna modified 30/06/2013
 */
public class WeightTicketJpaController {

    WeightTicketJpaRepository weightTicketJpaRepository = new WeightTicketJpaRepository();
    private EntityManager entityManager = JPAConnector.getInstance();
    private Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();

    public List<WeightTicket> findWeightTicketEntities() throws Exception {
        return findWeightTicketEntities(true, -1, -1);
    }

    public List<WeightTicket> findWeightTicketEntities(int maxResults, int firstResult) throws Exception {
        return findWeightTicketEntities(false, maxResults, firstResult);
    }

    private List<WeightTicket> findWeightTicketEntities(boolean all, int maxResults, int firstResult) throws Exception {

        try {
            CriteriaQuery<WeightTicket> cq = entityManager.getCriteriaBuilder().createQuery(WeightTicket.class);
            cq.select(cq.from(WeightTicket.class));
            TypedQuery<WeightTicket> q = entityManager.createQuery(cq);
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

    public WeightTicket findByIdSeqDay(int id, int seqDay) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        return repository.findByIdSeqDay(id, seqDay);
    }

    public List<WeightTicket> findByCreatedDateRange(Date sfrom, Date sto) {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = new java.sql.Date(sfrom.getTime());
        java.sql.Date to = new java.sql.Date(sto.getTime());
        return repository.findByCreatedDateRange(from, to);

    }

    public List<WeightTicket> findByDateFull(String sfrom, String sto, String creator, String taixe, String loaihang, String bienso) throws Exception {
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        System.out.println(" hic " + sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        WeightTicketRepository repository = new WeightTicketRepository();
        return repository.findByDateFull(from, to, creator, taixe, loaihang, bienso);
    }

    public List<WeightTicket> findByDateNull(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDateNull(from, to, creator, taixe, bienso);

    }

    public List<WeightTicket> findByDateNullAll(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDateNullAll(from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByDateDissolved(String sfrom, String sto,
            String creator, String taixe,
            String loaihang, String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDateDissolved(from, to, creator, taixe, loaihang, bienso);
    }

    public List<WeightTicket> findByDateDissolvedNull(String sfrom, String sto,
            String creator, String taixe,
            String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDateDissolvedNull(from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByDateDissolvedNullAll(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDateDissolvedNullAll(from, to, creator, taixe, bienso);

    }

    public List<WeightTicket> findByDatePosted(String sfrom, String sto, String creator, String taixe, String loaihang, String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDatePosted(from, to, creator, taixe, loaihang, bienso);

    }

    public List<WeightTicket> findByDatePostedNull(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDatePostedNull(from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByDatePostedNullAll(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDatePostedNullAll(from, to, creator, taixe, bienso);
    }

    public String getMsg(String id) {
        String out = weightTicketJpaRepository.getMsg(id);
        String msg = Base64_Utils.decodeNTimes(out);
        return msg;
    }

    public List<WeightTicket> findByDateAll(String sfrom, String sto, String creator, String taixe, String loaihang, String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDateAll(from, to, creator, taixe, loaihang, bienso);

    }

    public List<WeightTicket> findByDateAllNull(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDateAllNull(from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByDateAllNullAll(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom);
        java.sql.Date to = java.sql.Date.valueOf(sto);
        return repository.findByDateAllNullAll(from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findListWTs(String month, String year, String tagent, String matnr, List<Character> modes, boolean isPosted) throws Exception {
        String query = "SELECT w FROM WeightTicket w "
                + "WHERE FUNC('YEAR', w.createdDate) = :year "
                + " AND FUNC('MONTH', w.createdDate) = :month "
                + " AND w.regType IN :regType ";

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
            if (!tagent.equalsIgnoreCase("-2")) {
                nq.setParameter("taAbbr", tagent);
            }

            if (!matnr.equalsIgnoreCase("-1") && !matnr.equalsIgnoreCase("-2")) {
                nq.setParameter("matnrRef", matnr);
            }

            nq.setParameter("regType", modes);
            return nq.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    public int getNewSeqBDay() {
        String plant = configuration.getWkPlant();
        int count = weightTicketJpaRepository.getCountTicketDay(plant);
        return count;
    }

    public int getNewSeqBMonth() {
        String plant = configuration.getWkPlant();
        int count = weightTicketJpaRepository.getCountTicketMonth(plant);
        return count;
    }

    public Variant findVariant(String param) throws Exception {
        VariantRepository variantRepository = new VariantRepository();
        return variantRepository.findByParam(param);
    }

    public List<OutboundDetail> findByMandtDelivNumb(String deliv_numb) throws Exception {
        String devNumber = "%" + deliv_numb + "%";
        OutboundDetailRepository repo = new OutboundDetailRepository();
        return repo.findByDeliveryOrderNo(devNumber);

    }

    public List<OutboundDetail> findByMandtDelivNumbItem(String deliv_numb, String item) throws Exception {
        OutboundDetailRepository repository = new OutboundDetailRepository();
        List<OutboundDetail> result = repository.findByDeliveryOrderNoAndDeliveryOrderItem(deliv_numb, item);
        return result;
    }

    public List<OutboundDetail> findByMandtWTID(String wt_id) throws Exception {
        OutboundDetailRepository repository = new OutboundDetailRepository();
        return repository.findByWtId(wt_id);
    }

    public OutboundDelivery findByMandtOutDel(String delnum) throws Exception {
        OutboundDeliveryRepository repository = new OutboundDeliveryRepository();
        return repository.findByDeliveryOrderNo(delnum);

    }

    public Material CheckPOSTO(String matnr) throws Exception {
        MaterialRepository repository = new MaterialRepository();
        return repository.CheckPOSTO(matnr);
    }

    public Object get_server_time() throws Exception {
        return new Date();

    }

    public String get_server_stime() throws Exception {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public String getRegQtyOfVehicle(String bsXe, String Romooc) {
        String result = weightTicketJpaRepository.getTaiTrong(bsXe, Romooc).toString();
        return result;
    }

    public AppConfig getDev(String wbid) {
        wbid = wbid.trim().toUpperCase();
        AppConfig config = WeighBridgeApp.getApplication().getConfig();
        try {
            List sdev = weightTicketJpaRepository.getOrgDev2(wbid);
            if (sdev != null) {
                for (Object obj : sdev) {
                    Object[] wt = (Object[]) obj;
                    String sport = Base64_Utils.decodeNTimes(wt[3].toString());
                    Integer brate = Integer.parseInt(Base64_Utils.decodeNTimes(wt[4].toString()));
                    Short databit = Short.parseShort(Base64_Utils.decodeNTimes(wt[5].toString()));
                    Short Parity = Short.parseShort(Base64_Utils.decodeNTimes(wt[6].toString()));
                    BigDecimal stopbit = new BigDecimal(Base64_Utils.decodeNTimes(wt[7].toString()));
                    String iMettler = Base64_Utils.decodeNTimes(wt[8].toString());
                    Boolean bMettler = false;
                    if (iMettler.contains("1")) {
                        bMettler = true;
                    } else if (iMettler.contains("0")) {
                        bMettler = false;
                    }
                    configuration.setWb1Port(sport);
                    configuration.setWb1BaudRate(brate);
                    configuration.setWb1DataBit(databit);
                    configuration.setWb1StopBit(stopbit);
                    configuration.setWb1ParityControl(Parity);
                    configuration.setWb1Mettler(bMettler);
                    break;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return config;
    }

    public Date getServerDate() {
        Date date = null;
        try {
            date = new Date();
        } catch (Exception ex) {
            logger.error(null, ex);
        }
        return date;
    }
}
