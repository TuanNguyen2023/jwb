/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.CustomerGetListConstants;
import com.gcs.wb.bapi.helper.structure.CustomerGetListStructure;
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
@Bapi(CustomerGetListConstants.BAPI_NAME)
public class CustomerGetListBapi implements Serializable {
    /**Import Parameter: IV_START_DATE */
    @Import
    @Parameter(CustomerGetListConstants.IV_START_DATE)
    private Date _ivStartDate;

    /**Import Parameter: IV_START_TIME */
    @Import
    @Parameter(CustomerGetListConstants.IV_START_TIME)
    private String _ivStartTime;

    /**Import Parameter: IV_END_DATE */
    @Import
    @Parameter(CustomerGetListConstants.IV_END_DATE)
    private Date _ivEndDate;

    /**Import Parameter: IV_END_TIME */
    @Import
    @Parameter(CustomerGetListConstants.IV_END_TIME)
    private String _ivEndTime;

    /**Import Parameter: IV_INDICATOR */
    @Import
    @Parameter(CustomerGetListConstants.IV_INDICATOR)
    private String _ivIndicator;

    /**Table: ET_CUSTOMER */
    @Table
    @Parameter(value = CustomerGetListConstants.ET_CUSTOMER, type = ParameterType.STRUCTURE)
    private List<CustomerGetListStructure> _etCustomer;

    public CustomerGetListBapi() {
        
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

    public List<CustomerGetListStructure> getListEtCustomer() {
        return  _etCustomer;
    }

    @Override
    public String toString() {
        return "CustomerGetListBapi{" + "_ivStartDate=" + _ivStartDate + ", _ivStartTime=" + _ivStartTime + ", _ivEndDate=" + _ivEndDate + ", _ivEndTime=" + _ivEndTime + ", _ivIndicator=" + _ivIndicator + '}';
    }
}
