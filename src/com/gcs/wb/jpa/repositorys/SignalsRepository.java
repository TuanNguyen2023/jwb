/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author dinhhn.vr
 */
public class SignalsRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public int getCountSingal() {
        int count = 0;
        Query query = (Query) entityManager.createNativeQuery("SELECT COUNT(cvalue) AS icount FROM signals WHERE cvalue LIKE '02' AND wbid LIKE '1411TC'");
        Object result = query.getSingleResult();
        if (result != null) {
            count = Integer.parseInt(result.toString());
        }
        return count;
    }
}
