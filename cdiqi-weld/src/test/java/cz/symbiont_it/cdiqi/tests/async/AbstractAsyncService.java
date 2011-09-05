package cz.symbiont_it.cdiqi.tests.async;

import javax.inject.Inject;

import cz.symbiont_it.cdiqi.api.AsyncInvocationContext;
import cz.symbiont_it.cdiqi.api.Asynchronous;

/**
 * 
 * @author Martin Kouba
 */
public abstract class AbstractAsyncService implements Asynchronous {

	@Inject
	AsyncResultStore resultStore;

	@Override
	public void execute(AsyncInvocationContext context) {
		resultStore.storeResult(getClass().getName(), ""
				+ Thread.currentThread().getId());
	}

}
