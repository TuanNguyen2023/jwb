/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt.structure;

import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtCreateConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Administrator
 */
@BapiStructure
public class GoodsMvtWeightTicketStructure implements Serializable {

    @Parameter(GoodsMvtCreateConstants.CLIENT)
    private String client;
    @Parameter(GoodsMvtCreateConstants.WPLANT)
    private String WPLANT;
    @Parameter(GoodsMvtCreateConstants.WB_ID)
    private String WB_ID;
    @Parameter(GoodsMvtCreateConstants.WT_ID)
    private String WT_ID;
    @Parameter(GoodsMvtCreateConstants.PO_NUMBER)
    private String PO_NUMBER;
    @Parameter(GoodsMvtCreateConstants.DO_NUMBER)
    private String DO_NUMBER;
    @Parameter(GoodsMvtCreateConstants.DO_WT)
    private String DO_WT;
    @Parameter(GoodsMvtCreateConstants.MATDOC)
    private String MATDOC;
    @Parameter(GoodsMvtCreateConstants.DOC_YEAR)
    private String DOC_YEAR;
    @Parameter(GoodsMvtCreateConstants.USERNAME)
    private String USERNAME;
    @Parameter(GoodsMvtCreateConstants.CREATE_DATE)
    private Date CREATE_DATE;
    @Parameter(GoodsMvtCreateConstants.CREATE_TIME)
    private Date CREATE_TIME;
    @Parameter(GoodsMvtCreateConstants.FDATE)
    private Date FDATE;
    @Parameter(GoodsMvtCreateConstants.FTIME)
    private Date FTIME;
    @Parameter(GoodsMvtCreateConstants.SDATE)
    private Date SDATE;
    @Parameter(GoodsMvtCreateConstants.STIME)
    private Date STIME;
    @Parameter(GoodsMvtCreateConstants.MVT_TYPE)
    private String MVT_TYPE;
    @Parameter(GoodsMvtCreateConstants.SLOC)
    private String SLOC;
//    @Parameter(GoodsMvtCreateConstants.DO_NUMBER)     private String BATCH = "BATCH";
    @Parameter(GoodsMvtCreateConstants.VTYPE)
    private String VTYPE;
    @Parameter(GoodsMvtCreateConstants.FSCALE)
    private BigDecimal FSCALE;
    @Parameter(GoodsMvtCreateConstants.SSCALE)
    private BigDecimal SSCALE;
    @Parameter(GoodsMvtCreateConstants.REGQTY)
    private BigDecimal REGQTY;
    @Parameter(GoodsMvtCreateConstants.GQTY)
    private BigDecimal GQTY;
    @Parameter(GoodsMvtCreateConstants.REGQTY_WT)
    private BigDecimal REGQTY_WT;
    @Parameter(GoodsMvtCreateConstants.GQTY_WT)
    private BigDecimal GQTY_WT;
    @Parameter(GoodsMvtCreateConstants.UNIT)
    private String UNIT;
    @Parameter(GoodsMvtCreateConstants.TRANSID)
    private String TRANSID;
    @Parameter(GoodsMvtCreateConstants.SHIPTYPE)
    private String SHIPTYPE;
    @Parameter(GoodsMvtCreateConstants.DRIVERN)
    private String DRIVERN;
    @Parameter(GoodsMvtCreateConstants.DRIVERID)
    private String DRIVERID;
    @Parameter(GoodsMvtCreateConstants.SALEDT)
    private String SALEDT;
    @Parameter(GoodsMvtCreateConstants.MATID)
    private String MATID;
    @Parameter(GoodsMvtCreateConstants.MATNAME)
    private String MATNAME;
    @Parameter(GoodsMvtCreateConstants.KUNNR)
    private String KUNNR;
    @Parameter(GoodsMvtCreateConstants.LIFNR)
    private String LIFNR;
    @Parameter(GoodsMvtCreateConstants.CAT_TYPE)
    private String CAT_TYPE;
    @Parameter(GoodsMvtCreateConstants.NTEXT)
    private String NTEXT;
    @Parameter(GoodsMvtCreateConstants.BATCH)
    private String batch;
    @Parameter(GoodsMvtCreateConstants.LFART)
    private String lfart;
    @Parameter(GoodsMvtCreateConstants.STATUS)
    private String status;
    @Parameter(GoodsMvtCreateConstants.WT_ID_REF)
    private String wtIdRef;
    
    public GoodsMvtWeightTicketStructure() {
    }

    public GoodsMvtWeightTicketStructure(String WPLANT, String WB_ID, String WT_ID) {
        this.WPLANT = WPLANT;
        this.WB_ID = WB_ID;
        this.WT_ID = WT_ID;
    }

    
    public String getCAT_TYPE() {
        return CAT_TYPE;
    }

    public void setCAT_TYPE(String CAT_TYPE) {
        this.CAT_TYPE = CAT_TYPE;
    }

   

    public String getDOC_YEAR() {
        return DOC_YEAR;
    }

    public void setDOC_YEAR(String DOC_YEAR) {
        this.DOC_YEAR = DOC_YEAR;
    }

    public String getDO_NUMBER() {
        return DO_NUMBER;
    }

