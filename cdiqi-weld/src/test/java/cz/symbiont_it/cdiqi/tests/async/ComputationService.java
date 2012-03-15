package cz.symbiont_it.cdiqi.tests.async;

import javax.inject.Inject;

/**
 * 
 * @author Martin Kouba
 */
public abstract class ComputationService {

	@Inject
	AsyncResultStore resultStore;

}
