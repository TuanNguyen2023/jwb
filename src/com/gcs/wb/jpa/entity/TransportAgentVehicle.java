/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_transport_agent_vehicle")
@NamedQueries({
    @NamedQuery(name = "TransportAgentVehicle.findByTransportAgentId",
    query = "SELECT tv FROM TransportAgentVehicle tv"
    + " WHERE tv.transportAgent.id = :transportAgentId"),
    @NamedQuery(name = "TransportAgentVehicle.findByVehicleId",
    query = "SELECT tv FROM TransportAgentVehicle tv"
    + " WHERE tv.vehicle.id = :vehicleId"),
    @NamedQuery(name = "TransportAgentVehicle.findByTransportAgentIdAndVehicleId",
    query = "SELECT tv FROM TransportAgentVehicle tv"
    + " WHERE tv.transportAgent.id = :transportAgentId AND tv.vehicle.id = :vehicleId"),
    @NamedQuery(name = "TransportAgentVehicle.findByPlateNo",
    query = "SELECT tv FROM TransportAgentVehicle tv"
    + " WHERE tv.vehicle.plateNo = :plateNo"),
})
public class TransportAgentVehicle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "transport_agent_id")
    private TransportAgent transportAgent;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    public TransportAgentVehicle() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransportAgent getTransportAgent() {
        return transportAgent;
    }

    public void setTransportAgent(TransportAgent transportAgent) {
        this.transportAgent = transportAgent;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.TransportAgentVehicle[id=" + id + "]";
    }
}