    public void setDO_NUMBER(String DO_NUMBER) {
        this.DO_NUMBER = DO_NUMBER;
    }

    public String getDRIVERID() {
        return DRIVERID;
    }

    public void setDRIVERID(String DRIVERID) {
        this.DRIVERID = DRIVERID;
    }

    public String getDRIVERN() {
        return DRIVERN;
    }

    public void setDRIVERN(String DRIVERN) {
        this.DRIVERN = DRIVERN;
    }

    public Date getFDATE() {
        return FDATE;
    }

    public void setFDATE(Date FDATE) {
        this.FDATE = FDATE;
    }

    public BigDecimal getFSCALE() {
        return FSCALE;
    }

    public void setFSCALE(BigDecimal FSCALE) {
        this.FSCALE = FSCALE;
    }


    public BigDecimal getGQTY() {
        return GQTY;
    }

    public void setGQTY(BigDecimal GQTY) {
        this.GQTY = GQTY;
    }

    public String getKUNNR() {
        return KUNNR;
    }

    public void setKUNNR(String KUNNR) {
        this.KUNNR = KUNNR;
    }

    public String getLIFNR() {
        return LIFNR;
    }

    public void setLIFNR(String LIFNR) {
        this.LIFNR = LIFNR;
    }

    public String getMATDOC() {
        return MATDOC;
    }

    public void setMATDOC(String MATDOC) {
        this.MATDOC = MATDOC;
    }

    public String getMATID() {
        return MATID;
    }

    public void setMATID(String MATID) {
        this.MATID = MATID;
    }

    public String getMATNAME() {
        return MATNAME;
    }

    public void setMATNAME(String MATNAME) {
        this.MATNAME = MATNAME;
    }

    public String getMVT_TYPE() {
        return MVT_TYPE;
    }

    public void setMVT_TYPE(String MVT_TYPE) {
        this.MVT_TYPE = MVT_TYPE;
    }

    public String getNTEXT() {
        return NTEXT;
    }

    public void setNTEXT(String NTEXT) {
        this.NTEXT = NTEXT;
    }

    public String getPO_NUMBER() {
        return PO_NUMBER;
    }

    public void setPO_NUMBER(String PO_NUMBER) {
        this.PO_NUMBER = PO_NUMBER;
    }

    public BigDecimal getREGQTY() {
        return REGQTY;
    }

    public void setREGQTY(BigDecimal REGQTY) {
        this.REGQTY = REGQTY;
    }

    public String getSALEDT() {
        return SALEDT;
    }

    public void setSALEDT(String SALEDT) {
        this.SALEDT = SALEDT;
    }

    public Date getSDATE() {
        return SDATE;
    }

    public void setSDATE(Date SDATE) {
        this.SDATE = SDATE;
    }

    public String getSHIPTYPE() {
        return SHIPTYPE;
    }

    public void setSHIPTYPE(String SHIPTYPE) {
        this.SHIPTYPE = SHIPTYPE;
    }

    public String getSLOC() {
        return SLOC;
    }

    public void setSLOC(String SLOC) {
        this.SLOC = SLOC;
    }

    public BigDecimal getSSCALE() {
        return SSCALE;
    }

    public void setSSCALE(BigDecimal SSCALE) {
        this.SSCALE = SSCALE;
    }

    public Date getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(Date CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public Date getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(Date CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public Date getFTIME() {
        return FTIME;
    }

    public void setFTIME(Date FTIME) {
        this.FTIME = FTIME;
    }

    public Date getSTIME() {
        return STIME;
    }

    public void setSTIME(Date STIME) {
        this.STIME = STIME;
    }


    public String getTRANSID() {
        return TRANSID;
    }

    public void setTRANSID(String TRANSID) {
        this.TRANSID = TRANSID;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getVTYPE() {
        return VTYPE;
    }

    public void setVTYPE(String VTYPE) {
        this.VTYPE = VTYPE;
    }

    public String getWB_ID() {
        return WB_ID;
    }

    public void setWB_ID(String WB_ID) {
        this.WB_ID = WB_ID;
    }

    public String getWPLANT() {
        return WPLANT;
    }

    public void setWPLANT(String WPLANT) {
        this.WPLANT = WPLANT;
    }

    public String getWT_ID() {
        return WT_ID;
    }

    public void setWT_ID(String WT_ID) {
        this.WT_ID = WT_ID;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getLfart() {
        return lfart;
    }

    public void setLfart(String lfart) {
        this.lfart = lfart;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDO_WT() {
        return DO_WT;
    }

    public void setDO_WT(String DO_WT) {
        this.DO_WT = DO_WT;
    }

    public BigDecimal getGQTY_WT() {
        return GQTY_WT;
    }

    public void setGQTY_WT(BigDecimal GQTY_WT) {
        this.GQTY_WT = GQTY_WT;
    }

    public BigDecimal getREGQTY_WT() {
        return REGQTY_WT;
    }

    public void setREGQTY_WT(BigDecimal REGQTY_WT) {
        this.REGQTY_WT = REGQTY_WT;
    }

    public void setwtIdRef(String wtIdRef) {
        this.wtIdRef = wtIdRef;
    }

}
