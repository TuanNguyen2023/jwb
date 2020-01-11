/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.bapi.helper.structure.CustomerGetDetailStructure;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.CustomerPK;

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
}
