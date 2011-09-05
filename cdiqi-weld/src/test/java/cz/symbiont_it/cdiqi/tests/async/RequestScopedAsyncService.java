package cz.symbiont_it.cdiqi.tests.async;

import javax.enterprise.context.RequestScoped;

import cz.symbiont_it.cdiqi.api.AsyncInvocationContext;

/**
 * 
 * @author Martin Kouba
 */
@RequestScoped
public class RequestScopedAsyncService extends AbstractAsyncService {

	@Override
	public void execute(AsyncInvocationContext context) {
		super.execute(context);
		resultStore.storeResult(getClass().getName(),
				(String) context.get("TEST"));
	}

}
