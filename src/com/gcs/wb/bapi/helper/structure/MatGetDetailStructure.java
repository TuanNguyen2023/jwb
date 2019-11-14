/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.MatGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class MatGetDetailStructure implements Serializable {

    /**Client*/
    @Parameter(MatGetDetailConstants.MANDT)
    private String _mandt = null;
    /**Material Number*/
    @Parameter(MatGetDetailConstants.MATNR)
    private String _matnr = null;
    /**Material Description (Short Text)*/
    @Parameter(MatGetDetailConstants.MAKTX)
    private String _maktx = null;
    /**Material description in upper case for matchcodes*/
    @Parameter(MatGetDetailConstants.MAKTG)
    private String _maktg = null;
    /**Batch management requirement indicator*/
    @Parameter(MatGetDetailConstants.XCHPF)
    private String _xchpf = null;

    public MatGetDetailStructure() {
    }

    /**
     * Client
     * @return the _mandt
     */
    public String getMandt() {
        return _mandt;
    }

    /**
     * Material Number
     * @return the _matnr
     */
    public String getMatnr() {
        return _matnr;
    }

    /**
     * Material Description (Short Text)
     * @return the _maktx
     */
    public String getMaktx() {
        return _maktx;
    }

    /**
     * Material description in upper case for matchcodes
     * @return the _maktg
     */
    public String getMaktg() {
        return _maktg;
    }

    /**
     * Batch management requirement indicator
     * @return the _xchpf
     */
    public String getXchpf() {
        return _xchpf;
    }
}
