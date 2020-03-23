/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author HANGTT
 */
@Entity
@Table(name = "tbl_material_inter_plant")
@NamedQueries({
    @NamedQuery(name = "MaterialInterPlant.findAll", query = "SELECT m FROM MaterialInterPlant m"),
    @NamedQuery(name = "MaterialInterPlant.findByMatnrAndPlantInOut", query = "SELECT m FROM MaterialInterPlant m"
            + " WHERE m.mandt = :mandt "
            + " AND m.matnr = :matnr"
            + " AND m.plantIn = :plantIn"
            + " AND m.plantOut = :plantOut")
})
public class MaterialInterPlant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "mandt", unique = true)
    private String mandt;
    @Column(name = "plant_in", unique = true)
    private String plantIn;
    @Column(name = "plant_out", unique = true)
    private String plantOut;
    @Column(name = "matnr", unique = true)
    private String matnr;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
        
    public MaterialInterPlant() {
    }

    public MaterialInterPlant(int id) {
        this.id = id;
    }
    
    public MaterialInterPlant(String mandt, String wplant) {
        this.mandt = mandt;
    }
    
    public MaterialInterPlant(String mandt, String plantIn, String plantOut, String matnr) {
        this.mandt = mandt;
        this.plantIn = plantIn;
        this.plantOut = plantOut;
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

    public String getPlantIn() {
        return plantIn;
    }

    public void setPlantIn(String plantIn) {
        this.plantIn = plantIn;
    }

    public String getPlantOut() {
        return plantOut;
    }

    public void setPlantOut(String plantOut) {
        this.plantOut = plantOut;
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
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (matnr != null ? matnr.hashCode() : 0);
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (plantIn != null ? plantIn.hashCode() : 0);
        result = 31 * result + (plantOut != null ? plantOut.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }
}
