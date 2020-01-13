/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.bapi.helper.structure.TransportagentGetListStructure;
import com.gcs.wb.jpa.entity.TransportAgent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THANGPT
 */
public class TransportAgentsConverter extends AbstractThrowableConverter<List<TransportagentGetListStructure>, List<TransportAgent>, Exception> {

    @Override
    public List<TransportAgent> convert(List<TransportagentGetListStructure> fromList) {
        List<TransportAgent> toList = new ArrayList<TransportAgent>();
        for (TransportagentGetListStructure transport : fromList) {
            TransportAgent tAgent = null;
            tAgent = new TransportAgent(transport.getLifnr());
            if (transport.getName1() != null || transport.getName1().length() <= 35) {
                tAgent.setName(transport.getName1());
            } else {
                tAgent.setName(transport.getName2());
            }
            toList.add(tAgent);
        }
        return toList;
    }
}
