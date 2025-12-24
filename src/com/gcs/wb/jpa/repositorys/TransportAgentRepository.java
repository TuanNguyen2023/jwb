/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.TransportAgent;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class TransportAgentRepository {

    EntityManager entityManager = JPAConnector.getInstance();
    Logger logger = Logger.getLogger(this.getClass());

    /**
     * get list DVVC
     *
     * @return
     */
    public List<TransportAgent> getListTransportAgent() {
        List<TransportAgent> list = new ArrayList<>();
        try {
            TypedQuery<TransportAgent> typedQuery = entityManager.createNamedQuery("TransportAgent.findAll", TransportAgent.class);
            list = typedQuery.getResultList();
        } catch (Exception ex) {
            logger.error(null, ex);
            ExceptionUtil.checkDatabaseDisconnectedException(ex);
        }

        return list;
    }

    public DefaultListModel getTAModel() {
        List<TransportAgent> tAgents = getListTransportAgent();
        DefaultListModel model = new DefaultListModel();
        for (TransportAgent tagent : tAgents) {
            model.addElement(tagent);
        }
        return model;
    }

    public DefaultComboBoxModel getTAgentsModel() {
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        result = new DefaultComboBoxModel(getListTransportAgent().toArray());
        return result;
    }
}
