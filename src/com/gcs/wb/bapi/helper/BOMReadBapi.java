/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.constants.BOMReadConstants;
import com.gcs.wb.bapi.helper.structure.BOMReadStructure;
import java.io.Serializable;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;

/**
 *
 * @author vunguyent
 */
@Bapi(BOMReadConstants.BAPI_NAME)
public class BOMReadBapi implements Serializable {

    /**Material ntest */
    @Import
    @Parameter(BOMReadConstants.MATERIAL)
    private String _material;
    /**Plant*/
    @Import
    @Parameter(BOMReadConstants.PLANT)
    private String _plant;
    /**BOM Usage*/
    @Import
    @Parameter(BOMReadConstants.BOM_USAGE)
    private String _bom_usage;
    /**BOM Items*/
    @Table
    @Parameter(value = BOMReadConstants.T_STPO, type = ParameterType.STRUCTURE)
    private List<BOMReadStructure> _bom_items;

    public BOMReadBapi() {
    }

    /**
     * Material
     * @param material the _material to set
     */
    public void setMaterial(String material) {
        this._material = material;
    }

    /**
     * Plant
     * @param plant the _plant to set
     */
    public void setPlant(String plant) {
        this._plant = plant;
    }

    /**
     * BOM Usage
     * @param bom_usage the _bom_usage to set
     */
    public void setBom_usage(String bom_usage) {
        this._bom_usage = bom_usage;
        string sstr = "";
    }

    /**
     * BOM Items
     * @return the _bom_items
     */
    public List<BOMReadStructure> getBom_items() {
        return _bom_items;
    }
}
