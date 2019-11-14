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
public class VendorPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "LIFNR")
    private String lifnr;

    public VendorPK() {
    }

    public VendorPK(String mandt, String lifnr) {
        this.mandt = mandt;
        this.lifnr = lifnr;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (lifnr != null ? lifnr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof VendorPK)) {
            return false;
        }
        VendorPK other = (VendorPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.lifnr == null && other.lifnr != null) || (this.lifnr != null && !this.lifnr.equals(other.lifnr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.VendorPK[mandt=" + mandt + ", lifnr=" + lifnr + "]";
    }
}
