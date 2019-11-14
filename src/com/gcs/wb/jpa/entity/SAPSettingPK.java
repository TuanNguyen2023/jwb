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
public class SAPSettingPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "WPlant")
    private String wPlant;

    public SAPSettingPK() {
    }

    public SAPSettingPK(String mandt, String wPlant) {
        this.mandt = mandt;
        this.wPlant = wPlant;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (wPlant != null ? wPlant.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof SAPSettingPK)) {
            return false;
        }
        SAPSettingPK other = (SAPSettingPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.wPlant == null && other.wPlant != null) || (this.wPlant != null && !this.wPlant.equals(other.wPlant))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.SAPSettingPK[mandt=" + mandt + ", wPlant=" + wPlant + "]";
    }
}
