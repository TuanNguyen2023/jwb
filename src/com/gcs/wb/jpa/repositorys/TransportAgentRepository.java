/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.repositorys;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.JpaProperties;
import com.gcs.wb.jpa.entity.TransportAgent;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

/**
 *
 * @author dinhhn.vr
 */
public class TransportAgentRepository {

    EntityManager entityManager = JpaProperties.getEntityManager();

    public List<TransportAgent> getListTransportAgent() {
        TypedQuery<TransportAgent> typedQuery = entityManager.createNamedQuery("TransportAgent.findAll", TransportAgent.class);
        return typedQuery.getResultList();
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
