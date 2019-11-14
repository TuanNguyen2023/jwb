/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author vunguyent
 */
@Entity
@Table(name = "Unit", catalog = "jWeighBridge", schema = "")
@NamedQueries({
    @NamedQuery(name = "Unit.findAll", query = "SELECT u FROM Unit u"),
    @NamedQuery(name = "Unit.findByMandt", query = "SELECT u FROM Unit u WHERE u.unitPK.mandt = :mandt"),
    @NamedQuery(name = "Unit.findByMsehi", query = "SELECT u FROM Unit u WHERE u.unitPK.msehi = :msehi"),
    @NamedQuery(name = "Unit.findBySpras", query = "SELECT u FROM Unit u WHERE u.spras = :spras"),
    @NamedQuery(name = "Unit.findByAndec", query = "SELECT u FROM Unit u WHERE u.andec = :andec"),
    @NamedQuery(name = "Unit.findByDimid", query = "SELECT u FROM Unit u WHERE u.dimid = :dimid"),
    @NamedQuery(name = "Unit.findByZaehl", query = "SELECT u FROM Unit u WHERE u.zaehl = :zaehl"),
    @NamedQuery(name = "Unit.findByNennr", query = "SELECT u FROM Unit u WHERE u.nennr = :nennr"),
    @NamedQuery(name = "Unit.findByExp10", query = "SELECT u FROM Unit u WHERE u.exp10 = :exp10"),
    @NamedQuery(name = "Unit.findByAddko", query = "SELECT u FROM Unit u WHERE u.addko = :addko"),
    @NamedQuery(name = "Unit.findByDecan", query = "SELECT u FROM Unit u WHERE u.decan = :decan"),
    @NamedQuery(name = "Unit.findByIsocode", query = "SELECT u FROM Unit u WHERE u.isocode = :isocode"),
    @NamedQuery(name = "Unit.findByPrimaryUnit", query = "SELECT u FROM Unit u WHERE u.primaryUnit = :primaryUnit"),
    @NamedQuery(name = "Unit.findByMseh3", query = "SELECT u FROM Unit u WHERE u.mseh3 = :mseh3"),
    @NamedQuery(name = "Unit.findByMseht", query = "SELECT u FROM Unit u WHERE u.mseht = :mseht")})
public class Unit implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UnitPK unitPK;
    @Basic(optional = false)
    @Column(name = "SPRAS")
    private String spras;
    @Column(name = "ANDEC")
    private Integer andec;
    @Column(name = "DIMID")
    private String dimid;
    @Column(name = "ZAEHL")
    private Long zaehl;
    @Column(name = "NENNR")
    private Long nennr;
    @Column(name = "EXP10")
    private Integer exp10;
    @Column(name = "ADDKO")
    private BigDecimal addko;
    @Column(name = "DECAN")
    private Integer decan;
    @Column(name = "ISOCODE")
    private String isocode;
    @Column(name = "PRIMARY_UNIT")
    private String primaryUnit;
    @Column(name = "MSEH3")
    private String mseh3;
    @Column(name = "MSEHT")
    private String mseht;

    public Unit() {
    }

    public Unit(UnitPK unitPK) {
        this.unitPK = unitPK;
    }

    public Unit(UnitPK unitPK, String spras) {
        this.unitPK = unitPK;
        this.spras = spras;
    }

    public Unit(String mandt, String msehi) {
        this.unitPK = new UnitPK(mandt, msehi);
    }

    public UnitPK getUnitPK() {
        return unitPK;
    }

    public void setUnitPK(UnitPK unitPK) {
        this.unitPK = unitPK;
    }

    public String getSpras() {
        return spras;
    }

    public void setSpras(String spras) {
        this.spras = spras;
    }

    public Integer getAndec() {
        return andec;
    }

    public void setAndec(Integer andec) {
        this.andec = andec;
    }

    public String getDimid() {
        return dimid;
    }

    public void setDimid(String dimid) {
        this.dimid = dimid;
    }

    public Long getZaehl() {
        return zaehl;
    }

    public void setZaehl(Long zaehl) {
        this.zaehl = zaehl;
    }

    public Long getNennr() {
        return nennr;
    }

    public void setNennr(Long nennr) {
        this.nennr = nennr;
    }

    public Integer getExp10() {
        return exp10;
    }

    public void setExp10(Integer exp10) {
        this.exp10 = exp10;
    }

    public BigDecimal getAddko() {
        return addko;
    }

    public void setAddko(BigDecimal addko) {
        this.addko = addko;
    }

    public Integer getDecan() {
        return decan;
    }

    public void setDecan(Integer decan) {
        this.decan = decan;
    }

    public String getIsocode() {
        return isocode;
    }

    public void setIsocode(String isocode) {
        this.isocode = isocode;
    }

    public String getPrimaryUnit() {
        return primaryUnit;
    }

    public void setPrimaryUnit(String primaryUnit) {
        this.primaryUnit = primaryUnit;
    }

    public String getMseh3() {
        return mseh3;
    }

    public void setMseh3(String mseh3) {
        this.mseh3 = mseh3;
    }

    public String getMseht() {
        return mseht;
    }

    public void setMseht(String mseht) {
        this.mseht = mseht;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unitPK != null ? unitPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Unit)) {
            return false;
        }
        Unit other = (Unit) object;
        if ((this.unitPK == null && other.unitPK != null) || (this.unitPK != null && !this.unitPK.equals(other.unitPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Unit[unitPK=" + unitPK + "]";
    }
}
