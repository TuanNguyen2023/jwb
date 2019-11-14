/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author vunguyent
 */
@Entity
@Table(name = "TransportAgent", catalog = "jWeighBridge", schema = "")
@NamedQueries({
    @NamedQuery(name = "TransportAgent.findAll", query = "SELECT t FROM TransportAgent t"),
    @NamedQuery(name = "TransportAgent.findByAbbr", query = "SELECT t FROM TransportAgent t WHERE t.abbr = :abbr"),
    @NamedQuery(name = "TransportAgent.findByName", query = "SELECT t FROM TransportAgent t WHERE t.name = :name")})
public class TransportAgent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ABBR")
    private String abbr;
    @Column(name = "Name")
    private String name;

    public TransportAgent() {
    }

    public TransportAgent(String abbr) {
        this.abbr = abbr;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (abbr != null ? abbr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TransportAgent)) {
            return false;
        }
        TransportAgent other = (TransportAgent) object;
        if ((this.abbr == null && other.abbr != null) || (this.abbr != null && !this.abbr.equals(other.abbr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.TransportAgent[abbr=" + abbr + "]";
    }
}
