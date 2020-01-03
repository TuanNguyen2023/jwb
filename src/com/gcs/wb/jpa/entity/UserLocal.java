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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "user_local")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserLocal.login", query = "SELECT u FROM UserLocal u WHERE u.userLocalPK.mandt = :mandt AND u.userLocalPK.wplant = :wplant AND u.userLocalPK.id = :id AND u.pwd = :pwd")
})
public class UserLocal implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserLocalPK userLocalPK;
    @Column(name = "PWD")
    private String pwd;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "SLOC")
    private String sloc;
    @Column(name = "Active")
    private Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserLocal() {
    }

    public UserLocal(UserLocalPK userLocalPK) {
        this.userLocalPK = userLocalPK;
    }

    public UserLocal(String mandt, String wplant, String id) {
        this.userLocalPK = new UserLocalPK(mandt, wplant, id);
    }

    public UserLocalPK getUserLocalPK() {
        return userLocalPK;
    }

    public void setUserLocalPK(UserLocalPK userLocalPK) {
        this.userLocalPK = userLocalPK;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSloc() {
        return sloc;
    }

    public void setSloc(String sloc) {
        this.sloc = sloc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userLocalPK != null ? userLocalPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserLocal)) {
            return false;
        }
        UserLocal other = (UserLocal) object;
        if ((this.userLocalPK == null && other.userLocalPK != null) || (this.userLocalPK != null && !this.userLocalPK.equals(other.userLocalPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.UserLocal[ userLocalPK=" + userLocalPK + " ]";
    }
}
