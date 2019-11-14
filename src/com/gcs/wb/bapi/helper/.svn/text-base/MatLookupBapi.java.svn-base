/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.MatLookupConstants;
import com.gcs.wb.bapi.helper.structure.MatLookupStructure;
import java.io.Serializable;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author vunguyent
 */
@Bapi(value = MatLookupConstants.BAPI_NAME)
@ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
public class MatLookupBapi implements Serializable {

    /**Import Parameter: Plant*/
    @Import
    @Parameter(value = MatLookupConstants.ID_PLANT, type = ParameterType.SIMPLE)
    private String _plant = null;
    /**Import Parameter: search string*/
    @Import
    @Parameter(value = MatLookupConstants.ID_SDESC, type = ParameterType.SIMPLE)
    private String _sdesc = null;
    /**Table Parameter: List of found Materials*/
    @Table
    @Parameter(value = MatLookupConstants.MATNRLIST, type = ParameterType.STRUCTURE)
    private List<MatLookupStructure> _listMats;
    @Table
    @Parameter(value = MatLookupConstants.RETURN, type = ParameterType.STRUCTURE)
    private List<BapiRet2> _return;

    public MatLookupBapi() {
    }

    public MatLookupBapi(String _plant, String _sdesc) {
        this._plant = _plant;
        this._sdesc = _sdesc;
    }

    /**
     * Import Parameter: Plant
     * @param plant the _plant to set
     */
    public void setPlant(String plant) {
        this._plant = plant;
    }

    /**
     * Import Parameter: search string
     * @param sdesc the _sdesc to set
     */
    public void setSdesc(String sdesc) {
        this._sdesc = sdesc;
    }

    /**
     * Table Parameter: List of found Materials
     * @return the _listMats
     */
    public List<MatLookupStructure> getListMats() {
        return _listMats;
    }

    /**
     * @return the _return
     */
    public List<BapiRet2> getReturn() {
        return _return;
    }
}
