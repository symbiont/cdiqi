package cz.symbiont_it.cdiqi.tests.performance;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForTotalCount;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import cz.symbiont_it.cdiqi.api.QuartzSchedulerManager;
import cz.symbiont_it.cdiqi.tests.CdiqiTestUtil;

/**
 * Simple performance test. Useful when testing <a
 * href="https://issues.jboss.org/browse/WELD-920">WELD-920</a> memory leak.
 * 
 * This test is ignored by default.
 * 
 * @author Martin Kouba
 */
@RunWith(Arquillian.class)
public class PerformanceTest {

	static final int JOB_EXEC_COUNT = 500;

	@Inject
	private QuartzSchedulerManager quartzManager;

	@Inject
	private PerformanceResultStore resultStore;

	@Deployment
	public static WebArchive createTestArchive() {
		return CdiqiTestUtil.createTestArchive().addPackage(
				PerformanceTest.class.getPackage().getName());
	}

	/**
	 * 
	 * @throws SchedulerException
	 * @throws InterruptedException
	 * @throws ParseException
	 */
	@Ignore
	@Test
	public void testScheduler() throws SchedulerException,
			InterruptedException, ParseException {

		quartzManager.init();

		JobDetail job01 = newJob(DependentFrequentJob.class).withIdentity(
				"freqJob1", "testGroup1").build();
		Trigger trigger01 = newTrigger().withIdentity("trigger1", "testGroup1")
				.startNow()
				.withSchedule(repeatSecondlyForTotalCount(JOB_EXEC_COUNT, 1))
				.build();

		JobDetail job02 = newJob(DependentFrequentJob.class).withIdentity(
				"freqJob2", "testGroup1").build();
		Trigger trigger02 = newTrigger().withIdentity("trigger2", "testGroup1")
				.startNow()
				.withSchedule(repeatSecondlyForTotalCount(JOB_EXEC_COUNT, 1))
				.build();

		JobDetail job03 = newJob(DependentFrequentJob.class).withIdentity(
				"freqJob3", "testGroup1").build();
		Trigger trigger03 = newTrigger().withIdentity("trigger3", "testGroup1")
				.startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?"))
				.build();

		quartzManager.schedule(job01, trigger01);
		quartzManager.schedule(job02, trigger02);
		quartzManager.schedule(job03, trigger03);
		quartzManager.start();

		// Wait for job execution
		Thread.sleep(520000l);

		Assert.assertEquals(JOB_EXEC_COUNT,
				resultStore.getResultsCount("testGroup1.freqJob1"));
		Assert.assertEquals(JOB_EXEC_COUNT,
				resultStore.getResultsCount("testGroup1.freqJob2"));
		Assert.assertTrue(resultStore.hasUniqueResults());
	}
}
