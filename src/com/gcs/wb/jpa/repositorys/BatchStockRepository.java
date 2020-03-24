/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.BatchStock;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class BatchStockRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = Logger.getLogger(this.getClass());

    public List<BatchStock> getListBatchStock(String plant, String lgort, String matnr) {
        List<BatchStock> list = new ArrayList<>();
        try {
            TypedQuery<BatchStock> query = entityManager.createNamedQuery("BatchStock.findByWerksLgortMatnr", BatchStock.class);
            query.setParameter("werks", plant);
            query.setParameter("lgort", lgort);
            query.setParameter("matnr", matnr);
            list = query.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public BatchStock findByWerksLgortMatnrCharg(String werks, String lgort, String matnr, String charg) {
        try {
            TypedQuery<BatchStock> query = entityManager.createNamedQuery("BatchStock.findByWerksLgortMatnrCharg", BatchStock.class);
            query.setParameter("werks", werks);
            query.setParameter("lgort", lgort);
            query.setParameter("matnr", matnr);
            query.setParameter("charg", charg);
            List<BatchStock> batchStocks = query.getResultList();
            if (batchStocks != null && batchStocks.size() > 0) {
                return batchStocks.get(0);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return null;
    }

    public boolean hasData(String mandt, String wplant) {
        try {
            TypedQuery<BatchStock> typedQuery = entityManager.createNamedQuery("BatchStock.findByMandtWplant", BatchStock.class);
            typedQuery.setParameter("mandt", mandt);
            typedQuery.setParameter("wplant", wplant);
            List<BatchStock> batchStocks = typedQuery.getResultList();
            if (batchStocks != null && batchStocks.size() > 0) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return false;
    }
}
