/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.outbdlv.structure;

import com.gcs.wb.bapi.outbdlv.constants.OutbDeliveryCreateStoConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.BapiStructure;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class OutbDeliveryCreateStoStructure implements Serializable {

    private static final long serialVersionUID = 1L;
    @Parameter(OutbDeliveryCreateStoConstants.REF_DOC)
    private String _RefDoc;
    @Parameter(OutbDeliveryCreateStoConstants.REF_ITEM)
    private String _RefItem;
    @Parameter(OutbDeliveryCreateStoConstants.DLV_QTY)
    private BigDecimal _DlvQty;
    @Parameter(OutbDeliveryCreateStoConstants.SALES_UNIT)
    private String _SalesUnit;
    @Parameter(OutbDeliveryCreateStoConstants.SALES_UNIT_ISO)
    private String _SalesUnitIso;

    public OutbDeliveryCreateStoStructure() {
    }

    public OutbDeliveryCreateStoStructure(
            String refDoc, String refItem,
            BigDecimal dlvQty, String salesUnitIso) {
        this._RefDoc = refDoc;
        this._RefItem = refItem;
        this._DlvQty = dlvQty;
        this._SalesUnit = salesUnitIso;
//        this._SalesUnitIso = salesUnitIso;
    }

    /**
     * @return the _RefDoc
     */
    public String getRefDoc() {
        return _RefDoc;
    }

    /**
     * @param RefDoc the _RefDoc to set
     */
    public void setRefDoc(String RefDoc) {
        this._RefDoc = RefDoc;
    }

    /**
     * @return the _RefItem
     */
    public String getRefItem() {
        return _RefItem;
    }

    /**
     * @param RefItem the _RefItem to set
     */
    public void setRefItem(String RefItem) {
        this._RefItem = RefItem;
    }

    /**
     * @return the _DlvQty
     */
    public BigDecimal getDlvQty() {
        return _DlvQty;
    }

    /**
     * @param DlvQty the _DlvQty to set
     */
    public void setDlvQty(BigDecimal DlvQty) {
        this._DlvQty = DlvQty;
    }

    /**
     * @return the _SalesUnit
     */
    public String getSalesUnit() {
        return _SalesUnit;
    }

    /**
     * @param SalesUnit the _SalesUnit to set
     */
    public void setSalesUnit(String SalesUnit) {
        this._SalesUnit = SalesUnit;
    }

    /**
     * @return the _SalesUnitIso
     */
    public String getSalesUnitIso() {
        return _SalesUnitIso;
    }

    /**
     * @param SalesUnitIso the _SalesUnitIso to set
     */
    public void setSalesUnitIso(String SalesUnitIso) {
        this._SalesUnitIso = SalesUnitIso;
    }
}
