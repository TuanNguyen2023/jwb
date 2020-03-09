/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.SLocsGetListStructure;
import com.gcs.wb.bapi.helper.constants.SLocsGetListConstants;
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
@Bapi(SLocsGetListConstants.BAPI_NAME)
public class SLocsGetListBapi implements Serializable {

    @Import
    @Parameter(SLocsGetListConstants.ID_MANDT)
    private String _idMandt;
    @Import
    @Parameter(SLocsGetListConstants.ID_WERKS)
    private String _idWerks;
    @Table
    @Parameter(value = SLocsGetListConstants.TD_SLOCS)
    private List<SLocsGetListStructure> _tdSLocs;

    public SLocsGetListBapi() {
    }

    public SLocsGetListBapi(String idMandt, String idWerks) {
        this._idMandt = idMandt;
        this._idWerks = idWerks;
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
     * @return the _tdSLocs
     */
    public List<SLocsGetListStructure> getTdSLocs() {
        return _tdSLocs;
    }

    /**
     * @return the _idWerks
     */
    public String getIdWerks() {
        return _idWerks;
    }

    /**
     * @param idWerks the _idWerks to set
     */
    public void setIdWerks(String idWerks) {
        this._idWerks = idWerks;
    }

    @Override
    public String toString() {
        return "SLocsGetListBapi{" + "_idMandt=" + _idMandt + ", _idWerks=" + _idWerks + '}';
    }
}
