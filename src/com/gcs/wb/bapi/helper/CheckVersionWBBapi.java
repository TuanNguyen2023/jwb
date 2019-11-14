/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.CheckVersionWBConstants;
import com.gcs.wb.bapi.helper.constants.UserGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author vunguyent
 */
@Bapi(CheckVersionWBConstants.BAPI_NAME)
public class CheckVersionWBBapi implements Serializable {

    /**User Name (Import Parameter)*/
    @Import
    @Parameter(CheckVersionWBConstants.versionid)
    private String versionID = null;
    /**Address (Export Parameter)*/
    @Export
    @Parameter(value = CheckVersionWBConstants.result, type = ParameterType.STRUCTURE)
    private BapiRet2 result;

    public BapiRet2 getResult() {
        return result;
    }

    public void setResult(BapiRet2 result) {
        this.result = result;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public CheckVersionWBBapi(String versionID) {
        this.versionID = versionID;
    }
    /**Activity Group (Table Parameter)*/
    
}
