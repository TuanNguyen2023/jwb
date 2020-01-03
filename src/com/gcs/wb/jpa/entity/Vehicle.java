/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author vunguyent
 */
@Entity
@Table(name = "Vehicle")
@NamedQueries({
    @NamedQuery(name = "Vehicle.findByTaAbbr", query = "SELECT v FROM Vehicle v WHERE v.taAbbr = :taAbbr")})
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "SO_XE")
    private String soXe;
    @Column(name = "TA_ABBR")
    private String taAbbr;

    public Vehicle() {
    }

    public Vehicle(String soXe) {
        this.soXe = soXe;
    }

    public String getSoXe() {
        return soXe;
    }

    public void setSoXe(String soXe) {
        this.soXe = soXe;
    }

    /**
     * @return the taAbbr
     */
    public String getTaAbbr() {
        return taAbbr;
    }

    /**
     * @param taAbbr the taAbbr to set
     */
    public void setTaAbbr(String taAbbr) {
        this.taAbbr = taAbbr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (soXe != null ? soXe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vehicle)) {
            return false;
        }
        Vehicle other = (Vehicle) object;
        if ((this.soXe == null && other.soXe != null) || (this.soXe != null && !this.soXe.equals(other.soXe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Vehicle[soXe=" + soXe + "]";
    }
}
