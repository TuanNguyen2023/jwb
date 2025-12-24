/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt.structure;

import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtCreateConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class GoodsMvtCodeStructure implements Serializable {

    @Parameter(GoodsMvtCreateConstants.GM_CODE)
    private String _gmCode = "01";

    public GoodsMvtCodeStructure() {
    }
    
    public GoodsMvtCodeStructure(String gmCode) {
        this._gmCode = gmCode;
    }

    /**
     * @return the _gmCode
     */
    public String getGmCode() {
        return _gmCode;
    }

    /**
     * @param gmCode the _gmCode to set
     */
    public void setGmCode(String gmCode) {
        this._gmCode = gmCode;
    }
}
