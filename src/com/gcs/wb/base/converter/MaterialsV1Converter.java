/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.structure.MatLookupStructure;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.Material;
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
        Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
        for (MatLookupStructure mat : fromList) {
            Material m;
            if (configuration.isModeNormal()) {
                m = new Material(configuration.getSapClient(), configuration.getWkPlant(), mat.getMaterial());
            } else {
                m = new Material(configuration.getSapClient(), mat.getMaterial());
            }
            m.setMaktx(mat.getDesc());
            m.setMaktg(m.getMaktx().toUpperCase());
            m.setXchpf(mat.getXchpf() != null && mat.getXchpf().equalsIgnoreCase("X") ? 'X' : ' ');
            toList.add(m);
        }
        return toList;
    }
}

