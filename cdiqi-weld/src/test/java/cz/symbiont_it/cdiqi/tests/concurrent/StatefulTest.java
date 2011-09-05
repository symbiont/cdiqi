package cz.symbiont_it.cdiqi.tests.concurrent;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForTotalCount;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import cz.symbiont_it.cdiqi.api.QuartzSchedulerManager;
import cz.symbiont_it.cdiqi.tests.CdiqiTestUtil;

/**
 * Stateful job test.
 * 
 * @author Martin Kouba
 */
@RunWith(Arquillian.class)
public class StatefulTest {

	@Inject
	private QuartzSchedulerManager quartzManager;

	@Inject
	private NotGuardedApplicationScopedService notGuardedApplicationScopedService;

	@Deployment
	public static WebArchive createTestArchive() {
		return CdiqiTestUtil.createTestArchive().addPackage(
				StatefulTest.class.getPackage().getName());
	}

	/**
	 * 
	 * @throws SchedulerException
	 * @throws InterruptedException
	 */
	@Test
	public void testScheduler() throws SchedulerException, InterruptedException {

		quartzManager.init();

		// Long running stateful job
		JobDetail job01 = newJob(DependentStatefulJob.class).withIdentity(
				"longRunningJob1", "testGroup1").build();
		Trigger trigger01 = newTrigger().withIdentity("trigger1", "testGroup1")
				.startNow().withSchedule(repeatSecondlyForTotalCount(3, 2))
				.build();

		quartzManager.schedule(job01, trigger01);
		quartzManager.start();

		// Wait for job execution
		Thread.sleep(40000l);

		Assert.assertEquals(false, notGuardedApplicationScopedService.isBroken());
	}
}
