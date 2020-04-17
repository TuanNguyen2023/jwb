/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.bapi.helper.structure.TransportagentGetListStructure;
import com.gcs.wb.base.constant.Constants;
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
            if (transport.getKtokk().equals(Constants.KTOKK.Z004)) {
                TransportAgent tAgent = new TransportAgent(transport.getLifnr());
                String name = "";
                if (transport.getName1() != null) {
                    name = transport.getName1().trim();
                }

                if (transport.getName2() != null) {
                    name += " " + transport.getName2();
                }

                tAgent.setName(name.trim());
                toList.add(tAgent);
            }
        }

        return toList;
    }
}
