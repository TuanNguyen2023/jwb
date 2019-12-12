/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author vunguyent
 */
@Entity
@Table(name = "Vendor")
@NamedQueries({
    @NamedQuery(name = "Vendor.findAll", query = "SELECT v FROM Vendor v"),
    @NamedQuery(name = "Vendor.findByMandt", query = "SELECT v FROM Vendor v WHERE v.vendorPK.mandt = :mandt"),
    @NamedQuery(name = "Vendor.findByLifnr", query = "SELECT v FROM Vendor v WHERE v.vendorPK.lifnr = :lifnr"),
    @NamedQuery(name = "Vendor.findByName1", query = "SELECT v FROM Vendor v WHERE v.name1 = :name1"),
    @NamedQuery(name = "Vendor.findByName2", query = "SELECT v FROM Vendor v WHERE v.name2 = :name2")})
public class Vendor implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VendorPK vendorPK;
    @Column(name = "NAME1")
    private String name1;
    @Column(name = "NAME2")
    private String name2;

    public Vendor() {
    }

    public Vendor(VendorPK vendorPK) {
        this.vendorPK = vendorPK;
    }

    public Vendor(String mandt, String lifnr) {
        this.vendorPK = new VendorPK(mandt, lifnr);
    }

    public VendorPK getVendorPK() {
        return vendorPK;
    }

    public void setVendorPK(VendorPK vendorPK) {
        this.vendorPK = vendorPK;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vendorPK != null ? vendorPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Vendor)) {
            return false;
        }
        Vendor other = (Vendor) object;
        if ((this.vendorPK == null && other.vendorPK != null) || (this.vendorPK != null && !this.vendorPK.equals(other.vendorPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Vendor[vendorPK=" + vendorPK + "]";
    }
}
