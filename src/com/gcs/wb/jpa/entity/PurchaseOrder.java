/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    @Column(name = "mandt", unique = true)
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
    @Column(name = "rel_status")
    private String relStatus;
    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(TemporalType.DATE)
    private Date updatedDate;
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "purchase_order_id")
    private List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();

    public PurchaseOrder() {
    }

    public PurchaseOrder(String mandt, String poNumber) {
        this.mandt = mandt;
        this.poNumber = poNumber;
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

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<PurchaseOrderDetail> getPurchaseOrderDetails() {
        return purchaseOrderDetails;
    }

    public void setPurchaseOrderDetails(List<PurchaseOrderDetail> purchaseOrderDetails) {
        this.purchaseOrderDetails = purchaseOrderDetails;
    }
    
    public void addPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
        purchaseOrderDetail.setPurchaseOrder(this);
        purchaseOrderDetails.add(purchaseOrderDetail);
    }

    public PurchaseOrderDetail getPurchaseOrderDetail() {
        if (purchaseOrderDetails.isEmpty()) {
            PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
            purchaseOrderDetail.setPurchaseOrder(this);
            purchaseOrderDetails.add(purchaseOrderDetail);
        }
        return purchaseOrderDetails.stream()
		.sorted(Comparator.comparing(PurchaseOrderDetail::getPoItem))
		.collect(Collectors.toList()).get(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PurchaseOrder that = (PurchaseOrder) o;

        if (poNumber != null ? !poNumber.equals(that.poNumber) : that.poNumber != null) {
            return false;
        }

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
        result = 31 * result + (relStatus != null ? relStatus.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.PurchaseOrder[id=" + id + "]";
    }
}
