/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.DoGetDetailBapi;
import com.gcs.wb.bapi.helper.structure.DoGetDetailStructure;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.util.StringUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.OutboundDeliveryDetail;
import com.gcs.wb.jpa.repositorys.OutboundDetailRepository;
import com.gcs.wb.model.AppConfig;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();

    @Override
    public OutboundDelivery convertHasParameter(DoGetDetailBapi from, String val) throws Exception {
        OutboundDelivery outboundDelivery = null;
        OutboundDeliveryDetail outboundDeliveryDetail = null;
        String item_cat = "";
        String item_num = null;
        String item_num_free = null;
        boolean flag_free = true;
        boolean flag = true;
        BigDecimal item_qty = BigDecimal.ZERO;
        BigDecimal item_qty_free = BigDecimal.ZERO;
        BigDecimal sumLfimg = BigDecimal.ZERO;
        Date dateNow = new Date();
        // get create time
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("yyyy");
        formatter.applyPattern("HH:mm:ss");
        String createdTime = formatter.format(dateNow);
        
        List<DoGetDetailStructure> dos = from.getTd_dos();
        if (dos.size() > 0) {
            // <editor-fold defaultstate="collapsed" desc="Fill D.O Data">
            
            sumLfimg = BigDecimal.ZERO;
            //check do detail exist
            entityTransaction = entityManager.getTransaction();
            List<OutboundDeliveryDetail> outb_detail_check;
            //end check
            outboundDelivery = new OutboundDelivery(val);
            outboundDelivery.setMandt(configuration.getSapClient());
            outboundDelivery.setShipPoint(from.getEs_vstel());
            
            for (int i = 0; i < dos.size(); i++) {
                DoGetDetailStructure doItem = dos.get(i);
                try {
                    outb_detail_check = findByMandtDelivNumbItem(val, doItem.getPosnr().substring(4, 5));
                    if (outb_detail_check.size() > 0) {
                        outboundDeliveryDetail = outb_detail_check.get(0);
                        outboundDelivery.setId(outboundDeliveryDetail.getOutboundDelivery().getId());
                    } else {
                        outboundDeliveryDetail = new OutboundDeliveryDetail(val, doItem.getPosnr().substring(4, 5));
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SAPService.class.getName()).log(Level.SEVERE, null, ex);
                }

                // free goods processing
                item_cat = doItem.getPstyv();
                if (item_cat.equals("ZVNN")) {
                    outboundDelivery.setDeliveryItemFree(doItem.getPosnr());
                    outboundDelivery.setMatnrFree(doItem.getMatnr());
                    //set data cho details free goods
                    outboundDelivery.setFreeQty(doItem.getLfimg());
                    outboundDeliveryDetail.setFreeItem('X');
                    outboundDeliveryDetail.setLfimg(doItem.getLfimg());
                    outboundDeliveryDetail.setMeins(doItem.getMeins());
                    String split[] = doItem.getArktx().split("-");
                    outboundDeliveryDetail.setArktx(split[0].toString());
                    outboundDeliveryDetail.setMatnr(doItem.getMatnr());
                    outboundDeliveryDetail.setVgbel(doItem.getVgbel());
                    outboundDeliveryDetail.setBzirk(from.getEs_bzirk());
                    outboundDeliveryDetail.setBztxt(from.getEs_text());
                    //end set data
                    if (flag_free) {
                        item_num_free = doItem.getPosnr();
                        flag_free = false;
                    }
                    item_qty_free = item_qty_free.add(doItem.getLfimg());

                    //sumLfimg = doItem.getLfimg();
                }
                sumLfimg = sumLfimg.add(doItem.getLfimg());

                outboundDelivery.setDeliveryItem(doItem.getPosnr()); //Get position
                //set data out details hang thuong
                outboundDeliveryDetail.setLfimg(doItem.getLfimg());
                outboundDeliveryDetail.setShipTo(doItem.getKunnr());

                if ((!outboundDeliveryDetail.isPosted())
                        || (outboundDeliveryDetail.getLfimg() == null)
                        || (outboundDeliveryDetail.getLfimg().equals(BigDecimal.ZERO))) {
                    outboundDeliveryDetail.setLfimg(doItem.getLfimg());
                }
                outboundDeliveryDetail.setMeins(doItem.getMeins());
                String split[] = doItem.getArktx().split("-");
                outboundDeliveryDetail.setArktx(split[0]);
                outboundDeliveryDetail.setMatnr(doItem.getMatnr());
                outboundDeliveryDetail.setVgbel(doItem.getVgbel());
                outboundDeliveryDetail.setBzirk(from.getEs_bzirk());
                outboundDeliveryDetail.setBztxt(from.getEs_text());
                outboundDeliveryDetail.setMandt(configuration.getSapClient());
                outboundDeliveryDetail.setCreatedDate(dateNow);
                outboundDeliveryDetail.setVsbed(doItem.getVsbed());
                outboundDeliveryDetail.setZkvgr1(doItem.getZkvgr1());
                outboundDeliveryDetail.setZkvgr1Text(doItem.getZkvgr1Text());
                outboundDeliveryDetail.setZkvgr2(doItem.getZkvgr2());
                outboundDeliveryDetail.setZkvgr2Text(doItem.getZkvgr2Text());
                outboundDeliveryDetail.setZkvgr3(doItem.getZkvgr3());
                outboundDeliveryDetail.setZkvgr3Text(doItem.getZkvgr3Text());
                outboundDeliveryDetail.setSort1(doItem.getSort1());
                outboundDeliveryDetail.setFscale(doItem.getFscale());
                outboundDeliveryDetail.setSscale(doItem.getSscale());
                outboundDeliveryDetail.setSscale(doItem.getSscale());
                if(StringUtil.isNotEmptyString(doItem.getPoNumber())) {
                    outboundDeliveryDetail.setPoNumber(doItem.getPoNumber());
                }
                outboundDeliveryDetail.setCVendor(doItem.getCVendor());
                outboundDeliveryDetail.setTVendor(doItem.getTVendor());
                outboundDeliveryDetail.setZsling(StringUtil.isNotEmptyString(doItem.getZSling()) ? Integer.parseInt(doItem.getZSling()) : 0);
                outboundDeliveryDetail.setZPallet(StringUtil.isNotEmptyString(doItem.getZPallet()) ? Integer.parseInt(doItem.getZPallet()) : 0);

                outboundDeliveryDetail.setCreatedTime(createdTime);
                outboundDelivery.addOutboundDeliveryDetail(outboundDeliveryDetail);
                //end set data

                //only get number item dong dau
                if (!item_cat.equals("ZVNN")) {
                    item_qty = item_qty.add(doItem.getLfimg());
                    if (flag) {
                        item_num = doItem.getPosnr();
                        flag = false;
                    }
                }

                outboundDelivery.setErdat(doItem.getErdat());
                outboundDelivery.setLfart(doItem.getLfart());

                outboundDelivery.setWadat(doItem.getWadat());
                outboundDelivery.setLddat(doItem.getLddat());
                outboundDelivery.setKodat(doItem.getKodat());
                outboundDelivery.setLifnr(doItem.getLifnr());
                outboundDelivery.setKunnr(doItem.getKunnr());
                outboundDelivery.setKunag(doItem.getKunag());
                outboundDelivery.setTraty(doItem.getTraty());
                outboundDelivery.setTraid(doItem.getTraid());

                outboundDelivery.setBldat(doItem.getBldat());
                if (outboundDelivery.getMatnr() == null || outboundDelivery.getMatnr().trim().isEmpty()) {
                    outboundDelivery.setMatnr(doItem.getMatnr());
                    outboundDelivery.setArktx(doItem.getArktx());
                }
                outboundDelivery.setWerks(doItem.getWerks());
                outboundDelivery.setLgort(doItem.getLgort());
                outboundDelivery.setCharg(doItem.getCharg());
                outboundDelivery.setLichn(doItem.getLichn());
                outboundDelivery.setMeins(doItem.getMeins());
                outboundDelivery.setVrkme(doItem.getVrkme());
                outboundDelivery.setUebtk(doItem.getUebtk() == null || doItem.getUebtk().trim().isEmpty() ? ' ' : 'X');
                outboundDelivery.setUebto(doItem.getUebto());
                outboundDelivery.setUntto(doItem.getUntto());
                outboundDelivery.setVgbel(doItem.getVgbel());
                outboundDelivery.setVgpos(doItem.getVgpos());
                outboundDelivery.setBwart(doItem.getBwart());
                outboundDelivery.setBwtar(doItem.getBwtar());
                if (outboundDelivery.getBwtar() == null || outboundDelivery.getBwtar().trim().isEmpty()) {
                    outboundDelivery.setBwtar("PURC");
                }
                outboundDelivery.setRecvPlant(doItem.getRecv_plant());
                outboundDelivery.setKoquk(doItem.getKoquk() == null || doItem.getKoquk().trim().isEmpty() || doItem.getKoquk().trim().charAt(0) != 'C' ? ' ' : 'X');
                outboundDelivery.setKostk(doItem.getKostk() == null || doItem.getKostk().trim().isEmpty() || doItem.getKostk().trim().charAt(0) != 'C' ? ' ' : 'X');
                outboundDelivery.setWbstk(doItem.getWbstk() == null || doItem.getWbstk().trim().isEmpty() || doItem.getWbstk().trim().charAt(0) != 'C' ? ' ' : 'X');
//            }
                if (item_cat.equals("ZVNN")) {
                    outboundDelivery.setFreeQty(item_qty_free);
                }
                //outboundDelivery.setLfimg(item_qty);
                outboundDelivery.setLfimg(sumLfimg);
                outboundDelivery.setVbelnNach(doItem.getVbelnNach());
                outboundDelivery.setWtIdRef(doItem.getWtIdRef());
                outboundDelivery.setCreatedDate(dateNow);
                outboundDelivery.setCreatedTime(createdTime);
            }
            //set lai item number thanh number dau tien

            if (outboundDelivery.getDeliveryItem() != null) {
                if (!outboundDelivery.getDeliveryItem().equals(item_num)) {
                    outboundDelivery.setDeliveryItem(item_num);
                }
            }
            if (item_num_free != null) {

                if (!outboundDelivery.getDeliveryItemFree().equals(item_num_free)) {
                    outboundDelivery.setDeliveryItemFree(item_num_free);
                }
            }
            //th chi co hang free goods
            if (outboundDelivery.getDeliveryItem() == null) {
                outboundDelivery.setDeliveryItem(outboundDelivery.getDeliveryItemFree());
                outboundDelivery.setLfimg(outboundDelivery.getFreeQty());
                outboundDelivery.setDeliveryItemFree(null);
            }
        }

        return outboundDelivery;
    }

    @Override
    public OutboundDelivery convertsHasParameter(DoGetDetailBapi from, String val, boolean refresh) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<OutboundDeliveryDetail> findByMandtDelivNumbItem(String deliv_numb, String item) throws Exception {
        OutboundDetailRepository repository = new OutboundDetailRepository();
        List<OutboundDeliveryDetail> result = repository.findByDeliveryOrderNoAndDeliveryOrderItem(deliv_numb, item);
        return result;
    }

    public List<OutboundDeliveryDetail> findByMandtDelivNumb(String deliv_numb) throws Exception {
        String devNumber = "%" + deliv_numb + "%";
        OutboundDetailRepository repo = new OutboundDetailRepository();
        return repo.findByDeliveryOrderNo(devNumber);
    }

}
