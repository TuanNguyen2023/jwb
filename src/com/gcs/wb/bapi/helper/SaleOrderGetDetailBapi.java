/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.SaleOrderGetDetailConstants;
import com.gcs.wb.bapi.helper.structure.SaleOrderGetDetailStructure;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author HANGTT
 */
@Bapi(SaleOrderGetDetailConstants.BAPI_NAME)
public class SaleOrderGetDetailBapi {

    @Import
    @Parameter(SaleOrderGetDetailConstants.IM_VBELN)
    private String _imVbeln;

    @Export
    @Parameter(value = SaleOrderGetDetailConstants.EX_DELI_SCHED, type = ParameterType.STRUCTURE)
    private List<SaleOrderGetDetailStructure> _exDeliSched;

    @Export
    @Parameter(value = SaleOrderGetDetailConstants.EX_RETURN, type = ParameterType.STRUCTURE)
    private List<BapiRet2> _exReturn;

//    @Table
//    @Parameter(value = SaleOrderGetDetailConstants.T_RETURN, type = ParameterType.STRUCTURE)
//    private List<BapiRet2> _tReturn;
    public SaleOrderGetDetailBapi() {
    }

    public void setImVbeln(String imVbeln) {
        this._imVbeln = imVbeln;
    }

    public List<SaleOrderGetDetailStructure> getExDeliSched() {
        return _exDeliSched;
    }

    public List<BapiRet2> getExReturn() {
        return _exReturn;
    }

    @Override
    public String toString() {
        return "SaleOrderGetDetailBapi{" + "_imVbeln=" + _imVbeln + '}';
    }
}
