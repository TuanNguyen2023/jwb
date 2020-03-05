/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.SyncContractSOGetListConstants;
import com.gcs.wb.bapi.helper.structure.SalesOrderStructure;
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
@Bapi(SyncContractSOGetListConstants.BAPI_NAME)
public class SyncContractSOGetListBapi implements Serializable {
    @Import
    @Parameter(SyncContractSOGetListConstants.IV_KUNNR)
    private String _ivKunnr;

    @Import
    @Parameter(SyncContractSOGetListConstants.IV_DATE_F)
    private Date _ivDateF;
    
    @Import
    @Parameter(SyncContractSOGetListConstants.IV_DATE_T)
    private Date _ivDateT;
    
    @Import
    @Parameter(SyncContractSOGetListConstants.IV_VBELN)
    private String _ivVbeln;

    @Import
    @Parameter(SyncContractSOGetListConstants.IV_OPTION)
    private String _ivOption;
    
    @Table
    @Parameter(value = SyncContractSOGetListConstants.ET_SALESORDER, type = ParameterType.STRUCTURE)
    private List<SalesOrderStructure> _etSalesOrder;
    
    public SyncContractSOGetListBapi() {}
    
    public void setIvKunnr(String ivKunnr) {
        this._ivKunnr = ivKunnr;
    }
    
    public void setIvDateF(Date ivDateF) {
        this._ivDateF = ivDateF;
    }
    
    public void setIvDateT(Date ivDateT) {
        this._ivDateT = ivDateT;
    }
    
    public void setIvVbeln(String ivVbeln) {
        this._ivVbeln = ivVbeln;
    }
    
    public void setIvOption(String ivOption) {
        this._ivOption = ivOption;
    }
    
    public List<SalesOrderStructure> getListSalesOrder() {
        return  _etSalesOrder;
    }
}
