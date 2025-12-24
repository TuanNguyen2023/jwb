/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.bapi.helper.CustomerGetDetailBapi;
import com.gcs.wb.bapi.helper.structure.CustomerAdrcGetDetailStructure;
import com.gcs.wb.bapi.helper.structure.CustomerGetDetailStructure;
import com.gcs.wb.jpa.entity.Customer;

/**
 *
 * @author THANGPT
 */
public class CustomerConverter extends AbstractThrowableConverter<CustomerGetDetailBapi, Customer, Exception>{
    @Override
    public Customer convert(CustomerGetDetailBapi from){
        CustomerGetDetailStructure strucCust = from.getEsKna1();
        CustomerAdrcGetDetailStructure strucCustAdrc = from.getEsAdrc();
        Customer to = null;
        if (strucCust != null && (strucCust.getKunnr() != null && !strucCust.getKunnr().trim().isEmpty()) && strucCustAdrc != null) {
            to = new Customer();
            to.setMandt(strucCust.getMandt());
            to.setKunnr(strucCust.getKunnr());
            to.setAddrnumber(strucCustAdrc.getAddrnumber());
            to.setName1(strucCustAdrc.getName1());
            to.setName2(strucCustAdrc.getName2());
            to.setName3(strucCustAdrc.getName3());
            to.setName4(strucCustAdrc.getName4());
        }
        return to;
    }
}
