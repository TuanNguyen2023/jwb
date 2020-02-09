/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
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
@Table(name = "tbl_batch_stock")
@NamedQueries({
    @NamedQuery(name = "BatchStock.findAll", query = "SELECT bs FROM BatchStock bs"),
    @NamedQuery(name = "BatchStock.findByWerksLgortMatnr",
    query = "SELECT bs FROM BatchStock bs WHERE bs.werks = :werks AND bs.lgort = :lgort AND bs.matnr = :matnr"),
    @NamedQuery(name = "BatchStock.findByWerksLgortMatnrCharg",
    query = "SELECT bs FROM BatchStock bs WHERE bs.werks = :werks AND bs.lgort = :lgort AND bs.matnr = :matnr AND bs.charg = :charg")
})
public class BatchStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "mandt", unique = true)
    private String mandt;
    @Column(name = "werks", unique = true)
    private String werks;
    @Column(name = "lgort", unique = true)
    private String lgort;
    @Column(name = "matnr", unique = true)
    private String matnr;
    @Column(name = "charg", unique = true)
    private String charg;
    @Column(name = "lvorm")
    private Character lvorm;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;

    public BatchStock() {
    }

    public BatchStock(String mandt, String werks, String lgort, String matnr, String charg) {
        this.mandt = mandt;
        this.werks = werks;
        this.lgort = lgort;
        this.matnr = matnr;
        this.charg = charg;
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

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getLgort() {
        return lgort;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getCharg() {
        return charg;
    }

    public void setCharg(String charg) {
        this.charg = charg;
    }

    public Character getLvorm() {
        return lvorm;
    }

    public void setLvorm(Character lvorm) {
        this.lvorm = lvorm;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BatchStock that = (BatchStock) o;
        
        if (werks != null ? !werks.equals(that.werks) : that.werks != null) return false;
        if (lgort != null ? !lgort.equals(that.lgort) : that.lgort != null) return false;
        if (matnr != null ? !matnr.equals(that.matnr) : that.matnr != null) return false;
        if (charg != null ? !charg.equals(that.charg) : that.charg != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (werks != null ? werks.hashCode() : 0);
        result = 31 * result + (lgort != null ? lgort.hashCode() : 0);
        result = 31 * result + (matnr != null ? matnr.hashCode() : 0);
        result = 31 * result + (charg != null ? charg.hashCode() : 0);
        result = 31 * result + (lvorm != null ? lvorm.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.BatchStocks[id=" + id + "]";
    }
}
