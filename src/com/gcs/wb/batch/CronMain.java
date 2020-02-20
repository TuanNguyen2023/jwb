package com.gcs.wb.batch;

import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author THANGLH
 */
public class CronMain {

    public static Logger logger = Logger.getLogger(CronMain.class);

    public static void main(String[] args) {
        CronTriggerService cronTriggerService = new CronTriggerService();

        try {
            logger.info("Connect to Database...");
            cronTriggerService.openDbConnection();

            logger.info("Connect to SAP...");
            cronTriggerService.openSapConnection();

            logger.info("Processing...");
            cronTriggerService.execute();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CronMain.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);

            logger.info("Exit...");
            cronTriggerService.dispose();
        }
    }
}
