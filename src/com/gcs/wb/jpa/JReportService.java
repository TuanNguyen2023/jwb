/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa;

import com.gcs.wb.WeighBridgeApp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class JReportService {

    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    JFrame mainFrame = WeighBridgeApp.getApplication().getMainFrame();

    public void printReport(Map<String, Object> map, String reportName) {
        try {
            Connection connect = JReportConnector.getInstance();
            if (connect == null || !connect.isValid(0)) {
                JReportConnector.close();
                connect = JReportConnector.getInstance();
            }
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportName, map, connect);
            JasperViewer jv = new JasperViewer(jasperPrint, false);
            jv.setVisible(true);

        } catch (JRException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(mainFrame, e.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(mainFrame, "[Report] Không kết nối được database. Vui lòng kiểm tra lại đường chuyền.");
        }
    }

    public void printReportDataSource(Map<String, Object> map, String reportName, JRTableModelDataSource data) {
        JasperPrint jasperPrint;
        try {
            jasperPrint = JasperFillManager.fillReport(reportName, map, data);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            logger.error(ex);
        }
    }
}
