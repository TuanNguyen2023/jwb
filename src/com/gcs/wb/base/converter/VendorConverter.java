/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.bapi.helper.structure.VendorGetDetailStructure;
import com.gcs.wb.jpa.entity.Vendor;

/**
 *
 * @author THANGPT
 */
public class VendorConverter extends AbstractThrowableConverter<VendorGetDetailStructure, Vendor, Exception>{
    
    @Override
    public Vendor convert(VendorGetDetailStructure from){
        Vendor to = null;
        if (from != null && (from.getLifnr() != null && !from.getLifnr().trim().isEmpty())) {
            to = new Vendor();
            to.setMandt(from.getMandt());
            to.setLifnr(from.getLifnr().trim());
            to.setName1(from.getName1());
            to.setName2(from.getName2());
        }
        return to;
    }
}