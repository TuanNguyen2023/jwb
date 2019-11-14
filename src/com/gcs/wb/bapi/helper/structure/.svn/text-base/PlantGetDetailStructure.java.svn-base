/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.PlantGeDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class PlantGetDetailStructure implements Serializable {

    /**Client*/
    @Parameter(PlantGeDetailConstants.MANDT)
    private String _mandt;
    /**Plant*/
    @Parameter(PlantGeDetailConstants.WERKS)
    private String _werks;
    /**Name 1*/
    @Parameter(PlantGeDetailConstants.NAME1)
    private String _name1;
    /**Name 2*/
    @Parameter(PlantGeDetailConstants.NAME2)
    private String _name2;
    /**Valuation Area*/
    @Parameter(PlantGeDetailConstants.BWKEY)
    private String _bwkey;
    /**Customer Number 1*/
    @Parameter(PlantGeDetailConstants.KUNNR)
    private String _kunnr;
    /**Account Number of Vendor or Creditor*/
    @Parameter(PlantGeDetailConstants.LIFNR)
    private String _lifnr;

    public PlantGetDetailStructure() {
    }

    public PlantGetDetailStructure(
            String mandt, String werks, String name1, String bwkey, String kunnr, String lifnr) {
        this._mandt = mandt;
        this._werks = werks;
        this._name1 = name1;
        this._bwkey = bwkey;
        this._kunnr = kunnr;
        this._lifnr = lifnr;
    }

    /**
     * Client
     * @return the _mandt
     */
    public String getMandt() {
        return _mandt;
    }

    /**
     * Plant
     * @return the _werks
     */
    public String getWerks() {
        return _werks;
    }

    /**
     * Name 1
     * @return the _name1
     */
    public String getName1() {
        return _name1;
    }

    /**
     * Name 2
     * @return the _name2
     */
    public String getName2() {
        return _name2;
    }

    /**
     * Valuation Area
     * @return the _bwkey
     */
    public String getBwkey() {
        return _bwkey;
    }

    /**
     * Customer Number 1
     * @return the _kunnr
     */
    public String getKunnr() {
        return _kunnr;
    }

    /**
     * Account Number of Vendor or Creditor
     * @return the _lifnr
     */
    public String getLifnr() {
        return _lifnr;
    }
}
