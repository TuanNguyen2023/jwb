/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.bapi.helper.structure.VendorGetDetailStructure;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.entity.VendorPK;
import java.util.List;

/**
 *
 * @author THANGPT
 */
public class VendorConverter extends AbstractThrowableConverter<VendorGetDetailStructure, Vendor, Exception>{
    @Override
    public Vendor convert(VendorGetDetailStructure from){
        Vendor to = null;
        if (from != null && (from.getLifnr() != null && !from.getLifnr().trim().isEmpty())) {
            VendorPK pk = new VendorPK(from.getMandt(), from.getLifnr().trim());
            to = new Vendor(pk);
            to.setName1(from.getName1());
            to.setName2(from.getName2());
        }
        return to;
    }

    @Override
    public Vendor convertHasParameter(VendorGetDetailStructure from, String val) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vendor convertsHasParameter(VendorGetDetailStructure from, String val, boolean refresh) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}