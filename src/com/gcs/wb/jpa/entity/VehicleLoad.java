/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
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
 * @author thangtp.nr
 */
@Entity
@Table(name = "tbl_vehicle_load")
@NamedQueries({
    @NamedQuery(name = "VehicleLoad.findByPlateNo", query = "SELECT v FROM VehicleLoad v WHERE v.plateNo = :plateNo")})
public class VehicleLoad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "plate_no", unique = true)
    private String plateNo;
    @Column(name = "vehicle_load")
    private Float vehicleLoad;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;

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

    public Float getVehicleLoad() {
        return vehicleLoad;
    }

    public void setVehicleLoad(Float vehicleLoad) {
        this.vehicleLoad = vehicleLoad;
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
    public int hashCode() {
        int hash = (int) (id ^ (id >>> 31));
        hash = 32 * hash + this.id;
        hash = 32 * hash + Objects.hashCode(this.plateNo);
        hash = 32 * hash + Objects.hashCode(this.vehicleLoad);
        hash = 32 * hash + Objects.hashCode(this.createdDate);
        hash = 32 * hash + Objects.hashCode(this.updatedDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VehicleLoad other = (VehicleLoad) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.plateNo, other.plateNo)) {
            return false;
        }
        if (!Objects.equals(this.vehicleLoad, other.vehicleLoad)) {
            return false;
        }
        if (!Objects.equals(this.createdDate, other.createdDate)) {
            return false;
        }
        if (!Objects.equals(this.updatedDate, other.updatedDate)) {
            return false;
        }
        return true;
    }
    
}
