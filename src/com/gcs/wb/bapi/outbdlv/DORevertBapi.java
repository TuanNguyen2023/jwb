/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.outbdlv;

import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import com.gcs.wb.bapi.outbdlv.constants.DORevertConstants;
import java.io.Serializable;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author J
 */
@Bapi(DORevertConstants.BAPI_NAME)
@ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
public class DORevertBapi implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Import Parameters">
    /**Delivery Number*/
    @Import
    @Parameter(value = DORevertConstants.IV_DELIVERY)
    private String _iv_delivery;
    @Import
    @Parameter(value = DORevertConstants.I_WEIGHTTICKET, type = ParameterType.STRUCTURE)
    private GoodsMvtWeightTicketStructure _weightticket;
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">
    /**Return error*/
    @Table    
    @Parameter(value = DORevertConstants.RETURN, type = ParameterType.STRUCTURE)
    private List<BapiRet2> _return;
    // </editor-fold>    

    public String getIv_delivery() {
        return _iv_delivery;
    }

    public void setIv_delivery(String _iv_delivery) {
        this._iv_delivery = _iv_delivery;
    }

    public List<BapiRet2> getReturn() {
        return _return;
    }

    public void setReturn(List<BapiRet2> _return) {
        this._return = _return;
    }

    public DORevertBapi() {
    }

    
    public DORevertBapi(String _iv_delivery) {
        this._iv_delivery = _iv_delivery;
    }

    public GoodsMvtWeightTicketStructure getWeightticket() {
        return _weightticket;
    }

    public void setWeightticket(GoodsMvtWeightTicketStructure _weightticket) {
        this._weightticket = _weightticket;
    }
    
}
