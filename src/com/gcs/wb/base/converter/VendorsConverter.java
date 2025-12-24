/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.bapi.helper.structure.TransportagentGetListStructure;
import com.gcs.wb.jpa.entity.Vendor;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THANGPT
 */
public class VendorsConverter extends AbstractThrowableParamConverter<List<TransportagentGetListStructure>, List<Vendor>, Exception> {

    @Override
    public List<Vendor> convertHasParameter(List<TransportagentGetListStructure> fromList, String val) throws Exception {
        List<Vendor> toList = new ArrayList<Vendor>();
        for (TransportagentGetListStructure vendor : fromList) {
            Vendor v = new Vendor();
            v.setMandt(val);
            v.setLifnr(vendor.getLifnr());
            v.setName1(vendor.getName1());
            v.setName2(vendor.getName2());

            toList.add(v);
        }
        return toList;
    }

    @Override
    public List<Vendor> convertsHasParameter(List<TransportagentGetListStructure> from, String val, boolean refresh) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
