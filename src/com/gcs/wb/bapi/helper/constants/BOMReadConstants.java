/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.constants;

import org.hibersap.bapi.BapiConstants;

/**
 *
 * @author vunguyent
 */
public interface BOMReadConstants extends BapiConstants {

    String BAPI_NAME = "CSAP_MAT_BOM_READ";
    // <editor-fold defaultstate="collapsed" desc="Import">
    /**Material*/
    String MATERIAL = "MATERIAL";
    /**Plant*/
    String PLANT = "PLANT";
    /**BOM usage*/
    String BOM_USAGE = "BOM_USAGE";
    // </editor-fold>
    /**BOM items*/
    String T_STPO = "T_STPO";
    // <editor-fold defaultstate="collapsed" desc="T_STPO's structure">
    /**Item Category (Bill of Material)*/
    String ITEM_CATEG = "ITEM_CATEG";
    /**BOM Item Number*/
    String ITEM_NO = "ITEM_NO";
    /**BOM component*/
    String COMPONENT = "COMPONENT";
    /**Component unit of measure*/
    String COMP_UNIT = "COMP_UNIT";
    // </editor-fold>
}
