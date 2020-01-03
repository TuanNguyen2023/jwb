/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
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
@Table(name = "Reason")
@NamedQueries({
    @NamedQuery(name = "Reason.findByMandtBwart", query = "SELECT r FROM Reason r WHERE r.reasonPK.mandt = :mandt AND r.reasonPK.bwart = :bwart")})
public class Reason implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReasonPK reasonPK;
    @Column(name = "GRTXT")
    private String grtxt;

    public Reason() {
    }

    public Reason(ReasonPK reasonPK) {
        this.reasonPK = reasonPK;
    }

    public Reason(String mandt, String bwart, String grund) {
        this.reasonPK = new ReasonPK(mandt, bwart, grund);
    }

    public ReasonPK getReasonPK() {
        return reasonPK;
    }

    public void setReasonPK(ReasonPK reasonPK) {
        this.reasonPK = reasonPK;
    }

    public String getGrtxt() {
        return grtxt;
    }

    public void setGrtxt(String grtxt) {
        this.grtxt = grtxt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reasonPK != null ? reasonPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Reason)) {
            return false;
        }
        Reason other = (Reason) object;
        if ((this.reasonPK == null && other.reasonPK != null) || (this.reasonPK != null && !this.reasonPK.equals(other.reasonPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Reason[reasonPK=" + reasonPK + "]";
    }
}
