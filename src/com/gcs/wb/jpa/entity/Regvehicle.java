/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "regvehicle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Regvehicle.findAll", query = "SELECT r FROM Regvehicle r"),
    @NamedQuery(name = "Regvehicle.findBySoXe", query = "SELECT r FROM Regvehicle r WHERE r.regvehiclePK.soXe = :soXe"),
    @NamedQuery(name = "Regvehicle.findByTaAbbr", query = "SELECT r FROM Regvehicle r WHERE r.taAbbr = :taAbbr"),
    @NamedQuery(name = "Regvehicle.findByWplant", query = "SELECT r FROM Regvehicle r WHERE r.regvehiclePK.wplant = :wplant"),
    @NamedQuery(name = "Regvehicle.findByExpdate", query = "SELECT r FROM Regvehicle r WHERE r.expdate = :expdate"),
    @NamedQuery(name = "Regvehicle.findByItem", query = "SELECT r FROM Regvehicle r WHERE r.regvehiclePK.item = :item"),
    @NamedQuery(name = "Regvehicle.findByRegcategory", query = "SELECT r FROM Regvehicle r WHERE r.regvehiclePK.regcategory = :regcategory"),
    @NamedQuery(name = "Regvehicle.findByActive", query = "SELECT r FROM Regvehicle r WHERE r.active = :active"),
    @NamedQuery(name = "Regvehicle.findByNote", query = "SELECT r FROM Regvehicle r WHERE r.note = :note"),
    @NamedQuery(name = "Regvehicle.findByChangeby", query = "SELECT r FROM Regvehicle r WHERE r.changeby = :changeby"),
    @NamedQuery(name = "Regvehicle.findByModifieddate", query = "SELECT r FROM Regvehicle r WHERE r.modifieddate = :modifieddate"),
    @NamedQuery(name = "Regvehicle.newSQL", query = "SELECT r FROM Regvehicle r WHERE r.regvehiclePK.soXe = :soXe AND r.taAbbr = :taAbbr")})
public class Regvehicle implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RegvehiclePK regvehiclePK;
    @Column(name = "TA_ABBR")
    private String taAbbr;
    @Column(name = "EXPDATE")
    @Temporal(TemporalType.DATE)
    private Date expdate;
    @Column(name = "active")
    private Integer active;
    @Column(name = "note")
    private String note;
    @Column(name = "Changeby")
    @Temporal(TemporalType.DATE)
    private Date changeby;
    @Column(name = "Modifieddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifieddate;

    public Regvehicle() {
    }

    public Regvehicle(RegvehiclePK regvehiclePK) {
        this.regvehiclePK = regvehiclePK;
    }

    public Regvehicle(String soXe, String wplant, String item, String regcategory) {
        this.regvehiclePK = new RegvehiclePK(soXe, wplant, item, regcategory);
    }

    public RegvehiclePK getRegvehiclePK() {
        return regvehiclePK;
    }

    public void setRegvehiclePK(RegvehiclePK regvehiclePK) {
        this.regvehiclePK = regvehiclePK;
    }

    public String getTaAbbr() {
        return taAbbr;
    }

    public void setTaAbbr(String taAbbr) {
        this.taAbbr = taAbbr;
    }

    public Date getExpdate() {
        return expdate;
    }

    public void setExpdate(Date expdate) {
        this.expdate = expdate;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getChangeby() {
        return changeby;
    }

    public void setChangeby(Date changeby) {
        this.changeby = changeby;
    }

    public Date getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(Date modifieddate) {
        this.modifieddate = modifieddate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (regvehiclePK != null ? regvehiclePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Regvehicle)) {
            return false;
        }
        Regvehicle other = (Regvehicle) object;
        if ((this.regvehiclePK == null && other.regvehiclePK != null) || (this.regvehiclePK != null && !this.regvehiclePK.equals(other.regvehiclePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Regvehicle[ regvehiclePK=" + regvehiclePK + " ]";
    }
    
}
