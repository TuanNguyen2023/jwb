/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.VendorGetDetailStructure;
import com.gcs.wb.bapi.helper.constants.VendorGetDetailConstants;
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
@Bapi(VendorGetDetailConstants.BAPI_NAME)
public class VendorGetDetailBapi implements Serializable {

    /**Import Parameter: ID_LIFNR (Vendor Number) */
    @Import
    @Parameter(VendorGetDetailConstants.ID_LIFNR)
    private String _idLifnr;
    /**Export Parameter: ES_LFA1 (Vendor Structure)*/
    @Export
    @Parameter(value = VendorGetDetailConstants.ES_LFA1, type = ParameterType.STRUCTURE)
    private VendorGetDetailStructure _esLfa1;

    public VendorGetDetailBapi() {
    }

    /**
     * Import Parameter: ID_LIFNR (Vendor Number)
     * @param idLifnr the _idLifnr to set
     */
    public void setIdLifnr(String idLifnr) {
        this._idLifnr = idLifnr;
    }

    /**
     * Export Parameter: ES_LFA1 (Vendor Structure)
     * @return the _esLfa1
     */
    public VendorGetDetailStructure getEsLfa1() {
        return _esLfa1;
    }
}
