/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.UserGetDetailBapi;
import com.gcs.wb.bapi.helper.structure.UserGetDetailAddrStructure;
import com.gcs.wb.batch.CronTriggerService;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.jpa.repositorys.SAPSettingRepository;
import com.gcs.wb.jpa.repositorys.UserRepository;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.LoginService;
import com.gcs.wb.service.SyncMasterDataService;
import com.gcs.wb.views.LoginView;
import com.sap.conn.jco.JCoException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.hibersap.session.Credentials;
import org.hibersap.session.Session;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author THANGPT
 */
public class LoginController {

    private UserRepository userRepository;
    private SAPSettingRepository sapSettingRepository;
    private AppConfig appConfig;
    private Configuration configuration;
    private Credentials credentials = null;
    private User user = null;
    private String username = null;
    private String password = null;
    private boolean offlineMode = false;
    private JCoException jcoException = null;
    private LoginService loginService = new LoginService();
    ResourceMap resourceMap = Application.getInstance(WeighBridgeApp.class).getContext().getResourceMap(LoginView.class);
    private JFrame mainFrame = WeighBridgeApp.getApplication().getMainFrame();
    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LoginController.class);

    public LoginController(String username, String password) {
        userRepository = new UserRepository();
        sapSettingRepository = new SAPSettingRepository();

        appConfig = WeighBridgeApp.getApplication().getConfig();
        configuration = appConfig.getConfiguration();
        this.username = username;
        this.password = password;

        credentials = new Credentials();
        credentials.setClient(configuration.getSapClient());
        credentials.setUser(configuration.getSapUser());
        credentials.setPassword(configuration.getSapPass());
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public boolean isOfflineMode() {
        return offlineMode;
    }

    public User getUser() {
        return user;
    }

    public Object doLogin() throws Exception {
        UserGetDetailBapi userGetDetailBapi = new UserGetDetailBapi();
        userGetDetailBapi.setUserName(username);

        try {
            user = userRepository.findByMandtWplantUid(configuration.getSapClient(), configuration.getWkPlant(), username);

            if ((user == null) || (user != null && !user.getPassword().equals(password))) {
                throw new Exception(resourceMap.getString("msg.offlineUsernameOrPasswordInvalid"));
            }

            Session session = loginService.getSapSession(credentials);
            boolean onlineMode = true;
            try {
                loginService.checkVersionWB(session);
                logger.info("[SAP] Get User detail: " + userGetDetailBapi.toString());
                session.execute(userGetDetailBapi);  //Login
            } catch (Exception ex) {
                onlineMode = false;
                if (ex.getCause() instanceof JCoException) {
                    jcoException = (JCoException) ex.getCause();
                    if (jcoException.getGroup() == JCoException.JCO_ERROR_LOGON_FAILURE) {
                        JOptionPane.showMessageDialog(mainFrame, resourceMap.getString("msg.onlineUsernameOrPasswordInvalid"));
                    } else if (jcoException.getGroup() == JCoException.JCO_ERROR_COMMUNICATION) {
                        JOptionPane.showMessageDialog(mainFrame, resourceMap.getString("msg.connectToSAPFail"));
                    }
                }
            }

            if (onlineMode) {
                UserGetDetailAddrStructure userGetDetailAddrStructure = userGetDetailBapi.getAddress();
                String roles = loginService.getRoles(userGetDetailBapi);

                loginService.asyncUser(session, userGetDetailAddrStructure, roles,
                        userRepository.findByUid(configuration.getSapUser()),
                        configuration.getSapUser(), configuration.getSapPass());

                SyncMasterDataService syncMasterDataService = new SyncMasterDataService();
                syncMasterDataService.syncMasterDataWhenLogin();

                // init sync master data cron job
                (new CronTriggerService()).execute();
            } else {
                SAPSetting sapSetting = sapSettingRepository.findByMandtAndWplant(configuration.getSapClient(), configuration.getWkPlant());
                WeighBridgeApp.getApplication().setSapSetting(sapSetting);
            }

            offlineMode = !onlineMode;
        } catch (Exception ex) {
            throw ex;
        }

        return null;
    }
}
