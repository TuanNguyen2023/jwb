/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Administrator
 */
@Embeddable
public class RegvehiclePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "SO_XE")
    private String soXe;
    @Basic(optional = false)
    @Column(name = "WPLANT")
    private String wplant;
    @Basic(optional = false)
    @Column(name = "ITEM")
    private String item;
    @Basic(optional = false)
    @Column(name = "Reg_category")
    private String regcategory;

    public RegvehiclePK() {
    }

    public RegvehiclePK(String soXe, String wplant, String item, String regcategory) {
        this.soXe = soXe;
        this.wplant = wplant;
        this.item = item;
        this.regcategory = regcategory;
    }

    public String getSoXe() {
        return soXe;
    }

    public void setSoXe(String soXe) {
        this.soXe = soXe;
    }

    public String getWplant() {
        return wplant;
    }

    public void setWplant(String wplant) {
        this.wplant = wplant;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getRegcategory() {
        return regcategory;
    }

    public void setRegcategory(String regcategory) {
        this.regcategory = regcategory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (soXe != null ? soXe.hashCode() : 0);
        hash += (wplant != null ? wplant.hashCode() : 0);
        hash += (item != null ? item.hashCode() : 0);
        hash += (regcategory != null ? regcategory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegvehiclePK)) {
            return false;
        }
        RegvehiclePK other = (RegvehiclePK) object;
        if ((this.soXe == null && other.soXe != null) || (this.soXe != null && !this.soXe.equals(other.soXe))) {
            return false;
        }
        if ((this.wplant == null && other.wplant != null) || (this.wplant != null && !this.wplant.equals(other.wplant))) {
            return false;
        }
        if ((this.item == null && other.item != null) || (this.item != null && !this.item.equals(other.item))) {
            return false;
        }
        if ((this.regcategory == null && other.regcategory != null) || (this.regcategory != null && !this.regcategory.equals(other.regcategory))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.RegvehiclePK[ soXe=" + soXe + ", wplant=" + wplant + ", item=" + item + ", regcategory=" + regcategory + " ]";
    }
    
}
