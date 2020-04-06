/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa;

import com.gcs.wb.WeighBridgeApp;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JRSaveContributor;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.view.save.JRCsvSaveContributor;
import net.sf.jasperreports.view.save.JRPdfSaveContributor;
import net.sf.jasperreports.view.save.JRSingleSheetXlsSaveContributor;
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
            MyJasperViewer jv = new MyJasperViewer(jasperPrint);
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
            MyJasperViewer jasperViewer = new MyJasperViewer(jasperPrint);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            logger.error(ex);
        }
    }

    public class MyJasperViewer extends JasperViewer {

        public MyJasperViewer(JasperPrint jasperPrint) {
            super(jasperPrint, false);
            viewer.setSaveContributors(new JRSaveContributor[]{new JRPdfSaveContributor(Locale.getDefault(), null),
                new JRCsvSaveContributor(Locale.getDefault(), null),
                new JRSingleSheetXlsSaveContributor(Locale.getDefault(), null)});
        }
    }
}
