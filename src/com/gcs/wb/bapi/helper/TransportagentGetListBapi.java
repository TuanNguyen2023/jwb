/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.TransportagentGetListConstants;
import com.gcs.wb.bapi.helper.structure.TransportagentGetListStructure;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;

/**
 *
 * @author HANGTT
 */
@Bapi(TransportagentGetListConstants.BAPI_NAME)
public class TransportagentGetListBapi implements Serializable{
    
    /**Import Parameter: IV_START_DATE */
//    @Import
//    @Parameter(TransportagentGetListConstants.IV_START_DATE)
//    private Date _ivStartDate;
    
    /**Import Parameter: IV_START_TIME */
    @Import
    @Parameter(TransportagentGetListConstants.IV_START_TIME)
    private String _ivStartTime = null;
    
    /**Import Parameter: IV_END_TIME */
    @Import
    @Parameter(TransportagentGetListConstants.IV_END_TIME)
    private String _ivEndTime;
    
    /**Import Parameter: IV_INDICATOR */
    @Import
    @Parameter(TransportagentGetListConstants.IV_INDICATOR)
    private String _ivIndicator;
    
    /**Import Parameter: IV_PurORG */
    @Import
    @Parameter(TransportagentGetListConstants.IV_EKORG)
    private String _ivEkorg;
    /**Export Parameter: ES_LFA1 (Vendor Structure)*/
    @Table
    @Parameter(value = TransportagentGetListConstants.ET_VENDOR, type = ParameterType.STRUCTURE)
    private List<TransportagentGetListStructure> _etVendor;
    
    public TransportagentGetListBapi() {
    }

    /**
     * Import Parameter: IV_START_DATE
     * @param ivStartDate the _ivStartDate to set
     */
//    public void setIvStartDate(Date ivStartDate) {
//        this._ivStartDate = ivStartDate;
//    }
    
    /**
     * Import Parameter: IV_START_TIME
     * @param ivStartDate the _ivStartDate to set
     */
    public void setIvStartTime(String ivStartTime) {
        this._ivStartTime = ivStartTime;
    }
    
    /**
     * Import Parameter: IV_END_DATE
     * @param ivEndTime the _ivEndTime to set
     */
    public void setIvEndTime(String ivEndTime) {
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
     * Import Parameter: IV_PurORG
     * @param ivPurORG the _ivPurORG to set
     */
    public void setIvEkorg(String ivEkorg) {
        this._ivEkorg = ivEkorg;
    }

    /**
     * Export Parameter: ET_VENDOR (List DVVC)
     * @return the _etVendor
     */
    public List<TransportagentGetListStructure> getEtVendor() {
        return _etVendor;
    }

    @Override
    public String toString() {
        return "TransportagentGetListBapi{" + "_ivStartTime=" + _ivStartTime + ", _ivEndTime=" + _ivEndTime + ", _ivIndicator=" + _ivIndicator + ", _ivEkorg=" + _ivEkorg + '}';
    }
}
