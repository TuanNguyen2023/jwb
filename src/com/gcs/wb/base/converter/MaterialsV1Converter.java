/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.MatLookupStructure;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.model.AppConfig;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THANGPT
 */
public class MaterialsV1Converter extends AbstractThrowableConverter<List<MatLookupStructure>, List<Material>, Exception>{
    
    @Override
    public List<Material> convert(List<MatLookupStructure> fromList){
        List<Material> toList = new ArrayList<>();
        AppConfig config = WeighBridgeApp.getApplication().getConfig();
        for (MatLookupStructure mat : fromList) {
            Material m = null;
            if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                m = new Material(config.getsClient(), config.getwPlant(), mat.getMaterial());
            } else {
                m = new Material(config.getsClient(), mat.getMaterial());
            }
            m.setMaktx(mat.getDesc());
            m.setMaktg(m.getMaktx().toUpperCase());
            m.setXchpf(mat.getXchpf() != null && mat.getXchpf().equalsIgnoreCase("X") ? 'X' : ' ');
            toList.add(m);
        }
        return toList;
    }
}

