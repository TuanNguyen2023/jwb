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
 * @author admin
 */
@Embeddable
public class UserLocalPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "WPLANT")
    private String wplant;
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;

    public UserLocalPK() {
    }

    public UserLocalPK(String mandt, String wplant, String id) {
        this.mandt = mandt;
        this.wplant = wplant;
        this.id = id;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getWplant() {
        return wplant;
    }

    public void setWplant(String wplant) {
        this.wplant = wplant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (wplant != null ? wplant.hashCode() : 0);
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserLocalPK)) {
            return false;
        }
        UserLocalPK other = (UserLocalPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.wplant == null && other.wplant != null) || (this.wplant != null && !this.wplant.equals(other.wplant))) {
            return false;
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.UserLocalPK[ mandt=" + mandt + ", wplant=" + wplant + ", id=" + id + " ]";
    }
}
