/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_variant")
@NamedQueries({
    @NamedQuery(name = "Variant.findByParam", query = "SELECT v FROM Variant v WHERE v.param = :param")})
public class Variant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "param", unique = true)
    private String param;
    @Column(name = "mandt")
    private String mandt;
    @Column(name = "wplant")
    private String wplant;
    @Column(name = "value")
    private String value;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "deleted_date")
    private Date deletedDate;

    public Variant() {
    }

    public Variant(String param) {
        this.param = param;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

        Variant variant = (Variant) o;

        if (id != variant.id) return false;
        if (mandt != null ? !mandt.equals(variant.mandt) : variant.mandt != null) return false;
        if (wplant != null ? !wplant.equals(variant.wplant) : variant.wplant != null) return false;
        if (param != null ? !param.equals(variant.param) : variant.param != null) return false;
        if (value != null ? !value.equals(variant.value) : variant.value != null) return false;
        if (createdDate != null ? !createdDate.equals(variant.createdDate) : variant.createdDate != null) return false;
        if (updatedDate != null ? !updatedDate.equals(variant.updatedDate) : variant.updatedDate != null) return false;
        if (deletedDate != null ? !deletedDate.equals(variant.deletedDate) : variant.deletedDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (wplant != null ? wplant.hashCode() : 0);
        result = 31 * result + (param != null ? param.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (deletedDate != null ? deletedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Variant[id=" + id + "]";
    }
}
