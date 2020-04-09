/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.util.Date;
import java.time.LocalDate;
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
 * @author TaiTQ
 */
@Entity
@Table(name = "tbl_scheduler_sync")
@NamedQueries({
    @NamedQuery(name = "SchedulerSync.findAll", query = "SELECT s FROM SchedulerSync s"),
    @NamedQuery(name = "SchedulerSync.findByMandtWplant", query = "SELECT s FROM SchedulerSync s WHERE s.mandt = :mandt AND s.wplant = :wplant"),
    })
public class SchedulerSync implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "mandt", unique = true)
    private String mandt;
    @Column(name = "wplant", unique = true)
    private String wplant;
    @Column(name = "last_auto_sync")
    private Date lastAutoSync;
    @Column(name = "last_manual_sync")
    private Date lastManualSync;

    public SchedulerSync() {
    }
    
    public SchedulerSync(int id) {
        this.id = id;
    }
    
    public SchedulerSync(String mandt, String wplant) {
        this.mandt = mandt;
        this.wplant = wplant;
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

    public String getWplant() {
        return wplant;
    }

    public void setWplant(String wplant) {
        this.wplant = wplant;
    }

    public Date getLastAutoSync() {
        return lastAutoSync;
    }

    public void setLastAutoSync(Date lastAutoSync) {
        this.lastAutoSync = lastAutoSync;
    }

    public Date getLastManualSync() {
        return lastManualSync;
    }

    public void setLastManualSync(Date lastManualSync) {
        this.lastManualSync = lastManualSync;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SchedulerSync schedulerSync = (SchedulerSync) o;


        if (mandt != null ? !mandt.equals(schedulerSync.mandt) : schedulerSync.mandt != null) return false;
        if (wplant != null ? !wplant.equals(schedulerSync.wplant) : schedulerSync.wplant != null) return false;
        if (lastAutoSync != null ? !lastAutoSync.equals(schedulerSync.lastAutoSync) : schedulerSync.lastAutoSync != null) return false;
        if (lastManualSync != null ? !lastManualSync.equals(schedulerSync.lastManualSync) : schedulerSync.lastManualSync != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (wplant != null ? wplant.hashCode() : 0);
        result = 31 * result + (lastAutoSync != null ? lastAutoSync.hashCode() : 0);
        result = 31 * result + (lastManualSync != null ? lastManualSync.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.SchedulerSync[id=" + id + "]";
    }
    
    public boolean isAutoSyncAllowed() {
        if (lastAutoSync != null) {
            LocalDate now = java.time.LocalDate.now();
            if (lastAutoSync.getDate() == now.getDayOfMonth() 
                    && lastAutoSync.getMonth() == now.getMonthValue()
                    && lastAutoSync.getYear() == now.getYear()) {
                return true;
            } else {
                return false;
            }     
        } 
        
        // Allow to sync in case lastAutoSync not available
        return true;
    }
    
        public boolean isManualSyncAllowed() {
        if (lastManualSync != null) {
            if (System.currentTimeMillis() - lastManualSync.getTime() >= 3600000/120 ) {
                return true;
            } else {
                return false;
            }     
        } 
        
        // Allow to sync in case lastManualSync not available
        return true;
    }
}
