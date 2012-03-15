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
package cz.symbiont_it.cdiqi.spi;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * Bounds request context to single job execution.
 * 
 * @author Martin Kouba
 * @see JobListener
 * @see JobExecutionContext#put(Object, Object)
 */
public abstract class BoundRequestJobListener implements JobListener {

	protected static final String REQUEST_DATA_STORE_KEY = BoundRequestJobListener.class
			.getName() + "_REQUEST_DATA_STORE_KEY";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.JobListener#getName()
	 */
	public String getName() {
		return getClass().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.quartz.JobListener#jobToBeExecuted(org.quartz.JobExecutionContext)
	 */
	public void jobToBeExecuted(JobExecutionContext context) {
		startRequest(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.quartz.JobListener#jobExecutionVetoed(org.quartz.JobExecutionContext)
	 */
	public void jobExecutionVetoed(JobExecutionContext context) {
		endRequest(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.quartz.JobListener#jobWasExecuted(org.quartz.JobExecutionContext,
	 * org.quartz.JobExecutionException)
	 */
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		endRequest(context);
	}

	/**
	 * @param context
	 */
	protected abstract void startRequest(JobExecutionContext context);

	/**
	 * @param context
	 */
	protected abstract void endRequest(JobExecutionContext context);

}
