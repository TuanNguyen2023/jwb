/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.BatchStocksGetListConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class BatchStocksStructure implements Serializable {

    @Parameter(BatchStocksGetListConstants.MANDT)
    private String _mandt;
    @Parameter(BatchStocksGetListConstants.MATNR)
    private String _matnr;
    @Parameter(BatchStocksGetListConstants.WERKS)
    private String _werks;
    @Parameter(BatchStocksGetListConstants.LGORT)
    private String _lgort;
    @Parameter(BatchStocksGetListConstants.CHARG)
    private String _charg;
    @Parameter(BatchStocksGetListConstants.LVORM)
    private String _lvorm;

    public BatchStocksStructure() {
    }

    public BatchStocksStructure(
            String mandt,
            String matnr,
            String werks,
            String lgort,
            String charg,
            String lvorm) {
        this._mandt = mandt;
        this._matnr = matnr;
        this._werks = werks;
        this._lgort = lgort;
        this._charg = charg;
        this._lvorm = lvorm;
    }

    /**
     * @return the _mandt
     */
    public String getMandt() {
        return _mandt;
    }

    /**
     * @param mandt the _mandt to set
     */
    public void setMandt(String mandt) {
        this._mandt = mandt;
    }

    /**
     * @return the _matnr
     */
    public String getMatnr() {
        return _matnr;
    }

    /**
     * @param matnr the _matnr to set
     */
    public void setMatnr(String matnr) {
        this._matnr = matnr;
    }

    /**
     * @return the _werks
     */
    public String getWerks() {
        return _werks;
    }

    /**
     * @param werks the _werks to set
     */
    public void setWerks(String werks) {
        this._werks = werks;
    }

    /**
     * @return the _lgort
     */
    public String getLgort() {
        return _lgort;
    }

    /**
     * @param lgort the _lgort to set
     */
    public void setLgort(String lgort) {
        this._lgort = lgort;
    }

    /**
     * @return the _charg
     */
    public String getCharg() {
        return _charg;
    }

    /**
     * @param charg the _charg to set
     */
    public void setCharg(String charg) {
        this._charg = charg;
    }

    /**
     * @return the _lvorm
     */
    public String getLvorm() {
        return _lvorm;
    }

    /**
     * @param lvorm the _lvorm to set
     */
    public void setLvorm(String lvorm) {
        this._lvorm = lvorm;
    }
    
}
