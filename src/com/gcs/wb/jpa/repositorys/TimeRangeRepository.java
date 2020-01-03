/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.TimeRange;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author dinhhn.vr
 */
public class TimeRangeRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public TimeRange findByMandtWbId(String mandt, String wbId) {
        TimeRange t = null;
        TypedQuery query = entityManager.createNamedQuery("TimeRange.findByMandtWBID", TimeRange.class);
        query.setParameter("mandt", mandt);
        query.setParameter("wbId", wbId);
        List<TimeRange> list = query.getResultList();
        if ((list.size() > 0) && (list != null)) {
            t = list.get(0);
        }
        return t;
    }
}
