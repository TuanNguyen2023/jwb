/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.CustomerGetDetailStructure;
import com.gcs.wb.bapi.helper.constants.CustomerGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;

/**
 *
 * @author vunguyent
 */
@Bapi(CustomerGetDetailConstants.BAPI_NAME)
public class CustomerGetDetailBapi implements Serializable {

    /**Import Parameter: ID_KUNNR (Customer Number) */
    @Import
    @Parameter(CustomerGetDetailConstants.ID_KUNNR)
    private String _idKunnr;
    /**Export Parameter: ES_KNA1 (Customer Structure)*/
    @Export
    @Parameter(value = CustomerGetDetailConstants.ES_KNA1, type = ParameterType.STRUCTURE)
    private CustomerGetDetailStructure _esKna1;

    public CustomerGetDetailBapi() {
    }

    /**
     * Import Parameter: ID_KUNNR (Customer Number)
     * @param idKunnr the _idKunnr to set
     */
    public void setIdKunnr(String idKunnr) {
        this._idKunnr = idKunnr;
    }

    /**
     * Export Parameter: ES_KNA1 (Customer Structure)
     * @return the _esKna1
     */
    public CustomerGetDetailStructure getEsKna1() {
        return _esKna1;
    }

    @Override
    public String toString() {
        return "CustomerGetDetailBapi{" + "_idKunnr=" + _idKunnr + '}';
    }
}
