/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.DoGetDetailBapi;
import com.gcs.wb.bapi.helper.SAP2Local;
import com.gcs.wb.bapi.helper.structure.DoGetDetailStructure;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.OutboundDetail;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author THANGPT
 */
public class OutboundDeliveryConverter extends AbstractThrowableParamConverter<DoGetDetailBapi, OutboundDelivery, Exception> {

    @Override
    public OutboundDelivery convertHasParameter(DoGetDetailBapi from, String val) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutboundDelivery convertsHasParameter(DoGetDetailBapi from, String val, boolean refresh) throws Exception {
        OutboundDelivery outb = null;
        OutboundDetail outb_details = null;
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
            EntityManager em_check = WeighBridgeApp.getApplication().getEm();
            WeightTicketJpaController con_check = new WeightTicketJpaController();
            List<OutboundDetail> outb_detail_check;
            if (refresh == true) {
                try {
                    outb_detail_check = con_check.findByMandtDelivNumb(val);
                    if (outb_detail_check.size() > 0) {
                        em_check.getTransaction().begin();
                        for (int i = 0; i < outb_detail_check.size(); i++) {
                            em_check.remove(outb_detail_check.get(i));
                        }
                        em_check.getTransaction().commit();
                        em_check.clear();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SAP2Local.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            outb = new OutboundDelivery(val);
            outb.setShipPoint(from.getEs_vstel()); //set shipping point 20120712#01
            for (int i = 0; i < dos.size(); i++) {
                DoGetDetailStructure doItem = dos.get(i);
                try {
                    outb_detail_check = con_check.findByMandtDelivNumbItem(val, doItem.getPosnr().substring(4, 5));
                    if (outb_detail_check.size() > 0) {
                        outb_details = outb_detail_check.get(0);
                    } else {
                        outb_details = new OutboundDetail(val, doItem.getPosnr().substring(4, 5));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SAP2Local.class.getName()).log(Level.SEVERE, null, ex);
                }
                //{-20100212#01 free goods processing
                item_cat = doItem.getPstyv();
                //end set
                if (item_cat.equals("ZTNN")) {
                    outb.setDeliveryItemFree(doItem.getPosnr());
                    outb.setMatnrFree(doItem.getMatnr());
                    //set data cho details free goods
                    if (flag_detail == true) {
                        // outb_details.setDelivItem(doItem.getPosnr().substring(4, 5));
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
                //luu xuong database
                if (flag_detail == true) {
                    EntityManager em = WeighBridgeApp.getApplication().getEm();
                    em.getTransaction().begin();
                    //em.persist(outb_details);
                    em.merge(outb_details);
                    em.getTransaction().commit();
                    em.clear();
                }
                //end
                //chi lay val item dong dau
                if (!item_cat.equals("ZTNN")) {
                    item_qty = item_qty.add(doItem.getLfimg());
                    if (flag) {
                        item_num = doItem.getPosnr();
                        flag = false;
                    }
                }
                outb.setErdat(new java.sql.Date(doItem.getErdat().getTime()));
                outb.setLfart(doItem.getLfart());
                //autlf
                outb.setWadat(new java.sql.Date(doItem.getWadat().getTime()));
                outb.setLddat(new java.sql.Date(doItem.getLddat().getTime()));
                outb.setKodat(new java.sql.Date(doItem.getKodat().getTime()));
                //outb.setShipPoint(doItem.getVstel());
                outb.setLifnr(doItem.getLifnr());
                outb.setKunnr(doItem.getKunnr());
                outb.setKunag(doItem.getKunag());
                outb.setTraty(doItem.getTraty());
                outb.setTraid(doItem.getTraid());
                outb.setBldat(new java.sql.Date(doItem.getBldat().getTime()));
                if (outb.getMatnr() == null || outb.getMatnr().trim().isEmpty()) {
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
                if (item_cat.equals("ZTNN")) {
                    outb.setFreeQty(item_qty_free);
                }
                outb.setLfimg(item_qty);
            }
            //set lai item val thanh val dau tien
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
