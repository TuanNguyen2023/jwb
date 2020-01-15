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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_weight_ticket")
@NamedQueries({
    @NamedQuery(name = "WeightTicket.findAll", query = "SELECT w FROM WeightTicket w"),
    @NamedQuery(name = "WeightTicket.findByCreatedDateRange", query = "SELECT w FROM WeightTicket w WHERE w.createdDate BETWEEN :from AND :to"),
    @NamedQuery(name = "WeightTicket.findByDeliveryOrderNo", query = "SELECT w FROM WeightTicket w WHERE w.deliveryOrderNo = :deliveryOrderNo"),
    @NamedQuery(name = "WeightTicket.findBySoNiemXa", query = "SELECT w FROM WeightTicket w WHERE w.soNiemXa = :soNiemXa"),
    @NamedQuery(name = "WeightTicket.findByIdPlateNoMatnrRegType",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.id LIKE :id "
    + "  AND w.plateNo IN ( SELECT tv.vehicle.plateNo FROM TransportAgentVehicle tv WHERE tv.transportAgent.abbr = :taAbbr ) "
    + "  AND w.matnrRef = :matnrRef "
    + "  AND w.regType IN :regType"),
    @NamedQuery(name = "WeightTicket.findByIdPlateNoRegType",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.id LIKE :id "
    + "  AND w.plateNo IN ( SELECT tv.vehicle.plateNo FROM TransportAgentVehicle tv WHERE tv.transportAgent.abbr = :taAbbr ) "
    + "  AND w.matnrRef IS NULL "
    + "  AND w.regType IN :regType"),
    @NamedQuery(name = "WeightTicket.findByIdPlateNoMatnrRegTypeDissovled",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.id LIKE :id "
    + "  AND w.plateNo IN ( SELECT tv.vehicle.plateNo FROM TransportAgentVehicle tv WHERE tv.transportAgent.abbr = :taAbbr ) "
    + "  AND w.matnrRef = :matnrRef "
    + "  AND w.regType IN :regType "
    + "  AND w.status = '" + Constants.WeightTicket.STATUS_DISSOLVED + "'"),
    @NamedQuery(name = "WeightTicket.findByIdPlateNoRegTypeDissovled",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.id LIKE :id "
    + "  AND w.plateNo IN ( SELECT tv.vehicle.plateNo FROM TransportAgentVehicle tv WHERE tv.transportAgent.abbr = :taAbbr ) "
    + "  AND w.matnrRef IS NULL "
    + "  AND w.regType IN :regType "
    + "  AND w.status = '" + Constants.WeightTicket.STATUS_DISSOLVED + "'"),
    @NamedQuery(name = "WeightTicket.findByIdPlateNoMatnrRegTypePosted",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.id LIKE :id "
    + "  AND w.plateNo IN ( SELECT tv.vehicle.plateNo FROM TransportAgentVehicle tv WHERE tv.transportAgent.abbr = :taAbbr ) "
    + "  AND w.matnrRef = :matnrRef "
    + "  AND w.regType IN :regType "
    + "  AND w.status = '" + Constants.WeightTicket.STATUS_POSTED + "'"),
    @NamedQuery(name = "WeightTicket.findByIdPlateNoRegTypePosted",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.id LIKE :id "
    + "  AND w.plateNo IN ( SELECT tv.vehicle.plateNo FROM TransportAgentVehicle tv WHERE tv.transportAgent.abbr = :taAbbr ) "
    + "  AND w.matnrRef IS NULL "
    + "  AND w.regType IN :regType "
    + "  AND w.status = '" + Constants.WeightTicket.STATUS_POSTED + "'"),
    @NamedQuery(name = "WeightTicket.findByDateFull",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.driverName LIKE :driverName"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.matnrRef = :loaihang"),
    @NamedQuery(name = "WeightTicket.findByDateNull",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.driverName LIKE :driverName"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.matnrRef IS NULL"),
    @NamedQuery(name = "WeightTicket.findByDateNullAll",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.driverName LIKE :driverName"),
    @NamedQuery(name = "WeightTicket.findByDateDissolved",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.driverName LIKE :driverName"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.matnrRef = :loaihang"
    + "  AND w.status = '" + Constants.WeightTicket.STATUS_DISSOLVED + "'"),
    @NamedQuery(name = "WeightTicket.findByDateDissolvedNull",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.driverName LIKE :driverName"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.matnrRef IS NULL"
    + "  AND w.status = '" + Constants.WeightTicket.STATUS_DISSOLVED + "'"),
    @NamedQuery(name = "WeightTicket.findByDateDissolvedNullAll",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.driverName LIKE :driverName"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.status = '" + Constants.WeightTicket.STATUS_DISSOLVED + "'"),
    @NamedQuery(name = "WeightTicket.findByDatePosted",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.driverName LIKE :driverName"
    + "  AND w.matnrRef = :loaihang"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.status = '" + Constants.WeightTicket.STATUS_POSTED + "'"),
    @NamedQuery(name = "WeightTicket.findByDatePostedNull",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.driverName LIKE :driverName"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.matnrRef IS NULL"
    + "  AND w.status = '" + Constants.WeightTicket.STATUS_POSTED + "'"),
    @NamedQuery(name = "WeightTicket.findByDatePostedNullAll",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.driverName LIKE :driverName"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.status = '" + Constants.WeightTicket.STATUS_POSTED + "'"),
    @NamedQuery(name = "WeightTicket.findByDateAll",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.driverName LIKE :driverName"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.matnrRef = :loaihang"),
    @NamedQuery(name = "WeightTicket.findByDateAllNull",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.driverName LIKE :driverName"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.matnrRef IS NULL"),
    @NamedQuery(name = "WeightTicket.findByDateAllNullAll",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.createdDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.plateNo LIKE :plateNo"
    + "  AND w.driverName LIKE :driverName"),
    @NamedQuery(name = "WeightTicket.findByIdSeqDay",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.id = :id"
    + "  AND w.seqDay = :seqDay")
})
public class WeightTicket implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "mandt")
    private String mandt;
    @Column(name = "wplant")
    private String wplant;
    @Column(name = "seq_day")
    private int seqDay;
    @Column(name = "seq_month")
    private int seqMonth;
    @Column(name = "driver_name")
    private String driverName;
    @Column(name = "driver_id_no")
    private String driverIdNo;
    @Column(name = "plate_no")
    private String plateNo;
    @Column(name = "trailer_id")
    private String trailerId;
    @Column(name = "reg_type")
    private Character regType;
    @Column(name = "reg_item_description")
    private String regItemDescription;
    @Column(name = "req_item_quantity")
    private BigDecimal regItemQuantity;
    @Column(name = "delivery_order_no")
    private String deliveryOrderNo;
    @Column(name = "status")
    private String status;
    @Column(name = "so_niem_xa")
    private String soNiemXa;
    @Column(name = "offline_mode")
    private Boolean offlineMode;
    @Column(name = "f_scale")
    private BigDecimal fScale;
    @Column(name = "f_time")
    private Date fTime;
    @Column(name = "s_scale")
    private BigDecimal sScale;
    @Column(name = "s_time")
    private Date sTime;
    @Column(name = "mat_doc")
    private String matDoc;
    @Column(name = "ebeln")
    private String ebeln;
    @Column(name = "abbr")
    private String abbr;
    @Column(name = "wb_id")
    private String wbId;
    @Column(name = "creator")
    private String creator;
    @Column(name = "lgort")
    private String lgort;
    @Column(name = "charg")
    private String charg;
    @Column(name = "item")
    private String item;
    @Column(name = "matnr_ref")
    private String matnrRef;
    @Column(name = "no_more_gr")
    private Character noMoreGr;
    @Column(name = "move_type")
    private String moveType;
    @Column(name = "move_reas")
    private String moveReas;
    @Column(name = "text")
    private String text;
    @Column(name = "mvt_ind")
    private Character mvtInd;
    @Column(name = "lichn")
    private String lichn;
    @Column(name = "f_creator")
    private String fCreator;
    @Column(name = "s_creator")
    private String sCreator;
    @Column(name = "g_qty")
    private BigDecimal gQty;
    @Column(name = "unit")
    private String unit;
    @Column(name = "doc_year")
    private Integer docYear;
    @Column(name = "recv_matnr")
    private String recvMatnr;
    @Column(name = "recv_plant")
    private String recvPlant;
    @Column(name = "recv_lgort")
    private String recvLgort;
    @Column(name = "recv_charg")
    private String recvCharg;
    @Column(name = "recv_po")
    private String recvPo;
    @Column(name = "pp_procord")
    private String ppProcord;
    @Column(name = "pp_procordcnf")
    private String ppProcordcnf;
    @Column(name = "pp_procordcnfcnt")
    private String ppProcordcnfcnt;
    @Column(name = "kunnr")
    private String kunnr;
    @Column(name = "manual")
    private Character manual;
    @Column(name = "wt_id")
    private String wtId;
    @Column(name = "created_time")
    private String createdTime;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "deleted_date")
    private Date deletedDate;

    public WeightTicket() {
    }

    public WeightTicket(int id, String mandt, String wplant, int seqDay) {
        this.id = id;
        this.mandt = mandt;
        this.wplant = wplant;
        this.seqDay = seqDay;
    }

    public WeightTicket(int id, String mandt, String wplant, int seqDay, int seqMonth,
            String driverName, String driverIdNo, String plateNo, char regType,
            String regItemDescription, BigDecimal regItemQuantity, Date createdDate, String createdTime) {
        this.id = id;
        this.mandt = mandt;
        this.wplant = wplant;
        this.seqDay = seqDay;
        this.seqMonth = seqMonth;
        this.driverName = driverName;
        this.driverIdNo = driverIdNo;
        this.plateNo = plateNo;
        this.regType = regType;
        this.regItemDescription = regItemDescription;
        this.regItemQuantity = regItemQuantity;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getSeqDay() {
        return seqDay;
    }

    public void setSeqDay(int seqDay) {
        this.seqDay = seqDay;
    }

    public long getSeqMonth() {
        return seqMonth;
    }

    public void setSeqMonth(int seqMonth) {
        this.seqMonth = seqMonth;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverIdNo() {
        return driverIdNo;
    }

    public void setDriverIdNo(String driverIdNo) {
        this.driverIdNo = driverIdNo;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public Character getRegType() {
        return regType;
    }

    public void setRegType(Character regType) {
        this.regType = regType;
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

    public String getSoNiemXa() {
        return soNiemXa;
    }

    public void setSoNiemXa(String soNiemXa) {
        this.soNiemXa = soNiemXa;
    }

    public Boolean getOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(Boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    public BigDecimal getFScale() {
        return fScale;
    }

    public void setFScale(BigDecimal fScale) {
        this.fScale = fScale;
    }

    public Date getFTime() {
        return fTime;
    }

    public void setFTime(Date fTime) {
        this.fTime = fTime;
    }

    public BigDecimal getSScale() {
        return sScale;
    }

    public void setSScale(BigDecimal sScale) {
        this.sScale = sScale;
    }

    public Date getSTime() {
        return sTime;
    }

    public void setSTime(Date sTime) {
        this.sTime = sTime;
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

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getWbId() {
        return wbId;
    }

    public void setWbId(String wbId) {
        this.wbId = wbId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public Character getNoMoreGr() {
        return noMoreGr;
    }

    public void setNoMoreGr(Character noMoreGr) {
        this.noMoreGr = noMoreGr;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
    }

    public String getMoveReas() {
        return moveReas;
    }

    public void setMoveReas(String moveReas) {
        this.moveReas = moveReas;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getFCreator() {
        return fCreator;
    }

    public void setFCreator(String fCreator) {
        this.fCreator = fCreator;
    }

    public String getSCreator() {
        return sCreator;
    }

    public void setSCreator(String sCreator) {
        this.sCreator = sCreator;
    }

    public BigDecimal getGQty() {
        return gQty;
    }

    public void setGQty(BigDecimal gQty) {
        this.gQty = gQty;
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

    public String getRecvPlant() {
        return recvPlant;
    }

    public void setRecvPlant(String recvPlant) {
        this.recvPlant = recvPlant;
    }

    public String getRecvLgort() {
        return recvLgort;
    }

    public void setRecvLgort(String recvLgort) {
        this.recvLgort = recvLgort;
    }

    public String getRecvCharg() {
        return recvCharg;
    }

    public void setRecvCharg(String recvCharg) {
        this.recvCharg = recvCharg;
    }

    public String getRecvPo() {
        return recvPo;
    }

    public void setRecvPo(String recvPo) {
        this.recvPo = recvPo;
    }

    public String getPpProcord() {
        return ppProcord;
    }

    public void setPpProcord(String ppProcord) {
        this.ppProcord = ppProcord;
    }

    public String getPpProcordcnf() {
        return ppProcordcnf;
    }

    public void setPpProcordcnf(String ppProcordcnf) {
        this.ppProcordcnf = ppProcordcnf;
    }

    public String getPpProcordcnfcnt() {
        return ppProcordcnfcnt;
    }

    public void setPpProcordcnfcnt(String ppProcordcnfcnt) {
        this.ppProcordcnfcnt = ppProcordcnfcnt;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public Character getManual() {
        return manual;
    }

    public void setManual(Character manual) {
        this.manual = manual;
    }

    public String getWtId() {
        return wtId;
    }

    public void setWtId(String wtId) {
        this.wtId = wtId;
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

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeightTicket that = (WeightTicket) o;

        if (id != that.id) return false;
        if (mandt != null ? !mandt.equals(that.mandt) : that.mandt != null) return false;
        if (wplant != null ? !wplant.equals(that.wplant) : that.wplant != null) return false;
        if (seqDay != that.seqDay) return false;
        if (seqMonth != that.seqMonth) return false;
        if (driverName != null ? !driverName.equals(that.driverName) : that.driverName != null) return false;
        if (driverIdNo != null ? !driverIdNo.equals(that.driverIdNo) : that.driverIdNo != null) return false;
        if (plateNo != null ? !plateNo.equals(that.plateNo) : that.plateNo != null) return false;
        if (trailerId != null ? !trailerId.equals(that.trailerId) : that.trailerId != null) return false;
        if (regType != null ? !regType.equals(that.regType) : that.regType != null) return false;
        if (regItemDescription != null ? !regItemDescription.equals(that.regItemDescription) : that.regItemDescription != null)
            return false;
        if (regItemQuantity != null ? !regItemQuantity.equals(that.regItemQuantity) : that.regItemQuantity != null)
            return false;
        if (deliveryOrderNo != null ? !deliveryOrderNo.equals(that.deliveryOrderNo) : that.deliveryOrderNo != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (soNiemXa != null ? !soNiemXa.equals(that.soNiemXa) : that.soNiemXa != null) return false;
        if (offlineMode != null ? !offlineMode.equals(that.offlineMode) : that.offlineMode != null) return false;
        if (fScale != null ? !fScale.equals(that.fScale) : that.fScale != null) return false;
        if (fTime != null ? !fTime.equals(that.fTime) : that.fTime != null) return false;
        if (sScale != null ? !sScale.equals(that.sScale) : that.sScale != null) return false;
        if (sTime != null ? !sTime.equals(that.sTime) : that.sTime != null) return false;
        if (matDoc != null ? !matDoc.equals(that.matDoc) : that.matDoc != null) return false;
        if (ebeln != null ? !ebeln.equals(that.ebeln) : that.ebeln != null) return false;
        if (abbr != null ? !abbr.equals(that.abbr) : that.abbr != null) return false;
        if (wbId != null ? !wbId.equals(that.wbId) : that.wbId != null) return false;
        if (creator != null ? !creator.equals(that.creator) : that.creator != null) return false;
        if (lgort != null ? !lgort.equals(that.lgort) : that.lgort != null) return false;
        if (charg != null ? !charg.equals(that.charg) : that.charg != null) return false;
        if (item != null ? !item.equals(that.item) : that.item != null) return false;
        if (matnrRef != null ? !matnrRef.equals(that.matnrRef) : that.matnrRef != null) return false;
        if (noMoreGr != null ? !noMoreGr.equals(that.noMoreGr) : that.noMoreGr != null) return false;
        if (moveType != null ? !moveType.equals(that.moveType) : that.moveType != null) return false;
        if (moveReas != null ? !moveReas.equals(that.moveReas) : that.moveReas != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (mvtInd != null ? !mvtInd.equals(that.mvtInd) : that.mvtInd != null) return false;
        if (lichn != null ? !lichn.equals(that.lichn) : that.lichn != null) return false;
        if (fCreator != null ? !fCreator.equals(that.fCreator) : that.fCreator != null) return false;
        if (sCreator != null ? !sCreator.equals(that.sCreator) : that.sCreator != null) return false;
        if (gQty != null ? !gQty.equals(that.gQty) : that.gQty != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (docYear != null ? !docYear.equals(that.docYear) : that.docYear != null) return false;
        if (recvMatnr != null ? !recvMatnr.equals(that.recvMatnr) : that.recvMatnr != null) return false;
        if (recvPlant != null ? !recvPlant.equals(that.recvPlant) : that.recvPlant != null) return false;
        if (recvLgort != null ? !recvLgort.equals(that.recvLgort) : that.recvLgort != null) return false;
        if (recvCharg != null ? !recvCharg.equals(that.recvCharg) : that.recvCharg != null) return false;
        if (recvPo != null ? !recvPo.equals(that.recvPo) : that.recvPo != null) return false;
        if (ppProcord != null ? !ppProcord.equals(that.ppProcord) : that.ppProcord != null) return false;
        if (ppProcordcnf != null ? !ppProcordcnf.equals(that.ppProcordcnf) : that.ppProcordcnf != null) return false;
        if (ppProcordcnfcnt != null ? !ppProcordcnfcnt.equals(that.ppProcordcnfcnt) : that.ppProcordcnfcnt != null)
            return false;
        if (kunnr != null ? !kunnr.equals(that.kunnr) : that.kunnr != null) return false;
        if (manual != null ? !manual.equals(that.manual) : that.manual != null) return false;
        if (wtId != null ? !wtId.equals(that.wtId) : that.wtId != null) return false;
        if (createdTime != null ? !createdTime.equals(that.createdTime) : that.createdTime != null) return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;
        if (updatedDate != null ? !updatedDate.equals(that.updatedDate) : that.updatedDate != null) return false;
        if (deletedDate != null ? !deletedDate.equals(that.deletedDate) : that.deletedDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (wplant != null ? wplant.hashCode() : 0);
        result = 31 * result + (int) (seqDay ^ (seqDay >>> 31));
        result = 31 * result + (int) (seqMonth ^ (seqMonth >>> 31));
        result = 31 * result + (driverName != null ? driverName.hashCode() : 0);
        result = 31 * result + (driverIdNo != null ? driverIdNo.hashCode() : 0);
        result = 31 * result + (plateNo != null ? plateNo.hashCode() : 0);
        result = 31 * result + (trailerId != null ? trailerId.hashCode() : 0);
        result = 31 * result + (regType != null ? regType.hashCode() : 0);
        result = 31 * result + (regItemDescription != null ? regItemDescription.hashCode() : 0);
        result = 31 * result + (regItemQuantity != null ? regItemQuantity.hashCode() : 0);
        result = 31 * result + (deliveryOrderNo != null ? deliveryOrderNo.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (soNiemXa != null ? soNiemXa.hashCode() : 0);
        result = 31 * result + (offlineMode != null ? offlineMode.hashCode() : 0);
        result = 31 * result + (fScale != null ? fScale.hashCode() : 0);
        result = 31 * result + (fTime != null ? fTime.hashCode() : 0);
        result = 31 * result + (sScale != null ? sScale.hashCode() : 0);
        result = 31 * result + (sTime != null ? sTime.hashCode() : 0);
        result = 31 * result + (matDoc != null ? matDoc.hashCode() : 0);
        result = 31 * result + (ebeln != null ? ebeln.hashCode() : 0);
        result = 31 * result + (abbr != null ? abbr.hashCode() : 0);
        result = 31 * result + (wbId != null ? wbId.hashCode() : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (lgort != null ? lgort.hashCode() : 0);
        result = 31 * result + (charg != null ? charg.hashCode() : 0);
        result = 31 * result + (item != null ? item.hashCode() : 0);
        result = 31 * result + (matnrRef != null ? matnrRef.hashCode() : 0);
        result = 31 * result + (noMoreGr != null ? noMoreGr.hashCode() : 0);
        result = 31 * result + (moveType != null ? moveType.hashCode() : 0);
        result = 31 * result + (moveReas != null ? moveReas.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (mvtInd != null ? mvtInd.hashCode() : 0);
        result = 31 * result + (lichn != null ? lichn.hashCode() : 0);
        result = 31 * result + (fCreator != null ? fCreator.hashCode() : 0);
        result = 31 * result + (sCreator != null ? sCreator.hashCode() : 0);
        result = 31 * result + (gQty != null ? gQty.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (docYear != null ? docYear.hashCode() : 0);
        result = 31 * result + (recvMatnr != null ? recvMatnr.hashCode() : 0);
        result = 31 * result + (recvPlant != null ? recvPlant.hashCode() : 0);
        result = 31 * result + (recvLgort != null ? recvLgort.hashCode() : 0);
        result = 31 * result + (recvCharg != null ? recvCharg.hashCode() : 0);
        result = 31 * result + (recvPo != null ? recvPo.hashCode() : 0);
        result = 31 * result + (ppProcord != null ? ppProcord.hashCode() : 0);
        result = 31 * result + (ppProcordcnf != null ? ppProcordcnf.hashCode() : 0);
        result = 31 * result + (ppProcordcnfcnt != null ? ppProcordcnfcnt.hashCode() : 0);
        result = 31 * result + (kunnr != null ? kunnr.hashCode() : 0);
        result = 31 * result + (manual != null ? manual.hashCode() : 0);
        result = 31 * result + (wtId != null ? wtId.hashCode() : 0);
        result = 31 * result + (createdTime != null ? createdTime.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (deletedDate != null ? deletedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.WeightTicket[id=" + id + "]";
    }
}
