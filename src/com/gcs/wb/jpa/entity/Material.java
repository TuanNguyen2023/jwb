/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "material")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Material.findAll", query = "SELECT m FROM Material m"),
    @NamedQuery(name = "Material.findByMandt", query = "SELECT m FROM Material m WHERE m.materialPK.mandt = :mandt"),
    @NamedQuery(name = "Material.findByWplant", query = "SELECT m FROM Material m WHERE m.materialPK.wplant = :wplant"),
    @NamedQuery(name = "Material.findByMatnr", query = "SELECT m FROM Material m WHERE m.materialPK.matnr = :matnr"),
    @NamedQuery(name = "Material.findByMaktx", query = "SELECT m FROM Material m WHERE m.maktx = :maktx"),
    @NamedQuery(name = "Material.findByMaktg", query = "SELECT m FROM Material m WHERE m.maktg = :maktg"),
    @NamedQuery(name = "Material.findByXchpf", query = "SELECT m FROM Material m WHERE m.xchpf = :xchpf"),
    @NamedQuery(name = "Material.findByCheckPosto", query = "SELECT m FROM Material m WHERE m.checkPosto = :checkPosto"),
    @NamedQuery(name = "Material.CheckPOSTO", query = "SELECT m FROM Material m WHERE m.materialPK.mandt = :mandt AND m.materialPK.wplant = :wplant AND m.materialPK.matnr LIKE :matnr")
})
public class Material implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MaterialPK materialPK;
//    @Column(name = "WPLANT")
//    private String wplant;
    @Column(name = "MAKTX")
    private String maktx;
    @Column(name = "MAKTG")
    private String maktg;
    @Column(name = "XCHPF")
    private Character xchpf;
    @Column(name = "CHECK_POSTO")
    private String checkPosto;
    @Column(name = "Ximang")
    private String ximang;
    @Column(name = "Bag")
    private Boolean bag;
        
    public Material() {
    }

    public Material(MaterialPK materialPK) {
        this.materialPK = materialPK;
    }

    public Material(String mandt, String wplant, String matnr) {
        this.materialPK = new MaterialPK(mandt, wplant, matnr);
    }
    public Material(String mandt, String wplant) {
        this.materialPK = new MaterialPK(mandt, wplant);
    }

    public MaterialPK getMaterialPK() {
        return materialPK;
    }

    public void setMaterialPK(MaterialPK materialPK) {
        this.materialPK = materialPK;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getMaktg() {
        return maktg;
    }

    public void setMaktg(String maktg) {
        this.maktg = maktg;
    }

    public Character getXchpf() {
        return xchpf;
    }

    public void setXchpf(Character xchpf) {
        this.xchpf = xchpf;
    }

    public String getCheckPosto() {
        return checkPosto;
    }

    public void setCheckPosto(String checkPosto) {
        this.checkPosto = checkPosto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (materialPK != null ? materialPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Material)) {
            return false;
        }
        Material other = (Material) object;
        if ((this.materialPK == null && other.materialPK != null) || (this.materialPK != null && !this.materialPK.equals(other.materialPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Material[materialPK=" + materialPK + "]";
    }

    /**
     * @return the wplant
     */
//    public String getWplant() {
//        return wplant;
//    }

    /**
     * @param wplant the wplant to set
     */
//    public void setWplant(String wplant) {
//        this.wplant = wplant;
//    }

    /**
     * @return the ximang
     */
    public String getXimang() {
        return ximang;
    }

    /**
     * @param ximang the ximang to set
     */
    public void setXimang(String ximang) {
        this.ximang = ximang;
    }

    /**
     * @return the bag
     */
    public Boolean getBag() {
        return bag;
    }

    /**
     * @param bag the bag to set
     */
    public void setBag(Boolean bag) {
        this.bag = bag;
    }
   
}
