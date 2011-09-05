package cz.symbiont_it.cdiqi.tests.basic;

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
 * Basic test.
 * 
 * @author Martin Kouba
 */
@RunWith(Arquillian.class)
public class BasicTest {

	static final int JOB_EXEC_COUNT_DEPENDANT = 10;
	static final int JOB_EXEC_COUNT_REQUEST = 16;
	static final int JOB_EXEC_COUNT_APPLICATION = 5;

	@Inject
	private QuartzSchedulerManager quartzManager;

	@Inject
	BasicResultStore resultStore;

	@Deployment
	public static WebArchive createTestArchive() {
		return CdiqiTestUtil.createTestArchive().addPackage(
				BasicTest.class.getPackage().getName());
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

		// Request scoped job
		JobDetail job02 = newJob(SimpleRequestScopedJob.class).withIdentity(
				"testJob2", "testGroup1").build();
		Trigger trigger02 = newTrigger()
				.withIdentity("trigger2", "testGroup1")
				.startNow()
				.withSchedule(
						repeatSecondlyForTotalCount(JOB_EXEC_COUNT_REQUEST, 1))
				.build();
		quartzManager.schedule(job02, trigger02);

		// Application scoped job
		JobDetail job03 = newJob(SimpleApplicationScopedJob.class)
				.withIdentity("testJob3", "testGroup1").build();
		Trigger trigger03 = newTrigger()
				.withIdentity("trigger3", "testGroup1")
				.startNow()
				.withSchedule(
						repeatSecondlyForTotalCount(JOB_EXEC_COUNT_APPLICATION,
								5)).build();
		quartzManager.schedule(job03, trigger03);

		quartzManager.start();

		// Wait for job execution
		Thread.sleep(21000l);

		Assert.assertEquals(JOB_EXEC_COUNT_DEPENDANT,
				resultStore.getResultsCount(SimpleDependantJob.class.getName()));
		Assert.assertEquals(JOB_EXEC_COUNT_REQUEST, resultStore
				.getResultsCount(SimpleRequestScopedJob.class.getName()));
		Assert.assertEquals(JOB_EXEC_COUNT_APPLICATION, resultStore
				.getResultsCount(SimpleApplicationScopedJob.class.getName()));
		Assert.assertTrue(resultStore.hasUniqueResults());
	}
}
