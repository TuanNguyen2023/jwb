/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_vendor")
@NamedQueries({
    @NamedQuery(name = "Vendor.findAll", query = "SELECT v FROM Vendor v WHERE v.groupType IS NULL"),
    @NamedQuery(name = "Vendor.findByMandtWplant", query = "SELECT v FROM Vendor v WHERE v.mandt = :mandt AND v.wplant = :wplant AND v.groupType IS NULL"),
    @NamedQuery(name = "Vendor.findByMandtWplantLifnr", query = "SELECT v FROM Vendor v WHERE v.mandt = :mandt AND v.wplant = :wplant AND v.lifnr = :lifnr AND v.groupType IS NULL"),
    @NamedQuery(name = "Vendor.findByMandtWplantEkorg", query = "SELECT v FROM Vendor v WHERE v.mandt = :mandt AND v.wplant = :wplant AND v.ekorg = :ekorg AND v.groupType IS NULL"),
    @NamedQuery(name = "Vendor.findByMandtWplantLifnrIsCustomer", query = "SELECT v FROM Vendor v"
            + " WHERE v.mandt = :mandt"
            + " AND v.wplant = :wplant"
            + " AND v.lifnr = :lifnr"
            + " AND v.groupType = :groupType"),
    @NamedQuery(name = "Vendor.findByMandtWplantIsCustomer", query = "SELECT v FROM Vendor v WHERE v.mandt = :mandt AND v.wplant = :wplant AND v.groupType = :groupType")
})
public class Vendor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "lifnr", unique = true)
    private String lifnr;
    @Column(name = "mandt", unique = true)
    private String mandt;
    @Column(name = "wplant", unique = true)
    private String wplant;
    @Column(name = "name1")
    private String name1;
    @Column(name = "name2")
    private String name2;
    @Column(name = "ekorg", unique = true)
    private String ekorg;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "group_type")
    private String groupType;

    public Vendor() {
    }

    public Vendor(String lifnr) {
        this.lifnr = lifnr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getWplant() {
        return wplant;
    }

    public void setWplant(String wplant) {
        this.wplant = wplant;
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
    
    public String getEkorg() {
        return ekorg;
    }

    public void setEkorg(String ekorg) {
        this.ekorg = ekorg;
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
    
    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vendor vendor = (Vendor) o;
        if (lifnr != null ? !lifnr.equals(vendor.lifnr) : vendor.lifnr != null) {
            return false;
        }
        if (mandt != null ? !mandt.equals(vendor.mandt) : vendor.mandt != null) {
            return false;
        }
        if (wplant != null ? !wplant.equals(vendor.wplant) : vendor.wplant != null) {
            return false;
        }
        if (ekorg != null ? !ekorg.equals(vendor.ekorg) : vendor.ekorg != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (lifnr != null ? lifnr.hashCode() : 0);
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (name1 != null ? name1.hashCode() : 0);
        result = 31 * result + (name2 != null ? name2.hashCode() : 0);
        result = 31 * result + (ekorg != null ? ekorg.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return this.getName1();
    }
}
