/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.bapi.helper.structure.CustomerGetDetailStructure;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.CustomerPK;
import java.util.List;

/**
 *
 * @author THANGPT
 */
public class CustomerConverter extends AbstractThrowableConverter<CustomerGetDetailStructure, Customer, Exception>{
    @Override
    public Customer convert(CustomerGetDetailStructure from){
        Customer to = null;
        if (from != null && (from.getKunnr() != null && !from.getKunnr().trim().isEmpty())) {
            CustomerPK pk = new CustomerPK(from.getMandt(), from.getKunnr());
            to = new Customer(pk);
            to.setName1(from.getName1());
            to.setName2(from.getName2());
        }
        return to;
    }

    @Override
    public Customer convertHasParameter(CustomerGetDetailStructure from, String val) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Customer convertsHasParameter(CustomerGetDetailStructure from, String val, boolean refresh) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
