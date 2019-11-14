/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(name = "User", catalog = "jWeighBridge", schema = "")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByMandt", query = "SELECT u FROM User u WHERE u.userPK.mandt = :mandt"),
    @NamedQuery(name = "User.findByWPlant", query = "SELECT u FROM User u WHERE u.userPK.wPlant = :wPlant"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.userPK.id = :id"),
    @NamedQuery(name = "User.findByPwd", query = "SELECT u FROM User u WHERE u.pwd = :pwd"),
    @NamedQuery(name = "User.findByTitle", query = "SELECT u FROM User u WHERE u.title = :title"),
    @NamedQuery(name = "User.findByFullName", query = "SELECT u FROM User u WHERE u.fullName = :fullName"),
    @NamedQuery(name = "User.findByRoles", query = "SELECT u FROM User u WHERE u.roles = :roles"),
    @NamedQuery(name = "User.findByLanguP", query = "SELECT u FROM User u WHERE u.languP = :languP"),
    @NamedQuery(name = "User.findByLangupIso", query = "SELECT u FROM User u WHERE u.langupIso = :langupIso")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserPK userPK;
    @Basic(optional = false)
    @Column(name = "PWD")
    private String pwd;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "ROLES")
    private String roles;
    @Column(name = "LANGU_P")
    private Character languP;
    @Column(name = "LANGUP_ISO")
    private String langupIso;

    public User() {
    }

    public User(UserPK userPK) {
        this.userPK = userPK;
    }

    public User(UserPK userPK, String pwd) {
        this.userPK = userPK;
        this.pwd = pwd;
    }

    public User(String mandt, String wPlant, String id) {
        this.userPK = new UserPK(mandt, wPlant, id);
    }

    public UserPK getUserPK() {
        return userPK;
    }

    public void setUserPK(UserPK userPK) {
        this.userPK = userPK;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Character getLanguP() {
        return languP;
    }

    public void setLanguP(Character languP) {
        this.languP = languP;
    }

    public String getLangupIso() {
        return langupIso;
    }

    public void setLangupIso(String langupIso) {
        this.langupIso = langupIso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userPK != null ? userPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userPK == null && other.userPK != null) || (this.userPK != null && !this.userPK.equals(other.userPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.User[userPK=" + userPK + "]";
    }
}
