/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author vunguyent
 */
@Entity
@Table(name = "PurOrder")
@NamedQueries({
    @NamedQuery(name = "PurOrder.findAll", query = "SELECT p FROM PurOrder p"),
    @NamedQuery(name = "PurOrder.findByMandt", query = "SELECT p FROM PurOrder p WHERE p.purOrderPK.mandt = :mandt"),
    @NamedQuery(name = "PurOrder.findByPoNumber", query = "SELECT p FROM PurOrder p WHERE p.purOrderPK.poNumber = :poNumber"),
    @NamedQuery(name = "PurOrder.findByDocType", query = "SELECT p FROM PurOrder p WHERE p.docType = :docType"),
    @NamedQuery(name = "PurOrder.findByDeleteInd", query = "SELECT p FROM PurOrder p WHERE p.deleteInd = :deleteInd"),
    @NamedQuery(name = "PurOrder.findByStatus", query = "SELECT p FROM PurOrder p WHERE p.status = :status"),
    @NamedQuery(name = "PurOrder.findByCreatDate", query = "SELECT p FROM PurOrder p WHERE p.creatDate = :creatDate"),
    @NamedQuery(name = "PurOrder.findByVendor", query = "SELECT p FROM PurOrder p WHERE p.vendor = :vendor"),
    @NamedQuery(name = "PurOrder.findBySupplVend", query = "SELECT p FROM PurOrder p WHERE p.supplVend = :supplVend"),
    @NamedQuery(name = "PurOrder.findByCustomer", query = "SELECT p FROM PurOrder p WHERE p.customer = :customer"),
    @NamedQuery(name = "PurOrder.findBySupplPlnt", query = "SELECT p FROM PurOrder p WHERE p.supplPlnt = :supplPlnt"),
    @NamedQuery(name = "PurOrder.findByPoRelInd", query = "SELECT p FROM PurOrder p WHERE p.poRelInd = :poRelInd"),
    @NamedQuery(name = "PurOrder.findByPoItem", query = "SELECT p FROM PurOrder p WHERE p.poItem = :poItem"),
    @NamedQuery(name = "PurOrder.findByPoItemFree", query = "SELECT p FROM PurOrder p WHERE p.poItemFree = :poItemFree"),
    @NamedQuery(name = "PurOrder.findByIDeleteInd", query = "SELECT p FROM PurOrder p WHERE p.iDeleteInd = :iDeleteInd"),
    @NamedQuery(name = "PurOrder.findByIfDeleteInd", query = "SELECT p FROM PurOrder p WHERE p.ifDeleteInd = :ifDeleteInd"),
    @NamedQuery(name = "PurOrder.findByShortText", query = "SELECT p FROM PurOrder p WHERE p.shortText = :shortText"),
    @NamedQuery(name = "PurOrder.findByMaterial", query = "SELECT p FROM PurOrder p WHERE p.material = :material"),
    @NamedQuery(name = "PurOrder.findByPlant", query = "SELECT p FROM PurOrder p WHERE p.plant = :plant"),
    @NamedQuery(name = "PurOrder.findByStgeLoc", query = "SELECT p FROM PurOrder p WHERE p.stgeLoc = :stgeLoc"),
    @NamedQuery(name = "PurOrder.findByVendMat", query = "SELECT p FROM PurOrder p WHERE p.vendMat = :vendMat"),
    @NamedQuery(name = "PurOrder.findByQuantity", query = "SELECT p FROM PurOrder p WHERE p.quantity = :quantity"),
    @NamedQuery(name = "PurOrder.findByQuantityFree", query = "SELECT p FROM PurOrder p WHERE p.quantityFree = :quantityFree"),
    @NamedQuery(name = "PurOrder.findByPoUnit", query = "SELECT p FROM PurOrder p WHERE p.poUnit = :poUnit"),
    @NamedQuery(name = "PurOrder.findByPoUnitIso", query = "SELECT p FROM PurOrder p WHERE p.poUnitIso = :poUnitIso"),
    @NamedQuery(name = "PurOrder.findByQualInsp", query = "SELECT p FROM PurOrder p WHERE p.qualInsp = :qualInsp"),
    @NamedQuery(name = "PurOrder.findByOverDlvTol", query = "SELECT p FROM PurOrder p WHERE p.overDlvTol = :overDlvTol"),
    @NamedQuery(name = "PurOrder.findByUnlimitedDlv", query = "SELECT p FROM PurOrder p WHERE p.unlimitedDlv = :unlimitedDlv"),
    @NamedQuery(name = "PurOrder.findByUnderDlvTol", query = "SELECT p FROM PurOrder p WHERE p.underDlvTol = :underDlvTol"),
    @NamedQuery(name = "PurOrder.findByValType", query = "SELECT p FROM PurOrder p WHERE p.valType = :valType"),
    @NamedQuery(name = "PurOrder.findByNoMoreGr", query = "SELECT p FROM PurOrder p WHERE p.noMoreGr = :noMoreGr"),
    @NamedQuery(name = "PurOrder.findByFinalInv", query = "SELECT p FROM PurOrder p WHERE p.finalInv = :finalInv"),
    @NamedQuery(name = "PurOrder.findByItemCat", query = "SELECT p FROM PurOrder p WHERE p.itemCat = :itemCat"),
    @NamedQuery(name = "PurOrder.findByItemFreeCat", query = "SELECT p FROM PurOrder p WHERE p.itemFreeCat = :itemFreeCat"),
    @NamedQuery(name = "PurOrder.findByGrInd", query = "SELECT p FROM PurOrder p WHERE p.grInd = :grInd"),
    @NamedQuery(name = "PurOrder.findByGrNonVal", query = "SELECT p FROM PurOrder p WHERE p.grNonVal = :grNonVal"),
    @NamedQuery(name = "PurOrder.findByDelivCompl", query = "SELECT p FROM PurOrder p WHERE p.delivCompl = :delivCompl"),
    @NamedQuery(name = "PurOrder.findByPartDeliv", query = "SELECT p FROM PurOrder p WHERE p.partDeliv = :partDeliv"),
    @NamedQuery(name = "PurOrder.findByRelStatus", query = "SELECT p FROM PurOrder p WHERE p.relStatus = :relStatus")})
