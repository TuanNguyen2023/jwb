/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.SyncContractSOGetListConstants;
import java.io.Serializable;
import java.math.BigDecimal;
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
    @Parameter(SyncContractSOGetListConstants.KUNNR)
    private String _kunnr;
    @Parameter(SyncContractSOGetListConstants.KWMENG)
    private BigDecimal _kwmeng;
    @Parameter(SyncContractSOGetListConstants.FREE_QUA)
    private BigDecimal _freeQua;
    @Parameter(SyncContractSOGetListConstants.REC_QUA)
    private BigDecimal _recQua;
    @Parameter(SyncContractSOGetListConstants.W_NAME)
    private String _wName;
    @Parameter(SyncContractSOGetListConstants.SHIP_TO_NAME)
    private String _shipToName;
    @Parameter(SyncContractSOGetListConstants.NOTE)
    private String _note;
    @Parameter(SyncContractSOGetListConstants.CHANGED)
    private String _changed;
    @Parameter(SyncContractSOGetListConstants.VSBED)
    private String _vsbed;
    @Parameter(SyncContractSOGetListConstants.ZKVGR1)
    private String _zkvgr1;
    @Parameter(SyncContractSOGetListConstants.ZKVGR1_TEXT)
    private String _zkvgr1Text;
    @Parameter(SyncContractSOGetListConstants.ZKVGR2)
    private String _zkvgr2;
    @Parameter(SyncContractSOGetListConstants.ZKVGR2_TEXT)
    private String _zkvgr2Text;
    @Parameter(SyncContractSOGetListConstants.ZKVGR3)
    private String _zkvgr3;
    @Parameter(SyncContractSOGetListConstants.ZKVGR3_TEXT)
    private String _zkvgr3Text;
    @Parameter(SyncContractSOGetListConstants.SORT1)
    private String _sort1;
    @Parameter(SyncContractSOGetListConstants.TRAID)
    private String _traid;
    @Parameter(SyncContractSOGetListConstants.WERKS)
    private String _werks;

    public SalesOrderStructure() {
    }

    public String getVbeln() {
        return _vbeln;
    }

    public String getMatnr() {
        return _matnr;
    }

    public String getMaktx() {
        return _maktx;
    }

    public String getKunnr() {
        return _kunnr;
    }

    public BigDecimal getKwmeng() {
        return _kwmeng;
    }

    public BigDecimal getFreeQua() {
        return _freeQua;
    }

    public BigDecimal getRecQua() {
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

    public String getWerks() {
        return _werks;
    }

    public String getTraid() {
        return _traid;
    }

    public String getChanged() {
        return _changed;
    }

     public String getVsbed() {
        return _vsbed;
    }
    
    public String getZkvgr1() {
        return _zkvgr1;
    }
    
    public String getZkvgr1Text() {
        return _zkvgr1Text;
    }

    public String getZkvgr2() {
        return _zkvgr2;
    }

    public String getZkvgr2Text() {
        return _zkvgr2Text;
    }

    public String getZkvgr3() {
        return _zkvgr3;
    }

    public String getZkvgr3Text() {
        return _zkvgr3Text;
    }

    public String getSort1() {
        return _sort1;
    }
}
