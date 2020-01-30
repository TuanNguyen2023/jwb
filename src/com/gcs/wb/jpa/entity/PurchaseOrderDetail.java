/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_purchase_order")
@NamedQueries({
    @NamedQuery(name = "PurchaseOrder.findAll", query = "SELECT po FROM PurchaseOrder po"),
    @NamedQuery(name = "PurchaseOrder.findByPoNumber", query = "SELECT po FROM PurchaseOrder po WHERE po.poNumber = :poNumber")
})
public class PurchaseOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "po_item")
    private String poItem;
    @Column(name = "po_item_free")
    private String poItemFree;
    @Column(name = "i_delete_ind")
    private Character iDeleteInd;
    @Column(name = "if_delete_ind")
    private Character ifDeleteInd;
    @Column(name = "short_text")
    private String shortText;
    @Column(name = "material")
    private String material;
    @Column(name = "plant")
    private String plant;
    @Column(name = "stge_loc")
    private String stgeLoc;
    @Column(name = "vend_mat")
    private String vendMat;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "quantity_free")
    private BigDecimal quantityFree;
    @Column(name = "po_unit")
    private String poUnit;
    @Column(name = "po_unit_iso")
    private String poUnitIso;
    @Column(name = "qual_insp")
    private Character qualInsp;
    @Column(name = "over_dlv_tol")
    private BigDecimal overDlvTol;
    @Column(name = "unlimited_dlv")
    private Character unlimitedDlv;
    @Column(name = "under_dlv_tol")
    private BigDecimal underDlvTol;
    @Column(name = "val_type")
    private String valType;
    @Column(name = "no_more_gr")
    private Character noMoreGr;
    @Column(name = "final_inv")
    private Character finalInv;
    @Column(name = "item_cat")
    private Character itemCat;
    @Column(name = "item_free_cat")
    private Character itemFreeCat;
    @Column(name = "gr_ind")
    private Character grInd;
    @Column(name = "gr_non_val")
    private Character grNonVal;
    @Column(name = "deliv_compl")
    private Character delivCompl;
    @Column(name = "part_deliv")
    private Character partDeliv;
    @Column(name = "purchase_order_id")
    private int purchaseOrderId;
    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.DATE)
    private Date updatedDate;
    @Column(name = "deleted_date")
    @Temporal(TemporalType.DATE)
    private Date deletedDate;

    public PurchaseOrderDetail() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoItem() {
        return poItem;
    }

    public void setPoItem(String poItem) {
        this.poItem = poItem;
    }

    public String getPoItemFree() {
        return poItemFree;
    }

    public void setPoItemFree(String poItemFree) {
        this.poItemFree = poItemFree;
    }

    public Character getIDeleteInd() {
        return iDeleteInd;
    }

    public void setIDeleteInd(Character iDeleteInd) {
        this.iDeleteInd = iDeleteInd;
    }

    public Character getIfDeleteInd() {
        return ifDeleteInd;
    }

    public void setIfDeleteInd(Character ifDeleteInd) {
        this.ifDeleteInd = ifDeleteInd;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getStgeLoc() {
        return stgeLoc;
    }

    public void setStgeLoc(String stgeLoc) {
        this.stgeLoc = stgeLoc;
    }

    public String getVendMat() {
        return vendMat;
    }

    public void setVendMat(String vendMat) {
        this.vendMat = vendMat;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantityFree() {
        return quantityFree;
    }

    public void setQuantityFree(BigDecimal quantityFree) {
        this.quantityFree = quantityFree;
    }

    public String getPoUnit() {
        return poUnit;
    }

    public void setPoUnit(String poUnit) {
        this.poUnit = poUnit;
    }

    public String getPoUnitIso() {
        return poUnitIso;
    }

    public void setPoUnitIso(String poUnitIso) {
        this.poUnitIso = poUnitIso;
    }

    public Character getQualInsp() {
        return qualInsp;
    }

    public void setQualInsp(Character qualInsp) {
        this.qualInsp = qualInsp;
    }

    public BigDecimal getOverDlvTol() {
        return overDlvTol;
    }

    public void setOverDlvTol(BigDecimal overDlvTol) {
        this.overDlvTol = overDlvTol;
    }

    public Character getUnlimitedDlv() {
        return unlimitedDlv;
    }

    public void setUnlimitedDlv(Character unlimitedDlv) {
        this.unlimitedDlv = unlimitedDlv;
    }

    public BigDecimal getUnderDlvTol() {
        return underDlvTol;
    }

    public void setUnderDlvTol(BigDecimal underDlvTol) {
        this.underDlvTol = underDlvTol;
    }

    public String getValType() {
        return valType;
    }

    public void setValType(String valType) {
        this.valType = valType;
    }

    public Character getNoMoreGr() {
        return noMoreGr;
    }

    public void setNoMoreGr(Character noMoreGr) {
        this.noMoreGr = noMoreGr;
    }

    public Character getFinalInv() {
        return finalInv;
    }

    public void setFinalInv(Character finalInv) {
        this.finalInv = finalInv;
    }

    public Character getItemCat() {
        return itemCat;
    }

    public void setItemCat(Character itemCat) {
        this.itemCat = itemCat;
    }

    public Character getItemFreeCat() {
        return itemFreeCat;
    }

    public void setItemFreeCat(Character itemFreeCat) {
        this.itemFreeCat = itemFreeCat;
    }

    public Character getGrInd() {
        return grInd;
    }

    public void setGrInd(Character grInd) {
        this.grInd = grInd;
    }

    public Character getGrNonVal() {
        return grNonVal;
    }

    public void setGrNonVal(Character grNonVal) {
        this.grNonVal = grNonVal;
    }

    public Character getDelivCompl() {
        return delivCompl;
    }

    public void setDelivCompl(Character delivCompl) {
        this.delivCompl = delivCompl;
    }

    public Character getPartDeliv() {
        return partDeliv;
    }

    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public void setPartDeliv(Character partDeliv) {
        this.partDeliv = partDeliv;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchaseOrderDetail that = (PurchaseOrderDetail) o;

        if (poItem != null ? !poItem.equals(that.poItem) : that.poItem != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (poItem != null ? poItem.hashCode() : 0);
        result = 31 * result + (poItemFree != null ? poItemFree.hashCode() : 0);
        result = 31 * result + (iDeleteInd != null ? iDeleteInd.hashCode() : 0);
        result = 31 * result + (ifDeleteInd != null ? ifDeleteInd.hashCode() : 0);
        result = 31 * result + (shortText != null ? shortText.hashCode() : 0);
        result = 31 * result + (material != null ? material.hashCode() : 0);
        result = 31 * result + (plant != null ? plant.hashCode() : 0);
        result = 31 * result + (stgeLoc != null ? stgeLoc.hashCode() : 0);
        result = 31 * result + (vendMat != null ? vendMat.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (quantityFree != null ? quantityFree.hashCode() : 0);
        result = 31 * result + (poUnit != null ? poUnit.hashCode() : 0);
        result = 31 * result + (poUnitIso != null ? poUnitIso.hashCode() : 0);
        result = 31 * result + (qualInsp != null ? qualInsp.hashCode() : 0);
        result = 31 * result + (overDlvTol != null ? overDlvTol.hashCode() : 0);
        result = 31 * result + (unlimitedDlv != null ? unlimitedDlv.hashCode() : 0);
        result = 31 * result + (underDlvTol != null ? underDlvTol.hashCode() : 0);
        result = 31 * result + (valType != null ? valType.hashCode() : 0);
        result = 31 * result + (noMoreGr != null ? noMoreGr.hashCode() : 0);
        result = 31 * result + (finalInv != null ? finalInv.hashCode() : 0);
        result = 31 * result + (itemCat != null ? itemCat.hashCode() : 0);
        result = 31 * result + (itemFreeCat != null ? itemFreeCat.hashCode() : 0);
        result = 31 * result + (grInd != null ? grInd.hashCode() : 0);
        result = 31 * result + (grNonVal != null ? grNonVal.hashCode() : 0);
        result = 31 * result + (delivCompl != null ? delivCompl.hashCode() : 0);
        result = 31 * result + (partDeliv != null ? partDeliv.hashCode() : 0);
        result = 31 * result + (int) (purchaseOrderId ^ (purchaseOrderId >>> 31));
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (deletedDate != null ? deletedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.PurchaseOrder[id=" + id + "]";
    }
}
