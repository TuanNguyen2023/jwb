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
@Table(name = "WeightTicket")
@NamedQueries({
    @NamedQuery(name = "WeightTicket.findAll", query = "SELECT w FROM WeightTicket w"),
    @NamedQuery(name = "WeightTicket.findByMandt", query = "SELECT w FROM WeightTicket w WHERE w.weightTicketPK.mandt = :mandt"),
    @NamedQuery(name = "WeightTicket.findByWPlant", query = "SELECT w FROM WeightTicket w WHERE w.weightTicketPK.wPlant = :wPlant"),
    @NamedQuery(name = "WeightTicket.findById", query = "SELECT w FROM WeightTicket w WHERE w.weightTicketPK.id = :id"),
    @NamedQuery(name = "WeightTicket.findBySeqByDay", query = "SELECT w FROM WeightTicket w WHERE w.weightTicketPK.seqByDay = :seqByDay"),
    @NamedQuery(name = "WeightTicket.findBySeqByMonth", query = "SELECT w FROM WeightTicket w WHERE w.seqByMonth = :seqByMonth"),
    @NamedQuery(name = "WeightTicket.findByTenTaiXe", query = "SELECT w FROM WeightTicket w WHERE w.tenTaiXe = :tenTaiXe"),
    @NamedQuery(name = "WeightTicket.findByCmndBl", query = "SELECT w FROM WeightTicket w WHERE w.cmndBl = :cmndBl"),
    @NamedQuery(name = "WeightTicket.findBySoXe", query = "SELECT w FROM WeightTicket w WHERE w.soXe = :soXe"),
    @NamedQuery(name = "WeightTicket.findBySoRomooc", query = "SELECT w FROM WeightTicket w WHERE w.soRomooc = :soRomooc"),
    @NamedQuery(name = "WeightTicket.findByRegCategory", query = "SELECT w FROM WeightTicket w WHERE w.regCategory = :regCategory"),
    @NamedQuery(name = "WeightTicket.findByRegItemText", query = "SELECT w FROM WeightTicket w WHERE w.regItemText = :regItemText"),
    @NamedQuery(name = "WeightTicket.findByRegItemQty", query = "SELECT w FROM WeightTicket w WHERE w.regItemQty = :regItemQty"),
    @NamedQuery(name = "WeightTicket.findByCreateDate", query = "SELECT w FROM WeightTicket w WHERE w.createDate = :createDate"),
    @NamedQuery(name = "WeightTicket.findByCreateDateRange", query = "SELECT w FROM WeightTicket w WHERE w.weightTicketPK.mandt = :mandt AND w.weightTicketPK.wPlant = :wPlant AND w.createDate BETWEEN :from AND :to"),
    @NamedQuery(name = "WeightTicket.findByCreateTime", query = "SELECT w FROM WeightTicket w WHERE w.createTime = :createTime"),
    @NamedQuery(name = "WeightTicket.findByCreator", query = "SELECT w FROM WeightTicket w WHERE w.creator = :creator"),
    @NamedQuery(name = "WeightTicket.findByLgort", query = "SELECT w FROM WeightTicket w WHERE w.lgort = :lgort"),
    @NamedQuery(name = "WeightTicket.findByCharg", query = "SELECT w FROM WeightTicket w WHERE w.charg = :charg"),
    @NamedQuery(name = "WeightTicket.findByDelivNumb", query = "SELECT w FROM WeightTicket w WHERE w.delivNumb = :delivNumb"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDelivNumb", query = "SELECT w FROM WeightTicket w WHERE w.weightTicketPK.mandt = :mandt AND w.weightTicketPK.wPlant = :wPlant AND w.delivNumb = :delivNumb"),
    @NamedQuery(name = "WeightTicket.findByEbeln", query = "SELECT w FROM WeightTicket w WHERE w.ebeln = :ebeln"),
    @NamedQuery(name = "WeightTicket.findByItem", query = "SELECT w FROM WeightTicket w WHERE w.item = :item"),
    @NamedQuery(name = "WeightTicket.findByMatnrRef", query = "SELECT w FROM WeightTicket w WHERE w.matnrRef = :matnrRef"),
    @NamedQuery(name = "WeightTicket.findByNoMoreGr", query = "SELECT w FROM WeightTicket w WHERE w.noMoreGr = :noMoreGr"),
    @NamedQuery(name = "WeightTicket.findByMoveType", query = "SELECT w FROM WeightTicket w WHERE w.moveType = :moveType"),
    @NamedQuery(name = "WeightTicket.findByMoveReas", query = "SELECT w FROM WeightTicket w WHERE w.moveReas = :moveReas"),
    @NamedQuery(name = "WeightTicket.findByText", query = "SELECT w FROM WeightTicket w WHERE w.text = :text"),
    @NamedQuery(name = "WeightTicket.findByMvtInd", query = "SELECT w FROM WeightTicket w WHERE w.mvtInd = :mvtInd"),
    @NamedQuery(name = "WeightTicket.findByLichn", query = "SELECT w FROM WeightTicket w WHERE w.lichn = :lichn"),
    @NamedQuery(name = "WeightTicket.findBySoNiemXa", query = "SELECT w FROM WeightTicket w WHERE w.soNiemXa = :soNiemXa"),
    @NamedQuery(name = "WeightTicket.findByFScale", query = "SELECT w FROM WeightTicket w WHERE w.fScale = :fScale"),
    @NamedQuery(name = "WeightTicket.findByFTime", query = "SELECT w FROM WeightTicket w WHERE w.fTime = :fTime"),
    @NamedQuery(name = "WeightTicket.findByFCreator", query = "SELECT w FROM WeightTicket w WHERE w.fCreator = :fCreator"),
    @NamedQuery(name = "WeightTicket.findBySScale", query = "SELECT w FROM WeightTicket w WHERE w.sScale = :sScale"),
    @NamedQuery(name = "WeightTicket.findBySTime", query = "SELECT w FROM WeightTicket w WHERE w.sTime = :sTime"),
    @NamedQuery(name = "WeightTicket.findBySCreator", query = "SELECT w FROM WeightTicket w WHERE w.sCreator = :sCreator"),
    @NamedQuery(name = "WeightTicket.findByGQty", query = "SELECT w FROM WeightTicket w WHERE w.gQty = :gQty"),
    @NamedQuery(name = "WeightTicket.findByUnit", query = "SELECT w FROM WeightTicket w WHERE w.unit = :unit"),
    @NamedQuery(name = "WeightTicket.findByOfflineMode", query = "SELECT w FROM WeightTicket w WHERE w.offlineMode = :offlineMode"),
    @NamedQuery(name = "WeightTicket.findByTransferedPosting", query = "SELECT w FROM WeightTicket w WHERE w.transferedPosting = :transferedPosting"),
    @NamedQuery(name = "WeightTicket.findByDissolved", query = "SELECT w FROM WeightTicket w WHERE w.dissolved = :dissolved"),
    @NamedQuery(name = "WeightTicket.findByPosted", query = "SELECT w FROM WeightTicket w WHERE w.posted = :posted"),
    @NamedQuery(name = "WeightTicket.findByMatDoc", query = "SELECT w FROM WeightTicket w WHERE w.matDoc = :matDoc"),
    @NamedQuery(name = "WeightTicket.findByDocYear", query = "SELECT w FROM WeightTicket w WHERE w.docYear = :docYear"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantIdSoxeMatnrRegcat",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.weightTicketPK.id LIKE :id "
    + "  AND w.soXe IN ( SELECT v.soXe FROM Vehicle v WHERE v.taAbbr = :taAbbr ) "
    + "  AND w.matnrRef = :matnrRef "
    + "  AND w.regCategory IN :regCategory"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantIdSoxeRegcat",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.weightTicketPK.id LIKE :id "
    + "  AND w.soXe IN ( SELECT v.soXe FROM Vehicle v WHERE v.taAbbr = :taAbbr ) "
    + "  AND w.matnrRef IS NULL "
    + "  AND w.regCategory IN :regCategory"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantIdSoxeMatnrRegcatDissovled",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.weightTicketPK.id LIKE :id "
    + "  AND w.soXe IN ( SELECT v.soXe FROM Vehicle v WHERE v.taAbbr = :taAbbr ) "
    + "  AND w.matnrRef = :matnrRef "
    + "  AND w.regCategory IN :regCategory "
    + "  AND w.dissolved = 1"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantIdSoxeRegcatDissovled",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.weightTicketPK.id LIKE :id "
    + "  AND w.soXe IN ( SELECT v.soXe FROM Vehicle v WHERE v.taAbbr = :taAbbr ) "
    + "  AND w.matnrRef IS NULL "
    + "  AND w.regCategory IN :regCategory "
    + "  AND w.dissolved = 1"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantIdSoxeMatnrRegcatPosted",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.weightTicketPK.id LIKE :id "
    + "  AND w.soXe IN ( SELECT v.soXe FROM Vehicle v WHERE v.taAbbr = :taAbbr ) "
    + "  AND w.matnrRef = :matnrRef "
    + "  AND w.regCategory IN :regCategory "
    + "  AND w.posted = 1"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantIdSoxeRegcatPosted",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.weightTicketPK.id LIKE :id "
    + "  AND w.soXe IN ( SELECT v.soXe FROM Vehicle v WHERE v.taAbbr = :taAbbr ) "
    + "  AND w.matnrRef IS NULL "
    + "  AND w.regCategory IN :regCategory "
    + "  AND w.posted = 1"),
    //hoangvv modify
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDateFull",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    + "  AND w.matnrRef = :loaihang"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDateNull",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    + "  AND w.matnrRef IS NULL"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDateNullAll",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDateDissolved",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    + "  AND w.matnrRef = :loaihang"
    + "  AND w.dissolved = 1"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDateDissolvedNull",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    + "  AND w.matnrRef IS NULL"
    + "  AND w.dissolved = 1"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDateDissolvedNullAll",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    + "  AND w.dissolved = 1"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDatePosted",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"
    + "  AND w.matnrRef = :loaihang"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    //    + "  AND w.dissolved = 1")
    + "  AND w.posted = 1"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDatePostedNull",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    + "  AND w.matnrRef IS NULL"
    //    + "  AND w.dissolved = 1")
    + "  AND w.posted = 1"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDatePostedNullAll",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    //    + "  AND w.dissolved = 1")
    + "  AND w.posted = 1"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDateAll",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    + "  AND w.matnrRef = :loaihang"),
//    + "  AND w.dissolved = 1"),
//    + "  AND w.posted = 1"    ),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDateAllNull",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.tenTaiXe LIKE :tenTaiXe"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    + "  AND w.matnrRef IS NULL"),
    @NamedQuery(name = "WeightTicket.findByMandtWPlantDateAllNullAll",
    query = "SELECT w FROM WeightTicket w "
    + "WHERE w.weightTicketPK.mandt = :mandt "
    + "  AND w.weightTicketPK.wPlant = :wPlant "
    + "  AND w.createDate BETWEEN :from AND :to"
    + "  AND w.creator LIKE :creator"
    + "  AND w.soXe LIKE :soXe"
//    + "  AND w.createTime BETWEEN :timefrom AND :timeto"
    + "  AND w.tenTaiXe LIKE :tenTaiXe") //    + "  AND w.dissolved = 1")
//    + "  AND w.posted = 1"    ),
})
public class WeightTicket implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WeightTicketPK weightTicketPK;
    @Basic(optional = false)
    @Column(name = "SEQ_BY_MONTH")
    private int seqByMonth;
    @Basic(optional = false)
    @Column(name = "TEN_TAI_XE")
    private String tenTaiXe;
    @Basic(optional = false)
    @Column(name = "CMND_BL")
    private String cmndBl;
    @Basic(optional = false)
    @Column(name = "SO_XE")
    private String soXe;
    @Column(name = "SO_ROMOOC")
    private String soRomooc;
    @Basic(optional = false)
    @Column(name = "REG_CATEGORY")
    private char regCategory;
    @Basic(optional = false)
    @Column(name = "REG_ITEM_TEXT")
    private String regItemText;
    @Basic(optional = false)
    @Column(name = "REG_ITEM_QTY")
    private BigDecimal regItemQty;
    @Basic(optional = false)
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @Basic(optional = false)
    @Column(name = "CREATE_TIME")
    private String createTime;
    @Column(name = "CREATOR")
    private String creator;
    @Column(name = "LGORT")
    private String lgort;
    @Column(name = "CHARG")
    private String charg;
    @Column(name = "DELIV_NUMB")
    private String delivNumb;
    @Column(name = "EBELN")
    private String ebeln;
    @Column(name = "ITEM")
    private String item;
    @Column(name = "MATNR_REF")
    private String matnrRef;
    @Column(name = "NO_MORE_GR")
    private Character noMoreGr;
    @Column(name = "MOVE_TYPE")
    private String moveType;
    @Column(name = "MOVE_REAS")
    private String moveReas;
    @Column(name = "TEXT")
    private String text;
    @Column(name = "MVT_IND")
    private Character mvtInd;
    @Column(name = "LICHN")
    private String lichn;
    @Column(name = "SO_NIEM_XA")
    private String soNiemXa;
    @Column(name = "F_SCALE")
    private BigDecimal fScale;
    @Column(name = "F_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fTime;
    @Column(name = "F_CREATOR")
    private String fCreator;
    @Column(name = "S_SCALE")
    private BigDecimal sScale;
    @Column(name = "S_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sTime;
    @Column(name = "S_CREATOR")
    private String sCreator;
    @Column(name = "G_QTY")
    private BigDecimal gQty;
    @Column(name = "UNIT")
    private String unit;
    @Column(name = "OFFLINE_MODE")
    private Boolean offlineMode;
    @Column(name = "TRANSFERED_POSTING")
    private Boolean transferedPosting;
    @Column(name = "DISSOLVED")
    private Boolean dissolved;
    @Column(name = "POSTED")
    private int posted;
    @Column(name = "MAT_DOC")
    private String matDoc;
    @Column(name = "DOC_YEAR")
    private Integer docYear;
    @Column(name = "RECV_MATNR")
    private String recvMatnr;
    @Column(name = "RECV_PLANT")
    private String recvPlant;
    @Column(name = "RECV_LGORT")
    private String recvLgort;
    @Column(name = "RECV_CHARG")
    private String recvCharg;
    @Column(name = "RECV_PO")
    private String recvPo;
    @Column(name = "PP_PROCORD")
    private String ppProcord;
    @Column(name = "PP_PROCORDCNF")
    private String ppProcordcnf;
    @Column(name = "PP_PROCORDCNFCNT")
    private String ppProcordcnfcnt;
    @Column(name = "ABBR")
    private String abbr;
    @Column(name = "WB_ID")
    private String wbId;
    @Column(name = "KUNNR")
    private String kunnr;
    @Column(name = "MANUAL")
    private String manual;

    public WeightTicket() {
    }

    public WeightTicket(WeightTicketPK weightTicketPK) {
        this.weightTicketPK = weightTicketPK;
    }

    public WeightTicket(WeightTicketPK weightTicketPK, int seqByMonth, String tenTaiXe, String cmndBl, String soXe, char regCategory, String regItemText, BigDecimal regItemQty, Date createDate, String createTime) {
        this.weightTicketPK = weightTicketPK;
        this.seqByMonth = seqByMonth;
        this.tenTaiXe = tenTaiXe;
        this.cmndBl = cmndBl;
        this.soXe = soXe;
        this.regCategory = regCategory;
        this.regItemText = regItemText;
        this.regItemQty = regItemQty;
        this.createDate = createDate;
        this.createTime = createTime;
    }

    public WeightTicket(String mandt, String wPlant, String id, int seqByDay) {
        this.weightTicketPK = new WeightTicketPK(mandt, wPlant, id, seqByDay);
    }

    public WeightTicketPK getWeightTicketPK() {
        return weightTicketPK;
    }

    public void setWeightTicketPK(WeightTicketPK weightTicketPK) {
        this.weightTicketPK = weightTicketPK;
    }

    public int getSeqByMonth() {
        return seqByMonth;
    }

    public void setSeqByMonth(int seqByMonth) {
        this.seqByMonth = seqByMonth;
    }

    public String getTenTaiXe() {
        return tenTaiXe;
    }

    public void setTenTaiXe(String tenTaiXe) {
        this.tenTaiXe = tenTaiXe;
    }

    public String getCmndBl() {
        return cmndBl;
    }

    public void setCmndBl(String cmndBl) {
        this.cmndBl = cmndBl;
    }

    public String getSoXe() {
        return soXe;
    }

    public void setSoXe(String soXe) {
        this.soXe = soXe;
    }

    public String getSoRomooc() {
        return soRomooc;
    }

    public void setSoRomooc(String soRomooc) {
        this.soRomooc = soRomooc;
    }

    public char getRegCategory() {
        return regCategory;
    }

    public void setRegCategory(char regCategory) {
        this.regCategory = regCategory;
    }

    public String getRegItemText() {
        return regItemText;
    }

    public void setRegItemText(String regItemText) {
        this.regItemText = regItemText;
    }

    public BigDecimal getRegItemQty() {
        return regItemQty;
    }

    public void setRegItemQty(BigDecimal regItemQty) {
        this.regItemQty = regItemQty;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getDelivNumb() {
        return delivNumb;
    }

    public void setDelivNumb(String delivNumb) {
        this.delivNumb = delivNumb;
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

    public String getSoNiemXa() {
        return soNiemXa;
    }

    public void setSoNiemXa(String soNiemXa) {
        this.soNiemXa = soNiemXa;
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

    public String getFCreator() {
        return fCreator;
    }

    public void setFCreator(String fCreator) {
        this.fCreator = fCreator;
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

    public Boolean getOfflineMode() {
        return offlineMode;
    }

    public void setOfflineMode(Boolean offlineMode) {
        this.offlineMode = offlineMode;
    }

    public Boolean getTransferedPosting() {
        return transferedPosting;
    }

    public void setTransferedPosting(Boolean transferedPosting) {
        this.transferedPosting = transferedPosting;
    }

    public Boolean getDissolved() {
        return dissolved;
    }

    public void setDissolved(Boolean dissolved) {
        this.dissolved = dissolved;
    }

    public int getPosted() {
        return posted;
    }

//    public void setPosted(Boolean posted) {
//        this.setPosted(posted);
//    }
    public String getMatDoc() {
        return matDoc;
    }

    public void setMatDoc(String matDoc) {
        this.matDoc = matDoc;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (weightTicketPK != null ? weightTicketPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof WeightTicket)) {
            return false;
        }
        WeightTicket other = (WeightTicket) object;
        if ((this.weightTicketPK == null && other.weightTicketPK != null) || (this.weightTicketPK != null && !this.weightTicketPK.equals(other.weightTicketPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.WeightTicket[weightTicketPK=" + weightTicketPK + "]";
    }

    /**
     * @param posted the posted to set
     */
    public void setPosted(int posted) {
        this.posted = posted;
    }

    /**
     * @return the kunnr
     */
    public String getKunnr() {
        return kunnr;
    }

    /**
     * @param kunnr the kunnr to set
     */
    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    /**
     * @return the manual
     */
    public String getManual() {
        return manual;
    }

    /**
     * @param manual the manual to set
     */
    public void setManual(String manual) {
        this.manual = manual;
    }
}
