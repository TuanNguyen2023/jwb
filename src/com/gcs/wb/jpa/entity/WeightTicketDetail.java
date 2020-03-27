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
import javax.persistence.Temporal;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_weight_ticket_detail")
@NamedQueries({
    @NamedQuery(name = "WeightTicketDetail.findAll", query = "SELECT wd FROM WeightTicketDetail wd"),
    @NamedQuery(name = "WeightTicketDetail.findByPoNo", query = "SELECT wd FROM WeightTicketDetail wd WHERE wd.ebeln = :poNo AND wd.status = :status"),
    @NamedQuery(name = "WeightTicketDetail.findByDoNo", query = "SELECT wd FROM WeightTicketDetail wd WHERE wd.deliveryOrderNo = :deliveryOrderNo") 
})
public class WeightTicketDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "reg_item_description")
    private String regItemDescription;
    @Column(name = "reg_item_quantity")
    private BigDecimal regItemQuantity;
    @Column(name = "delivery_order_no")
    private String deliveryOrderNo;
    @Column(name = "mat_doc")
    private String matDoc;
    @Column(name = "ebeln")
    private String ebeln;
    @Column(name = "item")
    private String item;
    @Column(name = "matnr_ref")
    private String matnrRef;
    @Column(name = "mvt_ind")
    private Character mvtInd;
    @Column(name = "lichn")
    private String lichn;
    @Column(name = "unit")
    private String unit;
    @Column(name = "doc_year")
    private Integer docYear;
    @Column(name = "recv_matnr")
    private String recvMatnr;
