/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.SaleOrderGetDetailConstants;
import com.gcs.wb.bapi.helper.structure.SaleOrderGetDetailStructure;
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
    private SaleOrderGetDetailStructure _exDeliSched;

    @Export
    @Parameter(value = SaleOrderGetDetailConstants.EX_RETURN, type = ParameterType.STRUCTURE)
    private BapiRet2 _exReturn;

//    @Table
//    @Parameter(value = SaleOrderGetDetailConstants.T_RETURN, type = ParameterType.STRUCTURE)
//    private BapiRet2 _tReturn;
    public SaleOrderGetDetailBapi() {
    }

    public void setImVbeln(String imVbeln) {
        this._imVbeln = imVbeln;
    }

    public SaleOrderGetDetailStructure getExDeliSched() {
        return _exDeliSched;
    }

    public BapiRet2 getExReturn() {
        return _exReturn;
    }

    public String getReturnMessage() {
        String errorMsgs = "";
        if (_exReturn == null) {
            return errorMsgs;
        }

        if (_exReturn.getMessage() != null && !_exReturn.getMessage().trim().isEmpty()) {
            return _exReturn.getMessage().trim();
        }

        String msg = "";
        if (_exReturn.getMessageV1() != null && !_exReturn.getMessageV1().trim().isEmpty()) {
            msg += _exReturn.getMessageV1() + " ";
        }
        if (_exReturn.getMessageV2() != null && !_exReturn.getMessageV2().trim().isEmpty()) {
            msg += _exReturn.getMessageV2() + " ";
        }
        if (_exReturn.getMessageV3() != null && !_exReturn.getMessageV3().trim().isEmpty()) {
            msg += _exReturn.getMessageV3() + " ";
        }
        if (_exReturn.getMessageV4() != null && !_exReturn.getMessageV4().trim().isEmpty()) {
            msg += _exReturn.getMessageV4() + " ";
        }
        return msg;

    }

    @Override
    public String toString() {
        return "SaleOrderGetDetailBapi{" + "_imVbeln=" + _imVbeln + '}';
    }
}
