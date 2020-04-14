/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_weight_ticket_audit")
@NamedQueries({
    @NamedQuery(name = "WeightTicketAudit.findAll", query = "SELECT au FROM WeightTicketAudit au")
})
public class WeightTicketAudit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "mandt")
    private String mandt;
    @Column(name = "wplant")
    private String wplant;
    @Column(name = "wt_id")
    private String wtId;
    @Column(name = "author")
    private String author;
    @Column(name = "overwritten_time")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date overwrittenTime;
    @Column(name = "old_vehicle_no")
    private String oldVehicleNo;
    @Column(name = "new_vehicle_no")
    private String newVehicleNo;

    public WeightTicketAudit() {
    }

    public WeightTicketAudit(int id) {
        this.id = id;
    }

    public WeightTicketAudit(String mandt, String wplant, String wtId, String author) {
        this.mandt = mandt;
        this.wplant = wplant;
        this.wtId = wtId;
        this.author = author;
    }

    public WeightTicketAudit(String wtId, String author) {
        this.wtId = wtId;
        this.author = author;
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

    public String getWtId() {
        return wtId;
    }

    public void setWtId(String wtId) {
        this.wtId = wtId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getOverwrittenTime() {
        return overwrittenTime;
    }

    public void setOverwrittenTime(Date overwrittenTime) {
        this.overwrittenTime = overwrittenTime;
    }

    public String getOldVehicleNo() {
        return oldVehicleNo;
    }

    public void setOldVehicleNo(String oldVehicleNo) {
        this.oldVehicleNo = oldVehicleNo;
    }

    public String getNewVehicleNo() {
        return newVehicleNo;
    }

    public void setNewVehicleNo(String newVehicleNo) {
        this.newVehicleNo = newVehicleNo;
    }
}
