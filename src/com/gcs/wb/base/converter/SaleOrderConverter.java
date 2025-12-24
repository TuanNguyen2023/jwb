/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.SalesOrderStructure;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.SaleOrder;

/**
 *
 * @author THANGHL
 */
public class SaleOrderConverter extends AbstractThrowableConverter<SalesOrderStructure, SaleOrder, Exception>{
    @Override
    public SaleOrder convert(SalesOrderStructure from){
        Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
        SaleOrder to = null;
        if (from != null && (from.getVbeln() != null && !from.getVbeln().trim().isEmpty())) {
            to = new SaleOrder();
            to.setMandt(configuration.getSapClient());
            to.setWplant(from.getWerks());
            to.setSoNumber(from.getVbeln());
            to.setMatnr(from.getMatnr());
            to.setMaktx(from.getMaktx());
            to.setKunnr(from.getKunnr());
            to.setKwmeng(from.getKwmeng());
            to.setFreeQuantity(from.getFreeQua());
            to.setRecQuantity(from.getRecQua());
            to.setwName(from.getWName());
            to.setShipToName(from.getShipToName());
            to.setNote(from.getNote());
            to.setVsbed(from.getVsbed());
            to.setZkvgr1(from.getZkvgr1());
            to.setZkvgr1Text(from.getZkvgr1Text());
            to.setZkvgr2(from.getZkvgr2());
            to.setZkvgr2Text(from.getZkvgr2Text());
            to.setZkvgr3(from.getZkvgr3());
            to.setZkvgr3Text(from.getZkvgr3Text());
            to.setSort1(from.getSort1());
            to.setChanged(from.getChanged());
            to.setTraid(from.getTraid());
        }
        return to;
    }
}
