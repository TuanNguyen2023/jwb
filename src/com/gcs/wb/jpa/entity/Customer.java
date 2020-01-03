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
@Table(name = "Customer")
@NamedQueries({
    @NamedQuery(name = "Customer.findByMandt", query = "SELECT c FROM Customer c WHERE c.customerPK.mandt = :mandt")})
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CustomerPK customerPK;
    @Column(name = "NAME1")
    private String name1;
    @Column(name = "NAME2")
    private String name2;

    public Customer() {
    }

    public Customer(CustomerPK customerPK) {
        this.customerPK = customerPK;
    }

    public Customer(String mandt, String kunnr) {
        this.customerPK = new CustomerPK(mandt, kunnr);
    }

    public CustomerPK getCustomerPK() {
        return customerPK;
    }

    public void setCustomerPK(CustomerPK customerPK) {
        this.customerPK = customerPK;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerPK != null ? customerPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerPK == null && other.customerPK != null) || (this.customerPK != null && !this.customerPK.equals(other.customerPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Customer[customerPK=" + customerPK + "]";
    }
}
