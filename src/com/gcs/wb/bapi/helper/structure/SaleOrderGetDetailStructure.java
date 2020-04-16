/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.SaleOrderGetDetailConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author HANGTT
 */
@BapiStructure
public class SaleOrderGetDetailStructure implements Serializable {
    @Parameter(SaleOrderGetDetailConstants.VBELN)
    private String _vbeln;
    @Parameter(SaleOrderGetDetailConstants.MATNR)
    private String _matnr;
    @Parameter(SaleOrderGetDetailConstants.MAKTX)
    private String _maktx;
    @Parameter(SaleOrderGetDetailConstants.KUNNR)
    private String _kunnr;
    @Parameter(SaleOrderGetDetailConstants.KWMENG)
    private BigDecimal _kwmeng;
    @Parameter(SaleOrderGetDetailConstants.FREE_QUA)
    private BigDecimal _freeQua;
    @Parameter(SaleOrderGetDetailConstants.REC_QUA)
    private BigDecimal _recQua;
    @Parameter(SaleOrderGetDetailConstants.W_NAME)
    private String _wName;
    @Parameter(SaleOrderGetDetailConstants.SHIP_TO)
    private String _shipTo;
    @Parameter(SaleOrderGetDetailConstants.SHIP_TO_NAME)
    private String _shipToName;
    @Parameter(SaleOrderGetDetailConstants.NOTE)
    private String _note;
    @Parameter(SaleOrderGetDetailConstants.CHANGED)
    private String _changed;
    @Parameter(SaleOrderGetDetailConstants.VSBED)
    private String _vsbed;
    @Parameter(SaleOrderGetDetailConstants.ZKVGR1)
    private String _zkvgr1;
    @Parameter(SaleOrderGetDetailConstants.ZKVGR1_TEXT)
    private String _zkvgr1Text;
    @Parameter(SaleOrderGetDetailConstants.ZKVGR2)
    private String _zkvgr2;
    @Parameter(SaleOrderGetDetailConstants.ZKVGR2_TEXT)
    private String _zkvgr2Text;
    @Parameter(SaleOrderGetDetailConstants.ZKVGR3)
    private String _zkvgr3;
    @Parameter(SaleOrderGetDetailConstants.ZKVGR3_TEXT)
    private String _zkvgr3Text;
    @Parameter(SaleOrderGetDetailConstants.SORT1)
    private String _sort1;
    @Parameter(SaleOrderGetDetailConstants.TRAID)
    private String _traid;
    @Parameter(SaleOrderGetDetailConstants.WERKS)
    private String _werks;

    public SaleOrderGetDetailStructure() {
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

    public String getShipTo() {
        return _shipTo;
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
