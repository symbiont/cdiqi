package cz.symbiont_it.cdiqi.impl.weld;

/*
 * Copyright 2011, Symbiont v.o.s
 *	
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.weld.context.bound.BoundRequestContext;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.symbiont_it.cdiqi.spi.BoundRequestJobListener;

/**
 * Weld implementation. 
 * 
 * @author Martin Kouba
 */
public class WeldBoundRequestJobListener extends BoundRequestJobListener {

	private static final Logger logger = LoggerFactory
			.getLogger(WeldBoundRequestJobListener.class);

	@Inject
	private BoundRequestContext requestContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.symbiont_it.cdiqi.spi.BoundRequestJobListener#startRequest(org.quartz
	 * .JobExecutionContext)
	 */
	protected void startRequest(JobExecutionContext context) {

		Map<String, Object> requestDataStore = Collections
				.synchronizedMap(new HashMap<String, Object>());
		context.put(REQUEST_DATA_STORE_KEY, requestDataStore);

		requestContext.associate(requestDataStore);
		requestContext.activate();

		logger.debug("Bound request started");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.symbiont_it.cdiqi.spi.BoundRequestJobListener#endRequest(org.quartz
	 * .JobExecutionContext)
	 */
	@SuppressWarnings("unchecked")
	protected void endRequest(JobExecutionContext context) {

		try {

			requestContext.invalidate();
			requestContext.deactivate();

		} finally {
			requestContext.dissociate((Map<String, Object>) context
					.get(REQUEST_DATA_STORE_KEY));
		}
		logger.debug("Bound request ended");
	}

}
