/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt.structure;

import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtDoCreateConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class GoodsMvtItemDoStructure implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Structure Fields">
    /**Deilvery Number to search*/
    @Parameter(GoodsMvtDoCreateConstants.DELIV_NUMB_TO_SEARCH)
    private String _deliv_numb_to_search;
    /**Deilvery Item to search*/
    @Parameter(GoodsMvtDoCreateConstants.DELIV_ITEM_TO_SEARCH)
    private String _deliv_item_to_search;
    /**Deilvery Number*/
    @Parameter(GoodsMvtDoCreateConstants.DELIV_NUMB)
    private String _deliv_numb;
    /**Deilvery Item*/
    @Parameter(GoodsMvtDoCreateConstants.DELIV_ITEM)
    private String _deliv_item;
    /**Plant 	*/
    @Parameter(GoodsMvtDoCreateConstants.PLANT)
    private String _plant;
    /**Storage Location */
    @Parameter(GoodsMvtDoCreateConstants.STGE_LOC)
    private String _stge_loc;
    /**Batch Number 	*/
    @Parameter(GoodsMvtDoCreateConstants.BATCH)
    private String _batch;
    /**Movement Type 	*/
    @Parameter(GoodsMvtDoCreateConstants.MOVE_TYPE)
    private String _move_type;
    /**Quantity in Unit of Entry */
    @Parameter(GoodsMvtDoCreateConstants.ENTRY_QNT)
    private BigDecimal _entry_qnt;
    /**Unit of Entry 	*/
    @Parameter(GoodsMvtDoCreateConstants.ENTRY_UOM)
    private String _entry_uom;
    /**ISO code for unit of measurement*/
    @Parameter(GoodsMvtDoCreateConstants.ENTRY_UOM_ISO)
    private String _entry_uom_iso;
    /**Goods Recipient/Ship-To Party	*/
    @Parameter(GoodsMvtDoCreateConstants.GR_RCPT)
    private String _gr_rcpt;
    /**Movement Indicator */
    @Parameter(GoodsMvtDoCreateConstants.MVT_IND)
    private String _mvt_ind = "B";
    /**NO_MORE_GR */
    @Parameter(GoodsMvtDoCreateConstants.NO_MORE_GR)
    private String _no_more_gr;
    /**Reason for Movement*/
    @Parameter(GoodsMvtDoCreateConstants.MOVE_REAS)
    private String _move_reas;
    /**Item Text*/
    @Parameter(GoodsMvtDoCreateConstants.ITEM_TEXT)
    private String _item_text;
    // </editor-fold>

    public GoodsMvtItemDoStructure() {
        this._deliv_item = null;
        this._deliv_item_to_search = null;
        this._deliv_numb = null;
        this._deliv_numb_to_search = null;
        this._plant = null;
        this._stge_loc = null;
        this._batch = null;
        this._move_type = null;
        this._entry_qnt = null;
        this._entry_uom = null;
        this._entry_uom_iso = null;
        this._gr_rcpt = null;
        this._no_more_gr = null;
        this._move_reas = null;
        this._item_text = null;
    }

    /**
     * Deilvery Number to search
     * @return the _deliv_numb_to_search
     */
    public String getDeliv_numb_to_search() {
        return _deliv_numb_to_search;
    }

    /**
     * Deilvery Number to search
     * @param deliv_numb_to_search the _deliv_numb_to_search to set
     */
    public void setDeliv_numb_to_search(String deliv_numb_to_search) {
        this._deliv_numb_to_search = deliv_numb_to_search;
    }

    /**
     * Deilvery Item to search
     * @return the _deliv_item_to_search
     */
    public String getDeliv_item_to_search() {
        return _deliv_item_to_search;
    }

    /**
     * Deilvery Item to search
     * @param deliv_item_to_search the _deliv_item_to_search to set
     */
    public void setDeliv_item_to_search(String deliv_item_to_search) {
        this._deliv_item_to_search = deliv_item_to_search;
    }

    /**
     * Deilvery Number
     * @return the _deliv_numb
     */
    public String getDeliv_numb() {
        return _deliv_numb;
    }

    /**
     * Deilvery Number
     * @param deliv_numb the _deliv_numb to set
     */
    public void setDeliv_numb(String deliv_numb) {
        this._deliv_numb = deliv_numb;
    }

    /**
     * Deilvery Item
     * @return the _deliv_item
     */
    public String getDeliv_item() {
        return _deliv_item;
    }

    /**
     * Deilvery Item
     * @param deliv_item the _deliv_item to set
     */
    public void setDeliv_item(String deliv_item) {
        this._deliv_item = deliv_item;
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
}
