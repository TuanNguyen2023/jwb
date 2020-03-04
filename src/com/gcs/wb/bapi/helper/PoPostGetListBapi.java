/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.PoPostGetListConstants;
import com.gcs.wb.bapi.helper.structure.PODataOuboundStructure;
import com.gcs.wb.bapi.helper.structure.POSTODataOuboundStructure;
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
@Bapi(PoPostGetListConstants.BAPI_NAME)
public class PoPostGetListBapi implements Serializable{
    
    /**Import Parameter: IV_START_DATE */
    @Import
    @Parameter(PoPostGetListConstants.IV_START_DATE)
    private Date _ivStartDate;

    /**Import Parameter: IV_START_TIME */
    @Import
    @Parameter(PoPostGetListConstants.IV_START_TIME)
    private String _ivStartTime;

    /**Import Parameter: IV_END_DATE */
    @Import
    @Parameter(PoPostGetListConstants.IV_END_DATE)
    private Date _ivEndDate;

    /**Import Parameter: IV_END_TIME */
    @Import
    @Parameter(PoPostGetListConstants.IV_END_TIME)
    private String _ivEndTime;

    /**Import Parameter: IV_INDICATOR */
    @Import
    @Parameter(PoPostGetListConstants.IV_INDICATOR)
    private String _ivIndicator;

    /**Export Parameter: ET_PO_STO */
//    @Export
//    @Parameter(value = PoPostGetListConstants.ET_PO_STO, type = ParameterType.STRUCTURE)
//    private List<POSTODataOuboundStructure> _etPosto;

    /**Export Parameter: EV_MESSAGE_ID*/
//    @Export
//    @Parameter(PoPostGetListConstants.EV_MESSAGE_ID)
//    private String _evMessageID;

    /**Table: CT_PO_STO_HEADER */
    @Table
    @Parameter(value = PoPostGetListConstants.CT_PO_STO_HEADER, type = ParameterType.STRUCTURE)
    private List<PODataOuboundStructure> _ctPoPostoHeader;

    public PoPostGetListBapi() {
    }

    public void setIvStartDate(Date ivStartDate) {
        this._ivStartDate = ivStartDate;
    }

    public void setIvStartTime(String ivStartTime) {
        this._ivStartTime = ivStartTime;
    }

     public void setIvEndDate(Date ivEndDate) {
        this._ivEndDate = ivEndDate;
    }

    public void setIvEndTime(String ivEndTime) {
        this._ivEndTime = ivEndTime;
    }

    public void setIvIndicator(String ivIndicator) {
        this._ivIndicator = ivIndicator;
    }

//    public List<POSTODataOuboundStructure> getListPOSTODataOubound() {
//        return  _etPosto;
//    }
//
//    public String getEvMessageID() {
//        return  _evMessageID;
//    }

    public List<PODataOuboundStructure> getListPODataOubound() {
        return  _ctPoPostoHeader;
    }

}
