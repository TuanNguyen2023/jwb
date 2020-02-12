/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author thangtp.nr
 */
@Entity
@Table(name = "tbl_material_internal")
public class MaterialInternal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "mandt", unique = true)
    private String mandt;
    @Column(name = "wplant", unique = true)
    private String wplant;
    @Column(name = "matnr", unique = true)
    private String matnr;
    @Column(name = "maktx")
    private String maktx;
    @Column(name = "maktg")
    private String maktg;
    @Column(name = "xchpf")
    private Character xchpf;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;

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

    public String getWplant() {
        return wplant;
    }

    public void setWplant(String wplant) {
        this.wplant = wplant;
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
    public int hashCode() {
        int hash = (int) (id ^ (id >>> 32));
        hash = 32 * hash + this.id;
        hash = 32 * hash + Objects.hashCode(this.mandt);
        hash = 32 * hash + Objects.hashCode(this.wplant);
        hash = 32 * hash + Objects.hashCode(this.matnr);
        hash = 32 * hash + Objects.hashCode(this.maktx);
        hash = 32 * hash + Objects.hashCode(this.maktg);
        hash = 32 * hash + Objects.hashCode(this.xchpf);
        hash = 32 * hash + Objects.hashCode(this.createdDate);
        hash = 32 * hash + Objects.hashCode(this.updatedDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MaterialInternal other = (MaterialInternal) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.mandt, other.mandt)) {
            return false;
        }
        if (!Objects.equals(this.wplant, other.wplant)) {
            return false;
        }
        if (!Objects.equals(this.matnr, other.matnr)) {
            return false;
        }
        if (!Objects.equals(this.maktx, other.maktx)) {
            return false;
        }
        if (!Objects.equals(this.maktg, other.maktg)) {
            return false;
        }
        if (!Objects.equals(this.xchpf, other.xchpf)) {
            return false;
        }
        if (!Objects.equals(this.createdDate, other.createdDate)) {
            return false;
        }
        if (!Objects.equals(this.updatedDate, other.updatedDate)) {
            return false;
        }
        return true;
    }
    
}
