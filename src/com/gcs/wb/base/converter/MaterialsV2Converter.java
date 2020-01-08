/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.MaterialGetListStructure;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.model.AppConfig;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THANGPT
 */
public class MaterialsV2Converter extends AbstractThrowableConverter<List<MaterialGetListStructure>, List<Material>, Exception> {

    @Override
    public List<Material> convert(List<MaterialGetListStructure> fromList) {
        List<Material> toList = new ArrayList<Material>();
        AppConfig config = WeighBridgeApp.getApplication().getConfig();
        for (MaterialGetListStructure mat : fromList) {
            if (config.getwPlant().toString().equalsIgnoreCase(mat.getWerks())) {
                Material m = null;
                m = new Material(config.getsClient(), mat.getWerks(), mat.getMatnr());
                m.setMaktx(mat.getMaktx());
                m.setMaktg(mat.getMaktxLong());
                toList.add(m);
            }
        }
        return toList;
    }

    @Override
    public List<Material> convertHasParameter(List<MaterialGetListStructure> from, String val) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Material> convertsHasParameter(List<MaterialGetListStructure> from, String val, boolean refresh) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
