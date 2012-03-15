package cz.symbiont_it.cdiqi.tests.async;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * 
 * @author Martin Kouba
 */
@ApplicationScoped
public class ApplicationScopedComputationService extends ComputationService {

	public void observerComputationStart(@Observes @Normal ComputationStart event) {
		resultStore.storeResult(getClass().getName(), ""
				+ Thread.currentThread().getId());
	}
	
}
