/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.model;

import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.User;
import com.sap.conn.jco.JCoException;
import org.hibersap.session.Credentials;

/**
 *
 * @author THANGPT
 */
public class LoginViewModel {
    public Credentials credentials;
    public User user;
    public SAPSetting sapSetting;
    public JCoException jcoException;
    boolean lOfflineMode;
    boolean flag_login;
    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public boolean isFlag_login() {
        return flag_login;
    }

    public void setFlag_login(boolean flag_login) {
        this.flag_login = flag_login;
    }

    public JCoException getJcoException() {
        return jcoException;
    }

    public void setJcoException(JCoException jcoException) {
        this.jcoException = jcoException;
    }

    public boolean islOfflineMode() {
        return lOfflineMode;
    }

    public void setlOfflineMode(boolean lOfflineMode) {
        this.lOfflineMode = lOfflineMode;
    }

    public SAPSetting getSapSetting() {
        return sapSetting;
    }

    public void setSapSetting(SAPSetting sapSetting) {
        this.sapSetting = sapSetting;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
