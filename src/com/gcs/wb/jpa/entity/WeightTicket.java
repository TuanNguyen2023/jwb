/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import com.gcs.wb.base.constant.Constants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_weight_ticket")
@NamedQueries({
    @NamedQuery(name = "WeightTicket.findAll", query = "SELECT w FROM WeightTicket w"),
    @NamedQuery(name = "WeightTicket.findByCreatedDateRange", query = "SELECT w FROM WeightTicket w WHERE w.createdDate BETWEEN :from AND :to"),
    @NamedQuery(name = "WeightTicket.findByDeliveryOrderNo",
  query = "SELECT w FROM WeightTicket w "
    + " , IN(w.weightTicketDetails) wd "
    + " WHERE wd.deliveryOrderNo LIKE :deliveryOrderNo"),    @NamedQuery(name = "WeightTicket.findBySoNiemXa", query = "SELECT w FROM WeightTicket w WHERE w.soNiemXa = :soNiemXa"),
    @NamedQuery(name = "WeightTicket.findByDateFull",
            query = "SELECT w FROM WeightTicket w "
            + " , IN(w.weightTicketDetails) wd "
            + " WHERE w.createdDate BETWEEN :from AND :to"
            + "  AND w.creator LIKE :creator"
            + "  AND w.driverName LIKE :driverName"
            + "  AND w.plateNo LIKE :plateNo"
            + "  AND wd.matnrRef = :loaihang"),
    @NamedQuery(name = "WeightTicket.findByDateNull",
            query = "SELECT w FROM WeightTicket w "
            + " , IN(w.weightTicketDetails) wd "
            + " WHERE w.createdDate BETWEEN :from AND :to"
            + "  AND w.creator LIKE :creator"
            + "  AND w.driverName LIKE :driverName"
            + "  AND w.plateNo LIKE :plateNo"
            + "  AND wd.matnrRef IS NULL"),
    @NamedQuery(name = "WeightTicket.findByDateNullAll",
            query = "SELECT w FROM WeightTicket w "
            + "WHERE w.createdDate BETWEEN :from AND :to"
            + "  AND w.creator LIKE :creator"
            + "  AND w.plateNo LIKE :plateNo"
            + "  AND w.driverName LIKE :driverName"),
    @NamedQuery(name = "WeightTicket.findByDateDissolved",
            query = "SELECT w FROM WeightTicket w "
            + " , IN(w.weightTicketDetails) wd "
            + " WHERE w.createdDate BETWEEN :from AND :to"
            + "  AND w.creator LIKE :creator"
            + "  AND w.driverName LIKE :driverName"
            + "  AND w.plateNo LIKE :plateNo"
            + "  AND wd.matnrRef = :loaihang"
            + "  AND w.status = '" + Constants.WeightTicket.STATUS_DISSOLVED + "'"),
    @NamedQuery(name = "WeightTicket.findByDateDissolvedNull",
            query = "SELECT w FROM WeightTicket w "
            + " , IN(w.weightTicketDetails) wd "
            + " WHERE w.createdDate BETWEEN :from AND :to"
            + "  AND w.creator LIKE :creator"
            + "  AND w.driverName LIKE :driverName"
            + "  AND w.plateNo LIKE :plateNo"
            + "  AND wd.matnrRef IS NULL"
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
            + " , IN(w.weightTicketDetails) wd "
            + " WHERE w.createdDate BETWEEN :from AND :to"
            + "  AND w.creator LIKE :creator"
            + "  AND w.driverName LIKE :driverName"
            + "  AND wd.matnrRef = :loaihang"
            + "  AND w.plateNo LIKE :plateNo"
            + "  AND w.status = '" + Constants.WeightTicket.STATUS_POSTED + "'"),
    @NamedQuery(name = "WeightTicket.findByDatePostedNull",
            query = "SELECT w FROM WeightTicket w "
            + " , IN(w.weightTicketDetails) wd "
            + " WHERE w.createdDate BETWEEN :from AND :to"
            + "  AND w.creator LIKE :creator"
            + "  AND w.driverName LIKE :driverName"
            + "  AND w.plateNo LIKE :plateNo"
            + "  AND wd.matnrRef IS NULL"
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
            + " , IN(w.weightTicketDetails) wd "
            + " WHERE w.createdDate BETWEEN :from AND :to"
            + "  AND w.creator LIKE :creator"
            + "  AND w.driverName LIKE :driverName"
            + "  AND w.plateNo LIKE :plateNo"
            + "  AND wd.matnrRef = :loaihang"),
    @NamedQuery(name = "WeightTicket.findByDateAllNull",
            query = "SELECT w FROM WeightTicket w "
            + " , IN(w.weightTicketDetails) wd "
            + " WHERE w.createdDate BETWEEN :from AND :to"
            + "  AND w.creator LIKE :creator"
            + "  AND w.driverName LIKE :driverName"
            + "  AND w.plateNo LIKE :plateNo"
            + "  AND wd.matnrRef IS NULL"),
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
    @Column(name = "mandt", unique = true)
    private String mandt;
    @Column(name = "wplant", unique = true)
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
    @Column(name = "load_vendor")
    private String loadVendor;
    @Column(name = "trans_vendor")
    private String transVendor;
    @Column(name = "no_more_gr")
    private Character noMoreGr;
    @Column(name = "move_type")
    private String moveType;
    @Column(name = "move_reas")
    private String moveReas;
    @Column(name = "text")
    private String text;
    @Column(name = "so_niem_xa")
    private String soNiemXa;
    @Column(name = "f_scale")
    private BigDecimal fScale;
    @Column(name = "f_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fTime;
    @Column(name = "s_scale")
    private BigDecimal sScale;
    @Column(name = "s_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date sTime;
    @Column(name = "f_creator")
    private String fCreator;
    @Column(name = "s_creator")
    private String sCreator;
    @Column(name = "g_qty")
    private BigDecimal gQty;
    @Column(name = "recv_plant")
    private String recvPlant;
    @Column(name = "recv_lgort")
    private String recvLgort;
    @Column(name = "recv_charg")
    private String recvCharg;
    @Column(name = "recv_po")
    private String recvPo;
    @Column(name = "recv_matnr")
    private String recvMatnr;
    @Column(name = "manual")
    private Character manual;
    @Column(name = "wt_id")
    private String wtId;
    @Column(name = "offline_mode")
    private Boolean offlineMode;
    @Column(name = "status")
    private String status;
    @Column(name = "created_time")
    private String createdTime;
    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate = new Date();
    @Column(name = "updated_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;
    @Column(name = "posto")
    private String posto;
    @OneToMany(mappedBy = "weightTicket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "weight_ticket_id")
    private List<WeightTicketDetail> weightTicketDetails = new ArrayList<>();

    @Column(name = "sling")
    private int sling;
    @Column(name = "pallet")
    private int pallet;
    @Column(name = "mode")
    private String mode;
    @Column(name = "remark")
    private String remark;
    @Column(name = "batch")
    private String batch;
    @Column(name = "ticket_id")
    private String ticketId;
    @Column(name = "registered_number")
    private String registeredNumber;
    
    @Column(name = "weight_ticket_id_ref")
    private String weightTicketIdRef;

    public WeightTicket() {
    }

    public String getWeightTicketIdRef() {
        return weightTicketIdRef;
    }

    public void setWeightTicketIdRef(String weightTicketIdRef) {
        this.weightTicketIdRef = weightTicketIdRef;
    }

    public String getRegisteredNumber() {
        return registeredNumber;
    }

    public void setRegisteredNumber(String registeredNumber) {
        this.registeredNumber = registeredNumber;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicket_id(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getSling() {
        return sling;
    }

    public void setSling(int sling) {
        this.sling = sling;
    }

    public int getPallet() {
        return pallet;
    }

    public void setPallet(int pallet) {
        this.pallet = pallet;
    }

        public String getRecvMatnr() {
        return recvMatnr;
    }

    public void setRecvMatnr(String recvMatnr) {
        this.recvMatnr = recvMatnr;
    }

    public WeightTicket(int id) {
        this.id = id;
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
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPosto() {
        return posto;
    }

    public void setPosto(String posto) {
        this.posto = posto;
    }

    public List<WeightTicketDetail> getWeightTicketDetails() {
        return weightTicketDetails;
    }

    public void setWeightTicketDetails(List<WeightTicketDetail> weightTicketDetails) {
        this.weightTicketDetails = weightTicketDetails;
    }
    
    public void addWeightTicketDetail(WeightTicketDetail weightTicketDetail) {
        weightTicketDetail.setWeightTicket(this);
        weightTicketDetails.add(weightTicketDetail);
    }

    public WeightTicketDetail getWeightTicketDetail() {
        if (weightTicketDetails.isEmpty()) {
            WeightTicketDetail weightTicketDetail = new WeightTicketDetail();
            weightTicketDetail.setWeightTicket(this);
            weightTicketDetails.add(weightTicketDetail);
        }

        return weightTicketDetails.get(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WeightTicket that = (WeightTicket) o;

        if (id != that.id) {
            return false;
        }
        if (mandt != null ? !mandt.equals(that.mandt) : that.mandt != null) {
            return false;
        }
        if (wplant != null ? !wplant.equals(that.wplant) : that.wplant != null) {
            return false;
        }
        if (seqDay != that.seqDay) {
            return false;
        }
        if (seqMonth != that.seqMonth) {
            return false;
        }
        if (driverName != null ? !driverName.equals(that.driverName) : that.driverName != null) {
            return false;
        }
        if (driverIdNo != null ? !driverIdNo.equals(that.driverIdNo) : that.driverIdNo != null) {
            return false;
        }
        if (plateNo != null ? !plateNo.equals(that.plateNo) : that.plateNo != null) {
            return false;
        }
        if (trailerId != null ? !trailerId.equals(that.trailerId) : that.trailerId != null) {
            return false;
        }
        if (regType != null ? !regType.equals(that.regType) : that.regType != null) {
            return false;
        }
        if (status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }
        if (soNiemXa != null ? !soNiemXa.equals(that.soNiemXa) : that.soNiemXa != null) {
            return false;
        }
        if (offlineMode != null ? !offlineMode.equals(that.offlineMode) : that.offlineMode != null) {
            return false;
        }
        if (fScale != null ? !fScale.equals(that.fScale) : that.fScale != null) {
            return false;
        }
        if (fTime != null ? !fTime.equals(that.fTime) : that.fTime != null) {
            return false;
        }
        if (sScale != null ? !sScale.equals(that.sScale) : that.sScale != null) {
            return false;
        }
        if (sTime != null ? !sTime.equals(that.sTime) : that.sTime != null) {
            return false;
        }
        if (abbr != null ? !abbr.equals(that.abbr) : that.abbr != null) {
            return false;
        }
        if (wbId != null ? !wbId.equals(that.wbId) : that.wbId != null) {
            return false;
        }
        if (creator != null ? !creator.equals(that.creator) : that.creator != null) {
            return false;
        }
        if (lgort != null ? !lgort.equals(that.lgort) : that.lgort != null) {
            return false;
        }
        if (charg != null ? !charg.equals(that.charg) : that.charg != null) {
            return false;
        }
        if (noMoreGr != null ? !noMoreGr.equals(that.noMoreGr) : that.noMoreGr != null) {
            return false;
        }
        if (moveType != null ? !moveType.equals(that.moveType) : that.moveType != null) {
            return false;
        }
        if (moveReas != null ? !moveReas.equals(that.moveReas) : that.moveReas != null) {
            return false;
        }
        if (text != null ? !text.equals(that.text) : that.text != null) {
            return false;
        }
        if (fCreator != null ? !fCreator.equals(that.fCreator) : that.fCreator != null) {
            return false;
        }
        if (sCreator != null ? !sCreator.equals(that.sCreator) : that.sCreator != null) {
            return false;
        }
        if (gQty != null ? !gQty.equals(that.gQty) : that.gQty != null) {
            return false;
        }
        if (recvPlant != null ? !recvPlant.equals(that.recvPlant) : that.recvPlant != null) {
            return false;
        }
        if (recvLgort != null ? !recvLgort.equals(that.recvLgort) : that.recvLgort != null) {
            return false;
        }
        if (recvCharg != null ? !recvCharg.equals(that.recvCharg) : that.recvCharg != null) {
            return false;
        }
        if (recvPo != null ? !recvPo.equals(that.recvPo) : that.recvPo != null) {
            return false;
        }
        if (manual != null ? !manual.equals(that.manual) : that.manual != null) {
            return false;
        }
        if (wtId != null ? !wtId.equals(that.wtId) : that.wtId != null) {
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
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (soNiemXa != null ? soNiemXa.hashCode() : 0);
        result = 31 * result + (offlineMode != null ? offlineMode.hashCode() : 0);
        result = 31 * result + (fScale != null ? fScale.hashCode() : 0);
        result = 31 * result + (fTime != null ? fTime.hashCode() : 0);
        result = 31 * result + (sScale != null ? sScale.hashCode() : 0);
        result = 31 * result + (sTime != null ? sTime.hashCode() : 0);
        result = 31 * result + (abbr != null ? abbr.hashCode() : 0);
        result = 31 * result + (wbId != null ? wbId.hashCode() : 0);
        result = 31 * result + (creator != null ? creator.hashCode() : 0);
        result = 31 * result + (lgort != null ? lgort.hashCode() : 0);
        result = 31 * result + (charg != null ? charg.hashCode() : 0);
        result = 31 * result + (noMoreGr != null ? noMoreGr.hashCode() : 0);
        result = 31 * result + (moveType != null ? moveType.hashCode() : 0);
        result = 31 * result + (moveReas != null ? moveReas.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (fCreator != null ? fCreator.hashCode() : 0);
        result = 31 * result + (sCreator != null ? sCreator.hashCode() : 0);
        result = 31 * result + (gQty != null ? gQty.hashCode() : 0);
        result = 31 * result + (recvPlant != null ? recvPlant.hashCode() : 0);
        result = 31 * result + (recvLgort != null ? recvLgort.hashCode() : 0);
        result = 31 * result + (recvCharg != null ? recvCharg.hashCode() : 0);
        result = 31 * result + (recvPo != null ? recvPo.hashCode() : 0);
        result = 31 * result + (manual != null ? manual.hashCode() : 0);
        result = 31 * result + (wtId != null ? wtId.hashCode() : 0);
        result = 31 * result + (createdTime != null ? createdTime.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.WeightTicket[id=" + id + "]";
    }
}
