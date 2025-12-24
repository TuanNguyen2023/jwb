/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.PartnerGetListStructure;
import com.gcs.wb.jpa.entity.Partner;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THANGHL
 */
public class PartnersConverter extends AbstractThrowableConverter<List<PartnerGetListStructure>, List<Partner>, Exception> {

    @Override
    public List<Partner> convert(List<PartnerGetListStructure> fromList) {
        List<Partner> toList = new ArrayList<>();
        for (PartnerGetListStructure partnerFrom : fromList) {
            Partner partnerTo = new Partner();
            partnerTo.setKunnr(partnerFrom.getKunnr());
            partnerTo.setVkorg(partnerFrom.getVkorg());
            partnerTo.setVtweg(partnerFrom.getVtweg());
            partnerTo.setSpart(partnerFrom.getSpart());
            partnerTo.setParvw(partnerFrom.getParvw());
            partnerTo.setParza(partnerFrom.getParza());
            partnerTo.setKunn2(partnerFrom.getKunn2());

            if (!toList.contains(partnerTo)) {
                toList.add(partnerTo);
            }
        }

        return toList;
    }
}