//    @Column(name = "pp_procord")
//    private String ppProcord;
//    @Column(name = "pp_procordcnf")
//    private String ppProcordcnf;
//    @Column(name = "pp_procordcnfcnt")
//    private String ppProcordcnfcnt;
    @Column(name = "kunnr")
    private String kunnr;
    @Column(name = "status")
    private String status;
    @Column(name = "created_time")
    private String createdTime;
    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "MAT_DOC_GR")
    private String matDocGr;
    @Column(name = "MAT_DOC_GI")
    private String matDocGi;
    @Column(name = "so_number")
    private String soNumber;
    @Column(name = "abbr")
    private String abbr;
    @Column(name = "load_vendor")
    private String loadVendor;
    @Column(name = "trans_vendor")
    private String transVendor;
    @Column(name = "ship_to")
    private String shipTo;
    @Column(name = "IvMaterialDocument")
    private String ivMaterialDocument;
    @Column(name = "IvMatDocumentYear")
    private String ivMatDocumentYear;

    @ManyToOne
    @JoinColumn(name = "weight_ticket_id")
    private WeightTicket weightTicket;

    public WeightTicketDetail() {
    }

    public WeightTicketDetail(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegItemDescription() {
        return regItemDescription;
    }

    public void setRegItemDescription(String regItemDescription) {
        this.regItemDescription = regItemDescription;
    }

    public BigDecimal getRegItemQuantity() {
        return regItemQuantity;
    }

    public void setRegItemQuantity(BigDecimal regItemQuantity) {
        this.regItemQuantity = regItemQuantity;
    }

    public String getDeliveryOrderNo() {
        return deliveryOrderNo;
    }

    public void setDeliveryOrderNo(String deliveryOrderNo) {
        this.deliveryOrderNo = deliveryOrderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatDoc() {
        return matDoc;
    }

    public void setMatDoc(String matDoc) {
        this.matDoc = matDoc;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getMatnrRef() {
        return matnrRef;
    }

    public void setMatnrRef(String matnrRef) {
        this.matnrRef = matnrRef;
    }

    public Character getMvtInd() {
        return mvtInd;
    }

    public void setMvtInd(Character mvtInd) {
        this.mvtInd = mvtInd;
    }

    public String getLichn() {
        return lichn;
    }

    public void setLichn(String lichn) {
        this.lichn = lichn;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getDocYear() {
        return docYear;
    }

    public void setDocYear(Integer docYear) {
        this.docYear = docYear;
    }

    public String getRecvMatnr() {
        return recvMatnr;
    }

    public void setRecvMatnr(String recvMatnr) {
        this.recvMatnr = recvMatnr;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isDissolved() {
        return Constants.WeightTicket.STATUS_DISSOLVED.equals(status);
    }

    public void setDissolved(boolean isDissolved) {
        this.status = isDissolved ? Constants.WeightTicket.STATUS_DISSOLVED : null;
    }

    public boolean isPosted() {
        return Constants.WeightTicket.STATUS_POSTED.equals(status);
    }

    public void setPosted(boolean isPosted) {
        this.status = isPosted ? Constants.WeightTicket.STATUS_POSTED : null;
    }

    public WeightTicket getWeightTicket() {
        return weightTicket;
    }

    public void setWeightTicket(WeightTicket weightTicket) {
        this.weightTicket = weightTicket;
    }

    /**
     * @param matDocGr the matDocGr to set
     */
    public void setMatDocGr(String matDocGr) {
        this.matDocGr = matDocGr;
    }

    public String getMatDocGr() {
        return matDocGr;
    }

    /**
     * @param matDocGi the matDocGi to set
     */
    public void setMatDocGi(String matDocGi) {
        this.matDocGi = matDocGi;
    }

    public String getMatDocGi() {
        return matDocGi;
    }

    public String getSoNumber() {
        return soNumber;
    }

    public void setSoNumber(String soNumber) {
        this.soNumber = soNumber;
    }
    
    public String getLoadVendor() {
        return loadVendor;
    }

    public void setLoadVendor(String loadVendor) {
        this.loadVendor = loadVendor;
    }

    public String getTransVendor() {
        return transVendor;
    }

    public void setTransVendor(String transVendor) {
        this.transVendor = transVendor;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }
    
    public String getShipTo() {
        return shipTo;
    }

    public void setShipTo(String shipTo) {
        this.shipTo = shipTo;
    }
    
    public String getIvMaterialDocument() {
        return ivMaterialDocument;
    }

    public void setIvMaterialDocument(String ivMaterialDocument) {
        this.ivMaterialDocument = ivMaterialDocument;
    }

    public String getIvMatDocumentYear() {
        return ivMatDocumentYear;
    }

    public void setIvMatDocumentYear(String ivMatDocumentYear) {
        this.ivMatDocumentYear = ivMatDocumentYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WeightTicketDetail that = (WeightTicketDetail) o;

        if (id != that.id) {
            return false;
        }
        if (regItemDescription != null ? !regItemDescription.equals(that.regItemDescription) : that.regItemDescription != null) {
            return false;
        }
        if (regItemQuantity != null ? !regItemQuantity.equals(that.regItemQuantity) : that.regItemQuantity != null) {
            return false;
        }
        if (deliveryOrderNo != null ? !deliveryOrderNo.equals(that.deliveryOrderNo) : that.deliveryOrderNo != null) {
            return false;
        }
        if (status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }
        if (matDoc != null ? !matDoc.equals(that.matDoc) : that.matDoc != null) {
            return false;
        }
        if (ebeln != null ? !ebeln.equals(that.ebeln) : that.ebeln != null) {
            return false;
        }
        if (item != null ? !item.equals(that.item) : that.item != null) {
            return false;
        }
        if (matnrRef != null ? !matnrRef.equals(that.matnrRef) : that.matnrRef != null) {
            return false;
        }
        if (mvtInd != null ? !mvtInd.equals(that.mvtInd) : that.mvtInd != null) {
            return false;
        }
        if (lichn != null ? !lichn.equals(that.lichn) : that.lichn != null) {
            return false;
        }
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) {
            return false;
        }
        if (docYear != null ? !docYear.equals(that.docYear) : that.docYear != null) {
            return false;
        }
        if (recvMatnr != null ? !recvMatnr.equals(that.recvMatnr) : that.recvMatnr != null) {
            return false;
        }
//        if (ppProcord != null ? !ppProcord.equals(that.ppProcord) : that.ppProcord != null) {
//            return false;
//        }
//        if (ppProcordcnf != null ? !ppProcordcnf.equals(that.ppProcordcnf) : that.ppProcordcnf != null) {
//            return false;
//        }
//        if (ppProcordcnfcnt != null ? !ppProcordcnfcnt.equals(that.ppProcordcnfcnt) : that.ppProcordcnfcnt != null) {
//            return false;
//        }
        if (kunnr != null ? !kunnr.equals(that.kunnr) : that.kunnr != null) {
            return false;
        }
        if (createdTime != null ? !createdTime.equals(that.createdTime) : that.createdTime != null) {
            return false;
        }
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) {
            return false;
        }
        if (updatedDate != null ? !updatedDate.equals(that.updatedDate) : that.updatedDate != null) {
            return false;
        }
        if (abbr != null ? !abbr.equals(that.abbr) : that.abbr != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (regItemDescription != null ? regItemDescription.hashCode() : 0);
        result = 31 * result + (regItemQuantity != null ? regItemQuantity.hashCode() : 0);
        result = 31 * result + (deliveryOrderNo != null ? deliveryOrderNo.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (matDoc != null ? matDoc.hashCode() : 0);
        result = 31 * result + (ebeln != null ? ebeln.hashCode() : 0);
        result = 31 * result + (item != null ? item.hashCode() : 0);
        result = 31 * result + (matnrRef != null ? matnrRef.hashCode() : 0);
        result = 31 * result + (mvtInd != null ? mvtInd.hashCode() : 0);
        result = 31 * result + (lichn != null ? lichn.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (docYear != null ? docYear.hashCode() : 0);
        result = 31 * result + (recvMatnr != null ? recvMatnr.hashCode() : 0);
        //result = 31 * result + (ppProcord != null ? ppProcord.hashCode() : 0);
        //result = 31 * result + (ppProcordcnf != null ? ppProcordcnf.hashCode() : 0);
        //result = 31 * result + (ppProcordcnfcnt != null ? ppProcordcnfcnt.hashCode() : 0);
        result = 31 * result + (kunnr != null ? kunnr.hashCode() : 0);
        result = 31 * result + (createdTime != null ? createdTime.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (abbr != null ? abbr.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.WeightTicket[id=" + id + "]";
    }
}
