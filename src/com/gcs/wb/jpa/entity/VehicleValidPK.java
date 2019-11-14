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
 * @author gcsadmin
 */
@Embeddable
public class VehicleValidPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "SO_XE")
    private String soXe;
    @Basic(optional = false)
    @Column(name = "WPlant")
    private String wPlant;

    public VehicleValidPK() {
    }

    public VehicleValidPK(String soXe, String wPlant) {
        this.soXe = soXe;
        this.wPlant = wPlant;
    }

    public String getSoXe() {
        return soXe;
    }

    public void setSoXe(String soXe) {
        this.soXe = soXe;
    }

    public String getWPlant() {
        return wPlant;
    }

    public void setWPlant(String wPlant) {
        this.wPlant = wPlant;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (soXe != null ? soXe.hashCode() : 0);
        hash += (wPlant != null ? wPlant.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehicleValidPK)) {
            return false;
        }
        VehicleValidPK other = (VehicleValidPK) object;
        if ((this.soXe == null && other.soXe != null) || (this.soXe != null && !this.soXe.equals(other.soXe))) {
            return false;
        }
        if ((this.wPlant == null && other.wPlant != null) || (this.wPlant != null && !this.wPlant.equals(other.wPlant))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.VehicleValidPK[soXe=" + soXe + ", wPlant=" + wPlant + "]";
    }

}
