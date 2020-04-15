/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static final String SYNC_UNKNOWN_STATUS = "UNKNOWN";
    public static final String SYNC_COMPLETED = "COMPLETED";
    public static final String SYNC_ERROR = "ERROR";
    public static final String SYNC_IN_PROGRESS = "INPROGRESS";
    public static final int AN_HOUR = 3600000;
    
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
    @Column(name = "auto_sync_status")
    private String autoSyncStatus;
    @Column(name = "last_manual_sync")
    private Date lastManualSync;
    @Column(name = "manual_sync_status")
    private String manualSyncStatus;
            
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
    
    public String getAutoSyncStatus() {
        return autoSyncStatus;
    }

    public void setAutoSyncStatus(String autoSyncStatus) {
        this.autoSyncStatus = autoSyncStatus;
    }
    
    public String getManualSyncStatus() {
        return manualSyncStatus;
    }

    public void setManualSyncStatus(String manualSyncStatus) {
        this.manualSyncStatus = manualSyncStatus;
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
    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }
    public boolean isAutoSyncAllowed() {
        if (lastAutoSync != null) {
            if (isSameDay(lastAutoSync, new Date()) && (SYNC_COMPLETED.equals(autoSyncStatus) || SYNC_IN_PROGRESS.equals(autoSyncStatus))) {
                return false;
            } else {
                return true;
            }
        } 
        
        // Allow to sync in case lastAutoSync not available
        return true;
    }
    
    public boolean isManualSyncAllowed() {
        if (lastManualSync != null) {
            long syncTime = System.currentTimeMillis() - lastManualSync.getTime();
            if (SYNC_IN_PROGRESS.equals(manualSyncStatus) && syncTime < AN_HOUR * 2 && syncTime > 0) {
                return false;
            } else {
                return true;
            }
        }

        // Allow to sync in case lastManualSync not available
        return true;
    }
}
