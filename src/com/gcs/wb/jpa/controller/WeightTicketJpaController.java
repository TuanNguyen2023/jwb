/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.OutbDel;
import com.gcs.wb.jpa.entity.Variant;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.WeightTicketPK;
import com.gcs.wb.jpa.entity.OutbDetailsV2;
import com.gcs.wb.jpa.entity.UserLocal;


import com.gcs.wb.jpa.procedures.WeightTicketJpaRepository;

import com.gcs.wb.jpa.repositorys.CustomerRepository;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.OutbDelRepository;
import com.gcs.wb.jpa.repositorys.OutbDetailsV2Repository;
import com.gcs.wb.jpa.repositorys.UserLocalRepository;
import com.gcs.wb.jpa.repositorys.VariantRepository;
import com.gcs.wb.jpa.repositorys.WeightTicketRepository;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.text.SimpleDateFormat;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;
 import com.gcs.wb.utils.Base64_Utils; 
import com.gcs.wb.model.AppConfig; 
import  java.util.*;
/**
 *
 * Tuanna modified 30/06/2013
 */
public class WeightTicketJpaController {

    WeightTicketJpaRepository weightTicketJpaRepository = new WeightTicketJpaRepository();
    private EntityManager entityManager = JPAConnector.getInstance();
    private Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

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

    public WeightTicket findWeightTicket(WeightTicketPK id) throws Exception {

        try {
            return entityManager.find(WeightTicket.class, id);
        } catch (Exception ex) {
            logger.error(id, ex);
            throw ex;
        }
    }

    public List<WeightTicket> findByCreateDateRange(Date sfrom, Date sto) {
        WeightTicketRepository repository = new WeightTicketRepository();
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        java.sql.Date from = new java.sql.Date(sfrom.getTime());
        java.sql.Date to = new java.sql.Date(sto.getTime());
        return repository.findByCreateDateRange(client, plant, from, to);

    }

    public List<WeightTicket> findByMandtWPlantDateFull(String sfrom, String sto, String creator, String taixe, String loaihang, String bienso) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        System.out.println(" hic " + sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        WeightTicketRepository repository = new WeightTicketRepository();
        return repository.findByMandtWPlantDateFull(client, plant, from, to, creator, taixe, loaihang, bienso);
    }

    public List<WeightTicket> findByMandtWPlantDateNull(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDateNull(client, plant, from, to, creator, taixe, bienso);

    }

