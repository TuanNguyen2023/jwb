/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.DoGetDetailConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class DoGetDetailStructure implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Attributes">
    /**Delivery*/
    @Parameter(DoGetDetailConstants.VBELN)
    private String _vbeln;
    /**Delivery Item*/
    @Parameter(DoGetDetailConstants.POSNR)
    private String _posnr;
    /**Date on Which Record Was Created*/
    @Parameter(DoGetDetailConstants.ERDAT)
    private Date _erdat;
    /**Delivery Type*/
    @Parameter(DoGetDetailConstants.LFART)
    private String _lfart;
    /**Complete delivery defined for each sales order?*/
    @Parameter(DoGetDetailConstants.AUTLF)
    private String _autlf;
    /**Goods Issue Date*/
    @Parameter(DoGetDetailConstants.WADAT)
    private Date _wadat;
    /**Loading Date*/
    @Parameter(DoGetDetailConstants.LDDAT)
    private Date _lddat;
    /**Picking Date*/
    @Parameter(DoGetDetailConstants.KODAT)
    private Date _kodat;
    /**Shipping Point/Receiving Point*/
    @Parameter(DoGetDetailConstants.VSTEL)
    private String _vstel;
    /**Account Number of Vendor or Creditor*/
    @Parameter(DoGetDetailConstants.LIFNR)
    private String _lifnr;
    /**Customer Number 1(Ship-to Party)*/
    @Parameter(DoGetDetailConstants.KUNNR)
    private String _kunnr;
    /**Customer Number 1(Sold-to party)*/
    @Parameter(DoGetDetailConstants.KUNAG)
    private String _kunag;
    /**Means-of-Transport Type*/
    @Parameter(DoGetDetailConstants.TRATY)
    private String _traty;
    /**Means of Transport ID*/
    @Parameter(DoGetDetailConstants.TRAID)
    private String _traid;
    /**Document Date in Document*/
    @Parameter(DoGetDetailConstants.BLDAT)
    private Date _bldat;
    /**Material Number*/
    @Parameter(DoGetDetailConstants.MATNR)
    private String _matnr;
    /**Supplying (Issuing) Plant in Stock Transport Order*/
    @Parameter(DoGetDetailConstants.WERKS)
    private String _werks;
    /**Storage Location*/
    @Parameter(DoGetDetailConstants.LGORT)
    private String _lgort;
    /**Batch Number*/
    @Parameter(DoGetDetailConstants.CHARG)
    private String _charg;
    /**Vendor Batch Number*/
    @Parameter(DoGetDetailConstants.LICHN)
    private String _lichn;
    /**Actual quantity delivered (in sales units*/
    @Parameter(DoGetDetailConstants.LFIMG)
    private BigDecimal _lfimg;
    /**Base Unit of Measure*/
    @Parameter(DoGetDetailConstants.MEINS)
    private String _meins;
    /**Sales unit*/
    @Parameter(DoGetDetailConstants.VRKME)
    private String _vrkme;
    /**Indicator: Unlimited Overdelivery Allowed*/
    @Parameter(DoGetDetailConstants.UEBTK)
    private String _uebtk;
    /**Overdelivery Tolerance Limit*/
    @Parameter(DoGetDetailConstants.UEBTO)
    private BigDecimal _uebto;
    /**Underdelivery Tolerance Limit*/
    @Parameter(DoGetDetailConstants.UNTTO)
    private BigDecimal _untto;
    /**Short text for sales order item*/
    @Parameter(DoGetDetailConstants.ARKTX)
    private String _arktx;
    /**Document number of the reference document*/
    @Parameter(DoGetDetailConstants.VGBEL)
    private String _vgbel;
    /**Item number of the reference item*/
    @Parameter(DoGetDetailConstants.VGPOS)
    private String _vgpos;
    /**Valuation Type*/
    @Parameter(DoGetDetailConstants.BWTAR)
    private String _bwtar;
    /**Movement Type (Inventory Management)*/
    @Parameter(DoGetDetailConstants.BWART)
    private String _bwart;
    /**Receiving Plant*/
    @Parameter(DoGetDetailConstants.RECV_PLANT)
    private String _recv_plant;
    /**Overall picking / putaway status*/
    @Parameter(DoGetDetailConstants.KOSTK)
    private String _kostk;
    /**Status of pick confirmation*/
    @Parameter(DoGetDetailConstants.KOQUK)
    private String _koquk;
    /**Total goods movement status*/
    @Parameter(DoGetDetailConstants.WBSTK)
    private String _wbstk;
    //{+20100212#01 Add field
    //  Higher-level item in bill of material structures
    //  Sales document item category
    /**Higher-level item in bill of material structures*/
    @Parameter(DoGetDetailConstants.UEPOS)
    private String _uepos;
    /**Sales document item category*/
    @Parameter(DoGetDetailConstants.PSTYV)
    private String _pstyv;
    //}+20100212#01 Add field
    
    /**Number DO was posted*/
    @Parameter(DoGetDetailConstants.VBELN_NACH)
    private String _vbelnNach;
    /** Weigh ticker Ref*/
    @Parameter(DoGetDetailConstants.WT_ID_REF)
    private String _wtIdRef;
    @Parameter(DoGetDetailConstants.VSBED)
    private String _vsbed;
    @Parameter(DoGetDetailConstants.ZKVGR1)
    private String _zkvgr1;
    @Parameter(DoGetDetailConstants.ZKVGR1_TEXT)
    private String _zkvgr1Text;
    @Parameter(DoGetDetailConstants.ZKVGR2)
    private String _zkvgr2;
    @Parameter(DoGetDetailConstants.ZKVGR2_TEXT)
    private String _zkvgr2Text;
    @Parameter(DoGetDetailConstants.ZKVGR3)
    private String _zkvgr3;
    @Parameter(DoGetDetailConstants.ZKVGR3_TEXT)
    private String _zkvgr3Text;
    @Parameter(DoGetDetailConstants.SORT1)
    private String _sort1;
    @Parameter(DoGetDetailConstants.FSCALE)
    private BigDecimal _fscale;
    @Parameter(DoGetDetailConstants.SSCALE)
    private BigDecimal _sscale;
    @Parameter(DoGetDetailConstants.PO_NUMBER)
    private BigDecimal _poNumber;
    @Parameter(DoGetDetailConstants.C_VENDOR)
    private BigDecimal _cVendor;
    @Parameter(DoGetDetailConstants.T_VENDOR)
    private BigDecimal _tVendor;
    @Parameter(DoGetDetailConstants.ZSLING)
    private BigDecimal _zSling;
    @Parameter(DoGetDetailConstants.ZPALLET)
    private BigDecimal _zPallet;

    // </editor-fold>

    public DoGetDetailStructure() {
    }

    // <editor-fold defaultstate="collapsed" desc="Attributes' Getter/Setter">
    /**
     * Delivery
     * @return the _vbeln
     */
    public String getVbeln() {
        return _vbeln;
    }

    /**
     * Delivery Item
     * @return the _posnr
     */
    public String getPosnr() {
        return _posnr;
    }

    /**
     * Date on Which Record Was Created
     * @return the _erdat
     */
    public Date getErdat() {
        return _erdat;
    }

    /**
     * Delivery Type
     * @return the _lfart
     */
    public String getLfart() {
        return _lfart;
    }

    /**
     * Complete delivery defined for each sales order?
     * @return the _autlf
     */
    public String getAutlf() {
        return _autlf;
    }

    /**
     * Goods Issue Date
     * @return the _wadat
     */
    public Date getWadat() {
        return _wadat;
    }

    /**
     * Loading Date
     * @return the _lddat
     */
    public Date getLddat() {
        return _lddat;
    }

    /**
     * Picking Date
     * @return the _kodat
     */
    public Date getKodat() {
        return _kodat;
    }

    /**
     * Shipping Point/Receiving Point
     * @return the _vstel
     */
    public String getVstel() {
        return _vstel;
    }

    /**
     * Account Number of Vendor or Creditor
     * @return the _lifnr
     */
    public String getLifnr() {
        return _lifnr;
    }

    /**
     * Means-of-Transport Type
     * @return the _traty
     */
    public String getTraty() {
        return _traty;
    }

    /**
     * Means of Transport ID
     * @return the _traid
     */
    public String getTraid() {
        return _traid;
    }

    /**
     * Document Date in Document
     * @return the _bldat
     */
    public Date getBldat() {
        return _bldat;
    }

    /**
     * Material Number
     * @return the _matnr
     */
    public String getMatnr() {
        return _matnr;
    }

    /**
     * Supplying (Issuing) Plant in Stock Transport Order
     * @return the _werks
     */
    public String getWerks() {
        return _werks;
    }

    /**
     * Storage Location
     * @return the _lgort
     */
    public String getLgort() {
        return _lgort;
    }

    /**
     * Batch Number
     * @return the _charg
     */
    public String getCharg() {
        return _charg;
    }

    /**
     * Vendor Batch Number
     * @return the _lichn
     */
    public String getLichn() {
        return _lichn;
    }

    /**
     * Actual quantity delivered (in sales units
     * @return the _lfimg
     */
    public BigDecimal getLfimg() {
        return _lfimg;
    }

    /**
     * Base Unit of Measure
     * @return the _meins
     */
    public String getMeins() {
        return _meins;
    }

    /**
     * Sales unit
     * @return the _vrkme
     */
    public String getVrkme() {
        return _vrkme;
    }

    /**
     * Indicator: Unlimited Overdelivery Allowed
     * @return the _uebtk
     */
    public String getUebtk() {
        return _uebtk;
    }

    /**
     * Overdelivery Tolerance Limit
     * @return the _uebto
     */
    public BigDecimal getUebto() {
        return _uebto;
    }

    /**
     * Underdelivery Tolerance Limit
     * @return the _untto
     */
    public BigDecimal getUntto() {
        return _untto;
    }

    /**
     * Short text for sales order item
     * @return the _arktx
     */
    public String getArktx() {
        return _arktx;
    }

    /**
     * Document number of the reference document
     * @return the _vgbel
     */
    public String getVgbel() {
        return _vgbel;
    }

    /**
     * Item number of the reference item
     * @return the _vgpos
     */
    public String getVgpos() {
        return _vgpos;
    }

    /**
     * Valuation Type
     * @return the _bwtar
     */
    public String getBwtar() {
        return _bwtar;
    }

    /**
     * Movement Type (Inventory Management)
     * @return the _bwart
     */
    public String getBwart() {
        return _bwart;
    }

    /**
     * Receiving Plant
     * @return the _recv_plant
     */
    public String getRecv_plant() {
        return _recv_plant;
    }

    /**
     * @return the _kostk
     */
    public String getKostk() {
        return _kostk;
    }

    /**
     * @return the _koquk
     */
    public String getKoquk() {
        return _koquk;
    }

    /**
     * @return the _wbstk
     */
    public String getWbstk() {
        return _wbstk;
    }

    /**
     * Customer Number 1(Ship-to Party)
     * @return the _kunnr
     */
    public String getKunnr() {
        return _kunnr;
    }

    /**
     * Customer Number 1(Sold-to party)
     * @return the _kunag
     */
    public String getKunag() {
        return _kunag;
    }

    //{+20100212#01 Add new fields
    //Higher-level item in bill of material structures
    //Sales document item category
    /**
     * Higher-level item in bill of material structures
     * @return the _kunnr
     */
    public String getUepos() {
        return _uepos;
    }
    
    /**
     * Sales document item category
     * @return the _pstyv
     */
    public String getPstyv() {
        return _pstyv;
    }
    //}+20100212#01

    /**
     * Number DO was posted
     * @return the _vbelnNach
     */
    public String getVbelnNach() {
        return _vbelnNach;
    }

    /**
     * Weigh ticker Ref
     * @return the _wtIdRef
     */
    public String getWtIdRef() {
        return _wtIdRef;
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
    
    /**
     * fscale for Xuat plant
     * @return the _fscale
     */
    public BigDecimal getFscale() {
        return _fscale;
    }

    /**
     * sscale for Xuat plant
     * @return the _sscale
     */
    public BigDecimal getSscale() {
        return _sscale;
    }

    // </editor-fold>
}
