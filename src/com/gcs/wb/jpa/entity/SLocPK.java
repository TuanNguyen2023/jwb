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
public class SLocPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "WPlant")
    private String wPlant;
    @Basic(optional = false)
    @Column(name = "LGORT")
    private String lgort;

    public SLocPK() {
    }

    public SLocPK(String mandt, String wPlant, String lgort) {
        this.mandt = mandt;
        this.wPlant = wPlant;
        this.lgort = lgort;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getWPlant() {
        return wPlant;
    }

    public void setWPlant(String wPlant) {
        this.wPlant = wPlant;
    }

    public String getLgort() {
        return lgort;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (wPlant != null ? wPlant.hashCode() : 0);
        hash += (lgort != null ? lgort.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof SLocPK)) {
            return false;
        }
        SLocPK other = (SLocPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.wPlant == null && other.wPlant != null) || (this.wPlant != null && !this.wPlant.equals(other.wPlant))) {
            return false;
        }
        if ((this.lgort == null && other.lgort != null) || (this.lgort != null && !this.lgort.equals(other.lgort))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.SLocPK[mandt=" + mandt + ", wPlant=" + wPlant + ", lgort=" + lgort + "]";
    }
}
