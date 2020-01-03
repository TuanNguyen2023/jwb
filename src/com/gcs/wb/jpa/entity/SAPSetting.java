/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author vunguyent
 */
@Entity
@Table(name = "SAPSetting")
public class SAPSetting implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SAPSettingPK sAPSettingPK;
    @Column(name = "Name1")
    private String name1;
    @Column(name = "Name2")
    private String name2;
    @Column(name = "NAME_RPT")
    private String nameRpt;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "MATNR_PCB40")
    private String matnrPcb40;
    @Column(name = "MATNR_XMXA")
    private String matnrXmxa;
    @Column(name = "MATNR_CLINKER")
    private String matnrClinker;
    @Column(name = "CHECK_TALP")
    private Boolean checkTalp;
    @Column(name = "BACT1_VAL")
    private BigDecimal bact1Val;
    @Column(name = "BACT1_UNIT")
    private String bact1Unit;
    @Column(name = "BACT2_VAL")
    private BigDecimal bact2Val;
    @Column(name = "BACT2_UNIT")
    private String bact2Unit;
    @Column(name = "BACT3_VAL")
    private BigDecimal bact3Val;
    @Column(name = "BACT3_UNIT")
    private String bact3Unit;
    @Column(name = "BACT4_VAL")
    private BigDecimal bact4Val;
    @Column(name = "BACT4_UNIT")
    private String bact4Unit;
    @Column(name = "ROLE_WM")
    private String roleWm;
    @Column(name = "ROLE_SS")
    private String roleSs;
    @Column(name = "ROLE_AD")
    private String roleAd;
    @Column(name = "ROLE_SP")
    private String roleSp;
    @Column(name = "WB1_TOL")
    private BigDecimal wb1Tol;
    @Column(name = "WB2_TOL")
    private BigDecimal wb2Tol;
    @Column(name = "CHECK_POV")
    private Boolean checkPov;

    public SAPSetting() {
    }

    public SAPSetting(SAPSettingPK sAPSettingPK) {
        this.sAPSettingPK = sAPSettingPK;
    }

    public SAPSetting(String mandt, String wPlant) {
        this.sAPSettingPK = new SAPSettingPK(mandt, wPlant);
    }

    public SAPSettingPK getSAPSettingPK() {
        return sAPSettingPK;
    }

    public void setSAPSettingPK(SAPSettingPK sAPSettingPK) {
        this.sAPSettingPK = sAPSettingPK;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getNameRpt() {
        return nameRpt;
    }

    public void setNameRpt(String nameRpt) {
        this.nameRpt = nameRpt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMatnrPcb40() {
        return matnrPcb40;
    }

    public void setMatnrPcb40(String matnrPcb40) {
        this.matnrPcb40 = matnrPcb40;
    }

    public String getMatnrXmxa() {
        return matnrXmxa;
    }

    public void setMatnrXmxa(String matnrXmxa) {
        this.matnrXmxa = matnrXmxa;
    }

    public String getMatnrClinker() {
        return matnrClinker;
    }

    public void setMatnrClinker(String matnrClinker) {
        this.matnrClinker = matnrClinker;
    }

    public Boolean getCheckTalp() {
        return checkTalp;
    }

    public void setCheckTalp(Boolean checkTalp) {
        this.checkTalp = checkTalp;
    }

    public BigDecimal getBact1Val() {
        return bact1Val;
    }

    public void setBact1Val(BigDecimal bact1Val) {
        this.bact1Val = bact1Val;
    }

    public String getBact1Unit() {
        return bact1Unit;
    }

    public void setBact1Unit(String bact1Unit) {
        this.bact1Unit = bact1Unit;
    }

    public BigDecimal getBact2Val() {
        return bact2Val;
    }

    public void setBact2Val(BigDecimal bact2Val) {
        this.bact2Val = bact2Val;
    }

    public String getBact2Unit() {
        return bact2Unit;
    }

    public void setBact2Unit(String bact2Unit) {
        this.bact2Unit = bact2Unit;
    }

    public BigDecimal getBact3Val() {
        return bact3Val;
    }

    public void setBact3Val(BigDecimal bact3Val) {
        this.bact3Val = bact3Val;
    }

    public String getBact3Unit() {
        return bact3Unit;
    }

    public void setBact3Unit(String bact3Unit) {
        this.bact3Unit = bact3Unit;
    }

    public BigDecimal getBact4Val() {
        return bact4Val;
    }

    public void setBact4Val(BigDecimal bact4Val) {
        this.bact4Val = bact4Val;
    }

    public String getBact4Unit() {
        return bact4Unit;
    }

    public void setBact4Unit(String bact4Unit) {
        this.bact4Unit = bact4Unit;
    }

    public String getRoleWm() {
        return roleWm;
    }

    public void setRoleWm(String roleWm) {
        this.roleWm = roleWm;
    }

    public String getRoleSs() {
        return roleSs;
    }

    public void setRoleSs(String roleSs) {
        this.roleSs = roleSs;
    }

    public String getRoleAd() {
        return roleAd;
    }

    public void setRoleAd(String roleAd) {
        this.roleAd = roleAd;
    }

    public String getRoleSp() {
        return roleSp;
    }

    public void setRoleSp(String roleSp) {
        this.roleSp = roleSp;
    }

    public BigDecimal getWb1Tol() {
        return wb1Tol;
    }

    public void setWb1Tol(BigDecimal wb1Tol) {
        this.wb1Tol = wb1Tol;
    }

    public BigDecimal getWb2Tol() {
        return wb2Tol;
    }

    public void setWb2Tol(BigDecimal wb2Tol) {
        this.wb2Tol = wb2Tol;
    }
    
    public Boolean getCheckPov() {
        return checkPov;
    }

    public void setCheckPov(Boolean checkPov) {
        this.checkPov = checkPov;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sAPSettingPK != null ? sAPSettingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof SAPSetting)) {
            return false;
        }
        SAPSetting other = (SAPSetting) object;
        if ((this.sAPSettingPK == null && other.sAPSettingPK != null) || (this.sAPSettingPK != null && !this.sAPSettingPK.equals(other.sAPSettingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.SAPSetting[sAPSettingPK=" + sAPSettingPK + "]";
    }
}
