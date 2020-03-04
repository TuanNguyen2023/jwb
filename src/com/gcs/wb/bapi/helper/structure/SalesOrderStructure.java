/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.SyncContractSOGetListConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author HANGTT
 */
@BapiStructure
public class SalesOrderStructure implements Serializable {
    @Parameter(SyncContractSOGetListConstants.VBELN)
    private String _vbeln;
    @Parameter(SyncContractSOGetListConstants.MATNR)
    private String _matnr;
    @Parameter(SyncContractSOGetListConstants.MAKTX)
    private String _maktx;
    @Parameter(SyncContractSOGetListConstants.KWMENG)
    private String _kwmeng;
    @Parameter(SyncContractSOGetListConstants.FREE_QUA)
    private String _freeQua;
    @Parameter(SyncContractSOGetListConstants.REC_QUA)
    private String _recQua;
    @Parameter(SyncContractSOGetListConstants.W_NAME)
    private String _wName;
    @Parameter(SyncContractSOGetListConstants.SHIP_TO_NAME)
    private String _shipToName;
    @Parameter(SyncContractSOGetListConstants.NOTE)
    private String _note;
    
    public SalesOrderStructure() {}
    
    public String getVbeln() {
        return _vbeln;
    }

    public String getMatnr() {
        return _matnr;
    }
    
    public String getMaktx() {
        return _maktx;
    }
    
    public String getKwmeng() {
        return _kwmeng;
    }
    
    public String getFreeQua() {
        return _freeQua;
    }
    
     public String getRecQua() {
        return _recQua;
    }
    
    public String getWName() {
        return _wName;
    }
    
    public String getShipToName() {
        return _shipToName;
    }
    
    public String getNote() {
        return _note;
    }
}
