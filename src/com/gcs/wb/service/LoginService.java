/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.BAPIConfiguration;
import com.gcs.wb.bapi.helper.CheckVersionWBBapi;
import com.gcs.wb.bapi.helper.PlantGetDetailBapi;
import com.gcs.wb.bapi.helper.UserGetDetailBapi;
import com.gcs.wb.bapi.helper.constants.PlantGeDetailConstants;
import com.gcs.wb.bapi.helper.structure.UserGetDetailAGRStructure;
import com.gcs.wb.bapi.helper.structure.UserGetDetailAddrStructure;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.jpa.repositorys.SAPSettingRepository;
import com.gcs.wb.model.AppConfig;
import java.sql.Date;
import java.util.Calendar;
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
public class LoginService {

    private EntityManager entityManager = JPAConnector.getInstance();
    private EntityTransaction entityTransaction = entityManager.getTransaction();
    private AppConfig appConfig = WeighBridgeApp.getApplication().getConfig();
    private Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    private SAPSettingRepository sapSettingRepository = new SAPSettingRepository();
    private SAPSetting sapSetting = sapSettingRepository.getSAPSetting();

    public Session getSapSession(Credentials credentials) throws Exception {
        Session session = WeighBridgeApp.getApplication().getSAPSession();
        if (session != null) {
            session.close();
        }
        SessionManager sessionManager = BAPIConfiguration.getSessionManager(appConfig, credentials);
        session = sessionManager.openSession(credentials);
        WeighBridgeApp.getApplication().setSAPSession(session);
        return session;
    }

    public void checkVersionWB(Session session) throws Exception {
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

    public String getRoles(UserGetDetailBapi userGetDetailBapi) {
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

    public void asyncUser(Session session, UserGetDetailAddrStructure userGetDetailAddrStructure, String roles, User user, String username, String password) throws Exception {
        try {

            if (!entityTransaction.isActive()) {
                entityTransaction.begin();
            }

            boolean updateUser = true;
            if (user == null) {
                updateUser = false;
                if (sapSetting == null) {
                    PlantGetDetailBapi plantGetDetailBapi = new PlantGetDetailBapi(configuration.getSapClient(), configuration.getWkPlant());
                    session.execute(plantGetDetailBapi);
                    HashMap vals = plantGetDetailBapi.getEsPlant();

                    sapSetting = new SAPSetting();
                    sapSetting.setName1((String) vals.get(PlantGeDetailConstants.NAME1));
                    sapSetting.setName2((String) vals.get(PlantGeDetailConstants.NAME2));
                    sapSetting.setMandt(configuration.getSapClient());
                    sapSetting.setWplant(configuration.getWkPlant());
                    entityManager.persist(sapSetting);
                }
                user = new User(username, password);
            }

            user.setTitle(userGetDetailAddrStructure.getTitle());
            user.setFullname(userGetDetailAddrStructure.getFullName());
            user.setLang(userGetDetailAddrStructure.getLang().length() == 0 ? ' ' : userGetDetailAddrStructure.getLang().charAt(0));
            user.setLangIso(userGetDetailAddrStructure.getLangISO());
            user.setPassword(password);
            user.setMandt(configuration.getSapClient());
            user.setWplant(configuration.getWkPlant());
            if (roles != null) {
                user.setRoles(roles);
            }
            
            if (updateUser) {
                user.setUpdatedDate(new Date(Calendar.getInstance().getTime().getTime()));
                entityManager.merge(user);
            } else {
                entityManager.persist(user);
            }

            entityTransaction.commit();
            entityManager.clear();
        } catch (Exception ex) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw ex;
        }
    }
}
