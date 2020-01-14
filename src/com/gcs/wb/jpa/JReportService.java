/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa;

import java.sql.Connection;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;

/**
 *
 * @author dinhhn.vr
 */
public class JReportService {

    Connection connect = JReportConnector.getInstance();
    Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

    public void printReport(Map<String, Object> map, String reportName) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportName, map, connect);
            JasperViewer jv = new JasperViewer(jasperPrint, false);
            jv.setVisible(true);

        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
}
