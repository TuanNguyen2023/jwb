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
@Table(name = "tbl_material_group")
@NamedQueries({
    @NamedQuery(name = "MaterialGroup.findAll", query = "SELECT m FROM MaterialGroup m"),
    @NamedQuery(name = "MaterialGroup.findByMandtWplantMatnr", query = "SELECT m FROM MaterialGroup m WHERE m.mandt = :mandt AND m.wplant = :wplant AND m.matnr = :matnr"),
    @NamedQuery(name = "MaterialGroup.findByMatnr", query = "SELECT m FROM MaterialGroup m WHERE m.matnr = :matnr"),
    @NamedQuery(name = "MaterialGroup.checkMaterialTogether",
            query = "SELECT COUNT(m), COUNT(DISTINCT m.type) FROM MaterialGroup m "
            + " WHERE m.mandt = :mandt "
            + "      AND m.wplant = :wplant "
            + "      AND m.matnr IN :matnrs ")
})
public class MaterialGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "matnr", unique = true)
    private String matnr;
    @Column(name = "mandt", unique = true)
    private String mandt;
    @Column(name = "wplant", unique = true)
    private String wplant;
    @Column(name = "type")
    private String type;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
        
    public MaterialGroup() {
    }

    public MaterialGroup(int id) {
        this.id = id;
    }
    
    public MaterialGroup(String mandt, String wplant) {
        this.mandt = mandt;
        this.wplant = wplant;
    }
    
    public MaterialGroup(String mandt, String wplant, String matnr) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaterialGroup material = (MaterialGroup) o;

        if (matnr != null ? !matnr.equals(material.matnr) : material.matnr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (matnr != null ? matnr.hashCode() : 0);
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (wplant != null ? wplant.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }
}
