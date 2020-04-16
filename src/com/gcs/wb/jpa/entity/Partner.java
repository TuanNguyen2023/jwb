/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_partner")
@NamedQueries({
    @NamedQuery(name = "Partner.findAll", query = "SELECT p FROM Partner p"),
    @NamedQuery(name = "Partner.findByMandt", query = "SELECT p FROM Partner p WHERE p.mandt = :mandt"),
    @NamedQuery(name = "Partner.findByKunnr", query = "SELECT p FROM Partner p WHERE p.kunnr = :kunnr"),
    @NamedQuery(name = "Partner.findByMandtKunnr", query = "SELECT p FROM Partner p WHERE p.mandt = :mandt AND p.kunnr = :kunnr"),
    @NamedQuery(name = "Partner.findByMandtKunnrKunn2", query = "SELECT p FROM Partner p WHERE p.mandt = :mandt AND p.kunnr = :kunnr AND p.kunn2 = :kunn2"),
    @NamedQuery(name = "Partner.findPartner",
            query = "SELECT p FROM Partner p"
            + " WHERE p.mandt = :mandt"
            + " AND p.kunnr = :kunnr"
            + " AND p.vkorg = :vkorg"
            + " AND p.vtweg = :vtweg"
            + " AND p.spart = :spart"
            + " AND p.parvw = :parvw"
            + " AND p.parza = :parza"
    )
})
public class Partner implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "mandt", unique = true)
    private String mandt;
    @Column(name = "kunnr", unique = true)
    private String kunnr;
    @Column(name = "vkorg", unique = true)
    private String vkorg;
    @Column(name = "vtweg", unique = true)
    private String vtweg;
    @Column(name = "spart", unique = true)
    private String spart;
    @Column(name = "parvw", unique = true)
    private String parvw;
    @Column(name = "parza", unique = true)
    private String parza;
    @Column(name = "kunn2")
    private String kunn2;
    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;
    @Column(name = "updated_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;

    public Partner() {
    }

    public Partner(String kunnr) {
        this.kunnr = kunnr;
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

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getVtweg() {
        return vtweg;
    }

    public void setVtweg(String vtweg) {
        this.vtweg = vtweg;
    }

    public String getSpart() {
        return spart;
    }

    public void setSpart(String spart) {
        this.spart = spart;
    }

    public String getParvw() {
        return parvw;
    }

    public void setParvw(String parvw) {
        this.parvw = parvw;
    }

    public String getParza() {
        return parza;
    }

    public void setParza(String parza) {
        this.parza = parza;
    }

    public String getKunn2() {
        return kunn2;
    }

    public void setKunn2(String kunn2) {
        this.kunn2 = kunn2;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Partner partner = (Partner) o;

        if (kunnr != null ? !kunnr.equals(partner.kunnr) : partner.kunnr != null) {
            return false;
        }
        if (vkorg != null ? !vkorg.equals(partner.vkorg) : partner.vkorg != null) {
            return false;
        }
        if (vtweg != null ? !vtweg.equals(partner.vtweg) : partner.vtweg != null) {
            return false;
        }
        if (spart != null ? !spart.equals(partner.spart) : partner.spart != null) {
            return false;
        }
        if (parvw != null ? !parvw.equals(partner.parvw) : partner.parvw != null) {
            return false;
        }
        if (parza != null ? !parza.equals(partner.parza) : partner.parza != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + this.id;
        hash = 11 * hash + Objects.hashCode(this.mandt);
        hash = 11 * hash + Objects.hashCode(this.kunnr);
        hash = 11 * hash + Objects.hashCode(this.vkorg);
        hash = 11 * hash + Objects.hashCode(this.vtweg);
        hash = 11 * hash + Objects.hashCode(this.spart);
        hash = 11 * hash + Objects.hashCode(this.parvw);
        hash = 11 * hash + Objects.hashCode(this.parza);
        hash = 11 * hash + Objects.hashCode(this.kunn2);
        hash = 11 * hash + Objects.hashCode(this.createdDate);
        hash = 11 * hash + Objects.hashCode(this.updatedDate);
        return hash;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Partner[id=" + id + "]";
    }
}
