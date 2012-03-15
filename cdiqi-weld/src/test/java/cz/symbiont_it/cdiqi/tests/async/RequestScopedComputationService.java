package cz.symbiont_it.cdiqi.tests.async;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;

/**
 * 
 * @author Martin Kouba
 */
@RequestScoped
public class RequestScopedComputationService extends ComputationService {

	public void observerComputationStart(@Observes @Normal ComputationStart event) {
		resultStore.storeResult(getClass().getName(), ""
				+ Thread.currentThread().getId());
	}

}
