/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt;

import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtItemPoStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtCodeStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtHeaderStructure;
import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtCreateConstants;
import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtPoCreateConstants;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author Tran-Vu
 */
@Bapi(GoodsMvtPoCreateConstants.BAPI_NAME)
@ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
public class GoodsMvtPoCreateBapi implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Import Parameters">
    @Import
    @Parameter(value = GoodsMvtCreateConstants.GOODSMVT_HEADER, type = ParameterType.STRUCTURE)
    private GoodsMvtHeaderStructure _header;
    @Import
    @Parameter(value = GoodsMvtCreateConstants.GOODSMVT_CODE, type = ParameterType.STRUCTURE)
    private GoodsMvtCodeStructure _gmCode;
    @Import
    @Parameter(value = GoodsMvtCreateConstants.I_WEIGHTTICKET, type = ParameterType.STRUCTURE)
    private GoodsMvtWeightTicketStructure _weightticket;
    @Import
    @Parameter(GoodsMvtCreateConstants.IV_WBID_NOSAVE)
    private String _ivWbidNoSave;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Export Parameters">
    @Export
    @Parameter(GoodsMvtCreateConstants.MATERIALDOCUMENT)
    private String _matDoc;
    @Export
    @Parameter(GoodsMvtCreateConstants.MATDOCUMENTYEAR)
    private String _matYear;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">
    @Table
    @Parameter(GoodsMvtCreateConstants.GOODSMVT_ITEM)
    private List<GoodsMvtItemPoStructure> _items;
    @Table
    @Parameter(GoodsMvtCreateConstants.RETURN)
    private List<BapiRet2> _return;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Class Constructors">
    public GoodsMvtPoCreateBapi() {
        this._header = new GoodsMvtHeaderStructure();
        this._gmCode = new GoodsMvtCodeStructure();
        this._matDoc = null;
        this._matYear = null;
        this._items = new ArrayList<GoodsMvtItemPoStructure>();
    }

    public GoodsMvtPoCreateBapi(GoodsMvtCodeStructure gmcode) {
        this._header = new GoodsMvtHeaderStructure();
        this._gmCode = gmcode;
        this._matDoc = null;
        this._matYear = null;
        this._items = new ArrayList<GoodsMvtItemPoStructure>();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Field Getter/Setter">
    /**
     * @param header the _header to set
     */
    public void setHeader(GoodsMvtHeaderStructure header) {
        this._header = header;
    }

    /**
     * @return the _matDoc
     */
    public String getMatDoc() {
        return _matDoc;
    }

    /**
     * @return the _matYear
     */
    public String getMatYear() {
        return _matYear;
    }

    /**
     * @return the _items
     */
    public List<GoodsMvtItemPoStructure> getItems() {
        return _items;
    }

    /**
     * @param items the _items to set
     */
    public void setItems(List<GoodsMvtItemPoStructure> items) {
        this._items = items;
    }

    /**
     * @return the _return
     */
    public List<BapiRet2> getReturn() {
        return _return;
    }

    /**
     * @param gmCode the _gmCode to set
     */
    public void setGmCode(GoodsMvtCodeStructure gmCode) {
        this._gmCode = gmCode;
    }
    
    
    // </editor-fold>

    public GoodsMvtWeightTicketStructure getWeightticket() {
        return _weightticket;
    }

    public void setWeightticket(GoodsMvtWeightTicketStructure _weightticket) {
        this._weightticket = _weightticket;
    }

    public void setIvWbidNoSave(String ivWbidNoSave) {
        this._ivWbidNoSave = ivWbidNoSave;
    }
    
    public String getReturnMessage() {
        if (_return == null || _return.isEmpty()) {
            return null;
        }
        
        BapiRet2 bapiRet2 = _return.get(0);
        if (bapiRet2.getMessage() != null && !bapiRet2.getMessage().trim().isEmpty()) {
            return bapiRet2.getMessage().trim();
        }
        String msg = "";
        if(bapiRet2.getMessageV1() != null && !bapiRet2.getMessageV1().trim().isEmpty() ) {
            msg += bapiRet2.getMessageV1() + " ";
        }
        if(bapiRet2.getMessageV2() != null && !bapiRet2.getMessageV2().trim().isEmpty() ) {
            msg += bapiRet2.getMessageV2() + " ";
        }
        if(bapiRet2.getMessageV3() != null && !bapiRet2.getMessageV3().trim().isEmpty() ) {
            msg += bapiRet2.getMessageV3() + " ";
        }
        if(bapiRet2.getMessageV4() != null && !bapiRet2.getMessageV4().trim().isEmpty() ) {
            msg += bapiRet2.getMessageV4() + " ";
        }
        return msg.trim();
    }

    @Override
    public String toString() {
        return "GoodsMvtPoCreateBapi{" + "_header=" + _header + ", _gmCode=" + _gmCode + ", _weightticket=" + _weightticket + ", _ivWbidNoSave=" + _ivWbidNoSave + '}';
    }
}
