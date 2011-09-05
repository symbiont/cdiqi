package cz.symbiont_it.cdiqi.tests.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Martin Kouba
 */
@ApplicationScoped
public class NotGuardedApplicationScopedService {
	
	private static final Logger logger = LoggerFactory.getLogger(NotGuardedApplicationScopedService.class);

	private AtomicBoolean isInUse = new AtomicBoolean(false);
	
	private AtomicBoolean isBroken = new AtomicBoolean(false);

	/**
	 * 
	 */
	public void use() {
		
		logger.info("Use");

		if (isInUse.get())
			isBroken.set(true);

		isInUse.set(true);
	}

	/**
	 * 
	 */
	public void release() {

		logger.info("Release");
		
		if (!isInUse.get())
			isBroken.set(true);

		isInUse.set(false);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBroken() {
		return isBroken.get();
	}
	
}
