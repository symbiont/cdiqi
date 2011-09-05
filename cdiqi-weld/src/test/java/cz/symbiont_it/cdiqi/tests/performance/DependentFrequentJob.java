package cz.symbiont_it.cdiqi.tests.performance;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * @author Martin Kouba
 */
public class DependentFrequentJob implements Job {

	@Inject
	private PopularRequestScopedService popularService;

	@Inject
	private PerformanceResultStore resultStore;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		resultStore.storeResult(context.getJobDetail().getKey().toString(),
				popularService.doBusiness());
	}

}
