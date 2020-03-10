/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.outbdlv;

import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import com.gcs.wb.bapi.outbdlv.constants.DOCreate2PGIConstants;
import com.gcs.wb.bapi.outbdlv.constants.DOPostingPGIConstants;
import com.gcs.wb.bapi.outbdlv.constants.OutbDeliveryCreateStoConstants;
import com.gcs.wb.bapi.outbdlv.structure.VbkokStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbpokStructure;
import java.io.Serializable;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author HANGTT
 */
@Bapi(value = DOPostingPGIConstants.BAPI_NAME)
public class DOPostingPGIBapi implements Serializable {
    @Import
    @Parameter(value = DOPostingPGIConstants.IV_DELIVERY, type = ParameterType.SIMPLE)
    private String _delivery;
    @Import
    @Parameter(value = DOCreate2PGIConstants.VBKOK_WA, type = ParameterType.STRUCTURE)
    private VbkokStructure _vbkok_wa;
    @Import
    @Parameter(value = DOCreate2PGIConstants.I_WEIGHTTICKET, type = ParameterType.STRUCTURE)
    private GoodsMvtWeightTicketStructure _weightticket;
    @Import
    @Parameter(value = DOCreate2PGIConstants.IV_C_VENDOR, type = ParameterType.SIMPLE)
    private String _ivCVendor;
    @Import
    @Parameter(value = DOCreate2PGIConstants.IV_T_VENDOR, type = ParameterType.SIMPLE)
    private String _ivTVendor;
    
    @Export
    @Parameter(value = DOCreate2PGIConstants.MATERIALDOCUMENT, type = ParameterType.SIMPLE)
    private String _MatDoc;
    @Export
    @Parameter(value = DOCreate2PGIConstants.MATDOCUMENTYEAR, type = ParameterType.SIMPLE)
    private String _DocYear;
    @Table
    @Parameter(value = DOCreate2PGIConstants.VBPOK_TAB, type = ParameterType.STRUCTURE)
    private List<VbpokStructure> _vbpok_tab;
    @Table
    @Parameter(value = OutbDeliveryCreateStoConstants.RETURN, type = ParameterType.STRUCTURE)
    private List<BapiRet2> _Return;
    
    public DOPostingPGIBapi() {}
    
    /**
     * @param the _Delivery
     */
    public void setDelivery(String delivery) {
        this._delivery = delivery;
    }
    
    /**
     * @param vbkok_wa the _vbkok_wa to set
     */
    public void setVbkok_wa(VbkokStructure vbkok_wa) {
        this._vbkok_wa = vbkok_wa;
    }

    /**
     * @param vbpok_tab the _vbpok_tab to set
     */
    public void setVbpok_tab(List<VbpokStructure> vbpok_tab) {
        this._vbpok_tab = vbpok_tab;
    }

    /**
     * @return the _Return
     */
    public List<BapiRet2> getReturn() {
        return _Return;
    }

    /**
     * @return the _MatDoc
     */
    public String getMatDoc() {
        return _MatDoc;
    }

    /**
     * @return the _DocYear
     */
    public String getDocYear() {
        return _DocYear;
    }

    public GoodsMvtWeightTicketStructure getWeightticket() {
        return _weightticket;
    }

    public void setWeightticket(GoodsMvtWeightTicketStructure _weightticket) {
        this._weightticket = _weightticket;
    }

     /**
     * @param ivCVendor the _ivCVendor to set
     */
    public void setIvCVendor(String ivCVendor) {
        this._ivCVendor = ivCVendor;
    }

    /**
     * @param ivTVendor the _ivTVendor to set
     */
    public void setIvTVendor(String ivTVendor) {
        this._ivTVendor = ivTVendor;
    }

    @Override
    public String toString() {
        return "DOPostingPGIBapi{" + "_vbkok_wa=" + _vbkok_wa + ",_weightticket=" + _weightticket + ", _ivCVendor=" + _ivCVendor + ", _ivTVendor=" + _ivTVendor + '}';
    }
}
