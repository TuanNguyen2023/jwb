/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt.structure;

import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtPoCreateConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class GoodsMvtItemPoStructure implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Structure Fields">
    /**Material Number*/
    @Parameter(GoodsMvtPoCreateConstants.MATERIAL)
    private String _material;
    /**Vendor Account Number*/
    @Parameter(GoodsMvtPoCreateConstants.VENDOR)
    private String _vendor;
    /**Purchase Order Number*/
    @Parameter(GoodsMvtPoCreateConstants.PO_NUMBER)
    private String _po_number;
    /**Purchase Ordder Item*/
    @Parameter(GoodsMvtPoCreateConstants.PO_ITEM)
    private String _po_item;
    /**Plant 	*/
    @Parameter(GoodsMvtPoCreateConstants.PLANT)
    private String _plant;
    /**Storage Location */
    @Parameter(GoodsMvtPoCreateConstants.STGE_LOC)
    private String _stge_loc;
    /**Batch Number 	*/
    @Parameter(GoodsMvtPoCreateConstants.BATCH)
    private String _batch;
    /**Movement Type 	*/
    @Parameter(GoodsMvtPoCreateConstants.MOVE_TYPE)
    private String _move_type;
    /**Quantity in Unit of Entry */
    @Parameter(GoodsMvtPoCreateConstants.ENTRY_QNT)
    private BigDecimal _entry_qnt;
    /**Unit of Entry 	*/
    @Parameter(GoodsMvtPoCreateConstants.ENTRY_UOM)
    private String _entry_uom;
    /**ISO code for unit of measurement*/
    @Parameter(GoodsMvtPoCreateConstants.ENTRY_UOM_ISO)
    private String _entry_uom_iso;
    /**Goods Recipient/Ship-To Party	*/
    @Parameter(GoodsMvtPoCreateConstants.GR_RCPT)
    private String _gr_rcpt;
    /**Movement Indicator */
    @Parameter(GoodsMvtPoCreateConstants.MVT_IND)
    private String _mvt_ind = "B";
    /**NO_MORE_GR */
    @Parameter(GoodsMvtPoCreateConstants.NO_MORE_GR)
    private String _no_more_gr;
    /**Reason for Movement*/
    @Parameter(GoodsMvtPoCreateConstants.MOVE_REAS)
    private String _move_reas;
    /**Item Text*/
    @Parameter(GoodsMvtPoCreateConstants.ITEM_TEXT)
    private String _item_text;
    /**Receiving/Issuing Material*/
    @Parameter(GoodsMvtPoCreateConstants.MOVE_MAT)
    private String _moveMat;
    /**Receiving/Issuing Plant*/
    @Parameter(GoodsMvtPoCreateConstants.MOVE_PLANT)
    private String _movePlant;
    /**Receiving/Issuing Storage Location*/
    @Parameter(GoodsMvtPoCreateConstants.MOVE_STLOC)
    private String _moveSLoc;
    /**Receiving/Issuing Batch*/
    @Parameter(GoodsMvtPoCreateConstants.MOVE_BATCH)
    private String _moveBatch;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Class Constructors">
    public GoodsMvtItemPoStructure() {
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Field Getter/Setter">
    /**
     * Purchase Order Number
     * @return the _po_number
     */
    public String getPo_number() {
        return _po_number;
    }

    /**
     * Purchase Order Number
     * @param po_number the _po_number to set
     */
    public void setPo_number(String po_number) {
        this._po_number = po_number;
    }

    /**
     * Purchase Ordder Item
     * @return the _po_item
     */
    public String getPo_item() {
        return _po_item;
    }

    /**
     * Purchase Ordder Item
     * @param po_item the _po_item to set
     */
    public void setPo_item(String po_item) {
        this._po_item = po_item;
    }

    /**
     * Plant
     * @return the _plant
     */
    public String getPlant() {
        return _plant;
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
     * @return the _stge_loc
     */
    public String getStge_loc() {
        return _stge_loc;
    }

    /**
     * Storage Location
     * @param stge_loc the _stge_loc to set
     */
    public void setStge_loc(String stge_loc) {
        this._stge_loc = stge_loc;
    }

    /**
     * Batch Number
     * @return the _batch
     */
    public String getBatch() {
        return _batch;
    }

    /**
     * Batch Number
     * @param batch the _batch to set
     */
    public void setBatch(String batch) {
        this._batch = batch;
    }

    /**
     * Movement Type
     * @return the _move_type
     */
    public String getMove_type() {
        return _move_type;
    }

    /**
     * Movement Type
     * @param move_type the _move_type to set
     */
    public void setMove_type(String move_type) {
        this._move_type = move_type;
    }

    /**
     * Quantity in Unit of Entry
     * @return the _entry_qnt
     */
    public BigDecimal getEntry_qnt() {
        return _entry_qnt;
    }

    /**
     * Quantity in Unit of Entry
     * @param entry_qnt the _entry_qnt to set
     */
    public void setEntry_qnt(BigDecimal entry_qnt) {
        this._entry_qnt = entry_qnt;
    }

    /**
     * Unit of Entry
     * @return the _entry_uom
     */
    public String getEntry_uom() {
        return _entry_uom;
    }

    /**
     * Unit of Entry
     * @param entry_uom the _entry_uom to set
     */
    public void setEntry_uom(String entry_uom) {
        this._entry_uom = entry_uom;
    }

    /**
     * ISO code for unit of measurement
     * @return the _entry_uom_iso
     */
    public String getEntry_uom_iso() {
        return _entry_uom_iso;
    }

    /**
     * ISO code for unit of measurement
     * @param entry_uom_iso the _entry_uom_iso to set
     */
    public void setEntry_uom_iso(String entry_uom_iso) {
        this._entry_uom_iso = entry_uom_iso;
    }

    /**
     * Goods Recipient/Ship-To Party
     * @return the _gr_rcpt
     */
    public String getGr_rcpt() {
        return _gr_rcpt;
    }

    /**
     * Goods Recipient/Ship-To Party
     * @param gr_rcpt the _gr_rcpt to set
     */
    public void setGr_rcpt(String gr_rcpt) {
        this._gr_rcpt = gr_rcpt;
    }

    /**
     * Movement Indicator
     * @return the _mvt_ind
     */
    public String getMvt_ind() {
        return _mvt_ind;
    }

    /**
     * Movement Indicator
     * @param mvt_ind the _mvt_ind to set
     */
    public void setMvt_ind(String mvt_ind) {
        this._mvt_ind = mvt_ind;
    }

    /**
     * NO_MORE_GR
     * @return the _no_more_gr
     */
    public String getNo_more_gr() {
        return _no_more_gr;
    }

    /**
     * NO_MORE_GR
     * @param no_more_gr the _no_more_gr to set
     */
    public void setNo_more_gr(String no_more_gr) {
        this._no_more_gr = no_more_gr;
    }

    /**
     * Reason for Movement
     * @return the _move_reas
     */
    public String getMove_reas() {
        return _move_reas;
    }

    /**
     * Reason for Movement
     * @param move_reas the _move_reas to set
     */
    public void setMove_reas(String move_reas) {
        this._move_reas = move_reas;
    }

    /**
     * Item Text
     * @return the _item_text
     */
    public String getItem_text() {
        return _item_text;
    }

    /**
     * Item Text
     * @param item_text the _item_text to set
     */
    public void setItem_text(String item_text) {
        this._item_text = item_text;
    }

    /**
     * Material Number
     * @return the _material
     */
    public String getMaterial() {
        return _material;
    }

    /**
     * Material Number
     * @param material the _material to set
     */
    public void setMaterial(String material) {
        this._material = material;
    }

    /**
     * Vendor Account Number
     * @return the _vendor
     */
    public String getVendor() {
        return _vendor;
    }

    /**
     * Vendor Account Number
     * @param vendor the _vendor to set
     */
    public void setVendor(String vendor) {
        this._vendor = vendor;
    }

    /**
     * Receiving/Issuing Material
     * @return the _moveMat
     */
    public String getMoveMat() {
        return _moveMat;
    }

    /**
     * Receiving/Issuing Material
     * @param moveMat the _moveMat to set
     */
    public void setMoveMat(String moveMat) {
        this._moveMat = moveMat;
    }

    /**
     * Receiving/Issuing Plant
     * @return the _movePlant
     */
    public String getMovePlant() {
        return _movePlant;
    }

    /**
     * Receiving/Issuing Plant
     * @param movePlant the _movePlant to set
     */
    public void setMovePlant(String movePlant) {
        this._movePlant = movePlant;
    }

    /**
     * Receiving/Issuing Storage Location
     * @return the _moveSLoc
     */
    public String getMoveSLoc() {
        return _moveSLoc;
    }

    /**
     * Receiving/Issuing Storage Location
     * @param moveSLoc the _moveSLoc to set
     */
    public void setMoveSLoc(String moveSLoc) {
        this._moveSLoc = moveSLoc;
    }

    /**
     * Receiving/Issuing Batch
     * @return the _moveBatch
     */
    public String getMoveBatch() {
        return _moveBatch;
    }

    /**
     * Receiving/Issuing Batch
     * @param moveBatch the _moveBatch to set
     */
    public void setMoveBatch(String moveBatch) {
        this._moveBatch = moveBatch;
    }
    // </editor-fold>
}
