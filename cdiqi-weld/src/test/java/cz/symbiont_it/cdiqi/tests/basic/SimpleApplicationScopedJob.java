package cz.symbiont_it.cdiqi.tests.basic;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

/**
 * 
 * @author Martin Kouba
 */
@ApplicationScoped
public class SimpleApplicationScopedJob implements Job {

	private static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(SimpleApplicationScopedJob.class);
	
	@Inject
	BasicResultStore resultStore;
	
	@Inject
	SimpleRequestScopedService simpleService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		resultStore.storeResult(getClass().getName(),
				simpleService.doSomeBusiness(getClass().getName()));
		logger.info("Executed");
	}

}
