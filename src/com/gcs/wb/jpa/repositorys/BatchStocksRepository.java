/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.BatchStocks;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author dinhhn.vr
 */
public class BatchStocksRepository {

    EntityManager entityManager = JPAConnector.getInstance();

    public List<BatchStocks> getList(String client, String plant, String lgort, String matnrRef) {
        TypedQuery<BatchStocks> query = entityManager.createQuery("SELECT b FROM BatchStocks b WHERE b.batchStocksPK.mandt = :mandt AND b.batchStocksPK.werks = :werks AND b.batchStocksPK.lgort = :lgort  AND b.batchStocksPK.matnr = :matnr", BatchStocks.class);
        query.setParameter("mandt", client);
        query.setParameter("werks", plant);
        query.setParameter("lgort", lgort);
        query.setParameter("matnr", matnrRef);
        List<BatchStocks> batchs = query.getResultList();
        return batchs;
    }
    
}
