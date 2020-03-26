/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt;

import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtItemPoStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtCodeStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtHeaderStructure;
import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtCreateConstants;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import java.io.Serializable;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.bapi.BapiRet2;
import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtPOSTOCreatePGIConstants;
import com.gcs.wb.bapi.outbdlv.structure.IsExtensionStructure;
import com.gcs.wb.bapi.outbdlv.structure.OutbDeliveryCreateStoStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbkokStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbpokStructure;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author Hang Tran
 */
@Bapi(GoodsMvtPOSTOCreatePGIConstants.BAPI_NAME)
public class GoodsMvtPOSTOCreatePGIBapi implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Import Parameters">
    @Import
    @Parameter(value = GoodsMvtCreateConstants.GOODSMVT_HEADER, type = ParameterType.STRUCTURE)
    private GoodsMvtHeaderStructure _header;
    @Import
    @Parameter(value = GoodsMvtCreateConstants.GOODSMVT_CODE, type = ParameterType.STRUCTURE)
    private GoodsMvtCodeStructure _gmCode;
    @Import
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.VBKOK_WA, type = ParameterType.STRUCTURE)
    private VbkokStructure _vbkok_wa;
    @Import
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.IV_C_VENDOR, type = ParameterType.SIMPLE)
    private String _ivCVendor;
    @Import
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.IV_T_VENDOR, type = ParameterType.SIMPLE)
    private String _ivTVendor;
    @Import
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.ID_PARNR, type = ParameterType.SIMPLE)
    private String _idParnr;
    @Import
    @Parameter(value = GoodsMvtCreateConstants.I_WEIGHTTICKET, type = ParameterType.STRUCTURE)
    private GoodsMvtWeightTicketStructure _weightticket;
    @Import
    @Parameter(value = GoodsMvtCreateConstants.IS_EXTENSION, type = ParameterType.STRUCTURE)
    private IsExtensionStructure _isExtension;
    @Import
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.IV_MATERIALDOCUMENT_INPUT, type = ParameterType.SIMPLE)
    private String _ivMaterialDocument;
    @Import
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.IV_MATDOCUMENTYEAR_INPUT, type = ParameterType.SIMPLE)
    private String _ivMatDocumentYear;
    @Import
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.IV_RETYPE_MODE, type = ParameterType.SIMPLE)
    private String _ivReType;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Export Parameters">
    @Export
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.DELIVERY, type = ParameterType.SIMPLE)
    private String _Delivery;
    @Export
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.NUM_DELIVERIES, type = ParameterType.SIMPLE)
    private String _NumDeliveries;
    @Export
    @Parameter(GoodsMvtCreateConstants.MATERIALDOCUMENT)
    private String _matDoc;
    @Export
    @Parameter(GoodsMvtCreateConstants.MATDOCUMENTYEAR)
    private String _matYear;
    @Export
    @Parameter(GoodsMvtPOSTOCreatePGIConstants.MATERIALDOCUMENT_INPUT)
    private String _materialDocumentOut;
    @Export
    @Parameter(GoodsMvtPOSTOCreatePGIConstants.MATDOCUMENTYEAR_INPUT)
    private String _matDocumentYearOut;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">
    @Table
    @Parameter(GoodsMvtCreateConstants.GOODSMVT_ITEM)
    private List<GoodsMvtItemPoStructure> _items;
    @Table
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.STOCK_TRANS_ITEMS, type = ParameterType.STRUCTURE)
    private List<OutbDeliveryCreateStoStructure> _StockTransItems;
    @Table
    @Parameter(value = GoodsMvtPOSTOCreatePGIConstants.VBPOK_TAB, type = ParameterType.STRUCTURE)
    private List<VbpokStructure> _vbpok_tab;
    @Table
    @Parameter(GoodsMvtCreateConstants.RETURN)
    private List<BapiRet2> _return;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Class Constructors">
    public GoodsMvtPOSTOCreatePGIBapi() {
    }

    public GoodsMvtPOSTOCreatePGIBapi(GoodsMvtCodeStructure gmcode) {
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
     * @param gmCode the _gmCode to set
     */
    public void setGmCode(GoodsMvtCodeStructure gmCode) {
        this._gmCode = gmCode;
    }

    /**
     * @param vbkok_wa the _vbkok_wa to set
     */
    public void setVbkok_wa(VbkokStructure vbkok_wa) {
        this._vbkok_wa = vbkok_wa;
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

    /**
     * @param idParnr the _idParnr to set
     */
    public void setIdParnr(String idParnr) {
        this._idParnr = idParnr;
    }

    // </editor-fold>
    public GoodsMvtWeightTicketStructure getWeightticket() {
        return _weightticket;
    }

    public void setWeightticket(GoodsMvtWeightTicketStructure _weightticket) {
        this._weightticket = _weightticket;
    }

    /**
     * @return the _Delivery
     */
    public String getDelivery() {
        return _Delivery;
    }

    /**
     * @return the _NumDeliveries
     */
    public String getNumDeliveries() {
        return _NumDeliveries;
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
     * @param StockTransItems the _StockTransItems to set
     */
    public void setStockTransItems(List<OutbDeliveryCreateStoStructure> StockTransItems) {
        this._StockTransItems = StockTransItems;
    }

    /**
     * @param vbpok_tab the _vbpok_tab to set
     */
    public void setVbpok_tab(List<VbpokStructure> vbpok_tab) {
        this._vbpok_tab = vbpok_tab;
    }

    public void setIvMaterialDocument(String ivMaterialDocument) {
        this._ivMaterialDocument = ivMaterialDocument;
    }

    public void setIvMatDocumentYear(String ivMatDocumentYear) {
        this._ivMatDocumentYear = ivMatDocumentYear;
    }

    public void setIvReType(String ivReType) {
        this._ivReType = ivReType;
    }

    public String getMaterialDocumentOut() {
        return _materialDocumentOut;
    }

    public String getMatDocumentYearOut() {
        return _matDocumentYearOut;
    }

    /**
     * @return the _return
     */
    public List<BapiRet2> getReturn() {
        return _return;
    }

    public List<String> getReturnMessage() {
        List<String> errorMsgs = new ArrayList<>();
        if (_return == null || _return.isEmpty()) {
            return errorMsgs;
        }
        List<String> errors = _return.stream().map(bapiRet2 -> {
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

        List<String> normals = _return.stream().map(bapiRet2 -> {
            if (bapiRet2.getType() == 'E') {
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

        return errors.size() > 0 ? errors : normals;
    }

    public IsExtensionStructure getIsExtension() {
        return _isExtension;
    }

    public void setIsExtension(IsExtensionStructure _isExtension) {
        this._isExtension = _isExtension;
    }

    @Override
    public String toString() {
        return "GoodsMvtPOSTOCreatePGIBapi{" + "_header=" + _header + ", _gmCode=" + _gmCode + ", _vbkok_wa=" + _vbkok_wa + ", _ivCVendor=" + _ivCVendor + ", _ivTVendor=" + _ivTVendor + ", _idParnr=" + _idParnr + ", _weightticket=" + _weightticket + '}';
    }

}
