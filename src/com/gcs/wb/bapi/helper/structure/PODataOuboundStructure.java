/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.PoPostGetListConstants;
import java.io.Serializable;
import java.util.List;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;

/**
 *
 * @author HANGTT
 */
@BapiStructure
public class PODataOuboundStructure implements Serializable{
    /**Header*/
    @Parameter(value = PoPostGetListConstants.PO_HEADER, type = ParameterType.STRUCTURE)
    private List<POHeaderGetListStructure> _poHeader;

    /**Item*/
//    @Parameter(PoPostGetListConstants.PO_ITEM)
//    private List<POItemGetListStructure> _poItem;
    
    public PODataOuboundStructure() {}

    /**
     * @return the _poHeader
     */
    public List<POHeaderGetListStructure> getPoHeader() {
        return _poHeader;
    }

    /**
     * @return the _poItem
     */
//    public List<POItemGetListStructure> getPoItem() {
//        return _poItem;
//    }
}
