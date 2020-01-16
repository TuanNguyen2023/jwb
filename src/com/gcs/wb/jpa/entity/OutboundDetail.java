/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author thangtp.nr
 */
@Entity
@Table(name = "tbl_outbound_detail")
@NamedQueries({
    @NamedQuery(name = "OutboundDetail.findByDeliveryOrderNoAndDeliveryOrderItem",
    query = "SELECT o FROM OutboundDetail o WHERE"
    + " o.deliveryOrderNo LIKE :deliveryOrderNo"
    + " AND o.deliveryOrderItem = :deliveryOrderItem order by o.freeItem desc"),
    @NamedQuery(name = "OutboundDetail.findByDeliveryOrderNo",
    query = "SELECT o FROM OutboundDetail o WHERE"
    + " o.deliveryOrderNo LIKE :deliveryOrderNo order by o.freeItem desc"),
    @NamedQuery(name = "OutboundDetail.findByWtId",
    query = "SELECT o FROM OutboundDetail o WHERE"
    + " o.wtId LIKE :wtId order by o.freeItem desc")
})
public class OutboundDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "delivery_order_no", unique = true)
    private String deliveryOrderNo;
    @Column(name = "delivery_order_item", unique = true)
    private String deliveryOrderItem;
    @Column(name = "matnr")
    private String matnr;
    @Column(name = "arktx")
    private String arktx;
    @Column(name = "lfimg")
    private BigDecimal lfimg;//
    @Column(name = "meins")
    private String meins;
    @Column(name = "vgbel")
    private String vgbel;
    @Column(name = "free_item")
    private Character freeItem;
    @Column(name = "wt_id")
    private String wtId;
    @Column(name = "mat_doc")
    private String matDoc;
    @Column(name = "doc_year")
    private String docYear;
    @Column(name = "in_scale")
    private BigDecimal inScale;//
    @Column(name = "out_scale")
    private BigDecimal outScale;//
    @Column(name = "goods_qty")
    private BigDecimal goodsQty;//
    @Column(name = "bzirk")
    private String bzirk;
    @Column(name = "bztxt")
    private String bztxt;
    @Column(name = "lfimg_ori")
    private BigDecimal lfimg_ori;//
    @Column(name = "s_time")
    private Date sTime;
    @Column(name = "f_time")
    private Date fTime;
    @Column(name = "status")
    private String status;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "deleted_date")
    private Date deletedDate;
    @Column(name = "mandt")
    private String mandt;
    @Column(name = "wplant")
    private String wplant;

    public OutboundDetail() {
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
    
    public String getArktx() {
        return arktx;
    }

    public void setArktx(String arktx) {
        this.arktx = arktx;
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

    public String getDeliveryOrderItem() {
        return deliveryOrderItem;
    }

    public void setDeliveryOrderItem(String deliveryOrderItem) {
        this.deliveryOrderItem = deliveryOrderItem;
    }

    public String getDeliveryOrderNo() {
        return deliveryOrderNo;
    }

    public void setDeliveryOrderNo(String deliveryOrderNo) {
        this.deliveryOrderNo = deliveryOrderNo;
    }

    public String getDocYear() {
        return docYear;
    }

    public void setDocYear(String docYear) {
        this.docYear = docYear;
    }

    public Date getfTime() {
        return fTime;
    }

    public void setfTime(Date fTime) {
        this.fTime = fTime;
    }

    public Character getFreeItem() {
        return freeItem;
    }

    public void setFreeItem(Character freeItem) {
        this.freeItem = freeItem;
    }

    public BigDecimal getGoodsQty() {
        return goodsQty;
    }

    public void setGoodsQty(BigDecimal goodsQty) {
        this.goodsQty = goodsQty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getInScale() {
        return inScale;
    }

    public void setInScale(BigDecimal inScale) {
        this.inScale = inScale;
    }

    public BigDecimal getLfimg() {
        return lfimg;
    }

    public void setLfimg(BigDecimal lfimg) {
        this.lfimg = lfimg;
    }

    public BigDecimal getLfimg_ori() {
        return lfimg_ori;
    }

    public void setLfimg_ori(BigDecimal lfimg_ori) {
        this.lfimg_ori = lfimg_ori;
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

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public BigDecimal getOutScale() {
        return outScale;
    }

    public void setOutScale(BigDecimal outScale) {
        this.outScale = outScale;
    }

    public Date getsTime() {
        return sTime;
    }

    public void setsTime(Date sTime) {
        this.sTime = sTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getWtId() {
        return wtId;
    }

    public void setWtId(String wtId) {
        this.wtId = wtId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutboundDetail that = (OutboundDetail) o;

        if (id != that.id) return false;
        if (deliveryOrderItem != null ? !deliveryOrderItem.equals(that.deliveryOrderItem) : that.deliveryOrderItem != null)
            return false;
        if (matnr != null ? !matnr.equals(that.matnr) : that.matnr != null) return false;
        if (arktx != null ? !arktx.equals(that.arktx) : that.arktx != null) return false;
        if (lfimg != null ? !lfimg.equals(that.lfimg) : that.lfimg != null) return false;
        if (meins != null ? !meins.equals(that.meins) : that.meins != null) return false;
        if (vgbel != null ? !vgbel.equals(that.vgbel) : that.vgbel != null) return false;
        if (freeItem != null ? !freeItem.equals(that.freeItem) : that.freeItem != null) return false;
        if (wtId != null ? !wtId.equals(that.wtId) : that.wtId != null) return false;
        if (matDoc != null ? !matDoc.equals(that.matDoc) : that.matDoc != null) return false;
        if (docYear != null ? !docYear.equals(that.docYear) : that.docYear != null) return false;
        if (inScale != null ? !inScale.equals(that.inScale) : that.inScale != null) return false;
        if (outScale != null ? !outScale.equals(that.outScale) : that.outScale != null) return false;
        if (goodsQty != null ? !goodsQty.equals(that.goodsQty) : that.goodsQty != null) return false;
        if (bzirk != null ? !bzirk.equals(that.bzirk) : that.bzirk != null) return false;
        if (bztxt != null ? !bztxt.equals(that.bztxt) : that.bztxt != null) return false;
        if (lfimg != null ? !lfimg.equals(that.lfimg) : that.lfimg != null) return false;
        if (sTime != null ? !sTime.equals(that.sTime) : that.sTime != null) return false;
        if (fTime != null ? !fTime.equals(that.fTime) : that.fTime != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;
        if (updatedDate != null ? !updatedDate.equals(that.updatedDate) : that.updatedDate != null) return false;
        if (deletedDate != null ? !deletedDate.equals(that.deletedDate) : that.deletedDate != null) return false;
        if (mandt != null ? !mandt.equals(that.mandt) : that.mandt != null) return false;
        if (wplant != null ? !wplant.equals(that.wplant) : that.wplant != null) return false;

        return true;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (deliveryOrderItem != null ? deliveryOrderItem.hashCode() : 0);
        result = 31 * result + (matnr != null ? matnr.hashCode() : 0);
        result = 31 * result + (arktx != null ? arktx.hashCode() : 0);
        result = 31 * result + (lfimg != null ? lfimg.hashCode() : 0);
        result = 31 * result + (meins != null ? meins.hashCode() : 0);
        result = 31 * result + (vgbel != null ? vgbel.hashCode() : 0);
        result = 31 * result + (freeItem != null ? freeItem.hashCode() : 0);
        result = 31 * result + (wtId != null ? wtId.hashCode() : 0);
        result = 31 * result + (matDoc != null ? matDoc.hashCode() : 0);
        result = 31 * result + (docYear != null ? docYear.hashCode() : 0);
        result = 31 * result + (inScale != null ? inScale.hashCode() : 0);
        result = 31 * result + (outScale != null ? outScale.hashCode() : 0);
        result = 31 * result + (goodsQty != null ? goodsQty.hashCode() : 0);
        result = 31 * result + (bzirk != null ? bzirk.hashCode() : 0);
        result = 31 * result + (bztxt != null ? bztxt.hashCode() : 0);
        result = 31 * result + (lfimg != null ? lfimg.hashCode() : 0);
        result = 31 * result + (sTime != null ? sTime.hashCode() : 0);
        result = 31 * result + (fTime != null ? fTime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (deletedDate != null ? deletedDate.hashCode() : 0);
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (wplant != null ? wplant.hashCode() : 0);
        
        return result;

    }
}
