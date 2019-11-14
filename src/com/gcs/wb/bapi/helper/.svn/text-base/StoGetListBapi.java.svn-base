/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.StoGetListStructure;
import com.gcs.wb.bapi.helper.constants.StoGetListConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;

/**
 *
 * @author Tran-Vu
 */
@Bapi(StoGetListConstants.BAPI_NAME)
public class StoGetListBapi implements Serializable {

    @Import
    @Parameter(StoGetListConstants.ID_WERKS)
    private String _idWerks;
    @Table
    @Parameter(value=StoGetListConstants.TD_STOS,type=ParameterType.STRUCTURE)
    private List<StoGetListStructure> _tdSTOS;

    public StoGetListBapi() {
        this._tdSTOS = new ArrayList<StoGetListStructure>();
    }

    public StoGetListBapi(String idWerks) {
        this._tdSTOS = new ArrayList<StoGetListStructure>();
        this._idWerks = idWerks;
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

    /**
     * @return the _tdSTOS
     */
    public List<StoGetListStructure> getTdSTOS() {
        return _tdSTOS;
    }
}
