/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.MvtGetDetailStructure;
import com.gcs.wb.bapi.helper.constants.MvtGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;

/**
 *
 * @author Tran-Vu
 */
@Bapi(MvtGetDetailConstants.BAPI_NAME)
public class MvtGetDetailBapi implements Serializable {

    @Import
    @Parameter(value = MvtGetDetailConstants.ID_MANDT, type = ParameterType.SIMPLE)
    private String _idMandt = null;
    @Import
    @Parameter(value = MvtGetDetailConstants.ID_BWART, type = ParameterType.SIMPLE)
    private String _idBwart = null;
    @Export
    @Parameter(value = MvtGetDetailConstants.ES_MVT, type = ParameterType.STRUCTURE)
    private MvtGetDetailStructure item = null;

    public MvtGetDetailBapi() {
    }

    public MvtGetDetailBapi(String idMandt, String idBwart) {
        this._idMandt = idMandt;
        this._idBwart = idBwart;
    }

    /**
     * @param idMandt the _idMandt to set
     */
    public void setIdMandt(String idMandt) {
        this._idMandt = idMandt;
    }

    /**
     * @param idBwart the _idBwart to set
     */
    public void setIdBwart(String idBwart) {
        this._idBwart = idBwart;
    }

    /**
     * @return the item
     */
    public MvtGetDetailStructure getItem() {
        return item;
    }
}
