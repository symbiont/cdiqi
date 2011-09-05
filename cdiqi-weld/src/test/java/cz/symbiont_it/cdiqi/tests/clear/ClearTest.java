package cz.symbiont_it.cdiqi.tests.clear;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForTotalCount;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import cz.symbiont_it.cdiqi.api.QuartzSchedulerManager;
import cz.symbiont_it.cdiqi.tests.CdiqiTestUtil;

/**
 * Scheduler clear test.
 * 
 * @author Martin Kouba
 */
@RunWith(Arquillian.class)
public class ClearTest {

	static final int JOB_EXEC_COUNT_DEPENDANT = 10;

	@Inject
	private QuartzSchedulerManager quartzManager;

	@Inject
	ClearResultStore resultStore;

	@Deployment
	public static WebArchive createTestArchive() {
		return CdiqiTestUtil.createTestArchive().addPackage(
				ClearTest.class.getPackage().getName());
	}

	/**
	 * 
	 * @throws SchedulerException
	 * @throws InterruptedException
	 */
	@Test
	public void testScheduler() throws SchedulerException, InterruptedException {

		quartzManager.init();

		// Dependant job
		JobDetail job01 = newJob(SimpleDependantJob.class).withIdentity(
				"testJob1", "testGroup1").build();
		Trigger trigger01 = newTrigger()
				.withIdentity("trigger1", "testGroup1")
				.startNow()
				.withSchedule(
						repeatSecondlyForTotalCount(JOB_EXEC_COUNT_DEPENDANT, 2))
				.build();
		quartzManager.schedule(job01, trigger01);

		quartzManager.start();
		Thread.sleep(6000l);

		quartzManager.clear();
		Assert.assertTrue(resultStore.getResultsCount(SimpleDependantJob.class
				.getName()) < JOB_EXEC_COUNT_DEPENDANT);

		resultStore.clear();
		quartzManager.schedule(job01, trigger01);

		Thread.sleep(21000l);
		Assert.assertEquals(JOB_EXEC_COUNT_DEPENDANT,
				resultStore.getResultsCount(SimpleDependantJob.class.getName()));
	}
}
