package cz.symbiont_it.cdiqi.tests.async;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import javax.enterprise.event.Event;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;

import cz.symbiont_it.cdiqi.api.AsyncEvent;
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

	@Inject
	Event<AsyncEvent> event;

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
	@SuppressWarnings("serial")
	@Test
	public void testAsyncEvents() throws SchedulerException,
			InterruptedException {

		quartzManager.start();

		event.fire(new AsyncEvent(new ComputationStart(),
				new AnnotationLiteral<Normal>() {
				}));
		event.fire(new AsyncEvent(new ComputationStart(),
				new AnnotationLiteral<Pseudo>() {
				}));

		String id = "" + Thread.currentThread().getId();
		Thread.sleep(2000l);

		// Check number of results
		assertEquals(1l,
				resultStore.getResultsCount(DependentComputationService.class
						.getName()));
		assertEquals(1l,
				resultStore.getResultsCount(RequestScopedComputationService.class
						.getName()));
		assertEquals(1l,
				resultStore.getResultsCount(ApplicationScopedComputationService.class
						.getName()));

		// Check thread ids
		assertFalse(id.equals(resultStore.getResult(
				DependentComputationService.class.getName(), 0)));
		assertFalse(id.equals(resultStore.getResult(
				RequestScopedComputationService.class.getName(), 0)));
		assertFalse(id.equals(resultStore.getResult(
				ApplicationScopedComputationService.class.getName(), 0)));
		assertFalse(resultStore.getResult(
				DependentComputationService.class.getName(), 0).equals(
				resultStore.getResult(
						ApplicationScopedComputationService.class.getName(), 0)));
		assertEquals(resultStore.getResult(
				RequestScopedComputationService.class.getName(), 0),
				resultStore.getResult(
						ApplicationScopedComputationService.class.getName(), 0));
	}
}