public class PurOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PurOrderPK purOrderPK;
    @Column(name = "DOC_TYPE")
    private String docType;
    @Column(name = "DELETE_IND")
    private Character deleteInd;
    @Column(name = "STATUS")
    private Character status;
    @Column(name = "CREAT_DATE")
    @Temporal(TemporalType.DATE)
    private Date creatDate;
    @Column(name = "VENDOR")
    private String vendor;
    @Column(name = "SUPPL_VEND")
    private String supplVend;
    @Column(name = "CUSTOMER")
    private String customer;
    @Column(name = "SUPPL_PLNT")
    private String supplPlnt;
    @Column(name = "PO_REL_IND")
    private Character poRelInd;
    @Basic(optional = false)
    @Column(name = "PO_ITEM")
    private String poItem;
    @Column(name = "PO_ITEM_FREE")
    private String poItemFree;
    @Column(name = "I_DELETE_IND")
    private Character iDeleteInd;
    @Column(name = "IF_DELETE_IND")
    private Character ifDeleteInd;
    @Column(name = "SHORT_TEXT")
    private String shortText;
    @Column(name = "MATERIAL")
    private String material;
    @Column(name = "PLANT")
    private String plant;
    @Column(name = "STGE_LOC")
    private String stgeLoc;
    @Column(name = "VEND_MAT")
    private String vendMat;
    @Column(name = "QUANTITY")
    private BigDecimal quantity;
    @Column(name = "QUANTITY_FREE")
    private BigDecimal quantityFree;
    @Column(name = "PO_UNIT")
    private String poUnit;
    @Column(name = "PO_UNIT_ISO")
    private String poUnitIso;
    @Column(name = "QUAL_INSP")
    private Character qualInsp;
    @Column(name = "OVER_DLV_TOL")
    private BigDecimal overDlvTol;
    @Column(name = "UNLIMITED_DLV")
    private Character unlimitedDlv;
    @Column(name = "UNDER_DLV_TOL")
    private BigDecimal underDlvTol;
    @Column(name = "VAL_TYPE")
    private String valType;
    @Column(name = "NO_MORE_GR")
    private Character noMoreGr;
    @Column(name = "FINAL_INV")
    private Character finalInv;
    @Column(name = "ITEM_CAT")
    private Character itemCat;
    @Column(name = "ITEM_FREE_CAT")
    private Character itemFreeCat;
    @Column(name = "GR_IND")
    private Character grInd;
    @Column(name = "GR_NON_VAL")
    private Character grNonVal;
    @Column(name = "DELIV_COMPL")
    private Character delivCompl;
    @Column(name = "PART_DELIV")
    private Character partDeliv;
    @Column(name = "REL_STATUS")
    private String relStatus;

    public PurOrder() {
    }

    public PurOrder(PurOrderPK purOrderPK) {
        this.purOrderPK = purOrderPK;
    }

    public PurOrder(PurOrderPK purOrderPK, String poItem) {
        this.purOrderPK = purOrderPK;
        this.poItem = poItem;
    }

    public PurOrder(String mandt, String poNumber) {
        this.purOrderPK = new PurOrderPK(mandt, poNumber);
    }

    public PurOrderPK getPurOrderPK() {
        return purOrderPK;
    }

    public void setPurOrderPK(PurOrderPK purOrderPK) {
        this.purOrderPK = purOrderPK;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (purOrderPK != null ? purOrderPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PurOrder)) {
            return false;
        }
        PurOrder other = (PurOrder) object;
        if ((this.purOrderPK == null && other.purOrderPK != null) || (this.purOrderPK != null && !this.purOrderPK.equals(other.purOrderPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.PurOrder[purOrderPK=" + purOrderPK + "]";
    }
}
