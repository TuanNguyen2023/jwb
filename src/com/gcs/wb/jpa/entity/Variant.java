/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author gcsadmin
 */
@Entity
@Table(name = "variant")
@NamedQueries({
    @NamedQuery(name = "Variant.findByFullParam", query = "SELECT v FROM Variant v WHERE v.variantPK.param = :param AND v.variantPK.mandt = :mandt AND v.variantPK.wPlant = :wPlant")})
public class Variant implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VariantPK variantPK;
    @Lob
    @Column(name = "VALUE")
    private String value;
    @Column(name = "VALUE1")
    private String value1;

    public Variant() {
    }

    public Variant(VariantPK variantPK) {
        this.variantPK = variantPK;
    }

    public Variant(String mandt, String wPlant, String param) {
        this.variantPK = new VariantPK(mandt, wPlant, param);
    }

    public VariantPK getVariantPK() {
        return variantPK;
    }

    public void setVariantPK(VariantPK variantPK) {
        this.variantPK = variantPK;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (variantPK != null ? variantPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Variant)) {
            return false;
        }
        Variant other = (Variant) object;
        if ((this.variantPK == null && other.variantPK != null) || (this.variantPK != null && !this.variantPK.equals(other.variantPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Variant[variantPK=" + variantPK + "]";
    }

    /**
     * @return the value1
     */
    public String getValue1() {
        return value1;
    }

    /**
     * @param value1 the value1 to set
     */
    public void setValue1(String value1) {
        this.value1 = value1;
    }
}
