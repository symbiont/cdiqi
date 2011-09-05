package cz.symbiont_it.cdiqi.tests.async;

import javax.inject.Inject;

import junit.framework.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;

import cz.symbiont_it.cdiqi.api.AsyncInvocationContext;
import cz.symbiont_it.cdiqi.api.QuartzSchedulerManager;
import cz.symbiont_it.cdiqi.tests.CdiqiTestUtil;

/**
 * Simple asynchronous test.
 * 
 * @author Martin Kouba
 */
@RunWith(Arquillian.class)
public class AsyncTest {

	@Inject
	private QuartzSchedulerManager quartzManager;

	@Inject
	private AsyncResultStore resultStore;

	@Deployment
	public static WebArchive createTestArchive() {
		return CdiqiTestUtil.createTestArchive().addPackage(
				AsyncTest.class.getPackage().getName());
	}

	/**
	 * 
	 * @throws SchedulerException
	 * @throws InterruptedException
	 */
	@Test
	public void testAsyncExecution() throws SchedulerException,
			InterruptedException {

		quartzManager.start();

		quartzManager.executeAsynchronously(DependentAsyncService.class);
		quartzManager.executeAsynchronously(RequestScopedAsyncService.class,
				new AsyncInvocationContext().put("TEST",
						"RequestScopedAsyncService"));
		quartzManager
				.executeAsynchronously(ApplicationScopedAsyncService.class);

		String id = "" + Thread.currentThread().getId();
		Thread.sleep(2000l);

		Assert.assertEquals(1l, resultStore
				.getResultsCount(DependentAsyncService.class.getName()));
		Assert.assertEquals(2l, resultStore
				.getResultsCount(RequestScopedAsyncService.class.getName()));
		Assert.assertEquals(1l, resultStore
				.getResultsCount(ApplicationScopedAsyncService.class.getName()));

		Assert.assertFalse(id.equals(resultStore.getResult(
				DependentAsyncService.class.getName(), 0)));
		Assert.assertFalse(id.equals(resultStore.getResult(
				RequestScopedAsyncService.class.getName(), 0)));
		Assert.assertEquals("RequestScopedAsyncService", resultStore.getResult(
				RequestScopedAsyncService.class.getName(), 1));
		Assert.assertFalse(id.equals(resultStore.getResult(
				ApplicationScopedAsyncService.class.getName(), 0)));
	}
}
