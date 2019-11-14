/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.MatGetDetailStructure;
import com.gcs.wb.bapi.helper.constants.MatGetDetailConstants;
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
@Bapi(MatGetDetailConstants.BAPI_NAME)
public class MatGetDetailBapi implements Serializable {

    @Import
    @Parameter(MatGetDetailConstants.ID_MATNR)
    private String _id_matnr = null;
    @Export
    @Parameter(value = MatGetDetailConstants.ES_MAKT, type = ParameterType.STRUCTURE)
    private MatGetDetailStructure _es_makt;

    public MatGetDetailBapi() {
    }

    /**
     * @param id_matnr the _id_matnr to set
     */
    public void setId_matnr(String id_matnr) {
        this._id_matnr = id_matnr;
    }

    /**
     * @return the _es_makt
     */
    public MatGetDetailStructure getEs_makt() {
        return _es_makt;
    }
}
