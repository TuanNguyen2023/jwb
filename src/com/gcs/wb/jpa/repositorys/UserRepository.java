/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import javax.persistence.EntityManager;
import com.gcs.wb.jpa.entity.User;
import java.util.List;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class UserRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = Logger.getLogger(this.getClass());

    public User findByUid(String uid) {
        try {
            TypedQuery<User> typedQuery = entityManager.createNamedQuery("User.findByUid", User.class);
            typedQuery.setParameter("uid", uid);
            List<User> users = typedQuery.getResultList();

            if (users != null && users.size() >= 1) {
                return users.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public User findByMandtWplantUid(String mandt, String wplant, String uid) {
        try {
            TypedQuery<User> typedQuery = entityManager.createNamedQuery("User.findByMandtWplantUid", User.class);
            typedQuery.setParameter("mandt", mandt);
            typedQuery.setParameter("wplant", wplant);
            typedQuery.setParameter("uid", uid);
            List<User> users = typedQuery.getResultList();

            if (users != null && users.size() >= 1) {
                return users.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }
}
