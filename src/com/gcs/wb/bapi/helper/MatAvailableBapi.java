/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.MatAvailableConstants;
import com.gcs.wb.bapi.helper.structure.MatAvailableStructure;
import java.io.Serializable;
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
 * @author vunguyent
 */
@Bapi(MatAvailableConstants.BAPI_NAME)
@ThrowExceptionOnError(returnStructure = "EXPORT/RETURN")
public class MatAvailableBapi implements Serializable {

    /**Material Number*/
    @Import
    @Parameter(MatAvailableConstants.MATERIAL)
    private String _material;
    /**Unit of measure for display*/
    @Import
    @Parameter(MatAvailableConstants.UNIT)
    private String _unit;
    /**Plant*/
    @Import
    @Parameter(MatAvailableConstants.PLANT)
    private String _plant;
    /**Storage Location*/
    @Import
    @Parameter(MatAvailableConstants.STGE_LOC)
    private String _sloc;
    /**Batch*/
    @Import
    @Parameter(MatAvailableConstants.BATCH)
    private String _batch;
    /**RETURN*/
    @Export
    @Parameter(value = MatAvailableConstants.RETURN, type = ParameterType.STRUCTURE)
    private BapiRet2 _return;
    /**Output table (date and ATP quantity)*/
    @Table
    @Parameter(MatAvailableConstants.WMDVEX)
    private List<MatAvailableStructure> _wmdvex;

    public MatAvailableBapi() {
    }

    /**
     * Material Number
     * @param material the _material to set
     */
    public void setMaterial(String material) {
        this._material = material;
    }

    /**
     * Unit of measure for display
     * @param unit the _unit to set
     */
    public void setUnit(String unit) {
        this._unit = unit;
    }

    /**
     * Plant
     * @param plant the _plant to set
     */
    public void setPlant(String plant) {
        this._plant = plant;
    }

    /**
     * Storage Location
     * @param sloc the _sloc to set
     */
    public void setSloc(String sloc) {
        this._sloc = sloc;
    }

    /**
     * Batch
     * @param batch the _batch to set
     */
    public void setBatch(String batch) {
        this._batch = batch;
    }

    /**
     * RETURN
     * @return the _return
     */
    public BapiRet2 getReturn() {
        return _return;
    }

    /**
     * Output table (date and ATP quantity)
     * @return the _wmdvex
     */
    public List<MatAvailableStructure> getWmdvex() {
        return _wmdvex;
    }

    @Override
    public String toString() {
        return "MatAvailableBapi{" + "_material=" + _material + ", _unit=" + _unit + ", _plant=" + _plant + ", _sloc=" + _sloc + ", _batch=" + _batch + '}';
    }
}
