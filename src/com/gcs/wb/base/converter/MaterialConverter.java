/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.MatGetDetailStructure;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.model.AppConfig;
import java.util.List;

/**
 *
 * @author THANGPT
 */
public class MaterialConverter extends AbstractThrowableConverter<MatGetDetailStructure, Material, Exception> {
    
    @Override
    public Material convert(MatGetDetailStructure from) {
        Material to = null;
        if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
            AppConfig config = WeighBridgeApp.getApplication().getConfig();
            to = new Material(from.getMandt(), config.getwPlant(), from.getMatnr());
        } else {
            to = new Material(from.getMandt(), from.getMatnr());
        }
        to.setMaktx(from.getMaktx());
        to.setMaktg(from.getMaktg());
        to.setXchpf(from.getXchpf() != null && from.getXchpf().equalsIgnoreCase("X") ? 'X' : ' ');
        return to;
    }

    @Override
    public Material convertHasParameter(MatGetDetailStructure from, String val) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Material convertsHasParameter(MatGetDetailStructure from, String val, boolean refresh) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
