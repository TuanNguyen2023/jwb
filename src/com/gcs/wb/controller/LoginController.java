/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.UserGetDetailBapi;
import com.gcs.wb.bapi.helper.structure.UserGetDetailAddrStructure;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.jpa.repositorys.SAPSettingRepository;
import com.gcs.wb.jpa.repositorys.UserRepository;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.LoginService;
import com.sap.conn.jco.JCoException;
import org.hibersap.session.Credentials;
import org.hibersap.session.Session;

/**
 *
 * @author THANGPT
 */
public class LoginController {

    private UserRepository userRepository;
    private SAPSettingRepository sapSettingRepository;
    private AppConfig appConfig;
    private Credentials credentials = null;
    private SAPSetting sapSetting = null;
    private User user = null;
    private String lclient = null;
    private String lplant = null;
    private String username = null;
    private String password = null;
    private boolean offlineMode = false;
    private JCoException jcoException = null;
    private LoginService loginService = new LoginService();

    public LoginController(String username, String password) {
        userRepository = new UserRepository();
        sapSettingRepository = new SAPSettingRepository();

        appConfig = WeighBridgeApp.getApplication().getConfig();
        lclient = appConfig.getsClient();
        lplant = appConfig.getwPlant().toString();
        if (appConfig.getModeNormal()) {
            this.username = username;
            this.password = password;
        } else {
            this.username = appConfig.getUsrName();
            this.password = appConfig.getPsswrd();
        }

        credentials = new Credentials();
        credentials.setClient(lclient);
        credentials.setUser(this.username);
        credentials.setPassword(this.password);
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public SAPSetting getSapSetting() {
        return sapSetting;
    }

    public User getUser() {
        return user;
    }

    public Object doLogin() throws Exception {
        UserGetDetailBapi userGetDetailBapi = new UserGetDetailBapi();
        userGetDetailBapi.setUserName(username);

        try {
            user = userRepository.findByUid(username);
            sapSetting = sapSettingRepository.getSAPSetting();

            Session session = loginService.getSapSession(credentials);
            boolean sap_flag = true;
            try {
                if (appConfig.isCheckVersionWB()) {
                    loginService.checkVersionWB(session);
                }
                session.execute(userGetDetailBapi);  //Login
            } catch (Exception ex) {
                sap_flag = false;
                if (user != null && !user.getPassword().equals(password)) {
                    throw new Exception("Authenticated Failed!!!");
                } else if (ex.getCause() instanceof JCoException) {
                    jcoException = (JCoException) ex.getCause();
                    if (jcoException.getGroup() == JCoException.JCO_ERROR_LOGON_FAILURE) {
                        throw new Exception("Login Failed!!!");
                    }
                }
            }

            //set offline mode
            offlineMode = !sap_flag;
            if (sap_flag == true) {
                UserGetDetailAddrStructure userGetDetailAddrStructure = userGetDetailBapi.getAddress();
                String roles = loginService.getRoles(userGetDetailBapi);

                loginService.asyncUser(session, userGetDetailAddrStructure, roles, user, username, password);
            }
        } catch (Exception ex) {
            throw ex;
        }

        return null;
    }
}
