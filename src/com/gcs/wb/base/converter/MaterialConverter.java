/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.MatGetDetailStructure;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Material;

/**
 *
 * @author THANGPT
 */
public class MaterialConverter extends AbstractThrowableConverter<MatGetDetailStructure, Material, Exception> {
    
    @Override
    public Material convert(MatGetDetailStructure from) {
        Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
        Material to;
        if (configuration.isModeNormal()) {
            to = new Material(from.getMandt(), configuration.getWkPlant(), from.getMatnr());
        } else {
            to = new Material(from.getMandt(), from.getMatnr());
        }
        to.setMaktx(from.getMaktx());
        to.setMaktg(from.getMaktg());
        to.setXchpf(from.getXchpf() != null && from.getXchpf().equalsIgnoreCase("X") ? 'X' : ' ');
        return to;
    }
}
