/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.outbdlv.structure;

import com.gcs.wb.bapi.outbdlv.constants.WsDeliveryUpdateConstants;
import java.io.Serializable;
import java.util.Date;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class VbkokStructure implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Vbkok Fields">
    /**Delivery*/
    @Parameter(WsDeliveryUpdateConstants.VBELN_VL)
    private String _vbeln_vl;
    /**Picking Date*/
    @Parameter(value = WsDeliveryUpdateConstants.KODAT)
    private Date _kodat;
    /**Picking Time*/
    @Parameter(value = WsDeliveryUpdateConstants.KOUHR)
    private Date _kouhr;
    /**Automatically overwrite delivery quantity with picking qty*/
    @Parameter(value = WsDeliveryUpdateConstants.KOMUE)
    private String _komue;
    /**Post Goods Movement Automatically*/
    @Parameter(value = WsDeliveryUpdateConstants.WABUC)
    private String _wabuc;
    /**Actual Goods Movement Date*/
    @Parameter(value = WsDeliveryUpdateConstants.WADAT_IST)
    private Date _wadat_ist;
    /**Goods Issue Date*/
    @Parameter(value = WsDeliveryUpdateConstants.WADAT)
    private Date _wadat;
    /**Goods Issue Time*/
    @Parameter(value = WsDeliveryUpdateConstants.WAUHR)
    private Date _wauhr;
    /**Delivery date*/
    @Parameter(value = WsDeliveryUpdateConstants.LFDAT)
    private Date _lfdat;
    /**Delivery time*/
    @Parameter(value = WsDeliveryUpdateConstants.LFUHR)
    private Date _lfuhr;
    /**Driver Name*/
    @Parameter(value = WsDeliveryUpdateConstants.LIFEX)
    private String _lifex;
    /**Means-of-Transport Type*/
    @Parameter(value = WsDeliveryUpdateConstants.TRATY)
    private String _traty;
    /**Means of Transport ID*/
    @Parameter(value = WsDeliveryUpdateConstants.TRAID)
    private String _traid;
    // </editor-fold>

    public VbkokStructure() {
    }

    // <editor-fold defaultstate="collapsed" desc="Vbkok Field Getter/Setter">s
    /**
     * Delivery
     * @param vbeln_vl the _vbeln_vl to set
     */
    public void setVbeln_vl(String vbeln_vl) {
        this._vbeln_vl = vbeln_vl;
    }

    /**
     * Picking Date
     * @param kodat the _kodat to set
     */
    public void setKodat(Date kodat) {
        this._kodat = kodat;
    }

    /**
     * Picking Time
     * @param kouhr the _kouhr to set
     */
    public void setKouhr(Date kouhr) {
        this._kouhr = kouhr;
    }

    /**
     * Automatically overwrite delivery quantity with picking qty
     * @param komue the _komue to set
     */
    public void setKomue(String komue) {
        this._komue = komue;
    }

    /**
     * Post Goods Movement Automatically
     * @param wabuc the _wabuc to set
     */
    public void setWabuc(String wabuc) {
        this._wabuc = wabuc;
    }

    /**
     * Actual Goods Movement Date
     * @param wadat_ist the _wadat_ist to set
     */
    public void setWadat_ist(Date wadat_ist) {
        this._wadat_ist = wadat_ist;
    }

    /**
     * Goods Issue Date
     * @param wadat the _wadat to set
     */
    public void setWadat(Date wadat) {
        this._wadat = wadat;
    }

    /**
     * Goods Issue Time
     * @param wauhr the _wauhr to set
     */
    public void setWauhr(Date wauhr) {
        this._wauhr = wauhr;
    }

    /**
     * Delivery date
     * @param lfdat the _lfdat to set
     */
    public void setLfdat(Date lfdat) {
        this._lfdat = lfdat;
    }

    /**
     * Delivery time
     * @param lfuhr the _lfuhr to set
     */
    public void setLfuhr(Date lfuhr) {
        this._lfuhr = lfuhr;
    }

    /**
     * Driver Name
     * @param lifex the _lifex to set
     */
    public void setLifex(String lifex) {
        this._lifex = lifex;
    }

    /**
     * Means-of-Transport Type
     * @param traty the _traty to set
     */
    public void setTraty(String traty) {
        this._traty = traty;
    }

    /**
     * Means of Transport ID
     * @param traid the _traid to set
     */
    public void setTraid(String traid) {
        this._traid = traid;
    }
    // </editor-fold>
}
