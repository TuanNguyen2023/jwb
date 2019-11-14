/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author gcsadmin
 */
@Entity
@Table(name = "vehicle_valid")
@NamedQueries({
    @NamedQuery(name = "VehicleValid.findAll", query = "SELECT v FROM VehicleValid v"),
    @NamedQuery(name = "VehicleValid.findBySoXe", query = "SELECT v FROM VehicleValid v WHERE v.vehicleValidPK.soXe = :soXe"),
    @NamedQuery(name = "VehicleValid.findByWPlant", query = "SELECT v FROM VehicleValid v WHERE v.vehicleValidPK.wPlant = :wPlant"),
    @NamedQuery(name = "VehicleValid.findByNotValid", query = "SELECT v FROM VehicleValid v WHERE v.notValid = :notValid")})
public class VehicleValid implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VehicleValidPK vehicleValidPK;
    @Basic(optional = false)
    @Column(name = "not_valid")
    private boolean notValid;

    public VehicleValid() {
    }

    public VehicleValid(VehicleValidPK vehicleValidPK) {
        this.vehicleValidPK = vehicleValidPK;
    }

    public VehicleValid(VehicleValidPK vehicleValidPK, boolean notValid) {
        this.vehicleValidPK = vehicleValidPK;
        this.notValid = notValid;
    }

    public VehicleValid(String soXe, String wPlant) {
        this.vehicleValidPK = new VehicleValidPK(soXe, wPlant);
    }

    public VehicleValidPK getVehicleValidPK() {
        return vehicleValidPK;
    }

    public void setVehicleValidPK(VehicleValidPK vehicleValidPK) {
        this.vehicleValidPK = vehicleValidPK;
    }

    public boolean getNotValid() {
        return notValid;
    }

    public void setNotValid(boolean notValid) {
        this.notValid = notValid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vehicleValidPK != null ? vehicleValidPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VehicleValid)) {
            return false;
        }
        VehicleValid other = (VehicleValid) object;
        if ((this.vehicleValidPK == null && other.vehicleValidPK != null) || (this.vehicleValidPK != null && !this.vehicleValidPK.equals(other.vehicleValidPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.VehicleValid[vehicleValidPK=" + vehicleValidPK + "]";
    }

}
