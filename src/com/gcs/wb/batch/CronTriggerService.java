package com.gcs.wb.batch;

import com.gcs.wb.bapi.BAPIConfiguration;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.repositorys.ConfigurationRepository;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.LoginService;
import com.sap.conn.jco.JCoException;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import org.apache.log4j.Logger;
import org.hibersap.session.Credentials;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;

/**
 * Created by PHUCPH on 2/11/2020.
 */
public class CronTriggerService {

    private static final AppConfig appConfig = new AppConfig();
    private static Session sapSession;
    private final String CRON_EXPRESSION = "0 5 0 ? * * *"; // 00:05:00 every day

    public void execute() throws ParseException, SchedulerException {
        // Vendor
        JobDetail vendorJob = new JobDetail();
        vendorJob.setName("vendor-sync");
        vendorJob.setJobClass(SyncVendorJob.class);

        CronTrigger vendorTrigger = new CronTrigger();
        vendorTrigger.setName("vendor-sync-trigger");
        vendorTrigger.setCronExpression("0 0/1 * ? * * *");

        // Material
        JobDetail materialJob = new JobDetail();
        materialJob.setName("material-sync");
        materialJob.setJobClass(SyncMaterialJob.class);

        CronTrigger materialTrigger = new CronTrigger();
        materialTrigger.setName("material-sync-trigger");
        materialTrigger.setCronExpression("0 0/2 * ? * * *");

        // Sloc
        JobDetail slocJob = new JobDetail();
        slocJob.setName("sloc-sync");
        slocJob.setJobClass(SyncSlocJob.class);

        CronTrigger slocTrigger = new CronTrigger();
        slocTrigger.setName("sloc-sync-trigger");
        slocTrigger.setCronExpression("0 0/3 * ? * * *");

        // Batch Stock
        JobDetail batchStockJob = new JobDetail();
        batchStockJob.setName("batch-stock-sync");
        batchStockJob.setJobClass(SyncBatchStockJob.class);

        CronTrigger batchStockTrigger = new CronTrigger();
        batchStockTrigger.setName("batch-stock-sync-trigger");
        batchStockTrigger.setCronExpression("0 0/4 * ? * * *");

        // Execute
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(vendorJob, vendorTrigger);
        scheduler.scheduleJob(materialJob, materialTrigger);
        scheduler.scheduleJob(slocJob, slocTrigger);
        scheduler.scheduleJob(batchStockJob, batchStockTrigger);
    }

    public void openDbConnection() throws Exception {
        if (!appConfig.isHasDatabaseConfig()) {
            throw new Exception("Can't find info of database connection. Please check file application.properties");
        }

        JPAConnector.getInstance(appConfig);
        if (!JPAConnector.isOpen()) {
            throw new Exception("Can't connect to database. Please check file application.properties");
        }

        try {
            ConfigurationRepository configurationRepository = new ConfigurationRepository();
            appConfig.setConfiguration(configurationRepository.getConfiguration());
        } catch (Exception ex) {
            throw new Exception("Can't find Configuration data", ex);
        }
    }

    public void openSapConnection() throws Exception {
        Credentials credentials = new Credentials();
        credentials.setClient(appConfig.getConfiguration().getSapClient());
        credentials.setUser("hc_consult01");
        credentials.setPassword("gcsvn123");

        sapSession = initSapSession(credentials);
        try {
            LoginService.checkVersionWB(sapSession);
        } catch (Exception ex) {
            if (ex.getCause() instanceof JCoException) {
                JCoException jcoException = (JCoException) ex.getCause();
                if (jcoException.getGroup() == JCoException.JCO_ERROR_LOGON_FAILURE) {
                    throw new Exception("Login SAP failed", ex);
                } else if (jcoException.getGroup() == JCoException.JCO_ERROR_COMMUNICATION) {
                    throw new Exception("Can't connect to SAP", ex);
                }
            }
        }
    }

    public Session initSapSession(Credentials credentials) throws Exception {
        SessionManager sessionManager = BAPIConfiguration.getSessionManager(appConfig, credentials);
        return sessionManager.openSession(credentials);
    }

    public static Session getSapSession() {
        return sapSession;
    }

    public static AppConfig getAppConfig() {
        return appConfig;
    }

    public void dispose() {
        if (JPAConnector.isOpen()) {
            JPAConnector.close();
        }
    }
}
