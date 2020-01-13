/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import javax.persistence.EntityManager;
import com.gcs.wb.jpa.entity.User;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author dinhhn.vr
 */
public class UserRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public User findByUid(String uid) {
        TypedQuery<User> typedQuery = entityManager.createNamedQuery("User.findByUid", User.class);
        typedQuery.setParameter("uid", uid);
        List<User> users = typedQuery.getResultList();

        if (users != null && users.size() == 1) {
            return users.get(0);
        }

        return null;
    }
}
