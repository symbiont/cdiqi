package cz.symbiont_it.cdiqi.tests.concurrent;

import javax.inject.Inject;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Martin Kouba
 */
@DisallowConcurrentExecution
public class DependentStatefulJob implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(DependentStatefulJob.class);

	@Inject
	private NotGuardedApplicationScopedService notGuardedService;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		logger.info("Long running execution started");
		notGuardedService.use();
		
		try {
			// Long computation simulation
			Thread.sleep(10000l);
		} catch (InterruptedException e) {
			throw new JobExecutionException(e);
		}
		
		notGuardedService.release();
		logger.info("Long running execution ended");
	}

}
