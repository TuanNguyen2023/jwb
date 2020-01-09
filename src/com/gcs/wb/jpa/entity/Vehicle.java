/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_vehicle")
@NamedQueries({
    @NamedQuery(name = "Vehicle.findByAbbr",
    query = "SELECT v FROM Vehicle v"
    + " LEFT JOIN TransportAgentVehicle tv ON tv.vehicleId = v.id"
    + " LEFT JOIN TransportAgent t ON t.id = tv.transportAgentId WHERE t.abbr = :abbr")
})
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String STATUS_ACTIVED = "ACTIVED";
    private static final String STATUS_INACTIVED = "INACTIVED";
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Column(name = "plate_no", unique = true)
    private String plateNo;
    @Column(name = "type")
    private int type;
    @Column(name = "weight")
    private float weight;
    @Column(name = "status")
    private String status;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "deleted_date")
    private Date deletedDate;

    public Vehicle() {
    }

    public Vehicle(String plateNo) {
        this.plateNo = plateNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date created_date) {
        this.createdDate = created_date;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updated_date) {
        this.updatedDate = updated_date;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deleted_date) {
        this.deletedDate = deleted_date;
    }

    public boolean isProhibit() {
        return STATUS_ACTIVED.equals(this.status);
    }

    public void setProhibit(boolean isProhibit) {
        this.status = isProhibit ? STATUS_INACTIVED : STATUS_ACTIVED;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plateNo != null ? plateNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vehicle)) {
            return false;
        }
        Vehicle other = (Vehicle) object;
        if ((this.plateNo == null && other.plateNo != null) || (this.plateNo != null && !this.plateNo.equals(other.plateNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Vehicle[plateNo=" + plateNo + "]";
    }
}
