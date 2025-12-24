/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.PoGetDetailHeaderStructure;
import com.gcs.wb.bapi.helper.structure.PoGetDetailItemStructure;
import com.gcs.wb.bapi.helper.constants.PoGetDetailConstants;
import com.gcs.wb.bapi.helper.structure.PoShippingExpStructure;
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
@Bapi(PoGetDetailConstants.BAPI_NAME)
@ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
public class PoGetDetailBapi implements Serializable {

    /**Purchasing Document Number*/
    @Import
    @Parameter(PoGetDetailConstants.PURCHASEORDER)
    private String _PURCHASEORDER;
    /**Purchase Order Header Data*/
    @Export
    @Parameter(value = PoGetDetailConstants.POHEADER, type = ParameterType.STRUCTURE)
    private PoGetDetailHeaderStructure _poHeader;
    /**Purchase Order Item*/
    @Table
    @Parameter(value = PoGetDetailConstants.POITEM, type = ParameterType.STRUCTURE)
    private List<PoGetDetailItemStructure> _poItems;
    @Table
    @Parameter(value = PoGetDetailConstants.POSHIPPINGEXP, type = ParameterType.STRUCTURE)
    private List<PoShippingExpStructure> _poShippingExp;
    @Table
    @Parameter(value = PoGetDetailConstants.RETURN, type = ParameterType.STRUCTURE)
    private List<BapiRet2> _return;

    public PoGetDetailBapi() {
        _poItems = new ArrayList<>();
        _poShippingExp = new ArrayList<>();
    }

    /**
     * Purchasing Document Number
     * @param PURCHASEORDER the _PURCHASEORDER to set
     */
    public void setPURCHASEORDER(String PURCHASEORDER) {
        this._PURCHASEORDER = PURCHASEORDER;
    }

    /**
     * Purchase Order Header Data
     * @return the _poHeader
     */
    public PoGetDetailHeaderStructure getPoHeader() {
        return _poHeader;
    }

    /**
     * Purchase Order Item
     * @return the _poItems
     */
    public List<PoGetDetailItemStructure> getPoItems() {
        return _poItems;
    }
    
    /**
     * PO Shipping exp
     * @return the _poShippingExp
     */
    public List<PoShippingExpStructure> getPoShippingExps() {
        return _poShippingExp;
    }

    /**
     * @return the _return
     */
    public List<BapiRet2> getReturn() {
        return _return;
    }

    @Override
    public String toString() {
        return "PoGetDetailBapi{" + "_PURCHASEORDER=" + _PURCHASEORDER + '}';
    }
}
