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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_material")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Material.findAll", query = "SELECT m FROM Material m"),
    @NamedQuery(name = "Material.CheckPOSTO", query = "SELECT m FROM Material m WHERE m.matnr = :matnr")
})
public class Material implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "matnr", unique = true)
    private String matnr;
    @Column(name = "mandt")
    private String mandt;
    @Column(name = "wplant")
    private String wplant;
    @Column(name = "maktx")
    private String maktx;
    @Column(name = "maktg")
    private String maktg;
    @Column(name = "xchpf")
    private Character xchpf;
    @Column(name = "check_posto")
    private String checkPosto;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "deleted_date")
    private Date deletedDate;
        
    public Material() {
    }

    public Material(int id) {
        this.id = id;
    }
    
    public Material(String mandt, String wplant) {
        this.mandt = mandt;
        this.wplant = wplant;
    }
    
    public Material(String mandt, String wplant, String matnr) {
        this.mandt = mandt;
        this.wplant = wplant;
        this.matnr = matnr;
     }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
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

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getMaktg() {
        return maktg;
    }

    public void setMaktg(String maktg) {
        this.maktg = maktg;
    }

    public Character getXchpf() {
        return xchpf;
    }

    public void setXchpf(Character xchpf) {
        this.xchpf = xchpf;
    }

    public String getCheckPosto() {
        return checkPosto;
    }

    public void setCheckPosto(String checkPosto) {
        this.checkPosto = checkPosto;
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

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deleted_date) {
        this.deletedDate = deleted_date;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Material material = (Material) o;

        if (matnr != null ? !matnr.equals(material.matnr) : material.matnr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (matnr != null ? matnr.hashCode() : 0);
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (wplant != null ? wplant.hashCode() : 0);
        result = 31 * result + (maktx != null ? maktx.hashCode() : 0);
        result = 31 * result + (maktg != null ? maktg.hashCode() : 0);
        result = 31 * result + (xchpf != null ? xchpf.hashCode() : 0);
        result = 31 * result + (checkPosto != null ? checkPosto.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (deletedDate != null ? deletedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Material[id=" + id + "]";
    }
}
