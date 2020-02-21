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
@Table(name = "tbl_sloc")
@NamedQueries({
    @NamedQuery(name = "SLoc.findAll", query = "SELECT s FROM SLoc s"),
    @NamedQuery(name = "SLoc.findByMandtWplant", query = "SELECT s FROM SLoc s WHERE s.mandt = :mandt AND s.wplant = :wplant"),
    @NamedQuery(name = "SLoc.findByLgort", query = "SELECT s FROM SLoc s WHERE s.lgort = :lgort")
})
public class SLoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "lgort", unique = true)
    private String lgort;
    @Column(name = "mandt", unique = true)
    private String mandt;
    @Column(name = "wplant", unique = true)
    private String wplant;
    @Column(name = "lgobe")
    private String lgobe;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;

    public SLoc() {
    }

    public SLoc(String lgort) {
        this.lgort = lgort;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLgort() {
        return lgort;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort;
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

    public String getLgobe() {
        return lgobe;
    }

    public void setLgobe(String lgobe) {
        this.lgobe = lgobe;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SLoc sloc = (SLoc) o;
        if (lgort != null ? !lgort.equals(sloc.lgort) : sloc.lgort != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (lgort != null ? lgort.hashCode() : 0);
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (wplant != null ? wplant.hashCode() : 0);
        result = 31 * result + (lgobe != null ? lgobe.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.SLoc[id=" + id + "]";
    }
}
