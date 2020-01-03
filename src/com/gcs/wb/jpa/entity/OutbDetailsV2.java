/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "outb_details_v2")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OutbDetailsV2.findByMandtDelivNumbItem",
    query = "SELECT o FROM OutbDetailsV2 o WHERE o.outbDetailsV2PK.mandt = :mandt"
    + " AND o.outbDetailsV2PK.delivNumb LIKE :delivNumb"
    + " AND o.outbDetailsV2PK.delivItem = :delivItem order by o.freeItem desc"),
    @NamedQuery(name = "OutbDetailsV2.findByMandtDelivNumb",
    query = "SELECT o FROM OutbDetailsV2 o WHERE o.outbDetailsV2PK.mandt = :mandt"
    + " AND o.outbDetailsV2PK.delivNumb LIKE :delivNumb order by o.freeItem desc"),
    @NamedQuery(name = "OutbDetailsV2.findByMandtWTID",
    query = "SELECT o FROM OutbDetailsV2 o WHERE o.outbDetailsV2PK.mandt = :mandt"
    + " AND o.wtId LIKE :wtId order by o.freeItem desc")
})
public class OutbDetailsV2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OutbDetailsV2PK outbDetailsV2PK;
    @Column(name = "MATNR")
    private String matnr;
    @Column(name = "ARKTX")
    private String arktx;
    @Column(name = "LFIMG")
    private BigDecimal lfimg;
    @Column(name = "LFIMG_ORI")
    private BigDecimal lfimgOri;
    @Column(name = "MEINS")
    private String meins;
    @Column(name = "VGBEL")
    private String vgbel;
    @Column(name = "FREE_ITEM")
    private String freeItem;
    @Column(name = "WT_ID")
    private String wtId;
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

    public OutbDetailsV2() {
    }

    public OutbDetailsV2(OutbDetailsV2PK outbDetailsV2PK) {
        this.outbDetailsV2PK = outbDetailsV2PK;
    }

    public OutbDetailsV2(String mandt, String delivNumb, String delivItem) {
        this.outbDetailsV2PK = new OutbDetailsV2PK(mandt, delivNumb, delivItem);
    }

    public OutbDetailsV2PK getOutbDetailsV2PK() {
        return outbDetailsV2PK;
    }

    public void setOutbDetailsV2PK(OutbDetailsV2PK outbDetailsV2PK) {
        this.outbDetailsV2PK = outbDetailsV2PK;
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

    public String getWtId() {
        return wtId;
    }

    public void setWtId(String wtId) {
        this.wtId = wtId;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getMatDoc() {
        return matDoc;
    }

    public void setMatDoc(String matDoc) {
        this.matDoc = matDoc;
    }

    public String getDocYear() {
        return docYear;
    }

    public void setDocYear(String docYear) {
        this.docYear = docYear;
    }

    public BigDecimal getInScale() {
        return inScale;
    }

    public void setInScale(BigDecimal inScale) {
        this.inScale = inScale;
    }

    public BigDecimal getOutScale() {
        return outScale;
    }

    public void setOutScale(BigDecimal outScale) {
        this.outScale = outScale;
    }

    public BigDecimal getGoodsQty() {
        return goodsQty;
    }

    public void setGoodsQty(BigDecimal goodsQty) {
        this.goodsQty = goodsQty;
    }

    public String getBzirk() {
        return bzirk;
    }

    public void setBzirk(String bzirk) {
        this.bzirk = bzirk;
    }

    public String getBztxt() {
        return bztxt;
    }

    public void setBztxt(String bztxt) {
        this.bztxt = bztxt;
    }

    public BigDecimal getLfimgOri() {
        return lfimgOri;
    }

    public void setLfimgOri(BigDecimal lfimgOri) {
        this.lfimgOri = lfimgOri;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outbDetailsV2PK != null ? outbDetailsV2PK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OutbDetailsV2)) {
            return false;
        }
        OutbDetailsV2 other = (OutbDetailsV2) object;
        if ((this.outbDetailsV2PK == null && other.outbDetailsV2PK != null) || (this.outbDetailsV2PK != null && !this.outbDetailsV2PK.equals(other.outbDetailsV2PK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.OutbDetailsV2[ outbDetailsV2PK=" + outbDetailsV2PK + " ]";
    }
}
