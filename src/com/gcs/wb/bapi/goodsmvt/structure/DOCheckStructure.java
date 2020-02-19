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
public class DOCheckStructure implements Serializable {
    // <editor-fold defaultstate="collapsed" desc="Structure Fields">
    /**Sales and Distribution Document Number*/
    @Parameter(SOGetDetailConstants.VBELN_SO)
    private String _vbelnSO;
    
     /**Sales and Distribution Document Number*/
    @Parameter(SOGetDetailConstants.VBELN_DO)
    private String _vbelnDO;
    
     /**Message Text*/
    @Parameter(SOGetDetailConstants.MESSAGE)
    private String _message;
    
    // </editor-fold>
    
    public DOCheckStructure() {
        this._vbelnSO = null;
        this._vbelnDO = null;
        this._message = null;
    }

    // <editor-fold defaultstate="collapsed" desc="Field Getter/Setter">

    /**
     * @return the _vbelnSO
     */
    public String getVbelnSO() {
        return _vbelnSO;
    }

    /**
     * @param vbelnSO the _vbelnSO to set
     */
    public void setVbelnSO(String vbelnSO) {
        this._vbelnSO = vbelnSO;
    }

    /**
     * @return the _vbelnDO
     */
    public String getVbelnDO() {
        return _vbelnDO;
    }

    /**
     * @param vbelnDO the _vbelnDO to set
     */
    public void setVbelnDO(String vbelnDO) {
        this._vbelnDO = vbelnDO;
    }

    /**
     * @return the _message
     */
    public String getMessage() {
        return _message;
    }

    /**
     * @param message the _message to set
     */
    public void setMessage(String message) {
        this._message = message;
    }

    // </editor-fold>

}
