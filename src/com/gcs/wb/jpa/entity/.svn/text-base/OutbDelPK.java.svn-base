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
public class OutbDelPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "DELIV_NUMB")
    private String delivNumb;

    public OutbDelPK() {
    }

    public OutbDelPK(String mandt, String delivNumb) {
        this.mandt = mandt;
        this.delivNumb = delivNumb;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getDelivNumb() {
        return delivNumb;
    }

    public void setDelivNumb(String delivNumb) {
        this.delivNumb = delivNumb;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (delivNumb != null ? delivNumb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof OutbDelPK)) {
            return false;
        }
        OutbDelPK other = (OutbDelPK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.delivNumb == null && other.delivNumb != null) || (this.delivNumb != null && !this.delivNumb.equals(other.delivNumb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.OutbDelPK[mandt=" + mandt + ", delivNumb=" + delivNumb + "]";
    }
}
