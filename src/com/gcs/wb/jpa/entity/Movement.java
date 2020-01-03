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
 * @author vunguyent
 */
@Entity
@Table(name = "Movement")
@NamedQueries({
    @NamedQuery(name = "Movement.findByMandtBwartSpras", query = "SELECT m FROM Movement m WHERE m.movementPK.mandt = :mandt AND m.movementPK.bwart = :bwart AND m.spras = :spras")})
public class Movement implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MovementPK movementPK;
    @Basic(optional = false)
    @Column(name = "SPRAS")
    private String spras;
    @Column(name = "BTEXT")
    private String btext;

    public Movement() {
    }

    public Movement(MovementPK movementPK) {
        this.movementPK = movementPK;
    }

    public Movement(MovementPK movementPK, String spras) {
        this.movementPK = movementPK;
        this.spras = spras;
    }

    public Movement(String mandt, String bwart) {
        this.movementPK = new MovementPK(mandt, bwart);
    }

    public MovementPK getMovementPK() {
        return movementPK;
    }

    public void setMovementPK(MovementPK movementPK) {
        this.movementPK = movementPK;
    }

    public String getSpras() {
        return spras;
    }

    public void setSpras(String spras) {
        this.spras = spras;
    }

    public String getBtext() {
        return btext;
    }

    public void setBtext(String btext) {
        this.btext = btext;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movementPK != null ? movementPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Movement)) {
            return false;
        }
        Movement other = (Movement) object;
        if ((this.movementPK == null && other.movementPK != null) || (this.movementPK != null && !this.movementPK.equals(other.movementPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Movement[movementPK=" + movementPK + "]";
    }
}
