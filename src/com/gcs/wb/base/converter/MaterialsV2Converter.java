/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.MaterialGetListStructure;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.MaterialInternal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THANGPT
 */
public class MaterialsV2Converter extends AbstractThrowableConverter<List<MaterialGetListStructure>, List<Material>, Exception> {

    @Override
    public List<Material> convert(List<MaterialGetListStructure> fromList) {
        List<Material> toList = new ArrayList<>();
        Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
        for (MaterialGetListStructure mat : fromList) {
            if (configuration.getWkPlant().equalsIgnoreCase(mat.getWerks())) {
                Material m;
                m = new Material(configuration.getSapClient(), mat.getWerks(), mat.getMatnr());
                m.setMaktx(mat.getMaktx());
                m.setMaktg(mat.getMaktxLong());
                toList.add(m);
            }
        }
        return toList;
    }
    
    public List<MaterialInternal> convertMaster(List<MaterialGetListStructure> fromList) {
        List<MaterialInternal> toList = new ArrayList<>();
        Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
        for (MaterialGetListStructure mat : fromList) {
            if (configuration.getWkPlant().equalsIgnoreCase(mat.getWerks())) {
                MaterialInternal m;
                m = new MaterialInternal(configuration.getSapClient(), mat.getWerks(), mat.getMatnr());
                m.setWplant(mat.getWerks());
                m.setMatnr(mat.getMatnr());
                m.setMaktx(mat.getMaktx());
                m.setMaktg(mat.getMaktxLong());
                toList.add(m);
            }
        }
        return toList;
    }
}
