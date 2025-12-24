/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.BOMReadConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class BOMReadStructure implements Serializable {

    /**Item Category (Bill of Material)*/
    @Parameter(BOMReadConstants.ITEM_CATEG)
    private String _item_categ;
    /**BOM Item Number*/
    @Parameter(BOMReadConstants.ITEM_NO)
    private String _item_no;
    /**BOM component*/
    @Parameter(BOMReadConstants.COMPONENT)
    private String _component;
    /**Component unit of measure*/
    @Parameter(BOMReadConstants.COMP_UNIT)
    private String _comp_unit;

    public BOMReadStructure() {
    }

    /**
     * Item Category (Bill of Material)
     * @return the _item_categ
     */
    public String getItem_categ() {
        return _item_categ;
    }

    /**
     * BOM Item Number
     * @return the _item_no
     */
    public String getItem_no() {
        return _item_no;
    }

    /**
     * BOM component
     * @return the _component
     */
    public String getComponent() {
        return _component;
    }

    /**
     * Component unit of measure
     * @return the _comp_unit
     */
    public String getComp_unit() {
        return _comp_unit;
    }
}
