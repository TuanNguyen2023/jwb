/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.PlantGeDetailConstants;
import java.io.Serializable;
import java.util.HashMap;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@Bapi(PlantGeDetailConstants.BAPI_NAME)
public class PlantGetDetailBapi implements Serializable {

    @Import
    @Parameter(PlantGeDetailConstants.ID_MANDT)
    private String _idMandt;
    @Import
    @Parameter(PlantGeDetailConstants.ID_WERKS)
    private String _idWerks;
    @Export
    @Parameter(value = PlantGeDetailConstants.ES_PLANTS)
    private HashMap _esPlant;
//    private PlantGetDetailStructure _esPlant;

    public PlantGetDetailBapi() {
    }

    public PlantGetDetailBapi(String idMandt, String idWerks) {
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
     * @return the _tdPlants
     */
//    public PlantGetDetailStructure getEsPlant() {
    public HashMap getEsPlant() {
        return _esPlant;
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
        return "PlantGetDetailBapi{" + "_idMandt=" + _idMandt + ", _idWerks=" + _idWerks + '}';
    }
}
