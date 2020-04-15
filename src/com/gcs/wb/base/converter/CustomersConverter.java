/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.CustomerGetListStructure;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.model.AppConfig;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HANGTT
 */
public class CustomersConverter extends AbstractThrowableConverter<List<CustomerGetListStructure>, List<Customer>, Exception> {
    
    @Override
    public List<Customer> convert(List<CustomerGetListStructure> fromList) {
        List<Customer> toList = new ArrayList<Customer>();
        for (CustomerGetListStructure cusFrom : fromList) {
            Customer cusTo = new Customer(cusFrom.getKunnr());
            cusTo.setMandt(cusFrom.getMandt());
            cusTo.setName1(cusFrom.getName1());
            cusTo.setName2(cusFrom.getName2());
            cusTo.setName3(cusFrom.getName3());
            cusTo.setName4(cusFrom.getName4());
            if (!toList.contains(cusTo)) {
                toList.add(cusTo);
            }
        }

        return toList;
    }
}
