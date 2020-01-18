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
@Table(name = "tbl_varsp")
@NamedQueries({
    @NamedQuery(name = "Varsp.findAll", query = "SELECT v FROM Varsp v"),
    @NamedQuery(name = "Varsp.findBySpidWbidMatid",
    query = "SELECT v FROM Varsp v WHERE v.spid = :spid AND v.wbid = :wbid AND v.matid = :matid")
})
public class Varsp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "spid", unique = true)
    private String spid;
    @Column(name = "wbid", unique = true)
    private String wbid;
    @Column(name = "matid", unique = true)
    private String matid;
    @Column(name = "note")
    private String note;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "deleted_date")
    private Date deletedDate;

    public Varsp() {
    }

    public Varsp(String spid, String wbid, String matid) {
        this.spid = spid;
        this.wbid = wbid;
        this.matid = matid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    public String getWbid() {
        return wbid;
    }

    public void setWbid(String wbid) {
        this.wbid = wbid;
    }

    public String getMatid() {
        return matid;
    }

    public void setMatid(String matid) {
        this.matid = matid;
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

        Varsp varsp = (Varsp) o;

        if (id != varsp.id) return false;
        if (spid != null ? !spid.equals(varsp.spid) : varsp.spid != null) return false;
        if (wbid != null ? !wbid.equals(varsp.wbid) : varsp.wbid != null) return false;
        if (matid != null ? !matid.equals(varsp.matid) : varsp.matid != null) return false;
        if (note != null ? !note.equals(varsp.note) : varsp.note != null) return false;
        if (createdDate != null ? !createdDate.equals(varsp.createdDate) : varsp.createdDate != null) return false;
        if (updatedDate != null ? !updatedDate.equals(varsp.updatedDate) : varsp.updatedDate != null) return false;
        if (deletedDate != null ? !deletedDate.equals(varsp.deletedDate) : varsp.deletedDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (spid != null ? spid.hashCode() : 0);
        result = 31 * result + (wbid != null ? wbid.hashCode() : 0);
        result = 31 * result + (matid != null ? matid.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (deletedDate != null ? deletedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Varsp[id=" + id + "]";
    }
}
