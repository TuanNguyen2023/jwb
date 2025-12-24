/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.MvtReasonsGetListStructure;
import com.gcs.wb.bapi.helper.constants.MvtReasonsGetListConstants;
import java.io.Serializable;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;

/**
 *
 * @author Tran-Vu
 */
@Bapi(MvtReasonsGetListConstants.BAPI_NAME)
public class MvtReasonsGetListBapi implements Serializable {

    @Import
    @Parameter(MvtReasonsGetListConstants.ID_MANDT)
    private String _idMandt;
    @Import
    @Parameter(MvtReasonsGetListConstants.ID_BWART)
    private String _idBwart;
    @Table
    @Parameter(MvtReasonsGetListConstants.TD_MVT_REASONS)
    private List<MvtReasonsGetListStructure> _tdMvtsReasons;

    public MvtReasonsGetListBapi() {
    }

    public MvtReasonsGetListBapi(String idMandt, String idBwart) {
        this._idMandt = idMandt;
        this._idBwart = idBwart;
    }

    /**
     * @return the _idMandt
     */
    public String getIdMandt() {
        return _idMandt;
    }

    /**
     * @param idMandt the _idMandt to set
     */
    public void setIdMandt(String idMandt) {
        this._idMandt = idMandt;
    }

    /**
     * @return the _tdMvtsReasons
     */
    public List<MvtReasonsGetListStructure> getTdMvtsReasons() {
        return _tdMvtsReasons;
    }
}
