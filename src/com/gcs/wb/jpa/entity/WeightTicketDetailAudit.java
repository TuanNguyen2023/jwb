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
@Table(name = "tbl_weight_ticket_detail_audit")
@NamedQueries({
    @NamedQuery(name = "WeightTicketDetailAudit.findAll", query = "SELECT au FROM WeightTicketDetailAudit au")
})
public class WeightTicketDetailAudit implements Serializable {

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
    @Column(name = "old_material")
    private String oldMaterial;
    @Column(name = "new_material")
    private String newMaterial;
    @Column(name = "old_sold_to")
    private String oldSoldTo;
    @Column(name = "new_sold_to")
    private String newSoldTo;
    @Column(name = "old_ship_to")
    private String oldShipTo;
    @Column(name = "new_ship_to")
    private String newShipTo;

    public WeightTicketDetailAudit() {
    }

    public WeightTicketDetailAudit(int id) {
        this.id = id;
    }

    public WeightTicketDetailAudit(String mandt, String wplant, String wtId, String author) {
        this.mandt = mandt;
        this.wplant = wplant;
        this.wtId = wtId;
        this.author = author;
    }

    public WeightTicketDetailAudit(String wtId, String author) {
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

    public String getOldMaterial() {
        return oldMaterial;
    }

    public void setOldMaterial(String oldMaterial) {
        this.oldMaterial = oldMaterial;
    }

    public String getNewMaterial() {
        return newMaterial;
    }

    public void setNewMaterial(String newMaterial) {
        this.newMaterial = newMaterial;
    }

    public String getOldSoldTo() {
        return oldSoldTo;
    }

    public void setOldSoldTo(String oldSoldTo) {
        this.oldSoldTo = oldSoldTo;
    }

    public String getNewSoldTo() {
        return newSoldTo;
    }

    public void setNewSoldTo(String newSoldTo) {
        this.newSoldTo = newSoldTo;
    }

    public String getOldShipTo() {
        return oldShipTo;
    }

    public void setOldShipTo(String oldShipTo) {
        this.oldShipTo = oldShipTo;
    }

    public String getNewShipTo() {
        return newShipTo;
    }

    public void setNewShipTo(String newShipTo) {
        this.newShipTo = newShipTo;
    }

}
