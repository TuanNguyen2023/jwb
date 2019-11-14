/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.SLocsGetListConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class SLocsGetListStructure implements Serializable {

    @Parameter(SLocsGetListConstants.MANDT)
    private String _mandt;
    @Parameter(SLocsGetListConstants.WERKS)
    private String _werks;
    @Parameter(SLocsGetListConstants.LGORT)
    private String _lgort;
    @Parameter(SLocsGetListConstants.LGOBE)
    private String _lgobe;

    public SLocsGetListStructure() {
    }

    public SLocsGetListStructure(String mandt, String werks, String lgort, String lgobe) {
        this._mandt = mandt;
        this._werks = werks;
        this._lgort = lgort;
        this._lgobe = lgobe;
    }

    /**
     * @return the _mandt
     */
    public String getMandt() {
        return _mandt;
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
     * @return the _lgobe
     */
    public String getLgobe() {
        return _lgobe;
    }
}
