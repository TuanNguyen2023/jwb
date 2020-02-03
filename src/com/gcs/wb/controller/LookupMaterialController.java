/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.service.LookupMaterialService;
import java.util.List;

/**
 *
 * @author thangtp.nr
 */
public class LookupMaterialController {
    
    LookupMaterialService lookupMaterialService = new LookupMaterialService();
    
    public List<Material> getListMaterialByDesc(String desc){
        return lookupMaterialService.getListMaterialByDesc(desc);
    }
}
