/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.BAPIConfiguration;
import com.gcs.wb.bapi.helper.CheckVersionWBBapi;
import com.gcs.wb.bapi.helper.PlantGetDetailBapi;
import com.gcs.wb.bapi.helper.UserGetDetailBapi;
import com.gcs.wb.bapi.helper.constants.PlantGeDetailConstants;
import com.gcs.wb.bapi.helper.structure.UserGetDetailAGRStructure;
import com.gcs.wb.bapi.helper.structure.UserGetDetailAddrStructure;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.SAPSettingPK;
import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.jpa.repositorys.UserRepository;
import com.gcs.wb.model.AppConfig;
import com.sap.conn.jco.JCoException;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.hibersap.session.Credentials;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;

/**
 *
 * @author THANGPT
 */
public class LoginController {

    private UserRepository userRepository;
    private AppConfig appConfig;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;
    private Credentials credentials = null;
    private SAPSetting sapSetting = null;
    private User user = null;
    private String lclient = null;
    private String lplant = null;
    private String username = null;
    private String password = null;
    private boolean offlineMode = false;
    private JCoException jcoException = null;

    public LoginController(String username, String password) {
        userRepository = new UserRepository();
        entityManager = JPAConnector.getInstance();
        entityTransaction = entityManager.getTransaction();

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
            sapSetting = entityManager.find(SAPSetting.class, new SAPSettingPK(lclient, lplant));

            Session session = getSapSession();
            boolean sap_flag = true;
            try {
                if (appConfig.isCheckVersionWB()) {
                    checkVersionWB(session);
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
                String roles = getRoles(userGetDetailBapi);

                asyncUser(session, userGetDetailAddrStructure, roles);
            }
        } catch (Exception ex) {
            throw ex;
        }

        return null;
    }

    private Session getSapSession() throws Exception {
        Session session = WeighBridgeApp.getApplication().getSAPSession();
        if (session != null) {
            session.close();
            session = null;
        }
        SessionManager sessionManager = BAPIConfiguration.getSessionManager(appConfig, credentials);
        session = sessionManager.openSession(credentials);
        WeighBridgeApp.getApplication().setSAPSession(session);
        return session;
    }

    private void checkVersionWB(Session session) throws Exception {
        // Checking version of WB application
        CheckVersionWBBapi version = new CheckVersionWBBapi("2.40"); //09/07/2013
        try {
            session.execute(version);
            if (version.getResult() != null
                    && version.getResult().getId() != null
                    && version.getResult().getId().equals("UPDATEVERSION")) {
                throw new Exception(version.getResult().getMessage());
            }
        } catch (Exception ex) {
            // do nothing
        }
    }

    private String getRoles(UserGetDetailBapi userGetDetailBapi) {
        List<UserGetDetailAGRStructure> roles = userGetDetailBapi.getActgrps();
        StringBuilder sbRoles = new StringBuilder();
        try {
            for (int i = 0; i < roles.size(); i++) {
                UserGetDetailAGRStructure role = roles.get(i);
                sbRoles.append(role.getAgr_name());
                if (i < roles.size() - 1) {
                    sbRoles.append(",");
                }
            }

            return sbRoles.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    private void asyncUser(Session session, UserGetDetailAddrStructure userGetDetailAddrStructure, String roles) throws Exception {
        try {
            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            boolean updateUser = true;
            if (user == null) {
                updateUser = false;
                if (sapSetting == null) {
                    PlantGetDetailBapi plantGetDetailBapi = new PlantGetDetailBapi(lclient, lplant);
                    session.execute(plantGetDetailBapi);
                    HashMap vals = plantGetDetailBapi.getEsPlant();

                    sapSetting = new SAPSetting(new SAPSettingPK(lclient, lplant));
                    sapSetting.setName1((String) vals.get(PlantGeDetailConstants.NAME1));
                    sapSetting.setName2((String) vals.get(PlantGeDetailConstants.NAME2));
                    entityManager.persist(sapSetting);
                }
                user = new User(username, password);
            } else {
                updateUser = true;
            }

            user.setTitle(userGetDetailAddrStructure.getTitle());
            user.setFullname(userGetDetailAddrStructure.getFullName());
            user.setLang(userGetDetailAddrStructure.getLang().length() == 0 ? ' ' : userGetDetailAddrStructure.getLang().charAt(0));
            user.setLangIso(userGetDetailAddrStructure.getLangISO());
            user.setPassword(password);
            if (roles != null) {
                user.setRoles(roles);
            }

            if (updateUser) {
                entityManager.merge(user);
            } else {
                entityManager.persist(user);
            }

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            entityTransaction.rollback();
            throw ex;
        }
    }
}
