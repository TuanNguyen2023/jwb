/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.bapi.helper.DoGetDetailBapi;
import com.gcs.wb.bapi.helper.structure.DoGetDetailStructure;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.OutboundDeliveryDetail;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author THANGPT
 */
public class OutboundDeliveryConverter extends AbstractThrowableParamConverter<DoGetDetailBapi, OutboundDelivery, Exception> {

    EntityManager entityManager = JPAConnector.getInstance();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    
    @Override
    public OutboundDelivery convertHasParameter(DoGetDetailBapi from, String val) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutboundDelivery convertsHasParameter(DoGetDetailBapi from, String val, boolean refresh) throws Exception {
        OutboundDelivery outb = null;
        OutboundDeliveryDetail outb_details = null;
        String item_cat = "";
        String item_num = null;
        String item_num_free = null;
        boolean flag_free = true;
        boolean flag = true;
        boolean flag_detail = true;
        BigDecimal item_qty = BigDecimal.ZERO;
        BigDecimal item_qty_free = BigDecimal.ZERO;
        List<DoGetDetailStructure> dos = from.getTd_dos();
        if (dos.size() > 0) {
            // <editor-fold defaultstate="collapsed" desc="Fill D.O Data">
            //check do detail exist
            entityTransaction = entityManager.getTransaction();
            WeightTicketJpaController con_check = new WeightTicketJpaController();
            List<OutboundDeliveryDetail> outb_detail_check;
            if (refresh == true) {
                try {
                    outb_detail_check = con_check.findByMandtDelivNumb(val);
                    if (outb_detail_check.size() > 0) {
                        entityTransaction.begin();
                        
                        for (int i = 0; i < outb_detail_check.size(); i++) {
                            entityManager.remove(outb_detail_check.get(i));
                        }
                        entityTransaction.commit();
                        entityManager.clear();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SAPService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //end check
            outb = new OutboundDelivery(val);
            outb.setShipPoint(from.getEs_vstel());
            for (int i = 0; i < dos.size(); i++) {
                DoGetDetailStructure doItem = dos.get(i);
                try {
                    outb_detail_check = con_check.findByMandtDelivNumbItem(val, doItem.getPosnr().substring(4, 5));
                    if (outb_detail_check.size() > 0) {
                        outb_details = outb_detail_check.get(0);
                    } else {
                        outb_details = new OutboundDeliveryDetail(val, doItem.getPosnr().substring(4, 5));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SAPService.class.getName()).log(Level.SEVERE, null, ex);
                }
                // free goods processing
                item_cat = doItem.getPstyv();
                //set DO number cho details
                if (flag_detail == true) {
                }
                //end set
                if (item_cat.equals("ZTNN")) {
                    outb.setDeliveryItemFree(doItem.getPosnr());
                    outb.setMatnrFree(doItem.getMatnr());
                    //set data cho details free goods
                    if (flag_detail == true) {
                        outb_details.setFreeItem('X');
                        outb_details.setLfimg(doItem.getLfimg());
                        outb_details.setMeins(doItem.getMeins());
                        String split[] = doItem.getArktx().split("-");
                        outb_details.setArktx(split[0].toString());
                        outb_details.setMatnr(doItem.getMatnr());
                        outb_details.setVgbel(doItem.getVgbel());
                        outb_details.setBzirk(from.getEs_bzirk());
                        outb_details.setBztxt(from.getEs_text());
                    }
                    //end set data
                    if (flag_free) {
                        item_num_free = doItem.getPosnr();
                        flag_free = false;
                    }
                    item_qty_free = item_qty_free.add(doItem.getLfimg());
//                    continue;
                }

                outb.setDeliveryItem(doItem.getPosnr()); //Get position
                //set data out details hang thuong
                if (flag_detail == true) {
                    outb_details.setLfimg(doItem.getLfimg());

                    if ((!outb_details.isPosted())
                            || (outb_details.getLfimg() == null)
                            || (outb_details.getLfimg().equals(BigDecimal.ZERO))) {
                        outb_details.setLfimg(doItem.getLfimg());
                    }
                    outb_details.setMeins(doItem.getMeins());
                    String split[] = doItem.getArktx().split("-");
                    outb_details.setArktx(split[0].toString());
                    outb_details.setMatnr(doItem.getMatnr());
                    outb_details.setVgbel(doItem.getVgbel());
                    outb_details.setBzirk(from.getEs_bzirk());
                    outb_details.setBztxt(from.getEs_text());
                }
                //end set data
                //save database
                if (flag_detail == true) {
                    entityTransaction = entityManager.getTransaction();
                    entityTransaction.begin();
                    entityManager.merge(outb_details);
                    entityTransaction.commit();
                    entityManager.clear();
                }
                //end
                //only get number item dong dau
                if (!item_cat.equals("ZTNN")) {
                    item_qty = item_qty.add(doItem.getLfimg());
                    if (flag) {
                        item_num = doItem.getPosnr();
                        flag = false;
                    }
                }

                outb.setErdat(new java.sql.Date(doItem.getErdat().getTime()));
                outb.setLfart(doItem.getLfart());

                outb.setWadat(new java.sql.Date(doItem.getWadat().getTime()));
                outb.setLddat(new java.sql.Date(doItem.getLddat().getTime()));
                outb.setKodat(new java.sql.Date(doItem.getKodat().getTime()));
                outb.setLifnr(doItem.getLifnr());
                outb.setKunnr(doItem.getKunnr());
                outb.setKunag(doItem.getKunag());
                outb.setTraty(doItem.getTraty());
                outb.setTraid(doItem.getTraid());

                outb.setBldat(new java.sql.Date(doItem.getBldat().getTime()));
                if(outb.getMatnr() == null || outb.getMatnr().trim().isEmpty()) {
                    outb.setMatnr(doItem.getMatnr());
                }
                outb.setWerks(doItem.getWerks());
                outb.setLgort(doItem.getLgort());
                outb.setCharg(doItem.getCharg());
                outb.setLichn(doItem.getLichn());
                outb.setMeins(doItem.getMeins());
                outb.setVrkme(doItem.getVrkme());
                outb.setUebtk(doItem.getUebtk() == null || doItem.getUebtk().trim().isEmpty() ? ' ' : 'X');
                outb.setUebto(doItem.getUebto());
                outb.setUntto(doItem.getUntto());
                outb.setArktx(doItem.getArktx());
                outb.setVgbel(doItem.getVgbel());
                outb.setVgpos(doItem.getVgpos());
                outb.setBwart(doItem.getBwart());
                outb.setBwtar(doItem.getBwtar());
                if (outb.getBwtar() == null || outb.getBwtar().trim().isEmpty()) {
                    outb.setBwtar("PURC");
                }
                outb.setRecvPlant(doItem.getRecv_plant());
                outb.setKoquk(doItem.getKoquk() == null || doItem.getKoquk().trim().isEmpty() || doItem.getKoquk().trim().charAt(0) != 'C' ? ' ' : 'X');
                outb.setKostk(doItem.getKostk() == null || doItem.getKostk().trim().isEmpty() || doItem.getKostk().trim().charAt(0) != 'C' ? ' ' : 'X');
                outb.setWbstk(doItem.getWbstk() == null || doItem.getWbstk().trim().isEmpty() || doItem.getWbstk().trim().charAt(0) != 'C' ? ' ' : 'X');
//            }
                if (item_cat.equals("ZTNN")) {
                    outb.setFreeQty(item_qty_free);
                }
                outb.setLfimg(item_qty);
            }
            //set lai item number thanh number dau tien

            if (outb.getDeliveryItem() != null) {
                if (!outb.getDeliveryItem().equals(item_num)) {
                    outb.setDeliveryItem(item_num);
                }
            }
            if (item_num_free != null) {

                if (!outb.getDeliveryItemFree().equals(item_num_free)) {
                    outb.setDeliveryItemFree(item_num_free);
                }
            }
            //th chi co hang free goods

            if (outb.getDeliveryItem() == null) {
                outb.setDeliveryItem(outb.getDeliveryItemFree());
                outb.setLfimg(outb.getFreeQty());
                outb.setDeliveryItemFree(null);
                outb.setFreeQty(null);
            }

        }
        return outb;
    }
}
