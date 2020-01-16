/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import java.sql.Date;
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
@Table(name = "tbl_user")
@NamedQueries({
    @NamedQuery(name = "User.findByUid", query = "SELECT u FROM User u WHERE u.uid = :uid")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "uid", unique = true)
    private String uid;
    @Column(name = "password")
    private String password;
    @Column(name = "title")
    private String title;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "roles")
    private String roles;
    @Column(name = "lang")
    private Character lang;
    @Column(name = "lang_iso")
    private String langIso;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "updated_date")
    private Date updatedDate;
    @Column(name = "deleted_date")
    private Date deletedDate;
    @Column(name = "mandt")
    private String mandt;
    @Column(name = "wplant")
    private String wplan;

    public User() {
    }

    public User(String uid, String password) {
        this.uid = uid;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Character getLang() {
        return lang;
    }

    public void setLang(Character lang) {
        this.lang = lang;
    }

    public String getLangIso() {
        return langIso;
    }

    public void setLangIso(String langIso) {
        this.langIso = langIso;
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

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getWplan() {
        return wplan;
    }

    public void setWplan(String wplan) {
        this.wplan = wplan;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (uid != null ? !uid.equals(user.uid) : user.uid != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (title != null ? !title.equals(user.title) : user.title != null) return false;
        if (fullname != null ? !fullname.equals(user.fullname) : user.fullname != null) return false;
        if (roles != null ? !roles.equals(user.roles) : user.roles != null) return false;
        if (lang != null ? !lang.equals(user.lang) : user.lang != null) return false;
        if (langIso != null ? !langIso.equals(user.langIso) : user.langIso != null) return false;
        if (createdDate != null ? !createdDate.equals(user.createdDate) : user.createdDate != null) return false;
        if (updatedDate != null ? !updatedDate.equals(user.updatedDate) : user.updatedDate != null) return false;
        if (deletedDate != null ? !deletedDate.equals(user.deletedDate) : user.deletedDate != null) return false;
        if (mandt != null ? !mandt.equals(user.mandt) : user.mandt != null) return false;
        if (wplan != null ? !wplan.equals(user.wplan) : user.wplan != null) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 31));
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (fullname != null ? fullname.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        result = 31 * result + (langIso != null ? langIso.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (deletedDate != null ? deletedDate.hashCode() : 0);
        result = 31 * result + (mandt != null ? mandt.hashCode() : 0);
        result = 31 * result + (wplan != null ? wplan.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.User[uid=" + uid + "]";
    }
}
