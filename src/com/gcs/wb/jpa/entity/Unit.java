/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.util.Objects;
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
 * @author THANGPT
 */
@Entity
@Table(name = "tbl_unit")
@NamedQueries({
    @NamedQuery(name = "Unit.findAll", query = "SELECT s FROM Unit s")
})
public class Unit implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "weight_ticket_unit")
    private String weightTicketUnit;
    @Column(name = "purchase_unit")
    private String purchaseUnit;
    @Column(name = "material_unit")
    private String materialUnit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeightTicketUnit() {
        return weightTicketUnit;
    }

    public void setWeightTicketUnit(String weightTicketUnit) {
        this.weightTicketUnit = weightTicketUnit;
    }

    public String getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.id;
        hash = 59 * hash + Objects.hashCode(this.weightTicketUnit);
        hash = 59 * hash + Objects.hashCode(this.purchaseUnit);
        hash = 59 * hash + Objects.hashCode(this.materialUnit);
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
        final Unit other = (Unit) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.weightTicketUnit, other.weightTicketUnit)) {
            return false;
        }
        if (!Objects.equals(this.purchaseUnit, other.purchaseUnit)) {
            return false;
        }
        if (!Objects.equals(this.materialUnit, other.materialUnit)) {
            return false;
        }
        return true;
    }
    
    
}
