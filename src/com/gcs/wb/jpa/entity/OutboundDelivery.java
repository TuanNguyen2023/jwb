/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import com.gcs.wb.base.constant.Constants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_outbound_delivery")
@NamedQueries({
    @NamedQuery(name = "OutboundDelivery.findByDeliveryOrderNo", query = "SELECT o FROM OutboundDelivery o WHERE o.deliveryOrderNo LIKE :deliveryOrderNo")
})
public class OutboundDelivery implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "delivery_order_no", unique = true)
    private String deliveryOrderNo;
    @Column(name = "mandt")
    private String mandt;
    @Column(name = "wplant")
    private String wplant;
    @Column(name = "delivery_item")
    private String deliveryItem;
    @Column(name = "delivery_item_free")
    private String deliveryItemFree;
    @Column(name = "matnr")
    private String matnr;
    @Column(name = "arktx")
    private String arktx;
    @Column(name = "erdat")
    private Date erdat;
    @Column(name = "lfart")
    private String lfart;
    @Column(name = "lddat")
    private Date lddat;
    @Column(name = "wadat")
    private Date wadat;
    @Column(name = "kodat")
    private Date kodat;
    @Column(name = "ship_point")
    private String shipPoint;
    @Column(name = "lifnr")
    private String lifnr;
    @Column(name = "kunnr")
    private String kunnr;
    @Column(name = "kunag")
    private String kunag;
    @Column(name = "traty")
    private String traty;
    @Column(name = "traid")
    private String traid;
    @Column(name = "bldat")
    private Date bldat;
    @Column(name = "werks")
    private String werks;
    @Column(name = "recv_plant")
    private String recvPlant;
    @Column(name = "lgort")
    private String lgort;
    @Column(name = "charg")
    private String charg;
    @Column(name = "lichn")
    private String lichn;
    @Column(name = "lfimg")
    private BigDecimal lfimg;
    @Column(name = "free_qty")
    private BigDecimal freeQty;
    @Column(name = "meins")
    private String meins;
    @Column(name = "vrkme")
    private String vrkme;
    @Column(name = "untto")
    private BigDecimal untto;
    @Column(name = "uebto")
    private BigDecimal uebto;
    @Column(name = "uebtk")
    private Character uebtk;
    @Column(name = "vgbel")
    private String vgbel;
    @Column(name = "vgpos")
    private String vgpos;
    @Column(name = "bwtar")
    private String bwtar;
    @Column(name = "bwart")
    private String bwart;
    @Column(name = "kostk")
    private Character kostk;
    @Column(name = "koquk")
    private Character koquk;
    @Column(name = "wbstk")
    private Character wbstk;
    @Column(name = "mat_doc")
    private String matDoc;
    @Column(name = "matnr_free")
    private String matnrFree;
    @Column(name = "status")
    private String status;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "deleted_date")
    private Date deletedDate;

    public OutboundDelivery() {
    }

    public OutboundDelivery(String deliveryOrderNo) {
        this.deliveryOrderNo = deliveryOrderNo;
    }

    public OutboundDelivery(String deliveryOrderNo, String deliveryItem) {
        this.deliveryOrderNo = deliveryOrderNo;
        this.deliveryItem = deliveryItem;
    }

    public String getArktx() {
        return arktx;
    }

    public void setArktx(String arktx) {
        this.arktx = arktx;
    }

    public Date getBldat() {
        return bldat;
    }

    public void setBldat(Date bldat) {
        this.bldat = bldat;
    }

    public String getBwart() {
        return bwart;
    }

    public void setBwart(String bwart) {
        this.bwart = bwart;
    }

    public String getBwtar() {
        return bwtar;
    }

    public void setBwtar(String bwtar) {
        this.bwtar = bwtar;
    }

    public String getCharg() {
        return charg;
    }

    public void setCharg(String charg) {
        this.charg = charg;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeliveryItem() {
        return deliveryItem;
    }

    public void setDeliveryItem(String deliveryItem) {
        this.deliveryItem = deliveryItem;
    }

    public String getDeliveryItemFree() {
        return deliveryItemFree;
    }

    public void setDeliveryItemFree(String deliveryItemFree) {
        this.deliveryItemFree = deliveryItemFree;
    }

    public String getDeliveryOrderNo() {
        return deliveryOrderNo;
    }

    public void setDeliveryOrderNo(String deliveryOrderNo) {
        this.deliveryOrderNo = deliveryOrderNo;
    }

    public Date getErdat() {
        return erdat;
    }

    public void setErdat(Date erdat) {
        this.erdat = erdat;
    }

    public BigDecimal getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(BigDecimal freeQty) {
        this.freeQty = freeQty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getKodat() {
        return kodat;
    }

    public void setKodat(Date kodat) {
        this.kodat = kodat;
    }

    public Character getKoquk() {
        return koquk;
    }

    public void setKoquk(Character koquk) {
        this.koquk = koquk;
    }

    public Character getKostk() {
        return kostk;
    }

    public void setKostk(Character kostk) {
        this.kostk = kostk;
    }

    public String getKunag() {
        return kunag;
    }

    public void setKunag(String kunag) {
        this.kunag = kunag;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public Date getLddat() {
        return lddat;
    }

    public void setLddat(Date lddat) {
        this.lddat = lddat;
    }

    public String getLfart() {
        return lfart;
    }

    public void setLfart(String lfart) {
        this.lfart = lfart;
    }

    public BigDecimal getLfimg() {
        return lfimg;
    }

    public void setLfimg(BigDecimal lfimg) {
        this.lfimg = lfimg;
    }

    public String getLgort() {
        return lgort;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort;
    }

    public String getLichn() {
        return lichn;
    }

    public void setLichn(String lichn) {
        this.lichn = lichn;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getMatDoc() {
        return matDoc;
    }

    public void setMatDoc(String matDoc) {
        this.matDoc = matDoc;
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

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public String getRecvPlant() {
        return recvPlant;
    }

    public void setRecvPlant(String recvPlant) {
        this.recvPlant = recvPlant;
    }

    public String getShipPoint() {
        return shipPoint;
    }

    public void setShipPoint(String shipPoint) {
        this.shipPoint = shipPoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTraid() {
        return traid;
    }

    public void setTraid(String traid) {
        this.traid = traid;
    }

    public String getTraty() {
        return traty;
    }

    public void setTraty(String traty) {
        this.traty = traty;
    }

    public Character getUebtk() {
        return uebtk;
    }

    public void setUebtk(Character uebtk) {
        this.uebtk = uebtk;
    }

    public BigDecimal getUebto() {
        return uebto;
    }

    public void setUebto(BigDecimal uebto) {
        this.uebto = uebto;
    }

    public BigDecimal getUntto() {
        return untto;
    }

    public void setUntto(BigDecimal untto) {
        this.untto = untto;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
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

    public String getVrkme() {
        return vrkme;
    }

    public void setVrkme(String vrkme) {
        this.vrkme = vrkme;
    }

    public Date getWadat() {
        return wadat;
    }

    public void setWadat(Date wadat) {
        this.wadat = wadat;
    }

    public Character getWbstk() {
        return wbstk;
    }

    public void setWbstk(Character wbstk) {
        this.wbstk = wbstk;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getWplant() {
        return wplant;
    }

    public void setWplant(String wplant) {
        this.wplant = wplant;
    }
    
    public boolean isPosted() {
        return Constants.WeightTicket.STATUS_POSTED.equals(status);
    }
    
    public void setPosted(boolean isPosted) {
        this.status = isPosted ? Constants.WeightTicket.STATUS_POSTED : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutboundDelivery that = (OutboundDelivery) o;

        if (deliveryOrderNo != null ? !deliveryOrderNo.equals(that.deliveryOrderNo) : that.deliveryOrderNo != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (deliveryOrderNo != null ? deliveryOrderNo.hashCode() : 0);
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (wplant != null ? wplant.hashCode() : 0);
        result = 31 * result + (deliveryItem != null ? deliveryItem.hashCode() : 0);
        result = 31 * result + (deliveryItemFree != null ? deliveryItemFree.hashCode() : 0);
        result = 31 * result + (matnr != null ? matnr.hashCode() : 0);
        result = 31 * result + (arktx != null ? arktx.hashCode() : 0);
        result = 31 * result + (erdat != null ? erdat.hashCode() : 0);
        result = 31 * result + (lfart != null ? lfart.hashCode() : 0);
        result = 31 * result + (lddat != null ? lddat.hashCode() : 0);
        result = 31 * result + (wadat != null ? wadat.hashCode() : 0);
        result = 31 * result + (kodat != null ? kodat.hashCode() : 0);
        result = 31 * result + (shipPoint != null ? shipPoint.hashCode() : 0);
        result = 31 * result + (lifnr != null ? lifnr.hashCode() : 0);
        result = 31 * result + (kunnr != null ? kunnr.hashCode() : 0);
        result = 31 * result + (kunag != null ? kunag.hashCode() : 0);
        result = 31 * result + (traty != null ? traty.hashCode() : 0);
        result = 31 * result + (traid != null ? traid.hashCode() : 0);
        result = 31 * result + (bldat != null ? bldat.hashCode() : 0);
        result = 31 * result + (werks != null ? werks.hashCode() : 0);
        result = 31 * result + (recvPlant != null ? recvPlant.hashCode() : 0);
        result = 31 * result + (lgort != null ? lgort.hashCode() : 0);
        result = 31 * result + (charg != null ? charg.hashCode() : 0);
        result = 31 * result + (lichn != null ? lichn.hashCode() : 0);
        result = 31 * result + (lfimg != null ? lfimg.hashCode() : 0);
        result = 31 * result + (freeQty != null ? freeQty.hashCode() : 0);
        result = 31 * result + (meins != null ? meins.hashCode() : 0);
        result = 31 * result + (vrkme != null ? vrkme.hashCode() : 0);
        result = 31 * result + (untto != null ? untto.hashCode() : 0);
        result = 31 * result + (uebto != null ? uebto.hashCode() : 0);
        result = 31 * result + (uebtk != null ? uebtk.hashCode() : 0);
        result = 31 * result + (vgbel != null ? vgbel.hashCode() : 0);
        result = 31 * result + (vgpos != null ? vgpos.hashCode() : 0);
        result = 31 * result + (bwtar != null ? bwtar.hashCode() : 0);
        result = 31 * result + (bwart != null ? bwart.hashCode() : 0);
        result = 31 * result + (kostk != null ? kostk.hashCode() : 0);
        result = 31 * result + (koquk != null ? koquk.hashCode() : 0);
        result = 31 * result + (wbstk != null ? wbstk.hashCode() : 0);
        result = 31 * result + (matDoc != null ? matDoc.hashCode() : 0);
        result = 31 * result + (matnrFree != null ? matnrFree.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (deletedDate != null ? deletedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.OutbDel[id=" + id + "]";
    }
}
