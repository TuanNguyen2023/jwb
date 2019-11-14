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
 * @author vunguyent
 */
@Embeddable
public class ReasonPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "BWART")
    private String bwart;
    @Basic(optional = false)
    @Column(name = "GRUND")
    private String grund;

    public ReasonPK() {
    }

    public ReasonPK(String mandt, String bwart, String grund) {
        this.mandt = mandt;
        this.bwart = bwart;
        this.grund = grund;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getBwart() {
        return bwart;
    }

    public void setBwart(String bwart) {
        this.bwart = bwart;
    }

    public String getGrund() {
        return grund;
    }

    public void setGrund(String grund) {
        this.grund = grund;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (bwart != null ? bwart.hashCode() : 0);
        hash += (grund != null ? grund.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ReasonPK)) {
            return false;
        }
        ReasonPK other = (ReasonPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.bwart == null && other.bwart != null) || (this.bwart != null && !this.bwart.equals(other.bwart))) {
            return false;
        }
        if ((this.grund == null && other.grund != null) || (this.grund != null && !this.grund.equals(other.grund))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.ReasonPK[mandt=" + mandt + ", bwart=" + bwart + ", grund=" + grund + "]";
    }
}
