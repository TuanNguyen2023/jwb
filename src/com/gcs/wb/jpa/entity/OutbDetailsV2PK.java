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
 * @author Administrator
 */
@Embeddable
public class OutbDetailsV2PK implements Serializable {
    @Basic(optional = false)
    @Column(name = "MANDT")
    private String mandt;
    @Basic(optional = false)
    @Column(name = "DELIV_NUMB")
    private String delivNumb;
    @Basic(optional = false)
    @Column(name = "DELIV_ITEM")
    private String delivItem;

    public OutbDetailsV2PK() {
    }

    public OutbDetailsV2PK(String mandt, String delivNumb, String delivItem) {
        this.mandt = mandt;
        this.delivNumb = delivNumb;
        this.delivItem = delivItem;
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

    public String getDelivItem() {
        return delivItem;
    }

    public void setDelivItem(String delivItem) {
        this.delivItem = delivItem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mandt != null ? mandt.hashCode() : 0);
        hash += (delivNumb != null ? delivNumb.hashCode() : 0);
        hash += (delivItem != null ? delivItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OutbDetailsV2PK)) {
            return false;
        }
        OutbDetailsV2PK other = (OutbDetailsV2PK) object;
        if ((this.mandt == null && other.mandt != null) || (this.mandt != null && !this.mandt.equals(other.mandt))) {
            return false;
        }
        if ((this.delivNumb == null && other.delivNumb != null) || (this.delivNumb != null && !this.delivNumb.equals(other.delivNumb))) {
            return false;
        }
        if ((this.delivItem == null && other.delivItem != null) || (this.delivItem != null && !this.delivItem.equals(other.delivItem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.OutbDetailsV2PK[ mandt=" + mandt + ", delivNumb=" + delivNumb + ", delivItem=" + delivItem + " ]";
    }
    
}
