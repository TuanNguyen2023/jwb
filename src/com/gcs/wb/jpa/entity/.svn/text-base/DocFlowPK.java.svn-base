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
public class DocFlowPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "VBELV")
    private String vbelv;
    @Basic(optional = false)
    @Column(name = "VBELN")
    private String vbeln;

    public DocFlowPK() {
    }

    public DocFlowPK(String mandt, String vbelv, String vbeln) {
        this.mandt = mandt;
        this.vbelv = vbelv;
        this.vbeln = vbeln;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getVbelv() {
        return vbelv;
    }

    public void setVbelv(String vbelv) {
        this.vbelv = vbelv;
    }

    public String getVbeln() {
        return vbeln;
    }

    public void setVbeln(String vbeln) {
        this.vbeln = vbeln;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (vbelv != null ? vbelv.hashCode() : 0);
        hash += (vbeln != null ? vbeln.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof DocFlowPK)) {
            return false;
        }
        DocFlowPK other = (DocFlowPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.vbelv == null && other.vbelv != null) || (this.vbelv != null && !this.vbelv.equals(other.vbelv))) {
            return false;
        }
        if ((this.vbeln == null && other.vbeln != null) || (this.vbeln != null && !this.vbeln.equals(other.vbeln))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.DocFlowPK[mandt=" + mandt + ", vbelv=" + vbelv + ", vbeln=" + vbeln + "]";
    }
}
