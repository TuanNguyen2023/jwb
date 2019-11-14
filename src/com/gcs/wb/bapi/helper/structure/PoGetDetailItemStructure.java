/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.PoGetDetailConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.BapiStructure;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class PoGetDetailItemStructure implements Serializable {

    /**Item Number of Purchasing Document*/
    @Parameter(PoGetDetailConstants.PO_ITEM)
    private String _PO_ITEM;
    /**Deletion Indicator in Purchasing Document*/
    @Parameter(PoGetDetailConstants.DELETE_IND)
    private String _DELETE_IND;
    /**Short Text*/
    @Parameter(PoGetDetailConstants.SHORT_TEXT)
    private String _SHORT_TEXT;
    /**Material Number*/
    @Parameter(PoGetDetailConstants.MATERIAL)
    private String _MATERIAL;
    /**Plant*/
    @Parameter(PoGetDetailConstants.PLANT)
    private String _PLANT;
    /**Storage Location*/
    @Parameter(PoGetDetailConstants.STGE_LOC)
    private String _STGE_LOC;
    /**Material Number Used by Vendor*/
    @Parameter(PoGetDetailConstants.VEND_MAT)
    private String _VEND_MAT;
    /**Purchase Order Quantity*/
    @Parameter(PoGetDetailConstants.QUANTITY)
    private BigDecimal _QUANTITY;
    /**Purchase Order Unit of Measure*/
    @Parameter(PoGetDetailConstants.PO_UNIT)
    private String _PO_UNIT;
    /**Order unit in ISO code*/
    @Parameter(PoGetDetailConstants.PO_UNIT_ISO)
    private String _PO_UNIT_ISO;
    /**Stock Type*/
    @Parameter(PoGetDetailConstants.QUAL_INSP)
    private String _QUAL_INSP;
    /**Overdelivery Tolerance Limit*/
    @Parameter(PoGetDetailConstants.OVER_DLV_TOL)
    private BigDecimal _OVER_DLV_TOL;
    /**Indicator: Unlimited Overdelivery Allowed*/
    @Parameter(PoGetDetailConstants.UNLIMITED_DLV)
    private String _UNLIMITED_DLV;
    /**Underdelivery Tolerance Limit*/
    @Parameter(PoGetDetailConstants.UNDER_DLV_TOL)
    private BigDecimal _UNDER_DLV_TOL;
    /**Valuation Type*/
    @Parameter(PoGetDetailConstants.VAL_TYPE)
    private String _VAL_TYPE;
    /**"Delivery Completed" Indicator*/
    @Parameter(PoGetDetailConstants.NO_MORE_GR)
    private String _NO_MORE_GR;
    /**Final Invoice Indicator*/
    @Parameter(PoGetDetailConstants.FINAL_INV)
    private String _FINAL_INV;
    /**Item Category in Purchasing Document*/
    @Parameter(PoGetDetailConstants.ITEM_CAT)
    private String _ITEM_CAT;
    /**Goods Receipt Indicator*/
    @Parameter(PoGetDetailConstants.GR_IND)
    private String _GR_IND;
    /**Goods Receipt, Non-Valuated*/
    @Parameter(PoGetDetailConstants.GR_NON_VAL)
    private String _GR_NON_VAL;
    /**Free Item*/
    @Parameter(PoGetDetailConstants.FREE_ITEM)
    private String _FREE_ITEM;
    /**"Outward Delivery Completed" Indicator*/
    @Parameter(PoGetDetailConstants.DELIV_COMPL)
    private String _DELIV_COMPL;
    /**Partial Delivery at Item Level (Stock Transfer)*/
    @Parameter(PoGetDetailConstants.PART_DELIV)
    private String _PART_DELIV;

    public PoGetDetailItemStructure() {
    }

    /**
     * Item Number of Purchasing Document
     * @return the _PO_ITEM
     */
    public String getPO_ITEM() {
        return _PO_ITEM;
    }

    /**
     * Deletion Indicator in Purchasing Document
     * @return the _DELETE_IND
     */
    public String getDELETE_IND() {
        return _DELETE_IND;
    }

    /**
     * Short Text
     * @return the _SHORT_TEXT
     */
    public String getSHORT_TEXT() {
        return _SHORT_TEXT;
    }

    /**
     * Material Number
     * @return the _MATERIAL
     */
    public String getMATERIAL() {
        return _MATERIAL;
    }

    /**
     * Plant
     * @return the _PLANT
     */
    public String getPLANT() {
        return _PLANT;
    }

    /**
     * Storage Location
     * @return the _STGE_LOC
     */
    public String getSTGE_LOC() {
        return _STGE_LOC;
    }

    /**
     * Material Number Used by Vendor
     * @return the _VEND_MAT
     */
    public String getVEND_MAT() {
        return _VEND_MAT;
    }

    /**
     * Purchase Order Quantity
     * @return the _QUANTITY
     */
    public BigDecimal getQUANTITY() {
        return _QUANTITY;
    }

    /**
     * Purchase Order Unit of Measure
     * @return the _PO_UNIT
     */
    public String getPO_UNIT() {
        return _PO_UNIT;
    }

    /**
     * Order unit in ISO code
     * @return the _PO_UNIT_ISO
     */
    public String getPO_UNIT_ISO() {
        return _PO_UNIT_ISO;
    }

    /**
     * Stock Type
     * @return the _QUAL_INSP
     */
    public String getQUAL_INSP() {
        return _QUAL_INSP;
    }

    /**
     * Overdelivery Tolerance Limit
     * @return the _OVER_DLV_TOL
     */
    public BigDecimal getOVER_DLV_TOL() {
        return _OVER_DLV_TOL;
    }

    /**
     * Indicator: Unlimited Overdelivery Allowed
     * @return the _UNLIMITED_DLV
     */
    public String getUNLIMITED_DLV() {
        return _UNLIMITED_DLV;
    }

    /**
     * Underdelivery Tolerance Limit
     * @return the _UNDER_DLV_TOL
     */
    public BigDecimal getUNDER_DLV_TOL() {
        return _UNDER_DLV_TOL;
    }

    /**
     * Valuation Type
     * @return the _VAL_TYPE
     */
    public String getVAL_TYPE() {
        return _VAL_TYPE;
    }

    /**
     * "Delivery Completed" Indicator
     * @return the _NO_MORE_GR
     */
    public String getNO_MORE_GR() {
        return _NO_MORE_GR;
    }

    /**
     * Final Invoice Indicator
     * @return the _FINAL_INV
     */
    public String getFINAL_INV() {
        return _FINAL_INV;
    }

    /**
     * Item Category in Purchasing Document
     * @return the _ITEM_CAT
     */
    public String getITEM_CAT() {
        return _ITEM_CAT;
    }

    /**
     * Goods Receipt Indicator
     * @return the _GR_IND
     */
    public String getGR_IND() {
        return _GR_IND;
    }

    /**
     * Goods Receipt, Non-Valuated
     * @return the _GR_NON_VAL
     */
    public String getGR_NON_VAL() {
        return _GR_NON_VAL;
    }

    /**
     * Free Item
     * @return the _FREE_ITEM
     */
    public String getFREE_ITEM() {
        return _FREE_ITEM;
    }

    /**
     * "Outward Delivery Completed" Indicator
     * @return the _DELIV_COMPL
     */
    public String getDELIV_COMPL() {
        return _DELIV_COMPL;
    }

    /**
     * Partial Delivery at Item Level (Stock Transfer)
     * @return the _PART_DELIV
     */
    public String getPART_DELIV() {
        return _PART_DELIV;
    }
}
