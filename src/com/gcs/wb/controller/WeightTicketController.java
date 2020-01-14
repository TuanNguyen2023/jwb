/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.SAP2Local;
import com.gcs.wb.bapi.helper.structure.BatchStocksStructure;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.BatchStocks;
import com.gcs.wb.jpa.entity.BatchStocksPK;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.CustomerPK;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.entity.TimeRange;
import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.WeightTicketPK;
import com.gcs.wb.jpa.procedures.WeightTicketJpaRepository;
import com.gcs.wb.jpa.repositorys.BatchStocksRepository;
import com.gcs.wb.jpa.repositorys.CustomerRepository;
import com.gcs.wb.jpa.repositorys.SignalsRepository;
import com.gcs.wb.jpa.repositorys.TimeRangeRepository;
import com.gcs.wb.jpa.procedures.WeightTicketRepository;
import com.gcs.wb.model.AppConfig;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author THANGPT
 */
public class WeightTicketController {
    
    WeightTicketRepository weightTicketRepository = new WeightTicketRepository();
    BatchStocksRepository batchStocksRepository = new BatchStocksRepository();
    TimeRangeRepository timeRangeRepository = new TimeRangeRepository();
    private AppConfig config = null;
    private SAPSetting sapSetting;
    private User login;
    private String last_value = null;
    private String current_value = null;
    public HashMap hmMsg = new HashMap();
    CustomerRepository customerRepository = new CustomerRepository();
    SignalsRepository noneRepository = new SignalsRepository();
    WeightTicketJpaRepository weightTicketJpaRepository = new WeightTicketJpaRepository();
    EntityManager entityManager = JPAConnector.getInstance();
    String client = WeighBridgeApp.getApplication().getConfig().getsClient();
    
    public DefaultComboBoxModel getCustomerByMaNdt() {
        List kunnr = this.customerRepository.getCustomerByMaNdt(client);
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        result.addElement("");
        for (Object obj : kunnr) {
            Object[] wt = (Object[]) obj;
            CustomerPK custPK = new CustomerPK();
            Customer cust = new Customer();
            custPK.setMandt(WeighBridgeApp.getApplication().getConfig().getsClient());
            custPK.setKunnr(wt[1].toString());
            cust.setCustomerPK(custPK);
            cust.setName1(wt[2].toString());
            cust.setName2(wt[3].toString());
            if (result.getIndexOf(cust) < 0) {
                result.addElement(cust);
            }
        }
        return result;
    }
    
    public TimeRange getTime() {
        return timeRangeRepository.findByMandtWbId(client, WeighBridgeApp.getApplication().getConfig().getWbId());
    }
    
    public List<BatchStocks> getBatchStocks(SLoc selSloc, WeightTicket weightTicket) {
        return batchStocksRepository.getList(config.getsClient(), config.getwPlant(),
                selSloc.getSLocPK().getLgort(), weightTicket.getMatnrRef());
    }
    
    public List<BatchStocksStructure> getBatchStocksStructures(SLoc selSloc, WeightTicket weightTicket) {
        return SAP2Local.getBatchStocks(selSloc.getSLocPK().getLgort(), weightTicket.getMatnrRef());
    }
    
