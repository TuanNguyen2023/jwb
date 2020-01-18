/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.BatchStock;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author dinhhn.vr
 */
public class BatchStockRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public List<BatchStock> getListBatchStock(String plant, String lgort, String matnr) {
        TypedQuery<BatchStock> query = entityManager.createNamedQuery("BatchStock.findByWerksLgortMatnr", BatchStock.class);
        query.setParameter("werks", plant);
        query.setParameter("lgort", lgort);
        query.setParameter("matnr", matnr);
        return query.getResultList();
    }

    public BatchStock findByWerksLgortMatnrCharg(String werks, String lgort, String matnr, String charg) {
        TypedQuery<BatchStock> query = entityManager.createNamedQuery("BatchStock.findByWerksLgortMatnrCharg", BatchStock.class);
        query.setParameter("werks", werks);
        query.setParameter("lgort", lgort);
        query.setParameter("matnr", matnr);
        query.setParameter("charg", charg);
        List<BatchStock> batchStocks = query.getResultList();
        if (batchStocks != null && batchStocks.size() > 0) {
            return batchStocks.get(0);
        }

        return null;
    }
}
