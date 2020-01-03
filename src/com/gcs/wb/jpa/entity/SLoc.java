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
@Table(name = "SLoc")
@NamedQueries({
    @NamedQuery(name = "SLoc.findByMandtWPlant", query = "SELECT s FROM SLoc s WHERE s.sLocPK.mandt = :mandt AND s.sLocPK.wPlant = :wPlant")})
public class SLoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SLocPK sLocPK;
    @Column(name = "LGOBE")
    private String lgobe;

    public SLoc() {
    }

    public SLoc(SLocPK sLocPK) {
        this.sLocPK = sLocPK;
    }

    public SLoc(String mandt, String wPlant, String lgort) {
        this.sLocPK = new SLocPK(mandt, wPlant, lgort);
    }

    public SLocPK getSLocPK() {
        return sLocPK;
    }

    public void setSLocPK(SLocPK sLocPK) {
        this.sLocPK = sLocPK;
    }

    public String getLgobe() {
        return lgobe;
    }

    public void setLgobe(String lgobe) {
        this.lgobe = lgobe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sLocPK != null ? sLocPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof SLoc)) {
            return false;
        }
        SLoc other = (SLoc) object;
        if ((this.sLocPK == null && other.sLocPK != null) || (this.sLocPK != null && !this.sLocPK.equals(other.sLocPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.SLoc[sLocPK=" + sLocPK + "]";
    }
}
