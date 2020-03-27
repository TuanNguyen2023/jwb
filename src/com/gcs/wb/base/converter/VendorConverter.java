/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.VendorGetDetailStructure;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Vendor;

/**
 *
 * @author THANGPT
 */
public class VendorConverter extends AbstractThrowableConverter<VendorGetDetailStructure, Vendor, Exception>{
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    @Override
    public Vendor convert(VendorGetDetailStructure from){
        Vendor to = null;
        if (from != null && (from.getLifnr() != null && !from.getLifnr().trim().isEmpty())) {
            to = new Vendor();
            to.setMandt(from.getMandt());
            to.setWplant(configuration.getWkPlant());
            to.setLifnr(from.getLifnr().trim());
            to.setName1(from.getName1());
            to.setName2(from.getName2());
            to.setGroupType(Constants.GroupType.CUSTOMER);
        }
        return to;
    }
}