/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.PlantGetDetailBapi;
import com.gcs.wb.bapi.helper.UserGetDetailBapi;
import com.gcs.wb.bapi.helper.constants.PlantGeDetailConstants;
import com.gcs.wb.bapi.helper.structure.UserGetDetailAGRStructure;
import com.gcs.wb.bapi.helper.structure.UserGetDetailAddrStructure;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.SAPSettingPK;
import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.jpa.entity.UserPK;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.model.LoginViewModel;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibersap.session.Session;

/**
 *
 * @author THANGPT
 */
public class LoginController {

    AppConfig appConfig = WeighBridgeApp.getApplication().getConfig();

    public LoginViewModel findUserAndSap(EntityManager entityManager, String lclient, String lplant, String username) {
        LoginViewModel login = new LoginViewModel();
        User user = entityManager.find(User.class, new UserPK(lclient, lplant, username));
        SAPSetting sapSetting = entityManager.find(SAPSetting.class, new SAPSettingPK(lclient, lplant));

        login.setUser(user);
        login.setSapSetting(sapSetting);
        return login;
    }

    public User getSbRoles(EntityManager entityManager, User userView, SAPSetting sapSetting,
            String lclient, String username, String password, String lplant,
            Session session,UserGetDetailBapi userGetDetailBapi) {
        
        User user = userView;
        UserGetDetailAddrStructure userGetDetailAddrStructure = userGetDetailBapi.getAddress();
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
        } catch (Exception ex) {
        }
        //return sbRoles;
        entityManager.getTransaction().begin();

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
            user = new User(new UserPK(lclient, lplant, username), password);
        } else {
            updateUser = true;
        }

        user.setFullName(userGetDetailAddrStructure.getFullName());
        user.setLanguP(userGetDetailAddrStructure.getLang().length() == 0 ? ' ' : userGetDetailAddrStructure.getLang().charAt(0));
        user.setLangupIso(userGetDetailAddrStructure.getLangISO());
        user.setPwd(password);
        if (sbRoles != null) {
            user.setRoles(sbRoles.toString());
        }
        user.setTitle(userGetDetailAddrStructure.getTitle());

        if (updateUser) {
            entityManager.merge(user);
        } else {
            entityManager.persist(user);
        }
        entityManager.getTransaction().commit();
        entityManager.clear();
        
        return user;
    }
}
