/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;
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
@Table(name = "tbl_vehicle")
@NamedQueries({
    @NamedQuery(name = "Vehicle.findByPlateNo", query = "SELECT v FROM Vehicle v WHERE v.plateNo = :plateNo")})
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "plate_no", unique = true)
    private String plateNo;
    @Column(name = "type")
    private int type;
    @Column(name = "weight")
    private float weight;
    @Column(name = "valid_from")
    private Date validFrom;
    @Column(name = "valid_to")
    private Date validTo;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;

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

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
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

    public boolean isProhibit() {
        //return Constants.Vehicle.STATUS_INACTIVED.equals(this.status);
        Date dateNow = new Date(Calendar.getInstance().getTime().getTime());
        return !(dateNow.before(this.validTo) && dateNow.after(this.validFrom));
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
