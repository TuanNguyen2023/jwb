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
 * @author gcsadmin
 */
@Embeddable
public class VariantPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "WPlant")
    private String wPlant;
    @Basic(optional = false)
    @Column(name = "PARAM")
    private String param;

    public VariantPK() {
    }

    public VariantPK(String mandt, String wPlant, String param) {
        this.mandt = mandt;
        this.wPlant = wPlant;
        this.param = param;
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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (wPlant != null ? wPlant.hashCode() : 0);
        hash += (param != null ? param.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariantPK)) {
            return false;
        }
        VariantPK other = (VariantPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.wPlant == null && other.wPlant != null) || (this.wPlant != null && !this.wPlant.equals(other.wPlant))) {
            return false;
        }
        if ((this.param == null && other.param != null) || (this.param != null && !this.param.equals(other.param))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.VariantPK[mandt=" + mandt + ", wPlant=" + wPlant + ", param=" + param + "]";
    }

}
