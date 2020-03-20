/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.service.LoginService;
import com.sap.conn.jco.JCoException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.hibersap.session.Session;

/**
 *
 * @author THANGLH
 */
public class SAPSession {

    private Session session = null;
    private JFrame mainFrame = WeighBridgeApp.getApplication().getMainFrame();

    public SAPSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void execute(Object bapiObject) {
        while (true) {
            try {
                session.execute(bapiObject);
                break;
            } catch (Exception ex) {
                if (ex.getCause() instanceof JCoException) {
                    JCoException jcoException = (JCoException) ex.getCause();
                    if (jcoException.getGroup() == JCoException.JCO_ERROR_COMMUNICATION) {
                        int answer = JOptionPane.showConfirmDialog(
                                mainFrame,
                                "Mất kết nối đến SAP, kết nối lại?",
                                JOptionPane.OPTIONS_PROPERTY,
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE);

                        if (answer == JOptionPane.YES_OPTION) {
                            LoginService loginService = new LoginService();
                            session = loginService.reconnectSapSession();
                            continue;
                        }
                    }
                }

                throw ex;
            }
        }
    }

    public void close() {
        session.close();
    }

    public boolean isClosed() {
        return session.isClosed();
    }
}
