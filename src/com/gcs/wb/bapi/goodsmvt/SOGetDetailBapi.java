/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt;

import com.gcs.wb.bapi.goodsmvt.constants.SOGetDetailConstants;
import com.gcs.wb.bapi.goodsmvt.structure.DOCheckStructure;
import com.gcs.wb.bapi.goodsmvt.structure.SOCheckStructure;
import java.io.Serializable;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;

/**
 *
 * @author HANGTT
 */
@Bapi(SOGetDetailConstants.BAPI_NAME)
//@ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
public class SOGetDetailBapi implements Serializable {
    @Table
    @Parameter(value = SOGetDetailConstants.IT_SO_CHECK, type = ParameterType.STRUCTURE)
    private List<SOCheckStructure> _it_so_check;
    @Table
    @Parameter(SOGetDetailConstants.TD_DO_CHECK)
    private List<DOCheckStructure> _td_do_check;
    
     public SOGetDetailBapi() {
     }
     
     /**
     * @param _it_so_check the _it_so_check to set
     */
    public List<SOCheckStructure> getSOCheck() {
        return _it_so_check;
    }
    
    /**
     * @param _it_so_check the _it_so_check to set
     */
    public void setSOCheck(List<SOCheckStructure> sOCheck) {
        this._it_so_check = sOCheck;
    }


    /**
     * @return the _td_do_check
     */
    public List<DOCheckStructure> getDOCheck() {
        return _td_do_check;
    }
}

