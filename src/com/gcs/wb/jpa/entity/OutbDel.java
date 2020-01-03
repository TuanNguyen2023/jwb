/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author vunguyent
 */
@Entity
@Table(name = "OutbDel")
@NamedQueries({
    @NamedQuery(name = "OutbDel.findByMandtOutDel", query = "SELECT o FROM OutbDel o WHERE o.outbDelPK.mandt = :mandt AND o.outbDelPK.delivNumb LIKE :delivNumb")
})
public class OutbDel implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OutbDelPK outbDelPK;
    @Basic(optional = false)
    @Column(name = "DELIV_ITEM")
    private String delivItem;
    @Column(name = "DELIV_ITEM_FREE")
    private String delivItemFree;
    @Column(name = "MATNR")
    private String matnr;
    @Column(name = "MATNR_FREE")
    private String matnrFree;
    @Column(name = "ARKTX")
    private String arktx;
    @Column(name = "ERDAT")
    @Temporal(TemporalType.DATE)
    private Date erdat;
    @Column(name = "LFART")
    private String lfart;
    @Column(name = "LDDAT")
    @Temporal(TemporalType.DATE)
    private Date lddat;
    @Column(name = "WADAT")
    @Temporal(TemporalType.DATE)
    private Date wadat;
    @Column(name = "KODAT")
    @Temporal(TemporalType.DATE)
    private Date kodat;
    @Column(name = "SHIP_POINT")
    private String shipPoint;
    @Column(name = "LIFNR")
    private String lifnr;
    @Column(name = "KUNNR")
    private String kunnr;
    @Column(name = "KUNAG")
    private String kunag;
    @Column(name = "TRATY")
    private String traty;
    @Column(name = "TRAID")
    private String traid;
    @Column(name = "BLDAT")
    @Temporal(TemporalType.DATE)
    private Date bldat;
    @Column(name = "WERKS")
    private String werks;
    @Column(name = "RECV_PLANT")
    private String recvPlant;
    @Column(name = "LGORT")
    private String lgort;
    @Column(name = "CHARG")
    private String charg;
    @Column(name = "LICHN")
    private String lichn;
    @Column(name = "LFIMG")
    private BigDecimal lfimg;
    @Column(name = "FREE_QTY")
    private BigDecimal freeQty;
    @Column(name = "MEINS")
    private String meins;
    @Column(name = "VRKME")
    private String vrkme;
    @Column(name = "UNTTO")
    private BigDecimal untto;
    @Column(name = "UEBTO")
    private BigDecimal uebto;
    @Column(name = "UEBTK")
    private Character uebtk;
    @Column(name = "VGBEL")
    private String vgbel;
    @Column(name = "VGPOS")
    private String vgpos;
    @Column(name = "BWTAR")
    private String bwtar;
    @Column(name = "BWART")
    private String bwart;
    @Column(name = "KOSTK")
    private Character kostk;
    @Column(name = "KOQUK")
    private Character koquk;
    @Column(name = "WBSTK")
    private Character wbstk;
    @Column(name = "POSTED")
    private String posted;
    @Column(name = "MAT_DOC")
    private String matDoc;

    public OutbDel() {
    }

    public OutbDel(OutbDelPK outbDelPK) {
        this.outbDelPK = outbDelPK;
    }

    public OutbDel(OutbDelPK outbDelPK, String delivItem) {
        this.outbDelPK = outbDelPK;
        this.delivItem = delivItem;
    }

    public OutbDel(String mandt, String delivNumb) {
        this.outbDelPK = new OutbDelPK(mandt, delivNumb);
    }

    public OutbDelPK getOutbDelPK() {
        return outbDelPK;
    }

    public void setOutbDelPK(OutbDelPK outbDelPK) {
        this.outbDelPK = outbDelPK;
    }

    public String getDelivItem() {
        return delivItem;
    }

    public void setDelivItem(String delivItem) {
        this.delivItem = delivItem;
    }

    public String getDelivItemFree() {
        return delivItemFree;
    }

    public void setDelivItemFree(String delivItemFree) {
        this.delivItemFree = delivItemFree;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getMatnrFree() {
        return matnrFree;
    }

    public void setMatnrFree(String matnrFree) {
        this.matnrFree = matnrFree;
    }

    public String getArktx() {
        return arktx;
    }

    public void setArktx(String arktx) {
        this.arktx = arktx;
    }

    public Date getErdat() {
        return erdat;
    }

    public void setErdat(Date erdat) {
        this.erdat = erdat;
    }

    public String getLfart() {
        return lfart;
    }

    public void setLfart(String lfart) {
        this.lfart = lfart;
    }

    public Date getLddat() {
        return lddat;
    }

    public void setLddat(Date lddat) {
        this.lddat = lddat;
    }

    public Date getWadat() {
        return wadat;
    }

    public void setWadat(Date wadat) {
        this.wadat = wadat;
    }

    public Date getKodat() {
        return kodat;
    }

    public void setKodat(Date kodat) {
        this.kodat = kodat;
    }

    public String getShipPoint() {
        return shipPoint;
    }

    public void setShipPoint(String shipPoint) {
        this.shipPoint = shipPoint;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getKunag() {
        return kunag;
    }

    public void setKunag(String kunag) {
        this.kunag = kunag;
    }

    public String getTraty() {
        return traty;
    }

    public void setTraty(String traty) {
        this.traty = traty;
    }

    public String getTraid() {
        return traid;
    }

    public void setTraid(String traid) {
        this.traid = traid;
    }

    public Date getBldat() {
        return bldat;
    }

    public void setBldat(Date bldat) {
        this.bldat = bldat;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getRecvPlant() {
        return recvPlant;
    }

    public void setRecvPlant(String recvPlant) {
        this.recvPlant = recvPlant;
    }

    public String getLgort() {
        return lgort;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort;
    }

    public String getCharg() {
        return charg;
    }

    public void setCharg(String charg) {
        this.charg = charg;
    }

    public String getLichn() {
        return lichn;
    }

    public void setLichn(String lichn) {
        this.lichn = lichn;
    }

    public BigDecimal getLfimg() {
        return lfimg;
    }

    public void setLfimg(BigDecimal lfimg) {
        this.lfimg = lfimg;
    }

    public BigDecimal getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(BigDecimal freeQty) {
        this.freeQty = freeQty;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public String getVrkme() {
        return vrkme;
    }

    public void setVrkme(String vrkme) {
        this.vrkme = vrkme;
    }

    public BigDecimal getUntto() {
        return untto;
    }

    public void setUntto(BigDecimal untto) {
        this.untto = untto;
    }

    public BigDecimal getUebto() {
        return uebto;
    }

    public void setUebto(BigDecimal uebto) {
        this.uebto = uebto;
    }

    public Character getUebtk() {
        return uebtk;
    }

    public void setUebtk(Character uebtk) {
        this.uebtk = uebtk;
    }

    public String getVgbel() {
        return vgbel;
    }

    public void setVgbel(String vgbel) {
        this.vgbel = vgbel;
    }

    public String getVgpos() {
        return vgpos;
    }

    public void setVgpos(String vgpos) {
        this.vgpos = vgpos;
    }

    public String getBwtar() {
        return bwtar;
    }

    public void setBwtar(String bwtar) {
        this.bwtar = bwtar;
    }

    public String getBwart() {
        return bwart;
    }

    public void setBwart(String bwart) {
        this.bwart = bwart;
    }

    public Character getKostk() {
        return kostk;
    }

    public void setKostk(Character kostk) {
        this.kostk = kostk;
    }

    public Character getKoquk() {
        return koquk;
    }

    public void setKoquk(Character koquk) {
        this.koquk = koquk;
    }

    public Character getWbstk() {
        return wbstk;
    }

    public void setWbstk(Character wbstk) {
        this.wbstk = wbstk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outbDelPK != null ? outbDelPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof OutbDel)) {
            return false;
        }
        OutbDel other = (OutbDel) object;
        if ((this.outbDelPK == null && other.outbDelPK != null) || (this.outbDelPK != null && !this.outbDelPK.equals(other.outbDelPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.OutbDel[outbDelPK=" + outbDelPK + "]";
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
}
