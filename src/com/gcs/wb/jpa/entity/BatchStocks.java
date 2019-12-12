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

/**
 *
 * @author vunguyent
 */
@Entity
@Table(name = "BatchStocks")
@NamedQueries({
    @NamedQuery(name = "BatchStocks.findAll", query = "SELECT b FROM BatchStocks b"),
    @NamedQuery(name = "BatchStocks.findByMandt", query = "SELECT b FROM BatchStocks b WHERE b.batchStocksPK.mandt = :mandt"),
    @NamedQuery(name = "BatchStocks.findByWerks", query = "SELECT b FROM BatchStocks b WHERE b.batchStocksPK.werks = :werks"),
    @NamedQuery(name = "BatchStocks.findByLgort", query = "SELECT b FROM BatchStocks b WHERE b.batchStocksPK.lgort = :lgort"),
    @NamedQuery(name = "BatchStocks.findByMatnr", query = "SELECT b FROM BatchStocks b WHERE b.batchStocksPK.matnr = :matnr"),
    @NamedQuery(name = "BatchStocks.findByCharg", query = "SELECT b FROM BatchStocks b WHERE b.batchStocksPK.charg = :charg"),
    @NamedQuery(name = "BatchStocks.findByLvorm", query = "SELECT b FROM BatchStocks b WHERE b.lvorm = :lvorm")})
public class BatchStocks implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BatchStocksPK batchStocksPK;
    @Column(name = "LVORM")
    private Character lvorm;

    public BatchStocks() {
    }

    public BatchStocks(BatchStocksPK batchStocksPK) {
        this.batchStocksPK = batchStocksPK;
    }

    public BatchStocks(String mandt, String werks, String lgort, String matnr, String charg) {
        this.batchStocksPK = new BatchStocksPK(mandt, werks, lgort, matnr, charg);
    }

    public BatchStocksPK getBatchStocksPK() {
        return batchStocksPK;
    }

    public void setBatchStocksPK(BatchStocksPK batchStocksPK) {
        this.batchStocksPK = batchStocksPK;
    }

    public Character getLvorm() {
        return lvorm;
    }

    public void setLvorm(Character lvorm) {
        this.lvorm = lvorm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (batchStocksPK != null ? batchStocksPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof BatchStocks)) {
            return false;
        }
        BatchStocks other = (BatchStocks) object;
        if ((this.batchStocksPK == null && other.batchStocksPK != null) || (this.batchStocksPK != null && !this.batchStocksPK.equals(other.batchStocksPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.BatchStocks[batchStocksPK=" + batchStocksPK + "]";
    }
}
