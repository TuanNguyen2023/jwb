package com.gcs.wb.batch;

import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.Configuration;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.log4j.Logger;
import org.hibersap.session.Session;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by THANGLH
 */
public abstract class SyncJob implements Job {

    protected final Logger logger = Logger.getLogger(this.getClass());
    protected final Session session = CronTriggerService.getSapSession();
    protected final EntityManager entityManager = JPAConnector.getInstance();
    protected final Configuration configuration = CronTriggerService.getAppConfig().getConfiguration();
    protected final EntityTransaction entityTransaction = entityManager.getTransaction();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {}
}
