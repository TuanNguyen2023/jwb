/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt;

import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtItemDoStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtCodeStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtHeaderStructure;
import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtCreateConstants;
import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtDoCreateConstants;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
 * @author vunguyent
 */
@Bapi(GoodsMvtDoCreateConstants.BAPI_NAME)
@ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
public class GoodsMvtDoCreateBapi implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Import Parameters">
    @Import
    @Parameter(value = GoodsMvtCreateConstants.GOODSMVT_HEADER, type = ParameterType.STRUCTURE)
    private GoodsMvtHeaderStructure _header;
    @Import
    @Parameter(value = GoodsMvtCreateConstants.GOODSMVT_CODE, type = ParameterType.STRUCTURE)
    private final GoodsMvtCodeStructure _gmCode;
    @Import
    @Parameter(value = GoodsMvtCreateConstants.I_WEIGHTTICKET, type = ParameterType.STRUCTURE)
    private GoodsMvtWeightTicketStructure _weightticket;
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
    private List<GoodsMvtItemDoStructure> _items;

    @Table
    @Parameter(GoodsMvtCreateConstants.RETURN)
    private List<BapiRet2> _return;
    // </editor-fold>

    public GoodsMvtDoCreateBapi() {
        _header = new GoodsMvtHeaderStructure();
        _gmCode = new GoodsMvtCodeStructure();
        _matDoc = null;
        _matYear = null;
        _items = new ArrayList<GoodsMvtItemDoStructure>();
        _weightticket = null;
    }

    // <editor-fold defaultstate="collapsed" desc="Field Getter/Setter">
    /**
     * @return the _header
     */
    public GoodsMvtHeaderStructure getHeader() {
        return _header;
    }

    /**
     * @param header the _header to set
     */
    public void setHeader(GoodsMvtHeaderStructure header) {
        this._header = header;
    }

    /**
     * @return the _gmCode
     */
    public GoodsMvtCodeStructure getGmCode() {
        return _gmCode;
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
    public List<GoodsMvtItemDoStructure> getItems() {
        return _items;
    }

    /**
     * @param items the _items to set
     */
    public void setItems(List<GoodsMvtItemDoStructure> items) {
        this._items = items;
    }

    /**
     * @return the _return
     */
    public List<BapiRet2> getReturn() {
        return _return;
    }
    
    // </editor-fold>

    public GoodsMvtWeightTicketStructure getWeightticket() {
        return _weightticket;
    }

    public void setWeightticket(GoodsMvtWeightTicketStructure stWT) {
        _weightticket = stWT;
    }
    
    public List<String> getReturnMessage() {
        List<String> errorMsgs = new ArrayList<>();
        if (_return == null || _return.isEmpty()) {
            return errorMsgs;
        }
        
        return _return.stream().map(bapiRet2 -> {
            if (bapiRet2.getType() != 'E') {
                return null;
            }

            if (bapiRet2.getMessage() != null && !bapiRet2.getMessage().trim().isEmpty()) {
                return bapiRet2.getMessage().trim();
            }

            String msg = "";
            if (bapiRet2.getMessageV1() != null && !bapiRet2.getMessageV1().trim().isEmpty()) {
                msg += bapiRet2.getMessageV1() + " ";
            }
            if (bapiRet2.getMessageV2() != null && !bapiRet2.getMessageV2().trim().isEmpty()) {
                msg += bapiRet2.getMessageV2() + " ";
            }
            if (bapiRet2.getMessageV3() != null && !bapiRet2.getMessageV3().trim().isEmpty()) {
                msg += bapiRet2.getMessageV3() + " ";
            }
            if (bapiRet2.getMessageV4() != null && !bapiRet2.getMessageV4().trim().isEmpty()) {
                msg += bapiRet2.getMessageV4() + " ";
            }

            return msg;
        }).filter(t -> t != null).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "GoodsMvtDoCreateBapi{" + "_header=" + _header + ", _gmCode=" + _gmCode + ", _weightticket=" + _weightticket + '}';
    }
}
