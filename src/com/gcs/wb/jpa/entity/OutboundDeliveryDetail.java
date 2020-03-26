/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import com.gcs.wb.base.constant.Constants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author thangtp.nr
 */
@Entity
@Table(name = "tbl_outbound_delivery_detail")
@NamedQueries({
    @NamedQuery(name = "OutboundDeliveryDetail.findByDeliveryOrderNoAndDeliveryOrderItem",
    query = "SELECT o FROM OutboundDeliveryDetail o WHERE"
    + " o.deliveryOrderNo LIKE :deliveryOrderNo"
    + " AND o.deliveryOrderItem = :deliveryOrderItem order by o.freeItem desc"),
    @NamedQuery(name = "OutboundDeliveryDetail.findByDeliveryOrderNo",
    query = "SELECT o FROM OutboundDeliveryDetail o WHERE"
    + " o.deliveryOrderNo LIKE :deliveryOrderNo order by o.freeItem desc"),
    @NamedQuery(name = "OutboundDeliveryDetail.findByWtId",
    query = "SELECT o FROM OutboundDeliveryDetail o WHERE"
    + " o.wtId LIKE :wtId order by o.freeItem desc")
})
public class OutboundDeliveryDetail implements Serializable {

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
    private BigDecimal lfimg;
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
    private BigDecimal inScale;
    @Column(name = "out_scale")
    private BigDecimal outScale;
    @Column(name = "goods_qty")
    private BigDecimal goodsQty;
    @Column(name = "bzirk")
    private String bzirk;
    @Column(name = "bztxt")
    private String bztxt;
    @Column(name = "lfimg_ori")
    private BigDecimal lfimg_ori;
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
    @Column(name = "mandt", unique = true)
    private String mandt;
    @Column(name = "ship_to")
    private String shipTo;
    @Column(name = "vsbed")
    private String vsbed;
    @Column(name = "zkvgr1")
    private String zkvgr1;
    @Column(name = "zkvgr1_text")
    private String zkvgr1Text;
    @Column(name = "zkvgr2")
    private String zkvgr2;
    @Column(name = "zkvgr2_text")
    private String zkvgr2Text;
    @Column(name = "zkvgr3")
    private String zkvgr3;
    @Column(name = "zkvgr3_text")
    private String zkvgr3Text;
    @Column(name = "sort1")
    private String sort1;
    @Column(name = "fscale")
    private BigDecimal fScale;
    @Column(name = "sscale")
    private BigDecimal sScale;
    @Column(name = "poNumber")
    private String poNumber;
    @Column(name = "c_vendor")
    private String cVendor;
    @Column(name = "t_vendor")
    private String tVendor;
    @Column(name = "zsling")
    private Integer zSling;
    @Column(name = "zpallet")
    private Integer zPallet;

    @ManyToOne
    @JoinColumn(name = "outbound_delivery_id")
    private OutboundDelivery outboundDelivery;

    public OutboundDeliveryDetail() {
    }
    
    public OutboundDeliveryDetail(String deliveryOrderNo) {
        this.deliveryOrderNo = deliveryOrderNo;
    }
    
    public OutboundDeliveryDetail(String deliveryOrderNo, String deliveryOrderItem) {
        this.deliveryOrderNo = deliveryOrderNo;
        this.deliveryOrderItem = deliveryOrderItem;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
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

    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }
    
    public boolean isPosted() {
        return Constants.WeightTicket.STATUS_POSTED.equals(status);
    }
    
    public void setPosted(boolean isPosted) {
        this.status = isPosted ? Constants.WeightTicket.STATUS_POSTED : null;
    }

    public String getVsbed() {
        return vsbed;
    }

    public void setVsbed(String vsbed) {
        this.vsbed = vsbed;
    }
    
    public String getZkvgr1() {
        return zkvgr1;
    }

    public void setZkvgr1(String zkvgr1) {
        this.zkvgr1 = zkvgr1;
    }
    
    public String getZkvgr1Text() {
        return zkvgr1Text;
    }

    public void setZkvgr1Text(String zkvgr1Text) {
        this.zkvgr1Text = zkvgr1Text;
    }
    
    public String getZkvgr2() {
        return zkvgr2;
    }

    public void setZkvgr2(String zkvgr2) {
        this.zkvgr2 = zkvgr2;
    }
    
    public String getZkvgr2Text() {
        return zkvgr2Text;
    }

    public void setZkvgr2Text(String zkvgr2Text) {
        this.zkvgr2Text = zkvgr2Text;
    }
    
    public String getZkvgr3() {
        return zkvgr3;
    }

    public void setZkvgr3(String zkvgr3) {
        this.zkvgr3 = zkvgr3;
    }
    
    public String getZkvgr3Text() {
        return zkvgr3Text;
    }

    public void setZkvgr3Text(String zkvgr3Text) {
        this.zkvgr3Text = zkvgr3Text;
    }
    
    public String getSort1() {
        return sort1;
    }

    public void setSort1(String sort1) {
        this.sort1 = sort1;
    }

    public BigDecimal getFscale() {
        return fScale;
    }

    public void setFscale(BigDecimal fScale) {
        this.fScale = fScale;
    }

    public BigDecimal getSscale() {
        return sScale;
    }

    public void setSscale(BigDecimal sScale) {
        this.sScale = sScale;
    }
    
    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }
    
    public String getCVendor() {
        return cVendor;
    }

    public void setCVendor(String cVendor) {
        this.cVendor = cVendor;
    }
    
    public String getTVendor() {
        return tVendor;
    }

    public void setTVendor(String tVendor) {
        this.tVendor = tVendor;
    }
    
    public Integer getZsling() {
        return zSling;
    }

    public void setZsling(Integer zSling) {
        this.zSling = zSling;
    }
    
    public Integer getZPallet() {
        return zPallet;
    }

    public void setZPallet(Integer zPallet) {
        this.zPallet = zPallet;
    }

    public OutboundDelivery getOutboundDelivery() {
        return outboundDelivery;
    }

    public void setOutboundDelivery(OutboundDelivery outboundDelivery) {
        this.outboundDelivery = outboundDelivery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutboundDeliveryDetail that = (OutboundDeliveryDetail) o;

        if (deliveryOrderItem != null ? !deliveryOrderItem.equals(that.deliveryOrderItem) : that.deliveryOrderItem != null) return false;
        if (deliveryOrderNo != null ? !deliveryOrderNo.equals(that.deliveryOrderNo) : that.deliveryOrderNo != null) return false;
        
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
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        
        return result;

    }
}
