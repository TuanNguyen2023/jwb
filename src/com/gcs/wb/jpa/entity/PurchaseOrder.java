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
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "mandt")
    private String mandt;
    @Column(name = "po_number", unique = true)
    private String poNumber;
    @Column(name = "doc_type")
    private String docType;
    @Column(name = "delete_ind")
    private Character deleteInd;
    @Column(name = "status")
    private Character status;
    @Column(name = "creat_date")
    @Temporal(TemporalType.DATE)
    private Date creatDate;
    @Column(name = "vendor")
    private String vendor;
    @Column(name = "suppl_vend")
    private String supplVend;
    @Column(name = "customer")
    private String customer;
    @Column(name = "suppl_plnt")
    private String supplPlnt;
    @Column(name = "po_rel_ind")
    private Character poRelInd;
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
    @Column(name = "rel_status")
    private String relStatus;
    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    public PurchaseOrder() {
    }

    public PurchaseOrder(String poNumber) {
        this.poNumber = poNumber;
    }

    public PurchaseOrder(String poNumber, String poItem) {
        this.poNumber = poNumber;
        this.poItem = poItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Character getDeleteInd() {
        return deleteInd;
    }

    public void setDeleteInd(Character deleteInd) {
        this.deleteInd = deleteInd;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getSupplVend() {
        return supplVend;
    }

    public void setSupplVend(String supplVend) {
        this.supplVend = supplVend;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getSupplPlnt() {
        return supplPlnt;
    }

    public void setSupplPlnt(String supplPlnt) {
        this.supplPlnt = supplPlnt;
    }

    public Character getPoRelInd() {
        return poRelInd;
    }

    public void setPoRelInd(Character poRelInd) {
        this.poRelInd = poRelInd;
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

    public void setPartDeliv(Character partDeliv) {
        this.partDeliv = partDeliv;
    }

    public String getRelStatus() {
        return relStatus;
    }

    public void setRelStatus(String relStatus) {
        this.relStatus = relStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchaseOrder that = (PurchaseOrder) o;

        if (id != that.id) return false;
        if (mandt != null ? !mandt.equals(that.mandt) : that.mandt != null) return false;
        if (poNumber != null ? !poNumber.equals(that.poNumber) : that.poNumber != null) return false;
        if (docType != null ? !docType.equals(that.docType) : that.docType != null) return false;
        if (deleteInd != null ? !deleteInd.equals(that.deleteInd) : that.deleteInd != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (creatDate != null ? !creatDate.equals(that.creatDate) : that.creatDate != null) return false;
        if (vendor != null ? !vendor.equals(that.vendor) : that.vendor != null) return false;
        if (supplVend != null ? !supplVend.equals(that.supplVend) : that.supplVend != null) return false;
        if (customer != null ? !customer.equals(that.customer) : that.customer != null) return false;
        if (supplPlnt != null ? !supplPlnt.equals(that.supplPlnt) : that.supplPlnt != null) return false;
        if (poRelInd != null ? !poRelInd.equals(that.poRelInd) : that.poRelInd != null) return false;
        if (poItem != null ? !poItem.equals(that.poItem) : that.poItem != null) return false;
        if (poItemFree != null ? !poItemFree.equals(that.poItemFree) : that.poItemFree != null) return false;
        if (iDeleteInd != null ? !iDeleteInd.equals(that.iDeleteInd) : that.iDeleteInd != null) return false;
        if (ifDeleteInd != null ? !ifDeleteInd.equals(that.ifDeleteInd) : that.ifDeleteInd != null) return false;
        if (shortText != null ? !shortText.equals(that.shortText) : that.shortText != null) return false;
        if (material != null ? !material.equals(that.material) : that.material != null) return false;
        if (plant != null ? !plant.equals(that.plant) : that.plant != null) return false;
        if (stgeLoc != null ? !stgeLoc.equals(that.stgeLoc) : that.stgeLoc != null) return false;
        if (vendMat != null ? !vendMat.equals(that.vendMat) : that.vendMat != null) return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;
        if (quantityFree != null ? !quantityFree.equals(that.quantityFree) : that.quantityFree != null) return false;
        if (poUnit != null ? !poUnit.equals(that.poUnit) : that.poUnit != null) return false;
        if (poUnitIso != null ? !poUnitIso.equals(that.poUnitIso) : that.poUnitIso != null) return false;
        if (qualInsp != null ? !qualInsp.equals(that.qualInsp) : that.qualInsp != null) return false;
        if (overDlvTol != null ? !overDlvTol.equals(that.overDlvTol) : that.overDlvTol != null) return false;
        if (unlimitedDlv != null ? !unlimitedDlv.equals(that.unlimitedDlv) : that.unlimitedDlv != null) return false;
        if (underDlvTol != null ? !underDlvTol.equals(that.underDlvTol) : that.underDlvTol != null) return false;
        if (valType != null ? !valType.equals(that.valType) : that.valType != null) return false;
        if (noMoreGr != null ? !noMoreGr.equals(that.noMoreGr) : that.noMoreGr != null) return false;
        if (finalInv != null ? !finalInv.equals(that.finalInv) : that.finalInv != null) return false;
        if (itemCat != null ? !itemCat.equals(that.itemCat) : that.itemCat != null) return false;
        if (itemFreeCat != null ? !itemFreeCat.equals(that.itemFreeCat) : that.itemFreeCat != null) return false;
        if (grInd != null ? !grInd.equals(that.grInd) : that.grInd != null) return false;
        if (grNonVal != null ? !grNonVal.equals(that.grNonVal) : that.grNonVal != null) return false;
        if (delivCompl != null ? !delivCompl.equals(that.delivCompl) : that.delivCompl != null) return false;
        if (partDeliv != null ? !partDeliv.equals(that.partDeliv) : that.partDeliv != null) return false;
        if (relStatus != null ? !relStatus.equals(that.relStatus) : that.relStatus != null) return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (poNumber != null ? poNumber.hashCode() : 0);
        result = 31 * result + (docType != null ? docType.hashCode() : 0);
        result = 31 * result + (deleteInd != null ? deleteInd.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (creatDate != null ? creatDate.hashCode() : 0);
        result = 31 * result + (vendor != null ? vendor.hashCode() : 0);
        result = 31 * result + (supplVend != null ? supplVend.hashCode() : 0);
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (supplPlnt != null ? supplPlnt.hashCode() : 0);
        result = 31 * result + (poRelInd != null ? poRelInd.hashCode() : 0);
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
        result = 31 * result + (relStatus != null ? relStatus.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.PurchaseOrder[id=" + id + "]";
    }
}
