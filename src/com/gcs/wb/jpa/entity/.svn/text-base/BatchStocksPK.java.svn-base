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
public class BatchStocksPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "WERKS")
    private String werks;
    @Basic(optional = false)
    @Column(name = "LGORT")
    private String lgort;
    @Basic(optional = false)
    @Column(name = "MATNR")
    private String matnr;
    @Basic(optional = false)
    @Column(name = "CHARG")
    private String charg;

    public BatchStocksPK() {
    }

    public BatchStocksPK(String mandt, String werks, String lgort, String matnr, String charg) {
        this.mandt = mandt;
        this.werks = werks;
        this.lgort = lgort;
        this.matnr = matnr;
        this.charg = charg;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getLgort() {
        return lgort;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getCharg() {
        return charg;
    }

    public void setCharg(String charg) {
        this.charg = charg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (werks != null ? werks.hashCode() : 0);
        hash += (lgort != null ? lgort.hashCode() : 0);
        hash += (matnr != null ? matnr.hashCode() : 0);
        hash += (charg != null ? charg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof BatchStocksPK)) {
            return false;
        }
        BatchStocksPK other = (BatchStocksPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.werks == null && other.werks != null) || (this.werks != null && !this.werks.equals(other.werks))) {
            return false;
        }
        if ((this.lgort == null && other.lgort != null) || (this.lgort != null && !this.lgort.equals(other.lgort))) {
            return false;
        }
        if ((this.matnr == null && other.matnr != null) || (this.matnr != null && !this.matnr.equals(other.matnr))) {
            return false;
        }
        if ((this.charg == null && other.charg != null) || (this.charg != null && !this.charg.equals(other.charg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.BatchStocksPK[mandt=" + mandt + ", werks=" + werks + ", lgort=" + lgort + ", matnr=" + matnr + ", charg=" + charg + "]";
    }
}
