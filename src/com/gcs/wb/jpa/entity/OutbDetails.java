/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SAP
 */
@Entity
@Table(name = "outb_details")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OutbDetails.findAll", query = "SELECT o FROM OutbDetails o"),
    @NamedQuery(name = "OutbDetails.findByMandt", query = "SELECT o FROM OutbDetails o WHERE o.outbDetailsPK.mandt = :mandt"),
    @NamedQuery(name = "OutbDetails.findById", query = "SELECT o FROM OutbDetails o WHERE o.outbDetailsPK.id = :id"),
    @NamedQuery(name = "OutbDetails.findByDelivNumb", query = "SELECT o FROM OutbDetails o WHERE o.delivNumb = :delivNumb"),
    @NamedQuery(name = "OutbDetails.findByDelivItem", query = "SELECT o FROM OutbDetails o WHERE o.delivItem = :delivItem"),
    @NamedQuery(name = "OutbDetails.findByMatnr", query = "SELECT o FROM OutbDetails o WHERE o.matnr = :matnr"),
    @NamedQuery(name = "OutbDetails.findByArktx", query = "SELECT o FROM OutbDetails o WHERE o.arktx = :arktx"),
    @NamedQuery(name = "OutbDetails.findByLfimg", query = "SELECT o FROM OutbDetails o WHERE o.lfimg = :lfimg"),
    @NamedQuery(name = "OutbDetails.findByMeins", query = "SELECT o FROM OutbDetails o WHERE o.meins = :meins"),
    @NamedQuery(name = "OutbDetails.findByVgbel", query = "SELECT o FROM OutbDetails o WHERE o.vgbel = :vgbel"),
    @NamedQuery(name = "OutbDetails.findByFreeItem", query = "SELECT o FROM OutbDetails o WHERE o.freeItem = :freeItem"),
    @NamedQuery(name = "OutbDetails.findByMandtDelivNumb", 
    query = "SELECT o FROM OutbDetails o WHERE o.outbDetailsPK.mandt = :mandt"+
            " AND o.delivNumb LIKE :delivNumb order by o.freeItem desc"),
    @NamedQuery(name = "OutbDetails.findByMandtWTID", 
    query = "SELECT o FROM OutbDetails o WHERE o.outbDetailsPK.mandt = :mandt"+
            " AND o.wtID LIKE :wtID order by o.freeItem desc")
})
public class OutbDetails implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OutbDetailsPK outbDetailsPK;
    @Basic(optional = false)
    @Column(name = "DELIV_NUMB")
    private String delivNumb;
    @Basic(optional = false)
    @Column(name = "DELIV_ITEM")
    private String delivItem;
    @Column(name = "MATNR")
    private String matnr;
    @Column(name = "ARKTX")
    private String arktx;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "LFIMG")
    private BigDecimal lfimg;
    @Column(name = "MEINS")
    private String meins;
    @Column(name = "VGBEL")
    private String vgbel;
    @Column(name = "FREE_ITEM")
    private String freeItem;
    @Column(name = "WT_ID")
    private String wtID;
    @Column(name = "POSTED")
    private String posted;
    @Column(name = "MAT_DOC")
    private String matDoc;
    @Column(name = "DOC_YEAR")
    private String docYear;
    @Column(name = "IN_SCALE")
    private BigDecimal inScale;
    @Column(name = "OUT_SCALE")
    private BigDecimal outScale;
    @Column(name = "GOODS_QTY")
    private BigDecimal goodsQty;
    @Column(name = "BZIRK")
    private String bzirk;
    @Column(name = "BZTXT")
    private String bztxt;

    public OutbDetails() {
    }

    public OutbDetails(OutbDetailsPK outbDetailsPK) {
        this.outbDetailsPK = outbDetailsPK;
    }

    public OutbDetails(OutbDetailsPK outbDetailsPK, String delivNumb, String delivItem) {
        this.outbDetailsPK = outbDetailsPK;
        this.delivNumb = delivNumb;
        this.delivItem = delivItem;
    }

    public OutbDetails(String mandt, int id) {
        this.outbDetailsPK = new OutbDetailsPK(mandt, id);
    }

    public OutbDetailsPK getOutbDetailsPK() {
        return outbDetailsPK;
    }

    public void setOutbDetailsPK(OutbDetailsPK outbDetailsPK) {
        this.outbDetailsPK = outbDetailsPK;
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

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getArktx() {
        return arktx;
    }

    public void setArktx(String arktx) {
        this.arktx = arktx;
    }

    public BigDecimal getLfimg() {
        return lfimg;
    }

    public void setLfimg(BigDecimal lfimg) {
        this.lfimg = lfimg;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public String getVgbel() {
        return vgbel;
    }

    public void setVgbel(String vgbel) {
        this.vgbel = vgbel;
    }

    public String getFreeItem() {
        return freeItem;
    }

    public void setFreeItem(String freeItem) {
        this.freeItem = freeItem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outbDetailsPK != null ? outbDetailsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OutbDetails)) {
            return false;
        }
        OutbDetails other = (OutbDetails) object;
        if ((this.outbDetailsPK == null && other.outbDetailsPK != null) || (this.outbDetailsPK != null && !this.outbDetailsPK.equals(other.outbDetailsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.OutbDetails[ outbDetailsPK=" + outbDetailsPK + " ]";
    }

    /**
     * @return the wtID
     */
    public String getWtID() {
        return wtID;
    }

    /**
     * @param wtID the wtID to set
     */
    public void setWtID(String wtID) {
        this.wtID = wtID;
    }

    /**
     * @return the posted
     */
    public String getPosted() {
        return posted;
    }

    /**
     * @param posted the posted to set
     */
    public void setPosted(String posted) {
        this.posted = posted;
    }

    /**
     * @return the matDoc
     */
    public String getMatDoc() {
        return matDoc;
    }

    /**
     * @param matDoc the matDoc to set
     */
    public void setMatDoc(String matDoc) {
        this.matDoc = matDoc;
    }

    /**
     * @return the docYear
     */
    public String getDocYear() {
        return docYear;
    }

    /**
     * @param docYear the docYear to set
     */
    public void setDocYear(String docYear) {
        this.docYear = docYear;
    }

    /**
     * @return the inScale
     */
    public BigDecimal getInScale() {
        return inScale;
    }

    /**
     * @param inScale the inScale to set
     */
    public void setInScale(BigDecimal inScale) {
        this.inScale = inScale;
    }

    /**
     * @return the outScale
     */
    public BigDecimal getOutScale() {
        return outScale;
    }

    /**
     * @param outScale the outScale to set
     */
    public void setOutScale(BigDecimal outScale) {
        this.outScale = outScale;
    }

    /**
     * @return the goodsQty
     */
    public BigDecimal getGoodsQty() {
        return goodsQty;
    }

    /**
     * @param goodsQty the goodsQty to set
     */
    public void setGoodsQty(BigDecimal goodsQty) {
        this.goodsQty = goodsQty;
    }

    /**
     * @return the bzirk
     */
    public String getBzirk() {
        return bzirk;
    }

    /**
     * @param bzirk the bzirk to set
     */
    public void setBzirk(String bzirk) {
        this.bzirk = bzirk;
    }

    /**
     * @return the bztxt
     */
    public String getBztxt() {
        return bztxt;
    }

    /**
     * @param bztxt the bztxt to set
     */
    public void setBztxt(String bztxt) {
        this.bztxt = bztxt;
    }
    
}