    public void saveBatchStocks(List<BatchStocksStructure> bBatchStocks, List<BatchStocks> batchs, WeightTicket weightTicket) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        for (BatchStocksStructure b : bBatchStocks) {
            BatchStocks bs = new BatchStocks(new BatchStocksPK(config.getsClient(), config.getwPlant(), b.getLgort(), b.getMatnr(), b.getCharg()));
            bs.setLvorm(b.getLvorm() == null || b.getLvorm().trim().isEmpty() ? ' ' : b.getLvorm().charAt(0));
            if (batchs.indexOf(bs) == -1) {
                entityManager.persist(bs);
            } else {
                entityManager.merge(bs);
            }
            String sfix = "-";
            if (batchs.indexOf(bs) == -1 && b.getLgort().equalsIgnoreCase(weightTicket.getLgort())) {
                
                batchs.add(bs);
            }
        }
        entityManager.getTransaction().commit();
        entityManager.clear();
    }
    
    public DefaultComboBoxModel setCbxBatch(List<BatchStocks> batchs) {
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        for (BatchStocks b : batchs) {
            if (b.getLvorm() == null || b.getLvorm().toString().trim().isEmpty()) {
                // Fillter BATCH not contain "-" by Tuanna -10.01.2013 
                if (WeighBridgeApp.getApplication().getConfig().getwPlant().indexOf("1311") >= 0) {
                    result.addElement(b.getBatchStocksPK().getCharg());
                } else if (b.getBatchStocksPK().getCharg().indexOf("-") < 0) {
                    result.addElement(b.getBatchStocksPK().getCharg());
                }
            }
        }
        return result;
    }
    
    public void savePostAgainActionPerformed(WeightTicket weightTicket) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.merge(weightTicket);
        entityManager.getTransaction().commit();
        entityManager.clear();
    }
    
    public void saveKunnrItemStateChanged(WeightTicket weightTicket) {
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.merge(weightTicket);
        entityManager.getTransaction().commit();
    }
    
    public Date setTimeWeightTicket(String[] time) {
        String[] date = time[0].split("/");
        String[] hour = time[1].split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]), Integer.parseInt(hour[0]), Integer.parseInt(hour[1]), Integer.parseInt(hour[2]));
        Date stime = cal.getTime();
        System.out.println(stime);
        return stime;
        
    }
    
    public String readWT(String txt) {
        List wts = weightTicketRepository.getWeighTicketReg(txt);
        
        String kq = null;
        if (wts != null && wts.size() > 0) {
            kq = wts.get(0).toString();
        }
        if (kq != null && kq.length() > 0) {
            txt = kq.trim();
        }
        return txt;
    }
    
    public DefaultComboBoxModel getMaterialList() {
        List<Material> materials = null;

        //get data from DB
        TypedQuery<Material> tMaterial = entityManager.createQuery("SELECT m FROM Material m WHERE m.materialPK.wplant = :wplant order by m.materialPK.matnr asc", Material.class);
        tMaterial.setParameter("wplant", config.getwPlant());
        materials = tMaterial.getResultList();
        //get data from sap and sync DB
        //List<Material> mMaterials = SAP2Local.getLookUpMaterialsList(config.getwPlant());
        List<Material> mMaterials = SAP2Local.getMaterialsList();
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        for (Material mSap : mMaterials) {
            if (materials.indexOf(mSap) == -1) {
                entityManager.persist(mSap);
            } else {
                entityManager.merge(mSap);
            }
        }
        entityManager.getTransaction().commit();
        entityManager.clear();
        materials = tMaterial.getResultList();
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        for (Material m : materials) {
            result.addElement(m);
        }
        return result;
    }
    
    public WeightTicket findWeightTicket(WeightTicket weightTicket, String id, Integer seq) {
        entityManager.clear();
        weightTicket = entityManager.find(WeightTicket.class, new WeightTicketPK(config.getsClient(), config.getwPlant(), id, seq));
        entityManager.clear();
        return weightTicket;
    }
    
    public String getSoNiemXa(String pWtId) {
        return weightTicketRepository.getSoNiemXa(pWtId);
    }
    
    public void getTicketIndex(String maphieu, JTextField txtPONum, WeightTicket weightTicket, JRadioButton rbtPO) {
        String Posto = "";
        List wtxxx = weightTicketRepository.getTicketIndex(maphieu);
        if (wtxxx != null) {
            for (Object obj : wtxxx) {
                Object[] wt = (Object[]) obj;
                Posto = wt[2].toString().trim();
                txtPONum.setText(Posto);
                weightTicket.setAbbr(wt[3].toString().trim());
                rbtPO.setSelected(true);
            }
            
        }
    }
    
}
