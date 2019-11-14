/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.UnitsGetListConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class UnitsGetListStructure implements Serializable {

    /**Unit of Measurement*/
    @Parameter(UnitsGetListConstants.MSEHI)
    private String _msehi;
    /**No. of decimal places to which rounding should be performed*/
    @Parameter(UnitsGetListConstants.ANDEC)
    private int _andec;
    /**Dimension key*/
    @Parameter(UnitsGetListConstants.DIMID)
    private String _dimid;
    /**Numerator for conversion to SI unit*/
    @Parameter(UnitsGetListConstants.ZAEHL)
    private long _zaehl;
    /**Denominator for conversion into SI unit*/
    @Parameter(UnitsGetListConstants.NENNR)
    private long _nennr;
    /**base ten exponent for conversion to SI unit*/
    @Parameter(UnitsGetListConstants.EXP10)
    private int _exp10;
    /**Additive constant for conversion to SI unit*/
    @Parameter(UnitsGetListConstants.ADDKO)
    private BigDecimal _addko;
    /**Number of decimal places for number display*/
    @Parameter(UnitsGetListConstants.DECAN)
    private int _decan;
    /**ISO code for unit of measurement*/
    @Parameter(UnitsGetListConstants.ISOCODE)
    private String _isocode;
    /**Objects to be processed*/
    @Parameter(UnitsGetListConstants.PRIMARY)
    private String _primary;
    /**Language Key*/
    @Parameter(UnitsGetListConstants.SPRAS)
    private String _spras;
    /**External Unit of Measurement in Commercial Format (3-Char.)*/
    @Parameter(UnitsGetListConstants.MSEH3)
    private String _mseh3;
    /**Unit of Measurement Text (Maximum 10 Characters)*/
    @Parameter(UnitsGetListConstants.MSEHT)
    private String _mseht;

    public UnitsGetListStructure() {
    }

    public UnitsGetListStructure(String msehi, int andec, String dimid,
            long zaehl, long nennr, int exp10, BigDecimal addko, int decan,
            String isocode, String primary, String spras, String mseh3, String mseht) {
        this._msehi = msehi;
        this._andec = andec;
        this._dimid = dimid;
        this._zaehl = zaehl;
        this._nennr = nennr;
        this._exp10 = exp10;
        this._addko = addko;
        this._decan = decan;
        this._isocode = isocode;
        this._primary = primary;
        this._spras = spras;
        this._mseh3 = mseh3;
        this._mseht = mseht;
    }

    /**
     * Unit of Measurement
     * @return the _msehi
     */
    public String getMsehi() {
        return _msehi;
    }

    /**
     * No. of decimal places to which rounding should be performed
     * @return the _andec
     */
    public int getAndec() {
        return _andec;
    }

    /**
     * Dimension key
     * @return the _dimid
     */
    public String getDimid() {
        return _dimid;
    }

    /**
     * Numerator for conversion to SI unit
     * @return the _zaehl
     */
    public long getZaehl() {
        return _zaehl;
    }

    /**
     * Denominator for conversion into SI unit
     * @return the _nennr
     */
    public long getNennr() {
        return _nennr;
    }

    /**
     * base ten exponent for conversion to SI unit
     * @return the _exp10
     */
    public int getExp10() {
        return _exp10;
    }

    /**
     * Additive constant for conversion to SI unit
     * @return the _addko
     */
    public BigDecimal getAddko() {
        return _addko;
    }

    /**
     * Number of decimal places for number display
     * @return the _decan
     */
    public int getDecan() {
        return _decan;
    }

    /**
     * ISO code for unit of measurement
     * @return the _isocode
     */
    public String getIsocode() {
        return _isocode;
    }

    /**
     * Objects to be processed
     * @return the _primary
     */
    public String getPrimary() {
        return _primary;
    }

    /**
     * Language Key
     * @return the _spras
     */
    public String getSpras() {
        return _spras;
    }

    /**
     * External Unit of Measurement in Commercial Format (3-Char.)
     * @return the _mseh3
     */
    public String getMseh3() {
        return _mseh3;
    }

    /**
     * Unit of Measurement Text (Maximum 10 Characters)
     * @return the _mseht
     */
    public String getMseht() {
        return _mseht;
    }
}
