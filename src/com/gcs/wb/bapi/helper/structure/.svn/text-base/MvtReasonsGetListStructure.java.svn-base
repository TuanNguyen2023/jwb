/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.MvtReasonsGetListConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class MvtReasonsGetListStructure implements Serializable {

    @Parameter(MvtReasonsGetListConstants.MANDT)
    private String _mandt;
    @Parameter(MvtReasonsGetListConstants.BWART)
    private String _bwart;
    @Parameter(MvtReasonsGetListConstants.GRUND)
    private String _grund;
    @Parameter(MvtReasonsGetListConstants.SPRAS)
    private String _spras;
    @Parameter(MvtReasonsGetListConstants.GRTXT)
    private String _grtxt;

    public MvtReasonsGetListStructure() {
    }

    public MvtReasonsGetListStructure(String mandt, String spras, String bwart, String grund, String grtxt) {
        this._mandt = mandt;
        this._spras = spras;
        this._bwart = bwart;
        this._grund = grund;
        this._grtxt = grtxt;
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
     * @return the _spras
     */
    public String getSpras() {
        return _spras;
    }

    /**
     * @param spras the _spras to set
     */
    public void setSpras(String spras) {
        this._spras = spras;
    }

    /**
     * @return the _bwart
     */
    public String getBwart() {
        return _bwart;
    }

    /**
     * @param bwart the _bwart to set
     */
    public void setBwart(String bwart) {
        this._bwart = bwart;
    }

    /**
     * @return the _grund
     */
    public String getGrund() {
        return _grund;
    }

    /**
     * @param grund the _grund to set
     */
    public void setGrund(String grund) {
        this._grund = grund;
    }

    /**
     * @return the _grtxt
     */
    public String getGrtxt() {
        return _grtxt;
    }

    /**
     * @param grtxt the _grtxt to set
     */
    public void setGrtxt(String grtxt) {
        this._grtxt = grtxt;
    }
}
