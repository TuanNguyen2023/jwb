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
public class UnitPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "MSEHI")
    private String msehi;

    public UnitPK() {
    }

    public UnitPK(String mandt, String msehi) {
        this.mandt = mandt;
        this.msehi = msehi;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getMsehi() {
        return msehi;
    }

    public void setMsehi(String msehi) {
        this.msehi = msehi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (msehi != null ? msehi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof UnitPK)) {
            return false;
        }
        UnitPK other = (UnitPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.msehi == null && other.msehi != null) || (this.msehi != null && !this.msehi.equals(other.msehi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.UnitPK[mandt=" + mandt + ", msehi=" + msehi + "]";
    }
}
