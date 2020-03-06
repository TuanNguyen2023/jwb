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

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_sale_order")
@NamedQueries({
    @NamedQuery(name = "SaleOrder.findAll", query = "SELECT so FROM SaleOrder so"),
    @NamedQuery(name = "SaleOrder.findBySoNumber", query = "SELECT so FROM SaleOrder so WHERE so.soNumber = :soNumber")
})
public class SaleOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "so_number", unique = true)
    private String soNumber;
    @Column(name = "matnr")
    private String matnr;
    @Column(name = "maktx")
    private String maktx;
    @Column(name = "kwmeng")
    private BigDecimal kwmeng;
    @Column(name = "free_quantity")
    private BigDecimal freeQuantity;
    @Column(name = "rec_quantity")
    private BigDecimal recQuantity;
    @Column(name = "w_name")
    private String wName;
    @Column(name = "ship_to_name")
    private String shipToName;
    @Column(name = "note")
    private String note;
    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;

    public SaleOrder() {
    }

    public SaleOrder(int id) {
        this.id = id;
    }

    public SaleOrder(String soNumber) {
        this.soNumber = soNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSoNumber() {
        return soNumber;
    }

    public void setSoNumber(String soNumber) {
        this.soNumber = soNumber;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public BigDecimal getKwmeng() {
        return kwmeng;
    }

    public void setKwmeng(BigDecimal kwmeng) {
        this.kwmeng = kwmeng;
    }

    public BigDecimal getFreeQuantity() {
        return freeQuantity;
    }

    public void setFreeQuantity(BigDecimal freeQuantity) {
        this.freeQuantity = freeQuantity;
    }

    public BigDecimal getRecQuantity() {
        return recQuantity;
    }

    public void setRecQuantity(BigDecimal recQuantity) {
        this.recQuantity = recQuantity;
    }

    public String getwName() {
        return wName;
    }

    public void setwName(String wName) {
        this.wName = wName;
    }

    public String getShipToName() {
        return shipToName;
    }

    public void setShipToName(String shipToName) {
        this.shipToName = shipToName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date created_date) {
        this.createdDate = created_date;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updated_date) {
        this.updatedDate = updated_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SaleOrder sellOrder = (SaleOrder) o;

        if (soNumber != null ? !soNumber.equals(sellOrder.soNumber) : sellOrder.soNumber != null) {
            return false;
        }

        return true;
    }
}
