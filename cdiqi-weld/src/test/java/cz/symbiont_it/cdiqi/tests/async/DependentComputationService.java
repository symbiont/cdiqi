package cz.symbiont_it.cdiqi.tests.async;

import javax.enterprise.event.Observes;


/**
 * 
 * @author Martin Kouba
 */
public class DependentComputationService extends ComputationService {

	public void observerComputationStart(@Observes @Pseudo ComputationStart event) {
		resultStore.storeResult(getClass().getName(), ""
				+ Thread.currentThread().getId());
	}
	
}
