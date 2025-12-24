/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.StoGetListConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Parameter;
import org.hibersap.conversion.BooleanConverter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class StoGetListStructure implements Serializable {

    @Parameter(StoGetListConstants.VENDOR_NAME)
    private String _vendorName;
    @Parameter(StoGetListConstants.MANDT)
    private String _mandt;
    @Parameter(StoGetListConstants.EBELN)
    private String _ebeln;
    @Parameter(StoGetListConstants.EBELP)
    private String _ebelp;
    @Parameter(StoGetListConstants.LOEKZ)
    @Convert(converter = BooleanConverter.class)
    private boolean _loekz;
    @Parameter(StoGetListConstants.BEDAT)
    private Date _bedat;
    @Parameter(StoGetListConstants.LIFNR)
    private String _lifnr;
    @Parameter(StoGetListConstants.MATNR)
    private String _matnr;
    @Parameter(StoGetListConstants.TXZ01)
    private String _txz01;
    @Parameter(StoGetListConstants.MENGE)
    private BigDecimal _menge;
    @Parameter(StoGetListConstants.MEINS)
    private String _meins;
    @Parameter(StoGetListConstants.RESWK)
    private String _reswk;
    @Parameter(StoGetListConstants.WERKS)
    private String _werks;
    @Parameter(StoGetListConstants.LGORT)
    private String _lgort;
    @Parameter(StoGetListConstants.BWTAR)
    private String _bwtar;
    @Parameter(StoGetListConstants.UNTTO)
    private BigDecimal _untto;
    @Parameter(StoGetListConstants.UEBTO)
    private BigDecimal _uebto;
    @Parameter(StoGetListConstants.UEBTK)
    @Convert(converter = BooleanConverter.class)
    private boolean _uebtk;
    @Parameter(StoGetListConstants.VSTEL)
    private String _vstel;

    public StoGetListStructure() {
    }

    public StoGetListStructure(String vendorName,
            String mandt,
            String ebeln,
            String ebelp,
            boolean loekz,
            Date bedat,
            String lifnr,
            String matnr,
            String txz01,
            BigDecimal menge,
            String meins,
            String reswk,
            String werks,
            String lgort,
            String bwtar,
            BigDecimal untto,
            BigDecimal uebto,
            boolean uebtk,
            String vstel) {
        this._vendorName = vendorName;
        this._mandt = mandt;
        this._ebeln = ebeln;
        this._ebelp = ebelp;
        this._loekz = loekz;
        this._bedat = bedat;
        this._lifnr = lifnr;
        this._matnr = matnr;
        this._txz01 = txz01;
        this._menge = menge;
        this._meins = meins;
        this._reswk = reswk;
        this._werks = werks;
        this._lgort = lgort;
        this._bwtar = bwtar;
        this._untto = untto;
        this._uebto = uebto;
        this._uebtk = uebtk;
        this._vstel = vstel;
    }

    /**
     * @return the _vendorName
     */
    public String getVendorName() {
        return _vendorName;
    }

    /**
     * @return the _mandt
     */
    public String getMandt() {
        return _mandt;
    }

    /**
     * @return the _ebeln
     */
    public String getEbeln() {
        return _ebeln;
    }

    /**
     * @return the _ebelp
     */
    public String getEbelp() {
        return _ebelp;
    }

    /**
     * @return the _loekz
     */
    public boolean isLoekz() {
        return _loekz;
    }

    /**
     * @return the _bedat
     */
    public Date getBedat() {
        return _bedat;
    }

    /**
     * @return the _lifnr
     */
    public String getLifnr() {
        return _lifnr;
    }

    /**
     * @return the _matnr
     */
    public String getMatnr() {
        return _matnr;
    }

    /**
     * @return the _txz01
     */
    public String getTxz01() {
        return _txz01;
    }

    /**
     * @return the _menge
     */
    public BigDecimal getMenge() {
        return _menge;
    }

    /**
     * @return the _meins
     */
    public String getMeins() {
        return _meins;
    }

    /**
     * @return the _reswk
     */
    public String getReswk() {
        return _reswk;
    }

    /**
     * @return the _werks
     */
    public String getWerks() {
        return _werks;
    }

    /**
     * @return the _lgort
     */
    public String getLgort() {
        return _lgort;
    }

    /**
     * @return the _bwtar
     */
    public String getBwtar() {
        return _bwtar;
    }

    /**
     * @return the _untto
     */
    public BigDecimal getUntto() {
        return _untto;
    }

    /**
     * @return the _uebto
     */
    public BigDecimal getUebto() {
        return _uebto;
    }

    /**
     * @return the _uebtk
     */
    public boolean isUebtk() {
        return _uebtk;
    }

    /**
     * @return the _vstel
     */
    public String getVstel() {
        return _vstel;
    }
}
