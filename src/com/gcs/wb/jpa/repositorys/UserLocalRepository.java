/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.UserLocal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class UserLocalRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public UserLocal login(String client, String plant, String username, String password) {
        UserLocal user = new UserLocal();
        try {
            TypedQuery<UserLocal> query = entityManager.createNamedQuery("UserLocal.login", UserLocal.class);
            query.setParameter("mandt", client);
            query.setParameter("wplant", plant);
            query.setParameter("id", username);
            query.setParameter("pwd", password);
            List<UserLocal> list = query.getResultList();
            if (list != null && list.size() > 0) {
                user = list.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);

        }
        return user;
    }
}
