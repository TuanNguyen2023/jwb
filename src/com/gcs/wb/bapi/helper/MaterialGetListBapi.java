/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.MaterialGetListConstants;
import com.gcs.wb.bapi.helper.structure.MaterialGetListStructure;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;

/**
 *
 * @author HANGTT
 */
@Bapi(MaterialGetListConstants.BAPI_NAME)
public class MaterialGetListBapi  implements Serializable {
    /**Import Parameter: IV_START_DATE */
    @Import
    @Parameter(MaterialGetListConstants.IV_START_DATE)
    private Date _ivStartDate;
    
    /**Import Parameter: IV_START_TIME */
    @Import
    @Parameter(MaterialGetListConstants.IV_START_TIME)
    private Date _ivStartTime = null;
    
    /**Import Parameter: IV_END_TIME */
    @Import
    @Parameter(MaterialGetListConstants.IV_END_TIME)
    private Date _ivEndTime;
    
    /**Import Parameter: IV_INDICATOR */
    @Import
    @Parameter(MaterialGetListConstants.IV_INDICATOR)
    private String _ivIndicator;
    
    /**Export Parameter: ZTTMMVN_WB_MATERIAL */
    //@Export
    @Table
    @Parameter(value = MaterialGetListConstants.ET_MATERIAL, type = ParameterType.STRUCTURE)
    private List<MaterialGetListStructure> _etMaterial;
    
    public MaterialGetListBapi() {
    }
    
    /**
     * Import Parameter: IV_START_DATE
     * @param ivStartDate the _ivStartDate to set
     */
    public void setIvStartDate(Date ivStartDate) {
        this._ivStartDate = ivStartDate;
    }
    
    /**
     * Import Parameter: IV_START_TIME
     * @param ivStartDate the _ivStartDate to set
     */
    public void setIvStartTime(Date ivStartTime) {
        this._ivStartTime = ivStartTime;
    }
    
    /**
     * Import Parameter: IV_END_DATE
     * @param ivEndTime the _ivEndTime to set
     */
    public void setIvEndTime(Date ivEndTime) {
        this._ivEndTime = ivEndTime;
    }
    
    /**
     * Import Parameter: IV_INDICATOR
     * @param ivIndicator the _ivIndicator to set
     */
    public void setIvIndicator(String ivIndicator) {
        this._ivIndicator = ivIndicator;
    }
    
    /**
     * Export Parameter: ZTTMMVN_WB_MATERIAL
     * @return the _etMaterial
     */
    public List<MaterialGetListStructure> getEtMaterial() {
        return _etMaterial;
    }
}
