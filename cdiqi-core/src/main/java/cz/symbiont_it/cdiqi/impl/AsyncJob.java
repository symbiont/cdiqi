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
package cz.symbiont_it.cdiqi.impl;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.symbiont_it.cdiqi.api.AsyncEvent;

/**
 * Job that fires {@link AsyncEvent#getTargetEvent()} asynchronously.
 * 
 * @author Martin Kouba
 */
@Dependent
public class AsyncJob implements Job {

	public static final String KEY_ASYNC_EVENT = AsyncEvent.class.getName();

	private static final Logger logger = LoggerFactory
			.getLogger(AsyncJob.class);

	@Inject
	private BeanManager beanManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		try {

			AsyncEvent asyncEvent = (AsyncEvent) context.getJobDetail()
					.getJobDataMap().remove(KEY_ASYNC_EVENT);

			if (asyncEvent == null)
				// No async event to process - should never happen
				return;

			logger.debug("Fire async event [{}] with qualifiers {}", asyncEvent
					.getTargetEvent().getClass().getName(),
					asyncEvent.getQualifiers());

			beanManager.fireEvent(asyncEvent.getTargetEvent(),
					asyncEvent.getQualifiers());

		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}

}
