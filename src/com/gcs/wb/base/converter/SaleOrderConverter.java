/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.bapi.helper.structure.SalesOrderStructure;
import com.gcs.wb.jpa.entity.SaleOrder;

/**
 *
 * @author THANGHL
 */
public class SaleOrderConverter extends AbstractThrowableConverter<SalesOrderStructure, SaleOrder, Exception>{
    @Override
    public SaleOrder convert(SalesOrderStructure from){
        SaleOrder to = null;
        if (from != null && (from.getVbeln() != null && !from.getVbeln().trim().isEmpty())) {
            to = new SaleOrder();
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
        }
        return to;
    }
}
