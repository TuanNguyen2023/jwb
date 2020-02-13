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
@Table(name = "tbl_material_constraint")
public class MaterialConstraint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "matnr", unique = true)
    private String matnr;
    @Column(name = "required_niem_xa")
    private Boolean requiredNiemXa;
    @Column(name = "required_batch")
    private Boolean requiredBatch;
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

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public Boolean getRequiredNiemXa() {
        return requiredNiemXa;
    }

    public void setRequiredNiemXa(Boolean requiredNiemXa) {
        this.requiredNiemXa = requiredNiemXa;
    }

    public Boolean getRequiredBatch() {
        return requiredBatch;
    }

    public void setRequiredBatch(Boolean requiredBatch) {
        this.requiredBatch = requiredBatch;
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
        hash = 32 * hash + Objects.hashCode(this.matnr);
        hash = 32 * hash + Objects.hashCode(this.requiredNiemXa);
        hash = 32 * hash + Objects.hashCode(this.requiredBatch);
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
        final MaterialConstraint other = (MaterialConstraint) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.matnr, other.matnr)) {
            return false;
        }
        if (!Objects.equals(this.requiredNiemXa, other.requiredNiemXa)) {
            return false;
        }
        if (!Objects.equals(this.requiredBatch, other.requiredBatch)) {
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
