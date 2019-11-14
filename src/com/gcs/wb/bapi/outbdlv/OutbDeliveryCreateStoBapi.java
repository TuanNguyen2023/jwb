/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.outbdlv;

import com.gcs.wb.bapi.outbdlv.structure.OutbDeliveryCreateStoStructure;
import com.gcs.wb.bapi.outbdlv.constants.OutbDeliveryCreateStoConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author Tran-Vu
 */
@Bapi(OutbDeliveryCreateStoConstants.BAPI_NAME)
@ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
public class OutbDeliveryCreateStoBapi implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Export Parameters">
    @Export
    @Parameter(value = OutbDeliveryCreateStoConstants.DELIVERY, type = ParameterType.SIMPLE)
    private String _Delivery;
    @Export
    @Parameter(value = OutbDeliveryCreateStoConstants.NUM_DELIVERIES, type = ParameterType.SIMPLE)
    private String _NumDeliveries;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">
    @Table
    @Parameter(value = OutbDeliveryCreateStoConstants.STOCK_TRANS_ITEMS, type = ParameterType.STRUCTURE)
    private List<OutbDeliveryCreateStoStructure> _StockTransItems;
    @Table
    @Parameter(value = OutbDeliveryCreateStoConstants.RETURN, type = ParameterType.STRUCTURE)
    private List<BapiRet2> _Return;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Class Constructors">
    public OutbDeliveryCreateStoBapi() {
    }

    public OutbDeliveryCreateStoBapi(String RefDoc, String RefItem, BigDecimal DlvQty, String UnitISO) {
        _StockTransItems = new ArrayList<OutbDeliveryCreateStoStructure>();
        _StockTransItems.add(new OutbDeliveryCreateStoStructure(RefDoc, RefItem, DlvQty, UnitISO));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Field Getter/Setter">
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
     * @return the _StockTransItems
     */
    public List<OutbDeliveryCreateStoStructure> getStockTransItems() {
        return _StockTransItems;
    }

    /**
     * @param StockTransItems the _StockTransItems to set
     */
    public void setStockTransItems(List<OutbDeliveryCreateStoStructure> StockTransItems) {
        this._StockTransItems = StockTransItems;
    }

    /**
     * @return the _Return
     */
    public List<BapiRet2> getReturn() {
        return _Return;
    }
    // </editor-fold>
}
