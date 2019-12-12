/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
@Table(name = "DocFlow")
@NamedQueries({
    @NamedQuery(name = "DocFlow.findAll", query = "SELECT d FROM DocFlow d"),
    @NamedQuery(name = "DocFlow.findByMandt", query = "SELECT d FROM DocFlow d WHERE d.docFlowPK.mandt = :mandt"),
    @NamedQuery(name = "DocFlow.findByVbelv", query = "SELECT d FROM DocFlow d WHERE d.docFlowPK.vbelv = :vbelv"),
    @NamedQuery(name = "DocFlow.findByVbeln", query = "SELECT d FROM DocFlow d WHERE d.docFlowPK.vbeln = :vbeln"),
    @NamedQuery(name = "DocFlow.findByVbtypV", query = "SELECT d FROM DocFlow d WHERE d.vbtypV = :vbtypV"),
    @NamedQuery(name = "DocFlow.findByVbtypN", query = "SELECT d FROM DocFlow d WHERE d.vbtypN = :vbtypN"),
    @NamedQuery(name = "DocFlow.findByMjahr", query = "SELECT d FROM DocFlow d WHERE d.mjahr = :mjahr"),
    @NamedQuery(name = "DocFlow.findByRfmng", query = "SELECT d FROM DocFlow d WHERE d.rfmng = :rfmng"),
    @NamedQuery(name = "DocFlow.findByMeins", query = "SELECT d FROM DocFlow d WHERE d.meins = :meins"),
    @NamedQuery(name = "DocFlow.findByPlmin", query = "SELECT d FROM DocFlow d WHERE d.plmin = :plmin"),
    @NamedQuery(name = "DocFlow.findByErdat", query = "SELECT d FROM DocFlow d WHERE d.erdat = :erdat"),
    @NamedQuery(name = "DocFlow.findByErzet", query = "SELECT d FROM DocFlow d WHERE d.erzet = :erzet"),
    @NamedQuery(name = "DocFlow.findByMatnr", query = "SELECT d FROM DocFlow d WHERE d.matnr = :matnr"),
    @NamedQuery(name = "DocFlow.findByBwart", query = "SELECT d FROM DocFlow d WHERE d.bwart = :bwart")})
public class DocFlow implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocFlowPK docFlowPK;
    @Column(name = "VBTYP_V")
    private Character vbtypV;
    @Column(name = "VBTYP_N")
    private Character vbtypN;
    @Column(name = "MJAHR")
    @Temporal(TemporalType.DATE)
    private Date mjahr;
    @Column(name = "RFMNG")
    private BigDecimal rfmng;
    @Column(name = "MEINS")
    private String meins;
    @Column(name = "PLMIN")
    private Character plmin;
    @Column(name = "ERDAT")
    @Temporal(TemporalType.DATE)
    private Date erdat;
    @Column(name = "ERZET")
    @Temporal(TemporalType.TIME)
    private Date erzet;
    @Column(name = "MATNR")
    private String matnr;
    @Column(name = "BWART")
    private String bwart;

    public DocFlow() {
    }

    public DocFlow(DocFlowPK docFlowPK) {
        this.docFlowPK = docFlowPK;
    }

    public DocFlow(String mandt, String vbelv, String vbeln) {
        this.docFlowPK = new DocFlowPK(mandt, vbelv, vbeln);
    }

    public DocFlowPK getDocFlowPK() {
        return docFlowPK;
    }

    public void setDocFlowPK(DocFlowPK docFlowPK) {
        this.docFlowPK = docFlowPK;
    }

    public Character getVbtypV() {
        return vbtypV;
    }

    public void setVbtypV(Character vbtypV) {
        this.vbtypV = vbtypV;
    }

    public Character getVbtypN() {
        return vbtypN;
    }

    public void setVbtypN(Character vbtypN) {
        this.vbtypN = vbtypN;
    }

    public Date getMjahr() {
        return mjahr;
    }

    public void setMjahr(Date mjahr) {
        this.mjahr = mjahr;
    }

    public BigDecimal getRfmng() {
        return rfmng;
    }

    public void setRfmng(BigDecimal rfmng) {
        this.rfmng = rfmng;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public Character getPlmin() {
        return plmin;
    }

    public void setPlmin(Character plmin) {
        this.plmin = plmin;
    }

    public Date getErdat() {
        return erdat;
    }

    public void setErdat(Date erdat) {
        this.erdat = erdat;
    }

    public Date getErzet() {
        return erzet;
    }

    public void setErzet(Date erzet) {
        this.erzet = erzet;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getBwart() {
        return bwart;
    }

    public void setBwart(String bwart) {
        this.bwart = bwart;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (docFlowPK != null ? docFlowPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof DocFlow)) {
            return false;
        }
        DocFlow other = (DocFlow) object;
        if ((this.docFlowPK == null && other.docFlowPK != null) || (this.docFlowPK != null && !this.docFlowPK.equals(other.docFlowPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.DocFlow[docFlowPK=" + docFlowPK + "]";
    }
}