    public List<WeightTicket> findByMandtWPlantDateNullAll(String  sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDateNullAll(client, plant, from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByMandtWPlantDateDissolved(String  sfrom, String sto,
            String creator, String taixe,
            String loaihang, String bienso) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDateDissolved(client, plant, from, to, creator, taixe, loaihang, bienso);
    }

    public List<WeightTicket> findByMandtWPlantDateDissolvedNull(String sfrom, String sto,
            String creator, String taixe,
            String bienso) throws Exception {

        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDateDissolvedNull(client, plant, from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByMandtWPlantDateDissolvedNullAll(String  sfrom, String sto, String creator, String taixe, String bienso) throws Exception {

        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDateDissolvedNullAll(client, plant, from, to, creator, taixe, bienso);

    }

    public List<WeightTicket> findByMandtWPlantDatePosted(String sfrom, String sto, String creator, String taixe, String loaihang, String bienso) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDatePosted(client, plant, from, to, creator, taixe, loaihang, bienso);

    }

    public List<WeightTicket> findByMandtWPlantDatePostedNull(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {

        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDatePostedNull(client, plant, from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByMandtWPlantDatePostedNullAll(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDatePostedNullAll(client, plant, from, to, creator, taixe, bienso);
    }

    public String getMsg(String id) {
        String out = weightTicketJpaRepository.getMsg(id);
        String msg = Base64_Utils.decodeNTimes(out);
        return msg;
    }

    public List<WeightTicket> findByMandtWPlantDateAll(String sfrom, String sto, String creator, String taixe, String loaihang, String bienso) throws Exception {

        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDateAll(client, plant, from, to, creator, taixe, loaihang, bienso);

    }

    public List<WeightTicket> findByMandtWPlantDateAllNull(String sfrom, String sto, String creator, String taixe, String bienso) throws Exception {

        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDateAllNull(client, plant, from, to, creator, taixe, bienso);
    }

    public List<WeightTicket> findByMandtWPlantDateAllNullAll(String sfrom,String sto, String creator, String taixe, String bienso) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        WeightTicketRepository repository = new WeightTicketRepository();
        java.sql.Date from = java.sql.Date.valueOf(sfrom.toString());
        java.sql.Date to = java.sql.Date.valueOf(sto.toString());
        return repository.findByMandtWPlantDateAllNullAll(client, plant, from, to, creator, taixe, bienso);
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
        try {
            TypedQuery<WeightTicket> nq = entityManager.createNamedQuery(name_query, WeightTicket.class);
            nq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
            nq.setParameter("wPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
            nq.setParameter("id", id);
            // TODO: comment to test new database
//            nq.setParameter("taAbbr", tagent);
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
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        int count = weightTicketJpaRepository.getCountTicketDay(plant);
        return count;
    }

    public int getNewSeqBMonth() {
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        int count = weightTicketJpaRepository.getCountTicketMonth(plant);
        return count;
    }

    public List<Variant> findVariant(String param) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        VariantRepository variantRepository = new VariantRepository();
        return variantRepository.findVariant(param, client, plant);
    }
    
    public List<OutbDetailsV2> findByMandtDelivNumb(String deliv_numb) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String devNumber = "%" + deliv_numb + "%";
        OutbDetailsV2Repository repo = new OutbDetailsV2Repository();
        return repo.findByMandtDelivNumb(client, devNumber);

    }

    public List<OutbDetailsV2> findByMandtDelivNumbItem(String deliv_numb, String item) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        OutbDetailsV2Repository repository = new OutbDetailsV2Repository();
        List<OutbDetailsV2> result = repository.findByMandtDelivNumbItem(client, deliv_numb, item);
        return result;
    }
    
    public List<OutbDetailsV2> findByMandtWTID(String wt_id) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        OutbDetailsV2Repository repository = new OutbDetailsV2Repository();
        return repository.findByMandtWTID(client, wt_id);
    }
    
    public OutbDel findByMandtOutDel(String delnum) throws Exception {

        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        OutbDelRepository repository = new OutbDelRepository();
        return repository.findByMandtOutDel(client, delnum);

    }
    
    public List<Customer> findCustByMandt(String mandt) throws Exception {
        CustomerRepository customerRepository = new CustomerRepository();
        return customerRepository.findCustByMandt(mandt);


    }

    public UserLocal login(String username, String password) {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String plant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        UserLocalRepository repository = new UserLocalRepository();
        UserLocal user = repository.login(client, plant, username, password);
        return user;
    }

    public Material CheckPOSTO(String matnr) throws Exception {
        String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        String wplant = WeighBridgeApp.getApplication().getConfig().getwPlant();
        Material material = new Material();
        MaterialRepository repository = new MaterialRepository();
        material = repository.CheckPOSTO(client, wplant, matnr);
        return material;
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
        AppConfig config = null;
        config = WeighBridgeApp.getApplication().getConfig();
        try {
            List sdev = weightTicketJpaRepository.getOrgDev2(wbid);
            if (sdev != null) {
                for (Object obj : sdev) {
                    Object[] wt = (Object[]) obj;
                    String sport = Base64_Utils.decodeNTimes(wt[3].toString());
                    Integer brate = Integer.parseInt(Base64_Utils.decodeNTimes(wt[4].toString()));
                    Short databit = Short.parseShort(Base64_Utils.decodeNTimes(wt[5].toString()));
                    Short Parity = Short.parseShort(Base64_Utils.decodeNTimes(wt[6].toString()));
                    Short stopbit = Short.parseShort(Base64_Utils.decodeNTimes(wt[7].toString()));
                    String iMettler = Base64_Utils.decodeNTimes(wt[8].toString());
                    Boolean bMettler = false;
                    if (iMettler.indexOf("1") >= 0) {
                        bMettler = true;
                    } else if (iMettler.indexOf("0") >= 0) {
                        bMettler = false;
                    }
                    config.setB1Port(sport);
                    config.setB1Speed(brate);
                    config.setB1DBits(databit);
                    config.setB1PC(Parity);
                    config.setB1SBits(Float.parseFloat(stopbit.toString()));
                    config.setB1Mettler(bMettler);
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
