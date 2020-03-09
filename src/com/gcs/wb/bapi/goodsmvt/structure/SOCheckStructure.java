/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt.structure;

import com.gcs.wb.bapi.goodsmvt.constants.SOGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author HANGTT
 */
@BapiStructure
public class SOCheckStructure implements Serializable {
    // <editor-fold defaultstate="collapsed" desc="Structure Fields">
    /**SO Number to search*/
    @Parameter(SOGetDetailConstants.VBELN)
    private String _vbeln;
    
     /**Transport ID to search*/
    @Parameter(SOGetDetailConstants.TRAID)
    private String _traid;
    
     /**Romooc to search*/
    @Parameter(SOGetDetailConstants.TRAILER)
    private String _trailer;

    /** plant */
    @Parameter(SOGetDetailConstants.WERKS)
    private String _werks;
    
    // </editor-fold>
    
    public SOCheckStructure() {
    }

    // <editor-fold defaultstate="collapsed" desc="Field Getter/Setter">

    /**
     * @return the _vbeln
     */
    public String getVbeln() {
        return _vbeln;
    }

    /**
     * @param vbeln the _vbeln to set
     */
    public void setVbeln(String vbeln) {
        this._vbeln = vbeln;
    }

    /**
     * @return the _traid
     */
    public String getTraid() {
        return _traid;
    }

    /**
     * @param traid the _traid to set
     */
    public void setTraid(String traid) {
        this._traid = traid;
    }

    /**
     * @return the _trailer
     */
    public String getTrailer() {
        return _trailer;
    }

    /**
     * @param trailer the _trailer to set
     */
    public void setTrailer(String trailer) {
        this._trailer = trailer;
    }

    /**
     * @param _werks
     */
    public void setWerks(String _werks) {
        this._werks = _werks;
    }

    // </editor-fold>

}
